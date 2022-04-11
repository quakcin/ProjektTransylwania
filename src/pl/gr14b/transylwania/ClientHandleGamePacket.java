package pl.gr14b.transylwania;

public class ClientHandleGamePacket extends ClientHandlePacket
{
	ClientHandleGamePacket(Client client, String packetClass) {
		super(client, packetClass);
	}

	@Override
	void packetHandler(Object packet)
	{
		client.setClientGame((Game) packet);
	}
}
