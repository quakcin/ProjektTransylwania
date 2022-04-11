package pl.gr14b.transylwania;

abstract class ClientGameComponent
{
	Client client;
	Game clientGame;
	long tick;

	ClientGameComponent (Client client)
	{
		this.client = client;
	}

	private boolean canInvokeUpdateMethod()
	{
		return client != null
				&& client.getClientGame() != null
				&& client.getClientGame().getPlayer() != null;
	}

	void invokeComponent (long tick)
	{
		if (!canInvokeUpdateMethod())
			return;

		this.clientGame = client.getClientGame();
		this.tick = tick;
		updateComponent();
	}

	abstract void updateComponent ();

}
