package pl.gr14b.transylwania;

import java.io.Serializable;
import java.util.ArrayList;

abstract class Packets implements Serializable
{
}

class PlayersListPacket extends Packets implements Serializable
{
	private ArrayList<Player> players;

	ArrayList<Player> getPlayers() {
		return players;
	}
	PlayersListPacket(ArrayList<Player> players)
	{
		this.players = players;
	}
}

class PropsAndStatsListPacket extends Packets implements Serializable
{
	private ArrayList<Prop> props;
	int gameStatus;
	int waitingTime;
	int gameTime;
	boolean isWinner;

	ArrayList<Prop> getProps() {
		return props;
	}
	PropsAndStatsListPacket (ArrayList<Prop> props, int gameStatus, int waitingTime, int gameTime, boolean isWinner)
	{
		this.props = props;
		this.gameStatus = gameStatus;
		this.waitingTime = waitingTime;
		this.gameTime = gameTime;
		this.isWinner = isWinner;
	}
}

class LampsListPacket extends Packets implements Serializable
{
	private ArrayList<Lamp> lamps;
	private double globalLight;

	ArrayList<Lamp> getLamps() {
		return lamps;
	}
	LampsListPacket (ArrayList<Lamp> lamps, double globalLight)
	{
		this.lamps = lamps;
		this.globalLight = globalLight;
	}

	double getGlobalLight() {
		return globalLight;
	}
}
