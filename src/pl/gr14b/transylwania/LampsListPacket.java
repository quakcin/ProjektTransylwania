package pl.gr14b.transylwania;

import java.io.Serializable;
import java.util.ArrayList;

class LampsListPacket extends Packets implements Serializable
{
	private ArrayList<Lamp> lamps;
	private double globalLight;

	ArrayList<Lamp> getLamps()
	{
		return lamps;
	}

	LampsListPacket (ArrayList<Lamp> lamps, double globalLight)
	{
		this.lamps = lamps;
		this.globalLight = globalLight;
	}

	double getGlobalLight()
	{
		return globalLight;
	}

}
