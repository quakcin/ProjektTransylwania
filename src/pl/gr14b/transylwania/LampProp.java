package pl.gr14b.transylwania;

import java.awt.*;
import java.io.Serializable;

enum LampState
{
	DISABLED, ENABLED, BROKEN
}

class LampProp extends Prop implements Serializable
{
	private LampState status;

	LampProp(int x, int y)
	{
		super(x, y, 1);
		status = LampState.ENABLED;
	}

	@Override
	void Draw(Graphics g, int offX, int offY, Stuff stuff, Game clientGame)
	{
		int dx = offX + (int) (clientGame.getPlayer().getX() - getX());
		int dy = offY + (int) (clientGame.getPlayer().getY() - getY());
		g.drawImage(
				stuff.getLamps().get(status.ordinal()).getImage(),
				dx, dy,
				Constants.LAMP_RENDER_SIZE,
				Constants.LAMP_RENDER_SIZE,
				null
		);
	}

	void UseLamp (Player player, Game serverGame)
	{
		if (!willStatusChange(player))
			return;

		updateGameTimeAndPlaySound(player, serverGame);
		makePlayerFaceLampAndSyncWithServer(player);
		status = getNewStatus(player);
	}

	private void makePlayerFaceLampAndSyncWithServer (Player player)
	{
		player.face(
				getX() - Constants.DEFAULT_ROOM_PADDING,
				getY() - Constants.DEFAULT_ROOM_PADDING
		);

		player.setSpacePressedDisabled(Constants.LAMP_USE_DELAY);
		player.setForcingSynchronization(true);
	}

	private void updateGameTimeAndPlaySound (Player player, Game serverGame)
	{
		serverGame.setGameTime(
				serverGame.getGameTime() + getGameTimeDelta(player)
		);

		serverGame.playSoundNear(
				getX(), getY(),
				Constants.LAMP_SOUND_SPREAD,
				getLampInteractionSoundName(player)
		);
	}


	private LampState getNewStatus (Player player)
	{
		return canBreak(player)
				? LampState.BROKEN
				: LampState.ENABLED
				;
	}

	private int getGameTimeDelta (Player player)
	{
		return canBreak(player)
				? Constants.LAMP_PENALTY
				: Constants.LAMP_BONUS
				;
	}

	private String getLampInteractionSoundName (Player player)
	{
		return canBreak(player)
				? "lightDown"
				: "lightUp"
				;
	}

	private boolean canBreak (Player player)
	{
		return player.isPlayerAVampire() && isEnabled();
	}

	private boolean canEnable (Player player)
	{
		return player.isPlayerASurvivor() && isDisabled();
	}

	private boolean willStatusChange (Player player)
	{
		return canBreak(player)
				|| canEnable(player);
	}



	private boolean isEnabled ()
	{
		return status.equals(LampState.ENABLED);
	}

	private boolean isDisabled()
	{
		return status.equals(LampState.DISABLED);
	}

	void BlowOut ()
	{
		this.status = LampState.DISABLED;
		setSprite();
	}

}