package pl.gr14b.transylwania;

import java.io.Serializable;
import java.util.ArrayList;

class ChestsListPacket extends Packets implements Serializable
{
	private ArrayList<ChestProp> chestProps;

	ChestsListPacket (ArrayList<ChestProp> chestProps)
	{
		this.chestProps = chestProps;
	}

	public ArrayList<ChestProp> getChestProps()
	{
		return chestProps;
	}

}