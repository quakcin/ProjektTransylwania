package pl.gr14b.transylwania;

import java.io.Serializable;
import java.util.ArrayList;

abstract class Packets implements Serializable {}

class PlayersListPacket extends Packets implements Serializable
{
	private ArrayList<Player> players;

	ArrayList<Player> getPlayers() { return players; }
	PlayersListPacket(ArrayList<Player> players) { this.players = players; }
}

class PropsPacket extends Packets implements Serializable
{
	private ArrayList<Prop> props;
	ArrayList<Prop> getProps() { return props; }
	PropsPacket(ArrayList<Prop> props)
	{
		this.props = props;
	}
}

class FlagPacket extends Packets implements Serializable
{
	GameStatus gameStatus;
	int waitingTime;
	int gameTime;
	int afkPenalty;
	boolean isWinner;

	FlagPacket
	(
		GameStatus gameStatus,
		int waitingTime,
		int gameTime,
		int afkPenalty,
		boolean isWinner
	)
	{
		this.gameStatus = gameStatus;
		this.waitingTime = waitingTime;
		this.gameTime = gameTime;
		this.afkPenalty = afkPenalty;
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

class ChestsListPacket extends Packets implements Serializable
{
	private ArrayList<Chest> chests;
	ChestsListPacket (ArrayList<Chest> chests)
	{
		this.chests = chests;
	}
	public ArrayList<Chest> getChests() {
		return chests;
	}
}