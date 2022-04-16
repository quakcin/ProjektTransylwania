package pl.gr14b.transylwania;

class ServerGameLobbyStage extends ServerGameStage
{

	private int playersInQue;

	ServerGameLobbyStage(ServerBackgroundThread serverBackgroundThread, GameStatus stageCode) {
		super(serverBackgroundThread, stageCode);
		playersInQue = 0;
	}

	@Override
	void UpdateStage()
	{
		waitingForPlayersAndTimer();
		characterChangeListener();
	}

	private void waitingForPlayersAndTimer()
	{
		int playersCount
				= game.getPlayers().size();

		if (playersCount != playersInQue)
			anotherPlayerJoinEvent(playersCount);

		else if (enoughPlayersToStartTheGame(playersCount))
			finalCountDown();
	}

	private void anotherPlayerJoinEvent(int playersCount)
	{
		if (playersCount >= 2)
			game.setWaitingTime((Constants.MAX_PLAYERS - playersCount) * 2);

		else
			game.setWaitingTime(Constants.LOBBY_MAX_WAITING_TIME);

		playersInQue = playersCount;
	}

	private boolean enoughPlayersToStartTheGame (int playersCount)
	{
		return playersCount >= 2;
	}

	private void finalCountDown()
	{
		server.timeUpdate();

		if (server.isOverTime())
			startTheGame();
	}

	private void startTheGame()
	{
		spreadOutLampsAndChests();
		teleportPlayersToSpawn();
		updateGameStatus();
	}

	private void spreadOutLampsAndChests()
	{
		game.spreadOutLamps();
		game.spreadOutChests();
	}

	private void teleportPlayersToSpawn ()
	{
		for (Player player : game.getPlayers())
			player.teleportToSpawn(game.getPlayers());
	}

	private void updateGameStatus ()
	{
		game.setGameStatus(GameStatus.INTRO);
		game.setWaitingTime(Constants.INTRO_PHASE_DURATION);
		server.setTick(1);
	}

	private void characterChangeListener()
	{
		for (Player p : game.getPlayers())
			if (p.isSpacePressed())
			{
				p.randomizeCharacter();
				p.setSpacePressedDisabled(Constants.LOBBY_CHANGE_PLAYER_MODEL__DELAY);
			}
	}

}
