package pl.gr14b.transylwania;

class ClientGameFinalTicksComponent extends ClientGameComponent
{
	ClientGameFinalTicksComponent(Client client)
	{
		super(client);
	}

	@Override
	void updateComponent()
	{
		if (canPlayTickingSound())
			Stuff.playSound("tick");
	}

	private boolean canPlayTickingSound()
	{
		return isSpecificGameStageAndEvenTick()
				&& isTimerLowEnoughToPlayTickingSound();
	}

	private boolean isTimerLowEnoughToPlayTickingSound()
	{
		return clientGame.getGameTime() > 0
				&& clientGame.getGameTime() <= Constants.CLOCK_TICKING_TRIGGER;
	}

	private boolean isSpecificGameStageAndEvenTick()
	{
		return clientGame.getGameStatus().equals(GameStatus.KILLING)
				&& (tick % Constants.SERVER_ODD_TICK) == 0;
	}
}
