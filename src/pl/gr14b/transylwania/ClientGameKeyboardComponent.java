package pl.gr14b.transylwania;

class ClientGameKeyboardComponent extends ClientGameComponent
{
	ClientGameKeyboardComponent(Client client) {
		super(client);
	}

	@Override
	void updateComponent()
	{
		clientGame.getPlayer().setPlayerMoving(false);
		defaultKeymapListener(client.getKeyboardState());
		clientGame.getPlayer().updateFlagStatusCounters();
	}

	private void defaultKeymapListener(Boolean[] keyboardState)
	{
		if (keyboardState['w'])
		{
			clientGame.getPlayer().push(16.3, clientGame);
			clientGame.getPlayer().setPlayerMoving(true);
		}
		if (keyboardState['s'])
			clientGame.getPlayer().push(-12, clientGame);
		if (keyboardState['a'])
			clientGame.getPlayer().turn(-10);
		if (keyboardState['d'])
			clientGame.getPlayer().turn(10);
		if (keyboardState[' '])
			clientGame.getPlayer().setSpacePressedEnabled();
	}

}
