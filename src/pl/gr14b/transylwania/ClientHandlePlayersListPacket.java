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

		if (oldPlayerState != null)
			if (clientGame.getPlayer().getDist(oldPlayerState.getX(), oldPlayerState.getY()) < 150) // FIXME: make this value smaller
			{
				clientGame.getPlayer().copyLocation(oldPlayerState);
			}
	}
}
