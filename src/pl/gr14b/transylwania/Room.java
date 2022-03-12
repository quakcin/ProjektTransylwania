package pl.gr14b.transylwania;

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
		this.texture = (int) Math.round(Math.random() * 3);
	}

	int getX() {
		return x;
	}

	int getY() {
		return y;
	}

	int getTexture ()
	{
		return texture;
	}
}
