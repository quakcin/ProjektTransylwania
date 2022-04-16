package pl.gr14b.transylwania;

class ClientGameChaseSoundComponent extends ClientGameComponent
{

	private long chaseSongPlayTime;

	ClientGameChaseSoundComponent(Client client)
	{
		super(client);
		chaseSongPlayTime = 0;
	}

	@Override
	void updateComponent()
	{
		Player vamp = clientGame.getVamp();
		if (vamp != null)
			attemptPlayingChaseMusic(vamp);
	}

	private boolean isMainPlayerAVampire(Player vamp)
	{
		return vamp.getPlayerID() == clientGame.getPlayer().getPlayerID();
	}


	private boolean canPlayChaseMusic(Player vamp)
	{
		return clientGame.getPlayer().getDist(vamp.getX(), vamp.getY()) <= Constants.CHASE_MUSIC_RANGE
				&& !clientGame.getVamp().isHidden(clientGame.getChests());
	}

	private void resetChaseMusicTimer()
	{
		chaseSongPlayTime = 0;
	}

	private boolean isSupposedToPlayChaseMusic()
	{
		return chaseSongPlayTime == Constants.CHASE_MUSIC_RESET_TICK;
	}

	private void handleChaseMusicTimer()
	{
		chaseSongPlayTime += 1;
		if (hasChaseMusicFinishedPlaying())
			chaseSongPlayTime = 0;
	}

	private boolean hasChaseMusicFinishedPlaying ()
	{
		return chaseSongPlayTime > Constants.CHASE_MUSIC_DURATION;
	}

	private void attemptPlayingChaseMusic(Player vamp)
	{
		if (!isMainPlayerAVampire(vamp))
		{
			if (!canPlayChaseMusic(vamp))
				resetChaseMusicTimer();

			if (isSupposedToPlayChaseMusic())
				Stuff.playSound("chase");

			handleChaseMusicTimer();
		}
	}

}
