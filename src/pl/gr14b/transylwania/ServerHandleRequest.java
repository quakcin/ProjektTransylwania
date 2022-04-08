package pl.gr14b.transylwania;

import java.util.UUID;

abstract class ServerHandleRequest
{
	private String className;
	Game serverGame;
	ServerHandler serverHandler;

	ServerHandleRequest(String className, ServerHandler serverHandler)
	{
		this.serverGame = serverHandler.getServerGame();
		this.serverHandler = serverHandler;
		this.className = className;
	}

	boolean checkPacketType (Object testify)
	{
		return (testify.getClass().toString().equals(className));
	}

	abstract void handleRequest (Object clientResponse, UUID playerID);
}

