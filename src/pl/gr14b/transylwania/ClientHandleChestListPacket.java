package pl.gr14b.transylwania;

public class ClientHandleChestListPacket extends ClientHandlePacket
{
	ClientHandleChestListPacket(Client client, String packetClass) {
		super(client, packetClass);
	}

	@Override
	void packetHandler(Object packet)
	{
		ChestsListPacket chestsListPacket = (ChestsListPacket) packet;
		clientGame.setChests(chestsListPacket.getChestProps());
	}
}
