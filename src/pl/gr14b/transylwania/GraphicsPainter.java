package pl.gr14b.transylwania;

import java.awt.*;

public abstract class GraphicsPainter
{
	protected Client client;
	protected Stuff stuff;

	Game clientGame;
	Graphics g;

	int offX;
	int offY;
	int width;
	int height;

	GraphicsPainter (Client client)
	{
		this.client = client;
	}

	void beginPainting (ClientGraphics clientGraphics, Graphics g)
	{
		this.g = g;
		clientGame = client.getClientGame();
		stuff = client.getStuff();

		if (clientGame == null || stuff == null)
			return;

		width = clientGraphics.getBounds().width;
		height = clientGraphics.getBounds().height;
		offX = width >> 1;
		offY = height >> 1;
		paint();
	}

	abstract void paint ();

	public Stuff getStuff() {
		return stuff;
	}

	public Graphics getG() {
		return g;
	}

	public int getOffX() {
		return offX;
	}

	public int getOffY() {
		return offY;
	}

	public Player getPlayer ()
	{
		return clientGame.getPlayer();
	}
}
