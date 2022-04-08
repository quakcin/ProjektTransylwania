package pl.gr14b.transylwania;

import java.io.ObjectOutputStream;
import java.util.ArrayList;

class ServerResponseHandler
{
	private ObjectOutputStream objOut;
	private Game serverGame;

	ServerResponseHandler(ServerHandler serverHandler)
	{
		this.serverGame = serverHandler.getServerGame();
		this.objOut = serverHandler.getObjOut();
	}

	void handleResponse () throws Exception
	{
		if (isNextPacketForced())
			responseWithGodObject();
		else
			responseWithNextPacket();
	}


	private boolean isNextPacketForced()
	{
		return serverGame.getPlayer().getNextPacket() == 0
				|| serverGame.getPlayer().isForcingSynchronization()
				|| serverGame.getPlayer().isForcingLocationSynchronization();
	}

	private void responseWithGodObject() throws Exception
	{
		serverGame.getPlayer().setForcingSynchronization(false);
		serverGame.getPlayer().setForcingLocationSynchronization(false);
		objOut.writeObject(serverGame);
	}


	private void responseWithNextPacket() throws Exception
	{
		ArrayList<Packets> packets = new ArrayList<>();
		packPackets(packets);
		objOut.writeObject(getNextPacket(packets));
	}

	private void packPackets(ArrayList<Packets> packets)
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

	private Packets getNextPacket(ArrayList<Packets> packets)
	{
		int idx = serverGame.getPlayer().getNextPacket();
		return packets.get((idx <= 10)
				? 0
				: idx - 10
		);
	}

}

