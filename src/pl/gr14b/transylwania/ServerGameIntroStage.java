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
		double[] timesPerPlayers = {3d, 4.5d, 5.5d, 6.3d, 7d, 7.5d, 8d};
		return (int) Math.round(
				timesPerPlayers[game.countSurvivors() - 1] * 60d
		);
	}

	private void blowOutLamps()
	{
		for (Lamp lamp : game.getLamps())
			lamp.BlowOut();
	}

	private void awaitingIntroSound()
	{
		if (server.getTick() == 56) // FIXME: Magic Sound
		{
			game.playSoundNear(0, 0, 100000L, "intro");
			game.setGlobalLight(1.0d);
		}
	}

}
