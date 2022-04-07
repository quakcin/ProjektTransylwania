package pl.gr14b.transylwania;

public class ServerGameSummaryStage extends ServerGameStage
{
	ServerGameSummaryStage(ServerBackgroundThread serverBackgroundThread, GameStatus stageCode) {
		super(serverBackgroundThread, stageCode);
	}

	boolean isOverSummary ()
	{
		return server.getTick() > 60;
	}

	@Override
	void UpdateStage()
	{
		if (isOverSummary())
			game.Reset();
	}
}
