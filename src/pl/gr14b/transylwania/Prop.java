package pl.gr14b.transylwania;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

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

class Chest extends Prop implements Serializable
{
	private UUID playerUUID;
	Chest(int x, int y)
	{
		super(x, y, 0);
		playerUUID = null;
	}

	@Override
	void Draw(Graphics g, int offX, int offY, Stuff stuff, Game clientGame)
	{
		int dx = offX + (int) (clientGame.getPlayer().getX() - getX());
		int dy = offY + (int) (clientGame.getPlayer().getY() - getY());
		g.drawImage(stuff.getChest().getImage(), dx, dy, 70, 70, null);
	}

	public UUID getPlayerUUID() {
		return playerUUID;
	}

	public void setPlayerUUID(UUID playerUUID) {
		this.playerUUID = playerUUID;
	}

	public static boolean isPlayerHidden (ArrayList<Chest> chests, UUID playerID)
	{
		for (Chest chest : chests)
			if (chest.getPlayerUUID() != null)
				if (chest.getPlayerUUID().equals(playerID))
					return true;
		return false;
	}

	public void UseChest (Player player, Game game)
	{
		if (playerUUID == null)
		{
			// hide inside chest!
			playerUUID = player.getPlayerID();
			player.setX(getX());
			player.setY(getY());
			game.playSoundNear(getX(), getY(), 300, "chest_in");
		}
		else if (playerUUID.equals(player.getPlayerID()))
		{
			// leave chest!
			playerUUID = null;
			game.playSoundNear(getX(), getY(), 810, "chest_out");
			player.setAfkPenalty(0);
		}
		else
		{
			// someone else is already in the chest
			playerUUID = null;
			game.playSoundNear(getX(), getY(), 810, "chest_blocked");
			player.setAfkPenalty(0);
		}
	}

}

class Lamp extends Prop implements Serializable
{
	private final static int LAMP_DISABLED = 0;
	private final static int LAMP_ENABLED = 1;
	private final static int LAMP_BROKEN = 2;

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
		if (player.getPlayerType().equals(PlayerType.VAMPIRE)) // FIXME: .Make it more functional.
		{
			if (status == Lamp.LAMP_ENABLED) {
				status = Lamp.LAMP_BROKEN;
				serverGame.setGameTime(serverGame.getGameTime() + Game.LAMP_PENALTY);
				isEvent = true;
				serverGame.playSoundNear(getX(), getY(), 1000, "lightDown");
			}
		}
		else if (player.getPlayerType().equals(PlayerType.SURVIVOR))
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
			player.Face(getX() - 20, getY() - 20);
			player.setSpacePressedDisabled(25);
			player.setForcingSynchronization(true);
		}
	}

	void BlowOut ()
	{
		this.status = 0;
		setSprite();
	}

}