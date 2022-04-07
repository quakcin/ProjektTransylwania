package pl.gr14b.transylwania;
import java.util.concurrent.locks.Lock;

class ServerBackgroundThread extends Thread
{
	private ServerCollisionPreventingMechanism serverCollisionPreventingMechanism;
	private ServerGameStageHandler serverGameStageHandler;
	private Server server;
	private Game game;
	private Lock lock;
	private int tick;

	@Override
	public void run()
	{
		while (server.isServerRunning())
		{
			try
			{
				Thread.sleep(1000L / 20L);
				lock.lock();
				BackgroundUpdate();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			lock.unlock();
		}
	}

	ServerBackgroundThread(Server server)
	{
		serverCollisionPreventingMechanism
				= new ServerCollisionPreventingMechanism
				  (server.getServerGame());

		this.game = server.getServerGame();
		this.lock = server.getLock();
		this.server = server;
		tick = 0;

		serverGameStageHandler =
				new ServerGameStageHandler(this);
	}

	void BackgroundUpdate()
	{
		serverGameStageHandler.UpdateStage(game.getGameStatus());
		serverCollisionPreventingMechanism.preventCollisions();
		tick += 1;
	}

	boolean hasSecondPassed ()
	{
		return getTick() % 10 == 0;
	}

	public Game getGame () { return this.game; }
	public int getTick() { return tick; }
	public void setTick(int tick) { this.tick = tick; }
}
