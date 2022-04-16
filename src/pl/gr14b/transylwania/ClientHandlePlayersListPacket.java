package pl.gr14b.transylwania;

public class ClientHandlePlayersListPacket extends ClientHandlePacket
{
	ClientHandlePlayersListPacket(Client client, String packetClass) {
		super(client, packetClass);
	}

	@Override
	void packetHandler(Object packet)
	{
		Player oldPlayerState = clientGame.getPlayer();
		clientGame.setPlayers(((PlayersListPacket) packet).getPlayers());

		if (oldPlayerState != null && hasToSyncWithPlayerLocation(oldPlayerState))
			clientGame.getPlayer().copyLocation(oldPlayerState);
	}

	private boolean hasToSyncWithPlayerLocation (Player oldPlayerState)
	{
		return clientGame.getPlayer().getDist(oldPlayerState.getX(), oldPlayerState.getY())
				< Constants.SERVER_THROTTLING_LIMIT;
	}
}
