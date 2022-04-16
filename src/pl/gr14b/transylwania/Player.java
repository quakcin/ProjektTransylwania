package pl.gr14b.transylwania;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;


enum PlayerType
{
	SURVIVOR, VAMPIRE, GHOST
}

class Player implements Serializable
{

	private PlayerLocation playerLocation;
	private PlayerFlags playerFlags;

	private String nickName;
	private UUID playerID;
	private int character;
	private int health;


	Player(UUID playerID, String playerNickName, ArrayList<Player> existingPlayers)
	{

		this.playerLocation = new PlayerLocation(this);
		this.playerFlags = new PlayerFlags();

		this.nickName = playerNickName;
		this.playerID = playerID;
		this.health = 3;

		teleportToSpawn(existingPlayers);
	}

	UUID getPlayerID()
	{
		return playerID;
	}

	double getX()
	{
		return playerLocation.getX();
	}

	double getY()
	{
		return playerLocation.getY();
	}

	double getAng()
	{
		return playerLocation.getAng();
	}

	private String getNickName()
	{
		return nickName;
	}

	void push (double speed, Game clientGame)
	{
		playerLocation.push(speed, clientGame);
	}

	void turn (double delta)
	{
		playerLocation.turn(delta);
	}

	void copyLocation (Player otherPlayer)
	{
		playerLocation.copyLocation(otherPlayer);
	}

	private int getCharacter()
	{
		return character;
	}

	void teleportToSpawn(ArrayList<Player> allPlayers)
	{
		playerLocation.teleportToSpawn(allPlayers);
	}

	PlayerType getPlayerType()
	{
		return playerFlags.getPlayerType();
	}

	void setPlayerType(PlayerType playerType)
	{
		playerFlags.setPlayerType(playerType);
	}

	boolean isSpacePressed() {
		return playerFlags.isSpacePressed();
	}

	boolean isNotGhostAndHasSpacePressed()
	{
		return !getPlayerType().equals(PlayerType.GHOST)
				&& isSpacePressed();
	}

	void setSpacePressedDisabled(int delay)
	{
		playerFlags.setSpacePressedDisabled(delay);
	}

	void setSpacePressedEnabled()
	{
		playerFlags.setSpacePressedEnabled();
	}

	void copyFlags(Player otherPlayer)
	{
		playerFlags.copyFlags(otherPlayer);
	}

	PlayerFlags getPlayerFlags()
	{
		return playerFlags;
	}

	boolean isForcingSynchronization()
	{
		return playerFlags.isForcingSynchronization();
	}

	void setForcingSynchronization(boolean forcingSynchronization)
	{
		playerFlags.setForcingSynchronization(forcingSynchronization);
	}

	void Reset(ArrayList<Player> allPlayers)
	{
		playerFlags.reset();
		this.health = Constants.DEFAULT_PLAYER_HEALTH;
		teleportToSpawn(allPlayers);
	}

	int getHealth()
	{
		return health;
	}

	void setAng (double ang)
	{
		playerLocation.setAng(ang);
	}

	private void setHealth(int health)
	{
		if (health < 0)
			this.health = 0;
		else this.health = Math.min(health, Constants.DEFAULT_PLAYER_HEALTH);
	}

	double getDist(double dx, double dy)
	{
		return playerLocation.getDist(dx, dy);
	}

	void updateFlagStatusCounters()
	{
		playerFlags.updateFlagStatusCounters();
	}

	void randomizeCharacter()
	{
		int oldCharacter = this.character;

		while (oldCharacter == this.character)
		{
			this.character = Constants.random(0, Constants.CHARACTER_SPRITE_COUNT);
		}

		setForcingSynchronization(true);
	}

