package pl.gr14b.transylwania;

import java.util.UUID;

class ServerHandlePlayersRequest extends ServerHandleRequest
{
	ServerHandlePlayersRequest(String className, ServerHandler serverHandler) {
		super(className, serverHandler);
	}

	@Override
	void handleRequest(Object clientResponse, UUID playerID)
	{
		Player clientPlayer = (Player) clientResponse;
		serverGame.setPlayerID(playerID);

		if (isForcingSynchronization())
		{
			copyOverPlayerLocationAndFlags(clientPlayer);
			return;
		}

		if (!requiresSynchronization(clientPlayer))
			serverGame.getPlayer().copyLocation(clientPlayer);
	}

	private boolean isForcingSynchronization()
	{
		return !serverGame.getPlayer().isForcingSynchronization()
				&& !serverGame.getPlayer().isForcingLocationSynchronization();
	}

	private void copyOverPlayerLocationAndFlags(Player clientPlayer)
	{
		serverGame.getPlayer().copyLocation(clientPlayer);
		serverGame.getPlayer().copyFlags(clientPlayer);
	}

	private boolean requiresSynchronization(Player clientPlayer)
	{
		return !(
				serverGame.getPlayer().getDist(clientPlayer.getX(), clientPlayer.getY()) < 100
				&& !serverGame.getPlayer().isForcingLocationSynchronization()
		);
	}

}