package pl.gr14b.transylwania;

class PaintBackground extends GraphicsPainter
{

	PaintBackground(Client client) {
		super(client);
	}

	@Override
	void paint()
	{
		g.drawImage(stuff.getBackground(), 0, 0, width, height, null);
	}

}
