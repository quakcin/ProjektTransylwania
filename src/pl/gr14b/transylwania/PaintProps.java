package pl.gr14b.transylwania;

class PaintProps extends GraphicsPainter
{
	PaintProps(Client client) {
		super(client);
	}

	@Override
	void paint()
	{
		paintOrdinaryProps();
		paintLamps();
		paintChests();
	}

	private void paintOrdinaryProps()
	{
		for (Prop prop : clientGame.getProps())
			prop.Draw(g, offX, offY, stuff, clientGame);
	}

	private void paintLamps()
	{
		for (Lamp lamp : clientGame.getLamps())
			lamp.Draw(g, offX, offY, stuff, clientGame);
	}

	private void paintChests()
	{
		for (Chest chest : clientGame.getChests())
			chest.Draw(g, offX, offY, stuff, clientGame);
	}

}
