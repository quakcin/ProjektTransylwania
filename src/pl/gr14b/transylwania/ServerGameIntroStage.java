package pl.gr14b.transylwania;

class ServerGameIntroStage extends ServerGameStage
{

	ServerGameIntroStage(ServerBackgroundThread serverBackgroundThread, GameStatus stageCode) {
		super(serverBackgroundThread, stageCode);
	}

	@Override
	void UpdateStage()
	{
		server.timeUpdate();

		if (server.isOverTime())
			beginKillingStage();

		awaitingIntroSound();
	}


	private void beginKillingStage()
	{
		chooseVampire();
		changeGameStageToKilling();
		blowOutLamps();
	}

	private void chooseVampire()
	{
		Player vamp = getRandomPlayer();
		vamp.setPlayerType(PlayerType.VAMPIRE);
		vamp.setForcingSynchronization(true);
	}

	private Player getRandomPlayer ()
	{
		return game.getPlayers().get (
				(int) Math.round(Math.random() * (game.getPlayers().size() - 1))
		);
	}

	private void changeGameStageToKilling()
	{
		game.setGameTime(calculateMatchDuration());
		game.setGameStatus(GameStatus.KILLING);
	}

	private int  calculateMatchDuration ()
	{
		return (int) Math.round(
				Constants.GAME_TIMES_PER_PLAYERS[game.countSurvivors() - 1] * Constants.MINUTE_IN_SECONDS
		);
	}

	private void blowOutLamps()
	{
		for (LampProp lampProp : game.getLamps())
			lampProp.BlowOut();
	}

	private void awaitingIntroSound()
	{
		if (server.getTick() == Constants.INTRO_MUSIC_TICK)
		{
			game.playSoundNear(0, 0, Constants.FAR_PLANE, "intro");
			game.setGlobalLight(1d);
		}
	}

}
