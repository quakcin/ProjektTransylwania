package pl.gr14b.transylwania;

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
	private ServerBackgroundWorker serverBackgroundWorker;
	private final Lock lock;
	private Game serverGame;

	Server (int port) throws Exception
	{
		lock = new ReentrantLock();
		serverGame = new Game();
		serverBackgroundWorker = new ServerBackgroundWorker(serverGame);
		// TOO SLOW:
		ServerWorker serverWorker = new ServerWorker();
		serverWorker.start();

		ServerSocket serverSocket = new ServerSocket(port);

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
				lock.lock();
				// -- do stuff
				try
				{
					Object clientResponse = objIn.readObject();

					if (clientResponse instanceof Player)
					{
						Player clientPlayer = (Player) clientResponse;
						serverGame.setPlayerID(playerID);
						if (!serverGame.getPlayer().isForcingSynchronization() && !serverGame.getPlayer().isForcingLocationSynchronization())
						{
							serverGame.getPlayer().copyLocation(clientPlayer);
							serverGame.getPlayer().copyFlags(clientPlayer);
						}
						else
						{
							// even if tp'ed allow if not too far
							if (serverGame.getPlayer().getDist(clientPlayer.getX(), clientPlayer.getY()) < 100 && !serverGame.getPlayer().isForcingLocationSynchronization())
								serverGame.getPlayer().copyLocation(clientPlayer);
							else
							{
								System.out.println("[DEBUG] Probably teleporting player, if not -> game is broken!");
							}
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
						System.out.printf("Player %s has joined!\n", playerID);
					}

					// Send Back: serverGame object
					// This should never be the case!
					if (serverGame.getPlayer().getNextPacket() == 0 || serverGame.getPlayer().isForcingSynchronization() || serverGame.getPlayer().isForcingLocationSynchronization())
					{
						assert(serverGame.getPlayer().getPlayerID().equals(playerID));
						serverGame.getPlayer().setForcingSynchronization(false);
						serverGame.getPlayer().setForcingLocationSynchronization(false);
						objOut.writeObject(serverGame); // FIXME: Split synchronization to smaller packets
					}
					else if (serverGame.getPlayer().getNextPacket() <= 10)
					{
						assert(serverGame.getPlayer().getPlayerID().equals(playerID));
						objOut.writeObject(new PlayersListPacket(serverGame.getPlayers()));
					}
					else if (serverGame.getPlayer().getNextPacket() == 11)
					{
						assert(serverGame.getPlayer().getPlayerID().equals(playerID));
						objOut.writeObject(new PropsAndStatsListPacket(serverGame.getProps(), serverGame.getGameStatus(), serverGame.getWaitingTime(), serverGame.getGameTime(), serverGame.isWinnerFlag()));
					}
					else if (serverGame.getPlayer().getNextPacket() == 12)
					{
						assert(serverGame.getPlayer().getPlayerID().equals(playerID));
						objOut.writeObject(new LampsListPacket(serverGame.getLamps(), serverGame.getGlobalLight()));
					}
					else if (serverGame.getPlayer().getNextPacket() == 13)
					{
						assert(serverGame.getPlayer().getPlayerID().equals(playerID));
						objOut.writeObject(new ChestsListPacket(serverGame.getChests()));
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
					lock.unlock();
					try {
						Thread.sleep(5L);
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
			//noinspection InfiniteLoopStatement
			while (true)
			{
				lock.lock();
				try
				{
					serverBackgroundWorker.Update();
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
						Thread.sleep(1000L / 20L); // 20 tps, DO NOT CHANGE
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
			new Server(6969);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class ClientCreateServer
{
	private int port;
	ClientCreateServer (int port)
	{
		this.port = port;
		(new ClientSideServerThread()).start();
	}
	private class ClientSideServerThread extends Thread
	{
		@Override
		public void run ()
		{
			try {
				new Server(port);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
