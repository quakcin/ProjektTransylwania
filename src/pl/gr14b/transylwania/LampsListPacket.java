package pl.gr14b.transylwania;

import java.io.Serializable;
import java.util.ArrayList;

class LampsListPacket extends Packets implements Serializable
{
	private ArrayList<LampProp> lampProps;
	private double globalLight;

	ArrayList<LampProp> getLampProps()
	{
		return lampProps;
	}

	LampsListPacket (ArrayList<LampProp> lampProps, double globalLight)
	{
		this.lampProps = lampProps;
		this.globalLight = globalLight;
	}

	double getGlobalLight()
	{
		return globalLight;
	}

}
