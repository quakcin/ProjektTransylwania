package pl.gr14b.transylwania;

import java.awt.*;
import java.io.Serializable;

class Room implements Serializable
{
	private int x;
	private int y;
	private int texture;

	Room (int x, int y)
	{
		this.x = x;
		this.y = y;
		this.texture = Constants.random(0, 11);
	}

	void paintRoom (GraphicsPainter graphicsPainter)
	{
		int rx = graphicsPainter.getOffX() + (int) (graphicsPainter.getPlayer().getX() - getX() * 810 - 810);
		int ry = graphicsPainter.getOffY() + (int) (graphicsPainter.getPlayer().getY() - getY() * 810 - 810);
		graphicsPainter.getG().drawImage(
				graphicsPainter.getStuff().getRooms().get(getTexture()),
				rx, ry, 810, 810, null
		);
	}

	int getX()
	{
		return x;
	}
	int getY()
	{
		return y;
	}
	int getTexture ()
	{
		return texture;
	}
}
