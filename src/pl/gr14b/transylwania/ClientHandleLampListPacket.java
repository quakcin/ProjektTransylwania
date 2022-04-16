package pl.gr14b.transylwania;

public class ClientHandleLampListPacket extends ClientHandlePacket
{
	ClientHandleLampListPacket(Client client, String packetClass) {
		super(client, packetClass);
	}

	@Override
	void packetHandler(Object packet)
	{
		LampsListPacket lampsListPacket = (LampsListPacket) packet;
		clientGame.setLamps(lampsListPacket.getLampProps());
		client.setGlobalLight(lampsListPacket.getGlobalLight());
	}
}
