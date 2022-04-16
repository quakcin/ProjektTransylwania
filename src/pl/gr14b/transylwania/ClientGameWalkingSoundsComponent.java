package pl.gr14b.transylwania;

public class ClientGameWalkingSoundsComponent extends ClientGameComponent
{
	ClientGameWalkingSoundsComponent(Client client) {
		super(client);
	}

	private boolean isPlayerOnTheirStepTick()
	{
		return tick % Constants.PLAYER_STEP_TICK == 0;
	}

	@Override
	void updateComponent()
	{
		if (clientGame.getPlayer().isPlayerMoving() && isPlayerOnTheirStepTick())
			clientGame.getPlayer().playStepSound();
	}
}
