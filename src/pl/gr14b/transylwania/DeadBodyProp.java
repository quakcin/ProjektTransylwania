package pl.gr14b.transylwania;

import java.awt.*;
import java.io.Serializable;

class DeadBodyProp extends Prop implements Serializable
{

	DeadBodyProp(int x, int y) {
		super(x, y, Constants.random(0, 3));
	}

	@Override
	void Draw(Graphics g, int offX, int offY, Stuff stuff, Game clientGame)
	{
		int dx = offX + (int) (clientGame.getPlayer().getX() - getX());
		int dy = offY + (int) (clientGame.getPlayer().getY() - getY());
		g.drawImage(
				stuff.getDeadBodies().get(getSprite()).getImage(),
				dx, dy,
				Constants.DEFAULT_GRID_SIZE,
				Constants.DEFAULT_GRID_SIZE,
				null
		);
	}
}
