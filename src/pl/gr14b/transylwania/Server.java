package pl.gr14b.transylwania;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server
{
	static final int PORT = 6969;

	private ServerBackgroundWorker serverBackgroundWorker;
	private final Lock lock;
	private Game serverGame;

	private ServerWorker serverWorker;

	Server () throws Exception
	{
		lock = new ReentrantLock();
		serverGame = new Game();
		serverBackgroundWorker = new ServerBackgroundWorker(serverGame);
		// TOO SLOW:
		serverWorker = new ServerWorker();
		serverWorker.start();

		ServerSocket serverSocket = new ServerSocket(PORT);

		while (!serverSocket.isClosed())
		{
			Socket accepted = serverSocket.accept();
			ServerHandler serverHandler = new ServerHandler(accepted);
			serverHandler.start();
		}
	}

	private class ServerHandler extends Thread
	{
		private Socket socket;

		private ObjectOutputStream objOut;
		private ObjectInputStream objIn;
		private UUID playerID;
		private String playerNickname;

		ServerHandler(Socket socket) throws Exception
		{
			socket.setTcpNoDelay(true);
			this.socket = socket;
			objOut = new ObjectOutputStream(socket.getOutputStream());
			objIn = new ObjectInputStream(socket.getInputStream());
			playerID = null;
		}

		@Override
		public void run ()
		{
			while (!socket.isClosed())
			{
				//lock.lock();
				// -- do stuff
				try
				{
					Object clientResponse = objIn.readObject();

					if (clientResponse instanceof Player)
					{
						Player clientPlayer = (Player) clientResponse;
						serverGame.setPlayerID(playerID);
						if (!serverGame.getPlayer().isForcingSynchronization())
						{
							serverGame.getPlayer().copyLocation(clientPlayer);
							serverGame.getPlayer().copyFlags(clientPlayer);
						}
					}
					else if (clientResponse instanceof Hello)
					{
						// New Player Has Joined!
						if (serverGame.getPlayers().size() >= 8)
						{
							// DOS
							return;
						}
						// Otherwise: Add 'Em
						Hello helloPacket = (Hello) clientResponse;
						playerID = serverGame.playerJoinEvent(helloPacket.getNickName(), serverGame.getPlayers());
						serverGame.setPlayerID(playerID);
						playerNickname = helloPacket.getNickName();
					}

					// Send Back: serverGame object

					if (serverGame.getPlayerByUUID(playerID).getNextPacket() == 0 || serverGame.getPlayerByUUID(playerID).isForcingSynchronization())
					{
						serverGame.getPlayer().setForcingSynchronization(false);
						objOut.writeObject(serverGame);
					}
					else if (serverGame.getPlayerByUUID(playerID).getNextPacket() <= 10)
					{
						objOut.writeObject(new PlayersListPacket(serverGame.getPlayers()));
					}
					else if (serverGame.getPlayerByUUID(playerID).getNextPacket() == 11)
					{
						objOut.writeObject(new PropsAndStatsListPacket(serverGame.getProps(), serverGame.getGameStatus(), serverGame.getWaitingTime(), serverGame.getGameTime()));
					}
					else if (serverGame.getPlayerByUUID(playerID).getNextPacket() == 12)
					{
						objOut.writeObject(new LampsListPacket(serverGame.getLamps()));
					}

					objOut.flush();
					objOut.reset();
				}
				catch (SocketException e)
				{
					System.out.printf("Player %s has disconnected!\n", playerID);

					for (int i = serverGame.getPlayers().size() - 1; i >= 0; i--)
						if (serverGame.getPlayers().get(i).getPlayerID().equals(playerID))
						{
							serverGame.getPlayers().remove(i);
							break;
						}

					break;
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					// -- release locks
					//lock.unlock();
					try {
						// Thread.sleep(1000L / 25L);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private class ServerWorker extends Thread
	{
		@Override
		public void run()
		{
			while (true)
			{
				lock.lock();
				try
				{
					serverBackgroundWorker.Update();
					// System.out.printf("There are %d active connections!\n", serverGame.getPlayers().size());
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					lock.unlock();
					try
					{
						Thread.sleep(1000L / 20L);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void main (String[] args)
	{
		try {
			new Server();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}