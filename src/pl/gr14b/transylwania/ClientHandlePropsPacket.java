package pl.gr14b.transylwania;

public class ClientHandlePropsPacket extends ClientHandlePacket
{
	ClientHandlePropsPacket(Client client, String packetClass) {
		super(client, packetClass);
	}

	@Override
	void packetHandler(Object packet)
	{
		clientGame.setProps(
				((PropsPacket) packet).getProps()
		);
	}
}
