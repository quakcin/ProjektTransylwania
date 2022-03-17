package pl.gr14b.transylwania;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

class Game implements Serializable
{
	// -- Game Status (Engine) flags
	static final int GAME_STATUS_LOBBY = 0;
	static final int GAME_STATUS_INTRO = 1;
	static final int GAME_STATUS_KILLING = 2;
	static final int GAME_STATUS_SUMMARY = 3;

	// -- Winner status
	static final boolean WINNER_VAMP = true;
	static final boolean WINNER_SURVIVOR = false;


	// -- In Game Flags and Constants
	static final int VAMP_ATTACK_DELAY = 30;
	static final int VAMP_ATTACK_MISS_DELAY = 15;
	static final int LOBBY_CHANGE_PLAYER_MODEL__DELAY = 15;

	// -- Parameters for game loop
	static final int LAMP_BONUS = 10;
	static final int LAMP_PENALTY = 12;

	// -- personal info:
	private UUID playerID;

	// -- game info
	private int gameStatus;
	private ArrayList<Player> players;
	private ArrayList<Prop> props;
	private ArrayList<Lamp> lamps;
	private ArrayList<Room> rooms;

	private Boolean[] verticalDoors;
	private Boolean[] horizontalDoors;

	// -- more data
	private int waitingTime;
	private int gameTime;
	private double globalLight;
	private boolean winnerFlag;

	final int MAP_SIZE = 5;

	// -- constructor
	Game ()
	{
		globalLight = 0.65d;
		waitingTime = 0;
		gameTime = 0;
		gameStatus = GAME_STATUS_LOBBY;
		players = new ArrayList<Player>();
		props = new ArrayList<Prop>(); // reuse by Reset(), also set in GenerateMap()
		lamps = new ArrayList<Lamp>();
		winnerFlag = false;

		playerID = null;
		GenerateMap();
	}

	private void GenerateMap ()
	{
		rooms = new ArrayList<Room>();
		verticalDoors = new Boolean[MAP_SIZE * (MAP_SIZE + 1)];
		horizontalDoors = new Boolean[MAP_SIZE * (MAP_SIZE + 1)];

		for (int i = 0; i < MAP_SIZE * (MAP_SIZE + 1); i++)
		{
			verticalDoors[i] = Math.random() * 100 > 75;
			horizontalDoors[i] = Math.random() * 100 > 80;
		}

		for (int i = 0; i < MAP_SIZE; i++)
		{
			// bottom
			horizontalDoors[i] = true;

			// top
			horizontalDoors[MAP_SIZE * (MAP_SIZE) + i] = true;

			// right
			verticalDoors[i] = true;

			// left
			verticalDoors[MAP_SIZE * (MAP_SIZE) + i] = true;
		}

		// -- Generate random rooms

		for (int y = 0; y < MAP_SIZE; y++)
			for (int x = 0; x < MAP_SIZE; x++)
				rooms.add(new Room(x, y));
	}

	Boolean[] getVerticalDoors ()
	{
		return verticalDoors;
	}

	Boolean[] getHorizontalDoors ()
	{
		return horizontalDoors;
	}

	void setPlayerID(UUID playerID) {
		this.playerID = playerID;
	}

	ArrayList<Player> getPlayers() {
		return players;
	}

	// Server Only:
	UUID playerJoinEvent (String playerNickName, ArrayList<Player> existingPlayers)
	{
		UUID newPlayerID = UUID.randomUUID();
		Player newPlayer = new Player(newPlayerID, playerNickName, existingPlayers);
		players.add(newPlayer);

		if (gameStatus == GAME_STATUS_INTRO || gameStatus == GAME_STATUS_KILLING || gameStatus == GAME_STATUS_SUMMARY)
			newPlayer.setPlayerType(Player.PLAYER_TYPE_GHOST);

		return newPlayerID;
	}

	Player getPlayer ()
	{
		for (Player p : players)
			if (p.getPlayerID().equals(playerID))
				return p;
		return null;
	}

	ArrayList<Room> getRooms() {
		return rooms;
	}

	int getGameStatus() {
		return gameStatus;
	}

	void setGameStatus(int gameStatus) {
		this.gameStatus = gameStatus;
	}

	int getWaitingTime() {
		return waitingTime;
	}

	void setWaitingTime(int waitingTime) {
		this.waitingTime = waitingTime;
	}

	int getGameTime() {
		return gameTime;
	}

	void setGameTime(int gameTime) {
		this.gameTime = gameTime;
	}

