package pl.gr14b.transylwania;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

class Game implements Serializable
{

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
	private GameStatus gameStatus;
	private ArrayList<Player> players;
	private ArrayList<Prop> props;
	private ArrayList<Lamp> lamps;
	private ArrayList<Room> rooms;
	private ArrayList<Chest> chests;

	private Boolean[] verticalDoors;
	private Boolean[] horizontalDoors;

	// -- more data
	private int waitingTime;
	private int gameTime;
	private double globalLight;
	private boolean winnerFlag;
	// private boolean roleCallFlag; <-- guess this on client side

	final int MAP_SIZE = 5;

	// -- constructor
	Game ()
	{
		globalLight = 0.65d;
		waitingTime = 0;
		gameTime = 0;
		gameStatus = GameStatus.LOBBY;
		players = new ArrayList<>();
		props = new ArrayList<>(); // reuse by Reset(), also set in GenerateMap()
		lamps = new ArrayList<>();
		chests = new ArrayList<>();
		winnerFlag = false;

		playerID = null;
		GenerateMap();
	}

	private void GenerateMap ()
	{
		rooms = new ArrayList<>();
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

		if (gameStatus == GameStatus.INTRO || gameStatus == GameStatus.KILLING || gameStatus == GameStatus.SUMMARY)
			newPlayer.setPlayerType(PlayerType.GHOST);

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

	GameStatus getGameStatus() {
		return gameStatus;
	}

	void setGameStatus(GameStatus gameStatus) {
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
		gameStatus = GameStatus.LOBBY;
		lamps = new ArrayList<>();
		chests = new ArrayList<>();
		setGlobalLight(0.6d);
		GenerateMap();

		// Remove dead bodies
		for (int i = props.size() - 1; i >= 0; i--)
			if (props.get(i) instanceof DeadBody)
				props.remove(i);

		int stopLimit = Math.max(32, props.size());
		for (int i = props.size(); i >= stopLimit; i--)
			props.remove(i);
	}

	int countSurvivors ()
	{
		int count = 0;
		for (Player p : players)
			if (p.getPlayerType().equals(PlayerType.SURVIVOR))
				count += 1;
		return count;
	}

	boolean isVampireConnected ()
	{
		for (Player p : players)
			if (p.getPlayerType().equals(PlayerType.VAMPIRE))
				return true;
		return false;
	}

	Player getVamp ()
	{
		for (Player p : players)
			if (p.getPlayerType().equals(PlayerType.VAMPIRE))
				return p;
		return null;
	}

	Player getNearestSurvivor (Player player)
	{
		Player nearest = null;

		for (Player p : players)
			if (p != player)
				if (p.getPlayerType().equals(PlayerType.SURVIVOR))
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

	ArrayList<Prop> getProps() {
		return props;
	}

	void setProps(ArrayList<Prop> props) {
		this.props = props;
	}

	int randRoomPos (int objSize)
	{
		int roomEdge = (Stuff.random(0, MAP_SIZE - 1) * 810 + 2 * 90);
		int roomOffset = Stuff.random(0, 810 - 2 * 90 - objSize - 20);
		return roomEdge + roomOffset;
	}

	boolean isColliding (int dx, int dy)
	{
		for (Lamp lamp : getLamps())
			if (lamp.getDist(dx, dy) < 100)
				return true;

		for (Chest chest : getChests())
			if (chest.getDist(dx, dy) < 400)
				return true;

		return false;
	}

	Integer[] randSpot (int objSize)
	{
		Integer[] pos = new Integer[2];
		do {
			for (int i = 0; i < 2; i++)
				pos[i] = randRoomPos(objSize);
		}
		while (isColliding(pos[0], pos[1]));
		return pos;
	}

	void spreadOutLamps()
	{
		for (int i = 0; i < players.size() * 5; i++)
		{
			Integer[] pos = randSpot(60);
			lamps.add(new Lamp(pos[0], pos[1]));
		}
	}

	void spreadOutChests()
	{
		for (int i = 0; i < players.size() * 6; i++)
		{
			Integer[] pos = randSpot(100);
			System.out.println("New chest at: " + pos[0] + " x " + pos[1]);
			chests.add(new Chest(pos[0], pos[1]));
		}
	}

	ArrayList<Lamp> getLamps()
	{
		return lamps;
	}

	void setPlayers (ArrayList<Player> players)
	{
		this.players = players;
	}

	void setLamps (ArrayList<Lamp> lamps)
	{
		this.lamps = lamps;
	}

	Chest getChestInUse (Player player)
	{
		Chest nearest = null;
		double nDist = 0xFFFF;
		for (Chest chest : chests)
		{
			double dist = player.getDist(chest.getX() - 30, chest.getY() - 30);
			if (dist < nDist)
			{
				nDist = dist;
				nearest = chest;
			}
		}
		if (nDist < 90)
			return nearest;
		else
			return null;
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

		if (Chest.isPlayerHidden(getChests(), surv.getPlayerID()))
			return null;

		double maxVisDistance = Math.ceil((double) countSurvivors() / 3.5) * 810 + 500;
		double distToPlayer = vamp.getDist(surv.getX(), surv.getY());

		if (distToPlayer <= maxVisDistance)
			return surv;
		else
			return null;
	}

	private ArrayList<Player> getPlayersNear(double dx, double dy, double dist)
	{
		ArrayList<Player> playersNear = new ArrayList<>();

		for (Player p : players)
			if (p.getDist(dx, dy) <= dist)
				playersNear.add(p);

		return playersNear;
	}

	void playSoundNear(double dx, double dy, double near, String sound)
	{
		for (Player p : getPlayersNear(dx, dy, near)) {
			p.setNextSoundInQue(sound);
			p.setForcingSynchronization(true);
		}
	}

	double getGlobalLight() {
		return globalLight;
	}

	void setGlobalLight(double globalLight) {
		this.globalLight = globalLight;
	}

	boolean isWinnerFlag() {
		return winnerFlag;
	}

	void setWinnerFlag(boolean winnerFlag) {
		this.winnerFlag = winnerFlag;
	}

	ArrayList<Chest> getChests ()
	{
		return this.chests;
	}

	void setChests (ArrayList<Chest> chests)
	{
		this.chests = chests;
	}

	Player getPlayerByID (UUID id)
	{
		for (Player p : players)
			if (p.getPlayerID().equals(id))
				return p;
		return null;
	}

}
