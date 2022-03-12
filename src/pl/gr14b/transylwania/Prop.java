package pl.gr14b.transylwania;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

// TODO: Make a proper extended class instead of state-machine
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

	void setSprite (int sprite)
	{
		this.sprite = sprite;
	}

	abstract void Draw(Graphics g, int offX, int offY, Stuff stuff, Game clientGame);
}

class Blood extends Prop implements Serializable
{

	Blood(int x, int y) {
		super(x, y, Stuff.RandomBloodSplash());
	}

	@Override
	void Draw(Graphics g, int offX, int offY, Stuff stuff, Game clientGame)
	{
		int dx = offX + (int) (clientGame.getPlayer().getX() - getX());
		int dy = offY + (int) (clientGame.getPlayer().getY() - getY());
		g.drawImage(stuff.getBloodSplashes().get(getSprite()).getImage(), dx, dy, 90, 90, null);
	}
}

class DeadBody extends Prop implements Serializable
{

	DeadBody(int x, int y) {
		super(x, y, Stuff.RandomDeadBody());
	}

	@Override
	void Draw(Graphics g, int offX, int offY, Stuff stuff, Game clientGame)
	{
		int dx = offX + (int) (clientGame.getPlayer().getX() - getX());
		int dy = offY + (int) (clientGame.getPlayer().getY() - getY());
		g.drawImage(stuff.getDeadBodies().get(getSprite()).getImage(), dx, dy, 90, 90, null);
	}
}


class Lamp extends Prop implements Serializable
{
	final static int LAMP_DISABLED = 0;
	final static int LAMP_ENABLED = 1;
	final static int LAMP_BROKEN = 2;

	// Fields
	private int status;

	Lamp(int x, int y)
	{
		super(x, y, 1);
		status = Lamp.LAMP_ENABLED;
	}

	@Override
	void Draw(Graphics g, int offX, int offY, Stuff stuff, Game clientGame)
	{
		int dx = offX + (int) (clientGame.getPlayer().getX() - getX());
		int dy = offY + (int) (clientGame.getPlayer().getY() - getY());
		g.drawImage(stuff.getLamps().get(status).getImage(), dx, dy, 40, 40, null);
	}

	void UseLamp (Player player, Game serverGame)
	{
		boolean isEvent = false;
		if (player.getPlayerType() == Player.PLAYER_TYPE_VAMPIRE) // FIXME: .Make it more functional.
		{
			if (status == Lamp.LAMP_ENABLED) {
				status = Lamp.LAMP_BROKEN;
				serverGame.setGameTime(serverGame.getGameTime() + Game.LAMP_PENALTY);
				isEvent = true;
				serverGame.playSoundNear(getX(), getY(), 1000, "lightDown");
			}
		}
		else if (player.getPlayerType() == Player.PLAYER_TYPE_SURVIVOR)
		{
			if (status == Lamp.LAMP_DISABLED) {
				status = Lamp.LAMP_ENABLED;
				serverGame.setGameTime(serverGame.getGameTime() - Game.LAMP_BONUS);
				isEvent = true;
				serverGame.playSoundNear(getX(), getY(), 1000, "lightUp");
			}
		}

		if (isEvent)
		{
			player.Face(getX() - 25, getY() - 25);
			player.setSpacePressedDisabled(75);
			player.setForcingSynchronization(true);
		}
	}

	void BlowOut ()
	{
		this.status = 0;
		setSprite(0);
	}

}