package pl.gr14b.transylwania;

public class ClientGameIntroListenerComponent extends ClientGameComponent
{
	GameStatus oldGameStatus;
	ClientGameIntroListenerComponent(Client client) {
		super(client);
	}

	@Override
	void updateComponent()
	{
		if (oldGameStatus == null)
			oldGameStatus = clientGame.getGameStatus();

		if (!oldGameStatus.equals(clientGame.getGameStatus()) && clientGame.getGameStatus().equals(GameStatus.KILLING))
		{
			client.setRoleCallTime(25 * 4);
			client.setGlobalLight(0.75d);
			if (clientGame.getPlayer().getPlayerType().equals(PlayerType.VAMPIRE))
				Stuff.playSound("vamp-theme");
			else
				Stuff.playSound("surv-theme");
		}

		oldGameStatus = clientGame.getGameStatus();
	}
}
