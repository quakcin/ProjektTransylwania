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

class Player implements Serializable {
	// Flags and stuff

	private static final int PLAYER_MODELS_COUNT = 8;

	// Player specific boolean flags
	private boolean spacePressed;
	private int spacedPressedEnabled;
	private boolean forcingSynchronization;
	private boolean forcingLocationSynchronization;
	private boolean playerMoving;


	private String nickName;
	private UUID playerID;
	private double x;
	private double y;
	private double ang;
	private int character;
	private int afkPenalty;

	private PlayerType playerType;
	private int health;
	private int nextPacket;
	private String nextSoundInQue;

	Player(UUID playerID, String playerNickName, ArrayList<Player> existingPlayers) {
		// This is ALWAYS default
		this.forcingSynchronization = false;
		this.nickName = playerNickName;
		this.playerType = PlayerType.SURVIVOR;
		this.playerID = playerID;
		this.health = 3;
		this.nextPacket = 0;
		this.nextSoundInQue = null;
		this.forcingLocationSynchronization = false;

		// Player default position, TODO: Move this to a separate function
		teleportToSpawn(existingPlayers);
		this.ang = 0;

		// Player Flags
		this.spacePressed = false;
		this.spacedPressedEnabled = 0;
		this.playerMoving = false;
	}

	UUID getPlayerID() {
		return playerID;
	}


	double getX() {
		return x;
	}

	double getY() {
		return y;
	}

	double getAng() {
		return ang;
	}

	String getNickName() {
		return nickName;
	}


	private int pushAttempts;
	void Push (double speed, Game clientGame)
	{
		if (Chest.isPlayerHidden(clientGame.getChests(), playerID))
			return;

		pushAttempts = 0;
		RecursivePush(playerType.equals(PlayerType.VAMPIRE)? speed * 1.2 : speed, clientGame);
	}

	private void RecursivePush(double speed, Game clientGame) {
		// TODO: Take speed into account
		final double collider = 15;
		double vx = getX() + -1 * Math.cos(ang) * speed;
		double vy = getY() + -1 * Math.sin(ang) * speed;

		if (playerType.equals(PlayerType.GHOST)) {
			x = vx;
			y = vy;
			return;
		}

		boolean allow = true;
		for (int i = -1; i <= 1; i += 2)
			for (int j = -1; j <= 1; j += 2) {
				int mx = (int) ((vx + i * collider) / 90) % 9;
				int my = (int) ((vy + j * collider) / 90) % 9;

				// wall collision detection:
				if (((mx == 0 || mx == 8) && my != 4) || ((my == 0 || my == 8) && mx != 4)) {
					allow = false;
					break;
				}
			}

		// door collision detection:
		for (int y = 0; y < clientGame.MAP_SIZE + 1; y++)
			for (int x = 0; x < clientGame.MAP_SIZE; x++) {
				int index = y * (clientGame.MAP_SIZE) + x;
				int dx = x * 810 - 450 + 90 + 810;
				int dy = y * 810;
				double doorDist = Math.sqrt(Math.pow(vx - dx, 2) + Math.pow(vy - dy, 2));

				if (doorDist < 90 && clientGame.getHorizontalDoors()[index]) {
					allow = false;
					break;
				}
			}

		for (int y = 0; y < clientGame.MAP_SIZE; y++)
			for (int x = 0; x < clientGame.MAP_SIZE + 1; x++) {
				int index = x * (clientGame.MAP_SIZE) + y;
				int dx = x * 810;
				int dy = y * 810 - 450 + 90 + 810;
				double doorDist = Math.sqrt(Math.pow(vx - dx, 2) + Math.pow(vy - dy, 2));

				if (doorDist < 90 && clientGame.getVerticalDoors()[index]) {
					allow = false;
					break;
				}
			}

		for (Player p : clientGame.getPlayers())
			if (p.getPlayerID() != getPlayerID() && !p.getPlayerType().equals(PlayerType.GHOST))
				if (!Chest.isPlayerHidden(clientGame.getChests(), p.getPlayerID()))
					if (Math.sqrt(Math.pow(vx - p.getX(), 2) + Math.pow(vy - p.getY(), 2)) < 50) {
						allow = false;
						break;
					}

		if (allow)
		{
			x = vx;
			y = vy;
		}
		else
		{
			ang += 180 * (3.1415 / 180);
			pushAttempts += 1;
			if (pushAttempts < 10)
				RecursivePush(10, clientGame);
			ang += 180 * (3.1415 / 180);
		}
	}

