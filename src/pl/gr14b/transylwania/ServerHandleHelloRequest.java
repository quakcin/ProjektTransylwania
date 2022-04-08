package pl.gr14b.transylwania;

import java.util.UUID;

class ServerHandleHelloRequest extends ServerHandleRequest
{
	ServerHandleHelloRequest(String className, ServerHandler serverHandler) {
		super(className, serverHandler);
	}

	private boolean tooManyPlayers()
	{
		return serverGame.getPlayers().size() >= 8;
	}

	@Override
	void handleRequest (Object clientResponse, UUID playerID)
	{
		if (tooManyPlayers())
			return;

		playerID = serverGame.playerJoinEvent(
				((Hello) clientResponse).getNickName(), serverGame.getPlayers()
		);

		serverHandler.setPlayerID(playerID);
		serverGame.setPlayerID(playerID);

		System.out.printf("Player %s has joined!\n", playerID);
	}
}
