package pl.gr14b.transylwania;

import java.awt.*;
import java.io.Serializable;

abstract class Prop implements Serializable
{
	private int x;
	private int y;
	private int sprite;

	Prop (int x, int y, int sprite)
	{
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}

	int getX ()
	{
		return x;
	}

	int getY ()
	{
		return y;
	}

	int getSprite ()
	{
		return this.sprite;
	}

	void setSprite()
	{
		this.sprite = 0;
	}

	abstract void Draw(Graphics g, int offX, int offY, Stuff stuff, Game clientGame);

	double getDist (double dx, double dy)
	{
		return Math.sqrt(Math.pow(dx - getX(), 2) + Math.pow(dy - getY(), 2));
	}
}


