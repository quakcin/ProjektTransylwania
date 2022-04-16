package pl.gr14b.transylwania;

class ClientGameThrottleAnimationsComponent extends ClientGameComponent
{
	ClientGameThrottleAnimationsComponent(Client client) {
		super(client);
	}

	private boolean isMainPlayer (Player p)
	{
		return p.getPlayerID().equals(clientGame.getPlayer().getPlayerID());
	}

	private boolean isCloseEnough(Player p)
	{
		return p.getDist(clientGame.getPlayer().getX(), clientGame.getPlayer().getY())
				< Constants.CLIENT_ANIMATION_THROTTLE_LIMITER;
	}

	@Override
	void updateComponent()
	{
		for (Player p : clientGame.getPlayers())
			if (!isMainPlayer(p) && p.isPlayerMoving() && isCloseEnough(p))
				attemptPlayerPush(p);
	}

	private boolean isPlayerOnTheirSoundTick(Player p)
	{
		return (tick + clientGame.getPlayers().indexOf(p) * Constants.RANDOM_TICK_OFFSET_DELTA)
					% Constants.RANDOM_TICK_OFFSET_LIMIT == 0;
	}

	private void attemptPlayerPush(Player p)
	{
		if (isPlayerOnTheirSoundTick(p))
			p.playStepSound();

		p.push(Constants.PLAYER_DEFAULT_FORWARD_MOTION_SPEED, clientGame);
	}

}