	private void splatBloodAroundPlayer (Game serverGame)
	{
		serverGame.getProps().add(new BloodProp(
				(int) getX() + Constants.random(
						Constants.BLOOD_SPLASH_MIN_OFFSET,
						Constants.BLOOD_SPLASH_MAX_OFFSET
				),
				(int) getY() + Constants.random(
						Constants.BLOOD_SPLASH_MIN_OFFSET,
						Constants.BLOOD_SPLASH_MAX_OFFSET
				)
		));
	}

	private void stabPlayerWithAKnife (Game serverGame)
	{
		serverGame.playSoundNear(getX(), getY(), Constants.STAB_SOUND_SPREAD, "stab");
		splatBloodAroundPlayer(serverGame);
	}

	private void removePlayerFromChest (Game serverGame)
	{
		for (ChestProp chestProp : serverGame.getChests())
			if (matchPlayersUUID(chestProp.getPlayerUUID()))
				chestProp.setPlayerUUID(null);
	}

	private boolean isPlayerDeath ()
	{
		return getHealth() <= 0;
	}

	private void killPlayerOnSite (Game serverGame)
	{
		setPlayerType(PlayerType.GHOST);
		serverGame.getProps().add(new DeadBodyProp(
				(int) getX() + Constants.random(
						Constants.BLOOD_SPLASH_MIN_OFFSET,
						Constants.BLOOD_SPLASH_MAX_OFFSET
				),
				(int) getY() + Constants.random(
						Constants.BLOOD_SPLASH_MIN_OFFSET,
						Constants.BLOOD_SPLASH_MAX_OFFSET
				)
		));
		serverGame.playSoundNear(getX(), getY(), Constants.DEATH_SOUND_SPREAD, "death");
	}

	void Damage (Game serverGame)
	{
		setHealth(getHealth() - 1);
		stabPlayerWithAKnife(serverGame);
		removePlayerFromChest(serverGame);
		setForcingSynchronization(true);

		if (isPlayerDeath())
			killPlayerOnSite(serverGame);
	}

	void facePlayer (Player otherPlayer)
	{
		playerLocation.facePlayer(otherPlayer);
	}

	void face (double dx, double dy)
	{
		playerLocation.face(dx, dy);
	}

	int getNextPacket()
	{
		return playerFlags.getNextPacket();
	}

	void nextPacket()
	{
		playerFlags.nextPacket();
	}

	private ImageIcon getSurvivorSplash(Stuff stuff)
	{
		return isPlayerMoving()
				? stuff.getSurvivorsWalking().get(getCharacter())
				: stuff.getSurvivorsStanding().get(getCharacter())
				;
	}

	private ImageIcon getVampSplash(Stuff stuff)
	{
		if (isSpacePressed())
			return stuff.getVampAttacking();

		return isPlayerMoving()
				? stuff.getVampWalking()
				: stuff.getVampStanding()
				;
	}

	private ImageIcon getGhostSplash(Stuff stuff)
	{
		return stuff.getGhost();
	}

	boolean isPlayerASurvivor()
	{
		return getPlayerType().equals(PlayerType.SURVIVOR);
	}

	boolean isPlayerAVampire()
	{
		return getPlayerType().equals(PlayerType.VAMPIRE);
	}

	boolean isPlayerAGhost ()
	{
		return getPlayerType().equals(PlayerType.GHOST);
	}

	private ImageIcon getPlayerSplash(Stuff stuff)
	{
		if (isPlayerASurvivor())
			return getSurvivorSplash(stuff);

		if (isPlayerAVampire())
			return getVampSplash(stuff);

		return getGhostSplash(stuff);
	}


	private BufferedImage getScaledAndRotatedPlayerSplash(ImageIcon pfp)
	{
		AffineTransform aft = AffineTransform.getRotateInstance(
				getAng() + Constants.PLAYER_SPRITE_FIXED_ANGLE,
				pfp.getIconWidth() >> 1,
				pfp.getIconHeight() >> 1
		);
		AffineTransformOp aftop = new AffineTransformOp(aft, AffineTransformOp.TYPE_BILINEAR);

		BufferedImage bfi = new BufferedImage(
				pfp.getIconWidth(),
				pfp.getIconHeight(),
				BufferedImage.TYPE_INT_ARGB
		);
		Graphics bfig = bfi.createGraphics();
		pfp.paintIcon(null, bfig, 0, 0);
		return aftop.filter(bfi, null);
	}

