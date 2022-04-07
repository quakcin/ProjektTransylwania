package pl.gr14b.transylwania;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server
{
	private Boolean serverRunning;
	private final Lock lock;
	private Game serverGame;

	private void mainUpcomingConnectionsHandler(ServerSocket serverSocket) throws Exception
	{
		while (!serverSocket.isClosed())
		{
			Socket accepted = serverSocket.accept();
			ServerHandler serverHandler = new ServerHandler(accepted, this);
			serverHandler.start();
		}
		serverRunning = false;
	}

	Server (int port) throws Exception
	{
		lock = new ReentrantLock();
		serverGame = new Game();
		serverRunning = true;
		(new ServerBackgroundThread(this)).start();
		mainUpcomingConnectionsHandler(new ServerSocket(port));
	}

	public Lock getLock () { return this.lock; }
	public Game getServerGame () { return this.serverGame; }
	public Boolean isServerRunning() { return serverRunning; }
}

