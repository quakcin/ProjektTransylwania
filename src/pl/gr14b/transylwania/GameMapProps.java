package pl.gr14b.transylwania;

import java.io.Serializable;
import java.util.ArrayList;

public class GameMapProps implements Serializable
{
	// Game Map Props
	private ArrayList<Prop> props;
	private ArrayList<Lamp> lamps;
	private ArrayList<Chest> chests;
	private Game game;

	GameMapProps (Game game)
	{
		props = new ArrayList<>();
		lamps = new ArrayList<>();
		chests = new ArrayList<>();
		this.game = game;
	}

	int randRoomPos (int objSize)
	{
		int roomEdge = (Constants.random(0, Constants.MAP_SIZE - 1) * 810 + 2 * 90);
		int roomOffset = Constants.random(0, 810 - 2 * 90 - objSize - 20);
		return roomEdge + roomOffset;
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
		for (Lamp lamp : getLamps())
			if (lamp.getDist(dx, dy) < 100)
				return true;

		for (Chest chest : getChests())
			if (chest.getDist(dx, dy) < 400)
				return true;

		return false;
	}

	void spreadOutLamps ()
	{
		for (int i = 0; i < game.getPlayers().size() * 5; i++)
		{
			Integer[] pos = randSpot(60);
			lamps.add(new Lamp(pos[0], pos[1]));
		}
	}

	void spreadOutChests()
	{
		for (int i = 0; i < game.getPlayers().size() * 6; i++)
		{
			Integer[] pos = randSpot(100);
			chests.add(new Chest(pos[0], pos[1]));
		}
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


	public ArrayList<Prop> getProps() {
		return props;
	}

	public void setProps(ArrayList<Prop> props) {
		this.props = props;
	}

	public ArrayList<Lamp> getLamps() {
		return lamps;
	}

	public void setLamps(ArrayList<Lamp> lamps) {
		this.lamps = lamps;
	}

	public ArrayList<Chest> getChests() {
		return chests;
	}

	public void setChests(ArrayList<Chest> chests) {
		this.chests = chests;
	}
}
