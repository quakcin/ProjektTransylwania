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
			clientGame.getPlayer().push(Constants.PLAYER_DEFAULT_FORWARD_MOTION_SPEED, clientGame);
			clientGame.getPlayer().setPlayerMoving(true);
		}
		if (keyboardState['s'])
			clientGame.getPlayer().push(Constants.PLAYER_DEFAULT_BACKWARD_MOTION_SPEED, clientGame);
		if (keyboardState['a'])
			clientGame.getPlayer().turn(-Constants.PLAYER_DEFAULT_TURNING_SPEED);
		if (keyboardState['d'])
			clientGame.getPlayer().turn(Constants.PLAYER_DEFAULT_TURNING_SPEED);
		if (keyboardState[' '])
			clientGame.getPlayer().setSpacePressedEnabled();
	}

}
