package pl.gr14b.transylwania;

class ClientGameIntroListenerComponent extends ClientGameComponent
{
	private GameStatus oldGameStatus;

	ClientGameIntroListenerComponent (Client client)
	{
		super(client);
	}

	private boolean isRoleCall()
	{
		return !oldGameStatus.equals(clientGame.getGameStatus())
				&& clientGame.getGameStatus().equals(GameStatus.KILLING);
	}

	private boolean isMainPlayerAVampire()
	{
		return clientGame.getPlayer().getPlayerType().equals(PlayerType.VAMPIRE);
	}

	@Override
	void updateComponent()
	{
		if (oldGameStatus == null)
			oldGameStatus = clientGame.getGameStatus();

		if (isRoleCall())
			beginRoleCallPhase();

		oldGameStatus = clientGame.getGameStatus();
	}

	private void beginRoleCallPhase()
	{
		setRoleCallFlags();
		Stuff.playSound(isMainPlayerAVampire()
			? "vamp-theme"
			: "surv-theme"
		);
	}

	private void setRoleCallFlags()
	{
		client.setRoleCallTime(Constants.ROLE_CALL_DURATION);
		client.setGlobalLight(Constants.ROLE_CALL_LIGHTNESS);
	}
}
