package pl.gr14b.transylwania;

import java.util.ArrayList;
import java.util.UUID;

class ServerRequestHandler
{
	private ArrayList<ServerHandleRequest> handlers;

	ServerRequestHandler(ServerHandler serverHandler)
	{
		this.handlers = new ArrayList<>();
		handlers.add(new ServerHandlePlayersRequest(Player.class.toString(),serverHandler));
		handlers.add(new ServerHandleHelloRequest(Hello.class.toString(), serverHandler));
	}

	void handleRequest(Object packet, UUID playerID)
	{
		for (ServerHandleRequest serverHandleRequest : handlers)
			if (serverHandleRequest.checkPacketType(packet))
			{
				serverHandleRequest.handleRequest(packet, playerID);
				return;
			}
	}

}

