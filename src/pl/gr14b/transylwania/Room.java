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
		this.texture = Constants.random(
				0, Constants.ROOM_TEXTURE_COUNT
		);
	}

	void paintRoom (GraphicsPainter graphicsPainter)
	{
		int rx = graphicsPainter.getOffX() + (int) (
				graphicsPainter.getPlayer().getX()
				- getX() * Constants.DEFAULT_ROOM_SIZE
				- Constants.DEFAULT_ROOM_SIZE
		);

		int ry = graphicsPainter.getOffY() + (int) (
				graphicsPainter.getPlayer().getY()
				- getY() * Constants.DEFAULT_ROOM_SIZE
				- Constants.DEFAULT_ROOM_SIZE
		);

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