	private boolean isSupposedToDrawPlayerNickname(Game clientGame)
	{
		return clientGame.getGameStatus() == GameStatus.LOBBY;
	}

	private void drawNicknameAbovePlayersHead (GraphicsPainter graphicsPainter, int cx, int cy)
	{
		if (!isSupposedToDrawPlayerNickname(graphicsPainter.clientGame))
			return;
		graphicsPainter.getG().setFont(
				graphicsPainter.getStuff().getFont().deriveFont(Constants.PLAYER_NICKNAME_FONT_SIZE)
		);
		graphicsPainter.getG().setColor(new Color(255, 221, 0));
		graphicsPainter.getG().drawString(
				getNickName(),
				(int) (cx - (getNickName().length() * Constants.DEFAULT_NICK_DELTA)),
				cy - Constants.DEFAULT_NICK_OFFSET
		);
	}

	void drawPlayer (GraphicsPainter graphicsPainter)
	{
		int cx = graphicsPainter.getOffX() + (int) (graphicsPainter.getPlayer().getX() - getX());
		int cy = graphicsPainter.getOffY() + (int) (graphicsPainter.getPlayer().getY() - getY());

		ImageIcon pfp = getPlayerSplash(graphicsPainter.getStuff());
		graphicsPainter.getG().drawImage(
				getScaledAndRotatedPlayerSplash(pfp),
				cx - Constants.PLAYER_OFFSET,
				cy - Constants.PLAYER_OFFSET,
				Constants.PLAYER_SIZE,
				Constants.PLAYER_SIZE,
				null
		);

		drawNicknameAbovePlayersHead(graphicsPainter, cx, cy);
	}


	void playStepSound ()
	{
		if (getPlayerType().equals(PlayerType.SURVIVOR))
			Stuff.playSound("survStep");
		else if (getPlayerType().equals(PlayerType.VAMPIRE))
			Stuff.playSound("vampStep");
		else if (getPlayerType().equals(PlayerType.GHOST))
			Stuff.playSound("ghostStep");
	}


	boolean isPlayerMoving()
	{
		return playerFlags.isPlayerMoving();
	}

	void setPlayerMoving(boolean playerMoving)
	{
		playerFlags.setPlayerMoving(playerMoving);
	}

	String getNextSoundInQue()
	{
		return playerFlags.getNextSoundInQue();
	}

	void setNextSoundInQue(String nextSoundInQue)
	{
		playerFlags.setNextSoundInQue(nextSoundInQue);
	}

	boolean isForcingLocationSynchronization()
	{
		return playerFlags.isForcingLocationSynchronization();
	}

	void setForcingLocationSynchronization ( boolean forcingLocationSynchronization )
	{
		playerFlags.setForcingLocationSynchronization ( forcingLocationSynchronization ) ;
	}

	void setX (double x)
	{
		playerLocation.setX(x);
	}

	void setY (double y)
	{
		playerLocation.setY(y);
	}

	int getAfkPenalty()
	{
		return playerFlags.getAfkPenalty();
	}

	void setAfkPenalty(int afkPenalty)
	{
		playerFlags.setAfkPenalty(afkPenalty);
	}

	private boolean matchPlayersUUID (UUID otherPlayerID)
	{
		return otherPlayerID != null
				&& otherPlayerID.equals(getPlayerID());
	}

	boolean isHidden (ArrayList<ChestProp> chestProps)
	{
		for (ChestProp chestProp : chestProps)
			if (matchPlayersUUID(chestProp.getPlayerUUID()))
				return true;
		return false;
	}
}
