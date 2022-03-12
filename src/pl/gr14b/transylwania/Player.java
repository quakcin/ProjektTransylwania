package pl.gr14b.transylwania;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;


public class Player implements Serializable {
	// Flags and stuff
	static final int PLAYER_TYPE_SURVIVOR = 0;
	static final int PLAYER_TYPE_VAMPIRE = 1;
	static final int PLAYER_TYPE_GHOST = 2;

	static final int PLAYER_MODELS_COUNT = 4;

	// Player specific boolean flags
	private boolean spacePressed;
	private int spacedPressedEnabled;

	private boolean playerMoving;


	public final double DEG = 0.0174527777777778;

	private String nickName;
	private UUID playerID;
	private double x;
	private double y;
	private double ang;
	private int character;
	private boolean forcingSynchronization;
	private int playerType;
	private int health;
	private int nextPacket;
	private String nextSoundInQue;

	Player(UUID playerID, String playerNickName, ArrayList<Player> existingPlayers) {
		// This is ALWAYS default
		this.forcingSynchronization = false;
		this.nickName = playerNickName;
		this.playerType = PLAYER_TYPE_SURVIVOR;
		this.playerID = playerID;
		this.health = 3;
		this.nextPacket = 0;
		this.nextSoundInQue = null;

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
	boolean Push (double speed, Game clientGame)
	{
		pushAttempts = 0;
		return RecursivePush(playerType == Player.PLAYER_TYPE_VAMPIRE ? speed * 1.2 : speed, clientGame);
	}

	boolean RecursivePush (double speed, Game clientGame) {
		// TODO: Take speed into account
		final double collider = 15;
		double vx = getX() + -1 * Math.cos(ang) * speed;
		double vy = getY() + -1 * Math.sin(ang) * speed;

		if (playerType == PLAYER_TYPE_GHOST) {
			x = vx;
			y = vy;
			return true;
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
			if (p.getPlayerID() != getPlayerID() && p.getPlayerType() != PLAYER_TYPE_GHOST)
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
		return allow;
	}

	void Turn(double delta) {
		ang += delta * (3.1415 / 180);

		if (delta < 0)
			delta = 360 * (3.1415 / 180);
		else if (delta > 2 * (3.1415 / 180))
			delta = 0;
	}

	void copyLocation(Player otherPlayer) {
		x = otherPlayer.getX();
		y = otherPlayer.getY();
		ang = otherPlayer.getAng();
	}

	int getCharacter() {
		return character;
	}

	public void setCharacter(int character) {
		this.character = character;
	}


	void serverTeleport(double dx, double dy) {
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

	int getPlayerType() {
		return playerType;
	}

	void setPlayerType(int playerType) {
		this.playerType = playerType;
	}

	boolean isSpacePressed() {
		return spacePressed;
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
		playerType = PLAYER_TYPE_SURVIVOR;
		teleportToSpawn(allPlayers);
	}

	int getHealth() {
		return health;
	}

	void setAng (double ang)
	{
		this.ang = ang;
	}

	void setHealth(int health) {
		if (health < 0)
			this.health = 0;
		else if (health > 3)
			this.health = 3;
		else
			this.health = health;
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
			this.character = (int) Math.round(Math.random() * (Player.PLAYER_MODELS_COUNT - 1));
		}
		while (oldCharacter == this.character);
		setForcingSynchronization(true);
	}

	void Damage(int delta, Game serverGame) // oop is rly gut
	{
		setHealth(getHealth() - delta);

		// Spawn Blood
		// TODO: Make the randomization a proper constructor
		serverGame.getProps().add(new Blood(
				(int) getX() + Stuff.random(15, 90), (int) getY() + Stuff.random(15, 90)
		));

		serverGame.playSoundNear(getX(), getY(), 1000, "stab");

		// Check if player died
		if (getHealth() <= 0) {
			setPlayerType(Player.PLAYER_TYPE_GHOST);
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
	}

	void Face (double dx, double dy)
	{
		ang = Math.atan2(y - dy, x - dx);
		setForcingSynchronization(true);
	}

	public int getNextPacket() {
		return nextPacket;
	}

	void NextPacket ()
	{
		this.nextPacket += 1;
		if (this.nextPacket > 12)
			this.nextPacket = 1;
	}

	public boolean isPlayerMoving() {
		return playerMoving;
	}

	public void setPlayerMoving(boolean playerMoving) {
		this.playerMoving = playerMoving;
	}

	public String getNextSoundInQue() {
		return nextSoundInQue;
	}

	public void setNextSoundInQue(String nextSoundInQue) {
		this.nextSoundInQue = nextSoundInQue;
	}
}