	void Reset ()
	{
		for (Player p : players)
			p.Reset(players);
		// Now: Reset Map
		waitingTime = (8 - players.size()) * 5;
		gameTime = 0;
		gameStatus = GAME_STATUS_LOBBY;
		lamps = new ArrayList<Lamp>();
		setGlobalLight(0.6d);
		GenerateMap();
	}

	int countSurvivors ()
	{
		int count = 0;
		for (Player p : players)
			if (p.getPlayerType() == Player.PLAYER_TYPE_SURVIVOR)
				count += 1;
		return count;
	}

	boolean isVampireConnected ()
	{
		for (Player p : players)
			if (p.getPlayerType() == Player.PLAYER_TYPE_VAMPIRE)
				return true;
		return false;
	}

	Player getVamp ()
	{
		for (Player p : players)
			if (p.getPlayerType() == Player.PLAYER_TYPE_VAMPIRE)
				return p;
		return null;
	}

	Player getNearestSurvivor (Player player)
	{
		Player nearest = null;

		for (Player p : players)
			if (p != player)
				if (p.getPlayerType() == Player.PLAYER_TYPE_SURVIVOR)
					if (nearest == null)
						nearest = p;
					else
					{
						double dist = p.getDist(player.getX(), player.getY());
						if (dist < nearest.getDist(player.getX(), player.getY()))
							nearest = p;
					}
		return nearest;
	}

	public ArrayList<Prop> getProps() {
		return props;
	}

	public void setProps(ArrayList<Prop> props) {
		this.props = props;
	}

	public void spreadOutLampsAroundTheMap ()
	{

		for (int i = 0; i < players.size() * 5; i++)
		{
			int roomOffsetX = Stuff.random(0, MAP_SIZE - 1) * 810 + 2 * 90;
			int roomOffsetY = Stuff.random(0, MAP_SIZE - 1) * 810 + 2 * 90;
			int finX = Stuff.random(0, 810 - 2 * 90 - 50) + roomOffsetX;
			int finY = Stuff.random(0, 810 - 2 * 90 - 50) + roomOffsetY;
			lamps.add(new Lamp(finX, finY));
		}
	}

	public ArrayList<Lamp> getLamps ()
	{
		return lamps;
	}

	public Player getPlayerByUUID (UUID id)
	{
		for (Player p : players)
			if (p.getPlayerID().equals(id))
				return p;
		return null;
	}

	void setPlayers (ArrayList<Player> players)
	{
		this.players = players;
	}

	void setLamps (ArrayList<Lamp> lamps)
	{
		this.lamps = lamps;
	}

	Lamp getLampInUse (Player player)
	{
		Lamp nearest = null;
		double nDist = 0xFFFF;
		for (Lamp lamp : lamps)
		{
			double dist = player.getDist(lamp.getX() - 20, lamp.getY() - 20);
			if (dist < nDist)
			{
				nDist = dist;
				nearest = lamp;
			}
		}
		if (nDist < 90)
			return nearest;
		else
			return null;
	}

	Player getNearestPlayerToPointTo ()
	{
		Player vamp = getVamp();
		Player surv = getNearestSurvivor(vamp);

		if (vamp == null || surv == null)
			return null;

		double maxVisDistance = Math.ceil((double) countSurvivors() / 3.5) * 810 + 500;
		double minVisDistance = 500; // FIXME: .Make it smarter.
		double distToPlayer = vamp.getDist(surv.getX(), surv.getY());

		if (distToPlayer <= maxVisDistance && distToPlayer >= minVisDistance)
			return surv;
		else
			return null;
	}

	public ArrayList<Player> getPlayersNear (double dx, double dy, double dist)
	{
		ArrayList<Player> playersNear = new ArrayList<Player>();

		for (Player p : players)
			if (p.getDist(dx, dy) <= dist)
				playersNear.add(p);

		return playersNear;
	}

	public void playSoundNear (double dx, double dy, double near, String sound)
	{
		for (Player p : getPlayersNear(dx, dy, near)) {
			p.setNextSoundInQue(sound);
			p.setForcingSynchronization(true);
		}
	}

	public double getGlobalLight() {
		return globalLight;
	}

	public void setGlobalLight(double globalLight) {
		this.globalLight = globalLight;
	}

	public boolean isWinnerFlag() {
		return winnerFlag;
	}

	public void setWinnerFlag(boolean winnerFlag) {
		this.winnerFlag = winnerFlag;
	}
}
