package pl.gr14b.transylwania;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

class Game implements Serializable
{

	private ArrayList<Player> players;
	private UUID playerID;

	private GameMechanics gameMechanics;
	private GameFlags gameFlags;
	private GameMap gameMap;

	Game ()
	{
		gameMechanics = new GameMechanics(this);
		gameFlags = new GameFlags();
		gameMap = new GameMap(this);
		players = new ArrayList<>();
		playerID = null;
	}

	Boolean[] getVerticalDoors ()
	{
		return gameMap.getVerticalDoors();
	}

	Boolean[] getHorizontalDoors ()
	{
		return gameMap.getHorizontalDoors();
	}

	void setPlayerID(UUID playerID) {
		this.playerID = playerID;
	}

	ArrayList<Player> getPlayers() {
		return players;
	}

	UUID playerJoinEvent (String playerNickName, ArrayList<Player> existingPlayers)
	{
		return gameMechanics.playerJoinEvent(playerNickName, existingPlayers);
	}

	Player getPlayer ()
	{
		return gameMechanics.getPlayer(playerID);
	}

	ArrayList<Room> getRooms() {
		return gameMap.getRooms();
	}

	GameStatus getGameStatus() {
		return gameFlags.getGameStatus();
	}

	void setGameStatus(GameStatus gameStatus) {
		gameFlags.setGameStatus(gameStatus);
	}

	int getWaitingTime() {
		return gameFlags.getWaitingTime();
	}

	void setWaitingTime(int waitingTime) {
		gameFlags.setWaitingTime(waitingTime);
	}

	int getGameTime() {
		return gameFlags.getGameTime();
	}

	void setGameTime(int gameTime) {
		gameFlags.setGameTime(gameTime);
	}

	void resetGameServerWise ()
	{
		gameMechanics.resetGameServerWise();
	}

	int countSurvivors ()
	{
		return gameMechanics.countSurvivors();
	}

	boolean isVampireConnected ()
	{
		return gameMechanics.isVampireConnected();
	}

	Player getVamp ()
	{
		return gameMechanics.getVamp();
	}

	Player getNearestSurvivor (Player player)
	{
		return gameMechanics.getNearestSurvivor(player);
	}

	ArrayList<Prop> getProps() {
		return gameMap.getGameMapProps().getProps();
	}

	void setProps(ArrayList<Prop> props) {
		gameMap.getGameMapProps().setProps(props);
	}

	void spreadOutLamps ()
	{
		gameMap.getGameMapProps().spreadOutLamps();
	}

	void spreadOutChests()
	{
		gameMap.getGameMapProps().spreadOutChests();
	}

	ArrayList<Lamp> getLamps()
	{
		return gameMap.getGameMapProps().getLamps();
	}

	void setPlayers (ArrayList<Player> players)
	{
		this.players = players;
	}

	void setLamps (ArrayList<Lamp> lamps)
	{
		gameMap.getGameMapProps().setLamps(lamps);
	}

	Chest getChestInUse (Player player)
	{
		return gameMap.getGameMapProps().getChestInUse(player);
	}

	Lamp getLampInUse (Player player)
	{
		return gameMap.getGameMapProps().getLampInUse(player);
	}

	Player getNearestPlayerToPointTo ()
	{
		return gameMechanics.getNearestPlayerToPointTo();
	}

	private ArrayList<Player> getPlayersNear (double dx, double dy, double dist)
	{
		return gameMechanics.getPlayersNear(dx, dy, dist);
	}

	void playSoundNear (double dx, double dy, double near, String sound)
	{
		gameMechanics.playSoundNear(dx, dy, near, sound);
	}

	double getGlobalLight() {
		return gameFlags.getGlobalLight();
	}

	void setGlobalLight(double globalLight) {
		gameFlags.setGlobalLight(globalLight);
	}

	boolean isWinnerFlag() {
		return gameFlags.isWinnerFlag();
	}

	void setWinnerFlag(boolean winnerFlag) {
		gameFlags.setWinnerFlag(winnerFlag);
	}

	ArrayList<Chest> getChests ()
	{
		return gameMap.getGameMapProps().getChests();
	}

	void setChests (ArrayList<Chest> chests)
	{
		gameMap.getGameMapProps().setChests(chests);
	}

	Player getPlayerByID (UUID id)
	{
		return gameMechanics.getPlayerByID(id);
	}

	public void setGameMap(GameMap gameMap) {
		this.gameMap = gameMap;
	}
}
