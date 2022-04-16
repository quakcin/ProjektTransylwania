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
		for (LampProp lampProp : clientGame.getLamps())
			lampProp.Draw(g, offX, offY, stuff, clientGame);
	}

	private void paintChests()
	{
		for (ChestProp chestProp : clientGame.getChests())
			chestProp.Draw(g, offX, offY, stuff, clientGame);
	}

}