	void Turn(double delta) {
		ang += delta * (3.1415 / 180);
	}

	void copyLocation(Player otherPlayer) {
		x = otherPlayer.getX();
		y = otherPlayer.getY();
		ang = otherPlayer.getAng();
	}

	int getCharacter() {
		return character;
	}

	private void serverTeleport(double dx, double dy) {
		x = dx;
		y = dy;
		forcingSynchronization = true;
	}

	private double distanceToNearestPlayer(ArrayList<Player> allPlayers) {
		if (allPlayers.size() <= 1)
			return 0xFFFFFF;

		double nearest = 0xFFFFFF;
		for (Player p : allPlayers)
			if (p.getPlayerID() != playerID) {
				double dist = Math.sqrt(Math.pow(p.getX() - x, 2) + Math.pow(p.getY() - y, 2));
				if (dist < nearest)
					nearest = dist;
			}
		return nearest;
	}

	void teleportToSpawn(ArrayList<Player> allPlayers) {
		do {
			serverTeleport(2 * 810 + ((Math.random() * 6) + 1) * 100, 2 * 810 + ((Math.random() * 6) + 1) * 100);
		}
		while (distanceToNearestPlayer(allPlayers) < 90);
	}

	PlayerType getPlayerType() {
		return playerType;
	}

	void setPlayerType(PlayerType playerType) {
		this.playerType = playerType;
	}

	boolean isSpacePressed() {
		return spacePressed;
	}

	boolean isNotGhostAndHasSpacePressed()
	{
		return !getPlayerType().equals(PlayerType.GHOST)
				&& isSpacePressed();
	}

	void setSpacePressedDisabled(int delay) {
		spacePressed = false;
		spacedPressedEnabled = delay;
		forcingSynchronization = true;
	}

	void setSpacePressedEnabled() {
		spacePressed = (spacedPressedEnabled <= 0);
	}

	void copyFlags(Player otherPlayer) {
		spacePressed = otherPlayer.spacePressed;
		spacedPressedEnabled = otherPlayer.spacedPressedEnabled;
		nextPacket = otherPlayer.nextPacket;
		playerMoving = otherPlayer.playerMoving;
		nextSoundInQue = otherPlayer.nextSoundInQue;
	}

	boolean isForcingSynchronization() {
		return forcingSynchronization;
	}

	void setForcingSynchronization(boolean forcingSynchronization) {
		this.forcingSynchronization = forcingSynchronization;
	}

	void Reset(ArrayList<Player> allPlayers) {
		this.forcingSynchronization = true;
		this.spacePressed = false;
		this.health = 3;
		playerType = PlayerType.SURVIVOR;
		teleportToSpawn(allPlayers);
	}

	int getHealth() {
		return health;
	}

	void setAng (double ang)
	{
		this.ang = ang;
	}

	private void setHealth(int health) {
		if (health < 0)
			this.health = 0;
		else this.health = Math.min(health, 3);
	}

	double getDist(double dx, double dy) {
		return Math.sqrt(Math.pow(dx - x, 2) + Math.pow(dy - y, 2));
	}

	void updateFlagStatusCounters() {
		spacedPressedEnabled = Math.max(spacedPressedEnabled - 1, 0);
	}

	void randomizeCharacter() {
		if (Player.PLAYER_MODELS_COUNT == 1)
			return;

		int oldCharacter = this.character;

		do {
			this.character = Stuff.random(0, PLAYER_MODELS_COUNT - 1);
		}
		while (oldCharacter == this.character);
		setForcingSynchronization(true);
	}

	void Damage(Game serverGame) // oop is rly gut
	{
		setHealth(getHealth() - 1);

		// Spawn Blood
		// TODO: Make the randomization a proper constructor
		serverGame.getProps().add(new Blood(
				(int) getX() + Stuff.random(15, 90), (int) getY() + Stuff.random(15, 90)
		));

		serverGame.playSoundNear(getX(), getY(), 810 * 6, "stab");

		for (Chest chest : serverGame.getChests())
			if (chest.getPlayerUUID() != null)
				if (chest.getPlayerUUID().equals(playerID))
					chest.setPlayerUUID(null);

		// Check if player died
		if (getHealth() <= 0) {
			setPlayerType(PlayerType.GHOST);
			serverGame.getProps().add(new DeadBody(
					(int) getX() + Stuff.random(15, 90), (int) getY() + Stuff.random(15, 90)
			));
			serverGame.playSoundNear(getX(), getY(), 2000, "death");
		}

		setForcingSynchronization(true);
	}

