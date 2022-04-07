package pl.gr14b.transylwania;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.locks.Lock;

class ServerHandler extends Thread
{
	private Socket socket;

	private ServerResponseHandler serverResponseHandler;
	private ServerRequestHandler serverRequestHandler;
	private ObjectOutputStream objOut;
	private ObjectInputStream objIn;
	private Game serverGame;
	private Lock lock;
	private UUID playerID;

	ServerHandler(Socket socket, Server server) throws Exception
	{
		socket.setTcpNoDelay(true);
		this.socket = socket;
		this.lock = server.getLock();
		this.serverGame = server.getServerGame();

		objOut = new ObjectOutputStream(socket.getOutputStream());
		objIn = new ObjectInputStream(socket.getInputStream());
		playerID = null;

		serverResponseHandler = new ServerResponseHandler(this);
		serverRequestHandler = new ServerRequestHandler(this);
	}

	void handleDataExchange(Object clientResponse) throws Exception
	{
		serverRequestHandler.handleRequest(clientResponse, playerID);
		serverResponseHandler.handleResponse();
	}

	void handlePlayerDisappearance ()
	{
		System.out.printf("Player %s has disconnected!\n", playerID);

		for (int i = serverGame.getPlayers().size() - 1; i >= 0; i--)
			if (serverGame.getPlayers().get(i).getPlayerID().equals(playerID))
			{
				serverGame.getPlayers().remove(i);
				break;
			}
	}

	boolean isConnectedToAClient ()
	{
		try
		{
			Thread.sleep(5L);
			lock.lock();
			handleDataExchange(objIn.readObject());
			objOut.flush();
			objOut.reset();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			handlePlayerDisappearance();
			return false;
		}

		lock.unlock();
		return true;
	}

	@Override
	public void run ()
	{
		while (!socket.isClosed())
		{
			if (!isConnectedToAClient())
				break;
		}
	}

	ObjectOutputStream getObjOut () { return this.objOut; }
	ObjectInputStream getObjIn () { return this.objIn; }
	Game getServerGame () { return this.serverGame; }
	UUID getPlayerID () { return this.playerID; };
	void setPlayerID (UUID playerID) { this.playerID = playerID; }

}