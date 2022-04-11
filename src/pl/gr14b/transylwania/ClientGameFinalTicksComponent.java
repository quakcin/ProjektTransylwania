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
				&& clientGame.getGameTime() <= 15; // FIXME: MAKE INTO MAGIC NUMBER
	}

	private boolean isSpecificGameStageAndEvenTick()
	{
		return clientGame.getGameStatus().equals(GameStatus.KILLING)
				&& (tick % 25) == 0;
	}
}
