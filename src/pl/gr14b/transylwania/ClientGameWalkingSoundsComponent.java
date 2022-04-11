package pl.gr14b.transylwania;

public class ClientGameWalkingSoundsComponent extends ClientGameComponent
{
	ClientGameWalkingSoundsComponent(Client client) {
		super(client);
	}

	private boolean isPlayerOnTheirStepTick()
	{
		return tick % 13L == 0;
	}

	@Override
	void updateComponent()
	{
		if (clientGame.getPlayer().isPlayerMoving() && isPlayerOnTheirStepTick())
			clientGame.getPlayer().playStepSound();
	}
}
