package pl.gr14b.transylwania;

public abstract class ServerGameStage
{
	public ServerBackgroundThread server;
	private GameStatus stageCode;
	public Game game;

	ServerGameStage (ServerBackgroundThread serverBackgroundThread, GameStatus stageCode)
	{
		this.server = serverBackgroundThread;
		this.game = serverBackgroundThread.getGame();
		this.stageCode = stageCode;
	}

	boolean isSpecificStage (GameStatus stageCode)
	{
		return stageCode == this.stageCode;
	}

	abstract void UpdateStage();
}
