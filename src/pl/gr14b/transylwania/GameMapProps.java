package pl.gr14b.transylwania;

import java.io.Serializable;
import java.util.ArrayList;

public class GameMapProps implements Serializable
{
	// Game Map Props
	private ArrayList<Prop> props;
	private ArrayList<LampProp> lampProps;
	private ArrayList<ChestProp> chestProps;
	private Game game;

	GameMapProps (Game game)
	{
		props = new ArrayList<>();
		lampProps = new ArrayList<>();
		chestProps = new ArrayList<>();
		this.game = game;
	}

	int randRoomPos (int objSize)
	{
		int roomEdge = (
				Constants.random(0, Constants.MAP_SIZE - 1)
				* Constants.DEFAULT_ROOM_SIZE
				+ Constants.DEFAULT_ROOM_EDGE
		);
		int roomOffset = Constants.random(0, calculateMaximumRoomOffset(objSize));
		return roomEdge + roomOffset;
	}

	int calculateMaximumRoomOffset (int objSize)
	{
		return Constants.DEFAULT_ROOM_SIZE
				- 2 * Constants.DEFAULT_GRID_SIZE
				- objSize
				- Constants.DEFAULT_ROOM_PADDING
				;
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

	boolean isColliding (int dx, int dy)
	{
		for (LampProp lampProp : getLampProps())
			if (lampProp.getDist(dx, dy) < Constants.LAMP_COLLIDER)
				return true;

		for (ChestProp chestProp : getChestProps())
			if (chestProp.getDist(dx, dy) < Constants.CHEST_COLLIDER)
				return true;

		return false;
	}

	void spreadOutLamps ()
	{
		for (int i = 0; i < game.getPlayers().size() * Constants.LAMP_PLAYERS_DELTA; i++)
		{
			Integer[] pos = randSpot(Constants.DEFAULT_LAMP_SIZE);
			lampProps.add(new LampProp(pos[0], pos[1]));
		}
	}

	void spreadOutChests()
	{
		for (int i = 0; i < game.getPlayers().size() * Constants.CHEST_PLAYERS_DELTA; i++)
		{
			Integer[] pos = randSpot(Constants.DEFAULT_CHEST_SIZE);
			chestProps.add(new ChestProp(pos[0], pos[1]));
		}
	}

	LampProp getLampInUse (Player player)
	{
		LampProp nearest = null;
		double nDist = Constants.FAR_PLANE;
		for (LampProp lampProp : lampProps)
		{
			double dist = player.getDist(
					lampProp.getX() - Constants.DEFAULT_ROOM_PADDING,
					lampProp.getY() - Constants.DEFAULT_ROOM_PADDING
			);
			if (dist < nDist)
			{
				nDist = dist;
				nearest = lampProp;
			}
		}
		if (nDist < Constants.DEFAULT_GRID_SIZE)
			return nearest;
		else
			return null;
	}

	ChestProp getChestInUse (Player player)
	{
		ChestProp nearest = null;
		double nDist = Constants.FAR_PLANE;
		for (ChestProp chestProp : chestProps)
		{
			double dist = player.getDist(
					chestProp.getX() - Constants.DEFAULT_ROOM_PADDING,
					chestProp.getY() - Constants.DEFAULT_ROOM_PADDING
			);
			if (dist < nDist)
			{
				nDist = dist;
				nearest = chestProp;
			}
		}

		if (nDist < Constants.DEFAULT_GRID_SIZE)
			return nearest;
		else
			return null;
	}


	public ArrayList<Prop> getProps() {
		return props;
	}

	public void setProps(ArrayList<Prop> props) {
		this.props = props;
	}

	public ArrayList<LampProp> getLampProps() {
		return lampProps;
	}

	public void setLampProps(ArrayList<LampProp> lampProps) {
		this.lampProps = lampProps;
	}

	public ArrayList<ChestProp> getChestProps() {
		return chestProps;
	}

	public void setChestProps(ArrayList<ChestProp> chestProps) {
		this.chestProps = chestProps;
	}
}
