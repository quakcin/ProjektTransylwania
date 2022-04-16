package pl.gr14b.transylwania;

import java.io.Serializable;
import java.util.ArrayList;

public class GameMap implements Serializable
{
	private ArrayList<Room> rooms;
	private Boolean[] verticalDoors;
	private Boolean[] horizontalDoors;

	private GameMapProps gameMapProps;

	GameMap (Game game)
	{
		rooms = new ArrayList<>();
		gameMapProps = new GameMapProps(game);
		verticalDoors = new Boolean[Constants.MAP_SIZE * (Constants.MAP_SIZE + 1)];
		horizontalDoors = new Boolean[Constants.MAP_SIZE * (Constants.MAP_SIZE + 1)];

		generateRandomRooms();
		closeOffRandomDoors();
		closeOffDoorsOnTheEdgesOfTheMap();
	}


	private void generateRandomRooms ()
	{
		for (int y = 0; y < Constants.MAP_SIZE; y++)
			for (int x = 0; x < Constants.MAP_SIZE; x++)
				rooms.add(new Room(x, y));
	}

	private void closeOffRandomDoors ()
	{
		for (int i = 0; i < Constants.MAP_SIZE * (Constants.MAP_SIZE + 1); i++)
		{
			verticalDoors[i] = Math.random() * 100 > Constants.VERTICAL_DOORS_SPAWN_CHANCE;
			horizontalDoors[i] = Math.random() * 100 > Constants.HORIZONTAL_DOORS_SPAWN_CHANCE;
		}
	}

	private void closeOffDoorsOnTheEdgesOfTheMap ()
	{
		for (int i = 0; i < Constants.MAP_SIZE; i++)
		{
			horizontalDoors[i] = true;
			verticalDoors[i] = true;
			horizontalDoors[Constants.MAP_SIZE * (Constants.MAP_SIZE) + i] = true;
			verticalDoors[Constants.MAP_SIZE * (Constants.MAP_SIZE) + i] = true;
		}
	}

	public ArrayList<Room> getRooms() {
		return rooms;
	}

	public void setRooms(ArrayList<Room> rooms) {
		this.rooms = rooms;
	}

	public Boolean[] getVerticalDoors() {
		return verticalDoors;
	}

	public void setVerticalDoors(Boolean[] verticalDoors) {
		this.verticalDoors = verticalDoors;
	}

	public Boolean[] getHorizontalDoors() {
		return horizontalDoors;
	}

	public void setHorizontalDoors(Boolean[] horizontalDoors) {
		this.horizontalDoors = horizontalDoors;
	}

	public GameMapProps getGameMapProps() {
		return gameMapProps;
	}

	public void setGameMapProps(GameMapProps gameMapProps) {
		this.gameMapProps = gameMapProps;
	}
}
