package pl.gr14b.transylwania;

import java.io.Serializable;
import java.util.ArrayList;

class PropsPacket extends Packets implements Serializable
{
	private ArrayList<Prop> props;

	ArrayList<Prop> getProps()
	{
		return props;
	}

	PropsPacket(ArrayList<Prop> props)
	{
		this.props = props;
	}

}