	void FacePlayer(Player otherPlayer)
	{
		ang = Math.atan2(y - otherPlayer.getY(), x - otherPlayer.getX());
		setForcingSynchronization(true);
		setForcingLocationSynchronization(true);
	}

	void Face (double dx, double dy)
	{
		ang = Math.atan2(y - dy, x - dx);
		setForcingSynchronization(true);
		setForcingLocationSynchronization(true);
	}

	int getNextPacket() {
		return nextPacket;
	}

	void NextPacket ()
	{
		this.nextPacket += 1;
		if (this.nextPacket > 14)
			this.nextPacket = 1;
	}




	void drawPlayer (GraphicsPainter graphicsPainter)
	{
		int cx = graphicsPainter.getOffX() + (int) (graphicsPainter.getPlayer().getX() - getX());
		int cy = graphicsPainter.getOffY() + (int) (graphicsPainter.getPlayer().getY() - getY());

		ImageIcon pfp = null;

		if (getPlayerType().equals(PlayerType.SURVIVOR))
			pfp = isPlayerMoving() ? graphicsPainter.getStuff().getSurvivorsWalking().get(getCharacter()) : graphicsPainter.getStuff().getSurvivorsStanding().get(getCharacter());
		else if (getPlayerType().equals(PlayerType.GHOST))
			pfp = graphicsPainter.getStuff().getGhost();
		else if (getPlayerType().equals(PlayerType.VAMPIRE))
			if (isSpacePressed())
				pfp = graphicsPainter.getStuff().getVampAttacking();
			else if (isPlayerMoving())
				pfp = graphicsPainter.getStuff().getVampWalking();
			else
				pfp = graphicsPainter.getStuff().getVampStanding();

		assert pfp != null;
		AffineTransform aft = AffineTransform.getRotateInstance(getAng() + 1.57075, pfp.getIconWidth() >> 1, pfp.getIconHeight() >> 1);
		AffineTransformOp aftop = new AffineTransformOp(aft, AffineTransformOp.TYPE_BILINEAR);

		BufferedImage bfi = new BufferedImage(pfp.getIconWidth(), pfp.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics bfig = bfi.createGraphics();
		pfp.paintIcon(null, bfig, 0, 0);

		graphicsPainter.getG().drawImage(aftop.filter(bfi, null), cx - 40, cy - 40, 80, 80, null);


		if (graphicsPainter.clientGame.getGameStatus() == GameStatus.LOBBY) {
			//g.setFont(new Font("Arial", Font.BOLD, 20));
			graphicsPainter.getG().setFont(graphicsPainter.getStuff().getFont().deriveFont(20f));
			graphicsPainter.getG().setColor(new Color(255, 221, 0));
			graphicsPainter.getG().drawString(getNickName(), (int) (cx - (getNickName().length() * 5.5)), cy - 45);
		}
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


	boolean isPlayerMoving() {
		return playerMoving;
	}

	void setPlayerMoving(boolean playerMoving) {
		this.playerMoving = playerMoving;
	}

	String getNextSoundInQue() {
		return nextSoundInQue;
	}

	void setNextSoundInQue(String nextSoundInQue) {
		this.nextSoundInQue = nextSoundInQue;
	}

	boolean isForcingLocationSynchronization() {
		return forcingLocationSynchronization;
	}

	void setForcingLocationSynchronization(boolean forcingLocationSynchronization) {
		this.forcingLocationSynchronization = forcingLocationSynchronization;
	}

	void setX (double x) {
		this.x = x;
	}

	void setY (double y) {
		this.y = y;
	}

	public int getAfkPenalty() {
		return afkPenalty;
	}

	public void setAfkPenalty(int afkPenalty) {
		this.afkPenalty = afkPenalty;
	}

	boolean isHidden (ArrayList<Chest> chests)
	{
		for (Chest chest : chests)
			if (chest.getPlayerUUID() != null)
				if (chest.getPlayerUUID().equals(getPlayerID()))
					return true;
		return false;
	}
}
