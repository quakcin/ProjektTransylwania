package pl.gr14b.transylwania;

import java.util.ArrayList;
import java.util.UUID;

public class ServerRequestHandler
{
	private ArrayList<ServerHandleRequest> handlers;

	public ServerRequestHandler(ServerHandler serverHandler)
	{
		this.handlers = new ArrayList<>();
		handlers.add(new ServerHandlePlayersRequest(Player.class.toString(),serverHandler));
		handlers.add(new ServerHandleHelloRequest(Hello.class.toString(), serverHandler));
	}

	public void handleRequest (Object packet, UUID playerID)
	{
		for (ServerHandleRequest serverHandleRequest : handlers)
			if (serverHandleRequest.checkPacketType(packet))
			{
				serverHandleRequest.handleRequest(packet, playerID);
				return;
			}
	}


}

