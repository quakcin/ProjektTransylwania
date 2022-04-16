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

	ServerBackgroundThread (Server server)
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

	@Override
	public void run()
	{
		while (server.isServerRunning())
		{
			try
			{
				Thread.sleep(Constants.SERVER_THREAD_YIELD);
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

	private void BackgroundUpdate()
	{
		serverGameStageHandler.UpdateStage(game.getGameStatus());
		serverCollisionPreventingMechanism.preventCollisions();
		tick += 1;
	}

	boolean hasSecondPassed()
	{
		return getTick() % Constants.TICK_1SEC == 0;
	}

	boolean isOverTime ()
	{
		return game.getWaitingTime() <= 0;
	}

	private void secondHasPassed()
	{
		game.setWaitingTime(
				game.getWaitingTime() - 1
		);
	}

	void timeUpdate ()
	{
		if (hasSecondPassed())
			secondHasPassed();
	}

	public Game getGame () { return this.game; }
	int getTick() { return tick; }
	void setTick(int tick) { this.tick = tick; }
}
