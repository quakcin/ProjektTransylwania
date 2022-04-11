package pl.gr14b.transylwania;

abstract public class ClientHandlePacket
{
	String packetClass;
	Game clientGame;
	Client client;
	ClientHandlePacket (Client client, String packetClass)
	{
		this.client = client;
		this.packetClass = packetClass;
	}

	boolean isPacket (String otherPacketClass)
	{
		return packetClass.equals(otherPacketClass);
	}

	void handlePacket (Object packet)
	{
		this.clientGame = client.getClientGame();
		packetHandler(packet);
	}

	abstract void packetHandler(Object packet);



}

