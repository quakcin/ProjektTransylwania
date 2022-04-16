package pl.gr14b.transylwania;

class ServerGameSummaryStage extends ServerGameStage
{
	ServerGameSummaryStage(ServerBackgroundThread serverBackgroundThread, GameStatus stageCode) {
		super(serverBackgroundThread, stageCode);
	}

	@Override
	void UpdateStage()
	{
		if (isOverSummary())
			game.resetGameServerWise();
	}

	private boolean isOverSummary()
	{
		return server.getTick() > Constants.MINUTE_IN_SECONDS;
	}
}
