package pl.gr14b.transylwania;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class ClientGraphics extends JPanel
{

	private ArrayList<GraphicsPainter> painters;
	private Client client;

	ClientGraphics(Client client)
	{
		super();
		this.client = client;
		this.painters = new ArrayList<>();
		packPainters();
	}

	@Override
	protected void paintComponent (Graphics g)
	{
		super.paintComponent(g);

		for (GraphicsPainter painter : painters)
			painter.beginPainting(this, g);

		g.dispose();
	}

	private void packPainters()
	{
		painters.add(new PaintBackground(client));
		painters.add(new PaintMap(client));
		painters.add(new PaintProps(client));
		painters.add(new PaintPlayers(client));
		painters.add(new PaintHUD(client));
	}

}