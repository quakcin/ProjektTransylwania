package pl.gr14b.transylwania;

import java.awt.*;
import java.io.Serializable;

class BloodProp extends Prop implements Serializable
{

	BloodProp(int x, int y) {
		super(x, y, Constants.random(0, 4));
	}

	@Override
	void Draw(Graphics g, int offX, int offY, Stuff stuff, Game clientGame)
	{
		int dx = offX + (int) (clientGame.getPlayer().getX() - getX());
		int dy = offY + (int) (clientGame.getPlayer().getY() - getY());
		g.drawImage(
				stuff.getBloodSplashes().get(getSprite()).getImage(),
				dx, dy,
				Constants.DEFAULT_GRID_SIZE,
				Constants.DEFAULT_GRID_SIZE,
				null
		);
	}
}