package pl.gr14b.transylwania;

import java.io.Serializable;
import java.util.ArrayList;

class PlayersListPacket extends Packets implements Serializable
{
	private ArrayList<Player> players;

	ArrayList<Player> getPlayers()
	{
		return players;
	}

	PlayersListPacket(ArrayList<Player> players)
	{
		this.players = players;
	}

}