package pl.gr14b.transylwania;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class ServerResponseHandler
{
	public ObjectOutputStream objOut;
	public ObjectInputStream objIn;
	public Game serverGame;
	public UUID playerID;

	ServerResponseHandler(ServerHandler serverHandler)
	{
		this.serverGame = serverHandler.getServerGame();
		this.playerID = serverHandler.getPlayerID();
		this.objOut = serverHandler.getObjOut();
		this.objIn = serverHandler.getObjIn();
	}
	boolean isNextPacketForced ()
	{
		return serverGame.getPlayer().getNextPacket() == 0
				|| serverGame.getPlayer().isForcingSynchronization()
				|| serverGame.getPlayer().isForcingLocationSynchronization();
	}

	void responseWithGodObject () throws Exception {
		serverGame.getPlayer().setForcingSynchronization(false);
		serverGame.getPlayer().setForcingLocationSynchronization(false);
		objOut.writeObject(serverGame); // FIXME: Split synchronization to smaller packets
	}


	void packPackets (ArrayList<Packets> packets)
	{
		packets.add(new PlayersListPacket(serverGame.getPlayers()));
		packets.add(new PropsPacket(serverGame.getProps()));
		packets.add(new LampsListPacket(serverGame.getLamps(), serverGame.getGlobalLight()));
		packets.add(new ChestsListPacket(serverGame.getChests()));
		packets.add(new FlagPacket(serverGame.getGameStatus(),
				serverGame.getWaitingTime(),
				serverGame.getGameTime(),
				serverGame.getPlayer().getAfkPenalty(),
				serverGame.isWinnerFlag())
		);
	}

	Packets getNextPacket (ArrayList<Packets> packets)
	{
		int idx = serverGame.getPlayer().getNextPacket();
		return packets.get((idx <= 10)
				? 0
				: idx - 10
		);
	}

	void responseWithNextPacket () throws Exception {
		ArrayList<Packets> packets = new ArrayList<>();
		packPackets(packets);
		objOut.writeObject(getNextPacket(packets));
	}


	void handleResponse () throws Exception {
		if (isNextPacketForced())
			responseWithGodObject();
		else
			responseWithNextPacket();
	}

}

