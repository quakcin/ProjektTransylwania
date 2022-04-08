package pl.gr14b.transylwania;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server extends Thread
{
	private Boolean serverRunning;
	private final Lock lock;
	private Game serverGame;
	private int port;

	Server (int port)
	{
		lock = new ReentrantLock();
		serverGame = new Game();
		serverRunning = true;
		this.port = port;
	}

	@Override
	public void run ()
	{
		try
		{
			(new ServerBackgroundThread(this)).start();
			handleConnections(new ServerSocket(port));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void handleConnections (ServerSocket serverSocket) throws Exception
	{
		while (!serverSocket.isClosed())
		{
			Socket accepted = serverSocket.accept();
			ServerHandler serverHandler
					= new ServerHandler(accepted, this);
			serverHandler.start();
		}
		serverRunning = false;
	}

	public Lock getLock () { return this.lock; }
	public Game getServerGame () { return this.serverGame; }
	public Boolean isServerRunning() { return serverRunning; }
}

