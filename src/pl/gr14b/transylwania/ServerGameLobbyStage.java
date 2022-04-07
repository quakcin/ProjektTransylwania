package pl.gr14b.transylwania;

public class ServerGameLobbyStage extends ServerGameStage
{

	int playersInQue;

	ServerGameLobbyStage(ServerBackgroundThread serverBackgroundThread, GameStatus stageCode) {
		super(serverBackgroundThread, stageCode);
		playersInQue = 0;
	}

	void randomCharacterListener ()
	{
		for (Player p : game.getPlayers())
			if (p.isSpacePressed())
			{
				p.randomizeCharacter();
				p.setSpacePressedDisabled(Game.LOBBY_CHANGE_PLAYER_MODEL__DELAY);
			}
	}


	void anotherPlayerJoinEvent (int playersCount)
	{
		if (playersCount >= 2)
			game.setWaitingTime((8 - playersCount) * 2);

		else
			game.setWaitingTime(8 * 5);

		playersInQue = playersCount;
	}


	boolean enoughPlayersToStartTheGame (int playersCount) // OOP CC
	{
		return playersCount >= 2;
	}

	void startTheGame ()
	{
		game.setGameStatus(GameStatus.INTRO);
		game.spreadOutLamps();
		game.spreadOutChests();
		for (Player p : game.getPlayers())
			p.teleportToSpawn(game.getPlayers());
		game.setWaitingTime(5);

		server.setTick(1);
	}

	void finalCountDown ()
	{
		if (server.hasSecondPassed())
			game.setWaitingTime(game.getWaitingTime() - 1);

		if (game.getWaitingTime() <= 0)
			startTheGame();
	}

	void waitingForPlayersAndTimer ()
	{
		int playersCount
				= game.getPlayers().size();

		if (playersCount != playersInQue)
			anotherPlayerJoinEvent(playersCount);

		else if (enoughPlayersToStartTheGame(playersCount))
			finalCountDown();
	}

	@Override
	void UpdateStage()
	{
		waitingForPlayersAndTimer();
		randomCharacterListener();
	}
}
