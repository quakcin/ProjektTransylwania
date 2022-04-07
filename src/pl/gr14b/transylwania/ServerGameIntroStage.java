package pl.gr14b.transylwania;

public class ServerGameIntroStage extends ServerGameStage
{

	ServerGameIntroStage(ServerBackgroundThread serverBackgroundThread, GameStatus stageCode) {
		super(serverBackgroundThread, stageCode);
	}

	void chooseVampire ()
	{
		Player newVampire = game.getPlayers().get((int) Math.round(Math.random() * (game.getPlayers().size() - 1)));
		newVampire.setPlayerType(Player.PLAYER_TYPE_VAMPIRE);
		newVampire.setForcingSynchronization(true);
	}

	void changeGameStage ()
	{
		double[] timesPerPlayers = {3d, 4.5d, 5.5d, 6.3d, 7d, 7.5d, 8d};
		game.setGameTime((int) Math.round(timesPerPlayers[game.countSurvivors() - 1] * 60d)); // WARN: Might get over bounds
		game.setGameStatus(GameStatus.KILLING);
	}

	void blowOutLamps ()
	{
		for (Lamp lamp : game.getLamps())
			lamp.BlowOut();
	}

	void beginKillingStage ()
	{
		chooseVampire();
		changeGameStage();
		blowOutLamps();
	}

	void awaitingIntroSound ()
	{
		if (server.getTick() == 56) // FIXME: Magic Sound
		{
			game.playSoundNear(0, 0, 100000L, "intro");
			game.setGlobalLight(1.0d);
		}
	}

	@Override
	void UpdateStage()
	{
		if (server.hasSecondPassed())
			game.setWaitingTime(game.getWaitingTime() - 1);

		if (game.getWaitingTime() <= 0)
			beginKillingStage();

		awaitingIntroSound();
	}

}
