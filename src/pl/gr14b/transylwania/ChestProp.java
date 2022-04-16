package pl.gr14b.transylwania;


import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

class ChestProp extends Prop implements Serializable
{
	private UUID playerUUID;
	ChestProp(int x, int y)
	{
		super(x, y, 0);
		playerUUID = null;
	}

	@Override
	void Draw(Graphics g, int offX, int offY, Stuff stuff, Game clientGame)
	{
		int dx = offX + (int) (clientGame.getPlayer().getX() - getX());
		int dy = offY + (int) (clientGame.getPlayer().getY() - getY());
		g.drawImage(
				stuff.getChest().getImage(),
				dx, dy,
				Constants.CHEST_RENDER_SIZE,
				Constants.CHEST_RENDER_SIZE,
				null
		);
	}

	public UUID getPlayerUUID() {
		return playerUUID;
	}

	public void setPlayerUUID(UUID playerUUID) {
		this.playerUUID = playerUUID;
	}

	public static boolean isPlayerHidden (ArrayList<ChestProp> chestProps, UUID playerID)
	{
		for (ChestProp chestProp : chestProps)
			if (chestProp.getPlayerUUID() != null)
				if (chestProp.getPlayerUUID().equals(playerID))
					return true;
		return false;
	}

	public void UseChest (Player player, Game game)
	{
		if (!isSomebodyInsideThisChest())
			hidePlayerInChest(player, game);
		else
			makePlayerLeaveTheChest(player, game);
	}

	private void makePlayerLeaveTheChest (Player player, Game game)
	{
		playerUUID = null;
		game.playSoundNear(getX(), getY(), Constants.DEFAULT_ROOM_SIZE, getLeaveSoundEffect(player));
		player.setAfkPenalty(0);
	}

	private String getLeaveSoundEffect (Player player)
	{
		return isPlayerInChest(player)
				? "chest_out"
				: "chest_blocked"
				;
	}

	private boolean isPlayerInChest (Player player)
	{
		return playerUUID != null
				&& playerUUID.equals(player.getPlayerID());
	}

	private void hidePlayerInChest (Player player, Game game)
	{
		playerUUID = player.getPlayerID();
		player.setX(getX());
		player.setY(getY());
		game.playSoundNear(getX(), getY(), Constants.DEFAULT_ROOM_SIZE, "chest_in");
	}

	boolean isSomebodyInsideThisChest ()
	{
		return playerUUID != null;
	}
}

