package pl.gr14b.transylwania;

import java.io.Serializable;
import java.util.ArrayList;

class ChestsListPacket extends Packets implements Serializable
{
	private ArrayList<Chest> chests;

	ChestsListPacket (ArrayList<Chest> chests)
	{
		this.chests = chests;
	}

	public ArrayList<Chest> getChests()
	{
		return chests;
	}

}