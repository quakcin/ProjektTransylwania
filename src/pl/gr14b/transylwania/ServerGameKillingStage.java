package pl.gr14b.transylwania;

class ServerGameKillingStage extends ServerGameStage
{
	ServerGameKillingStage(ServerBackgroundThread serverBackgroundThread, GameStatus stageCode) {
		super(serverBackgroundThread, stageCode);
	}

	@Override
	void UpdateStage()
	{
		if (server.hasSecondPassed())
			game.setGameTime(game.getGameTime() - 1);

		if (!isGameOver())
			updateServerListeners();

		else
			endGame();
	}

	private boolean isGameOver()
	{
		return !game.isVampireConnected()
				|| game.countSurvivors() <= 0
				|| game.getGameTime() <= 0;
	}

	private void updateServerListeners()
	{
		vampAttackListener();
		lampActionListener();
		chestActionListener();
		antiAFKFilter();
	}

	private void vampAttackListener ()
	{
		Player vamp = game.getVamp();
		if (vamp != null)
			if (vamp.isSpacePressed())
				vampAttack(vamp);
	}

	private void vampAttack(Player vamp)
	{
		Player prey = game.getNearestSurvivor(vamp);

		if (prey != null)
			vampAttackPrey(vamp, prey);

		vamp.setForcingSynchronization(true);
	}


	private void vampAttackPrey(Player vamp, Player prey)
	{
		double dist
				= prey.getDist(vamp.getX(), vamp.getY());

		if (dist < Constants.VAMP_ATTACK_RANGE)
			vampSuccessfulAttack(vamp, prey);

		else if (dist < Constants.VAMP_AGRO_RANGE)
			vampFailedAttack(vamp, prey);

		else
			game.playSoundNear(
					vamp.getX(), vamp.getY(), Constants.FAR_PLANE,
					"vampScream"
			);
	}

	private void vampSuccessfulAttack(Player vamp, Player prey)
	{
		prey.Damage(game);
		vamp.facePlayer(prey);
		vamp.setSpacePressedDisabled(Constants.VAMP_ATTACK_DELAY);
	}

	private void vampFailedAttack(Player vamp, Player prey)
	{
		game.playSoundNear(vamp.getX(), vamp.getY(), Constants.DEFAULT_ROOM_SIZE * 2, "vampMiss");
		vamp.setSpacePressedDisabled(Constants.VAMP_ATTACK_MISS_DELAY);
		vamp.facePlayer(prey);
	}


	private void lampActionListener()
	{
		for (Player player : game.getPlayers())
			if (player.isNotGhostAndHasSpacePressed())
			{
				LampProp lampPropInUse = game.getLampInUse(player);
				if (lampPropInUse != null)
					lampPropInUse.UseLamp(player, game);
			}
	}

	private void chestActionListener ()
	{
		for (Player player : game.getPlayers())
			if (player.isNotGhostAndHasSpacePressed())
				chestAction(player);
	}

	private void chestAction (Player player)
	{
		ChestProp chestPropInUse = game.getChestInUse(player);
		if (chestPropInUse != null)
			chestPropInUse.UseChest(player, game);

		player.setSpacePressedDisabled(Constants.CHEST_INTERACTION_SPACE_DELAY);
		player.setForcingSynchronization(true);
	}

	private void antiAFKFilter ()
	{
		if (server.getTick() % Constants.TICK_1SEC != 0)
			return;

		for (ChestProp chestProp : game.getChests())
			if (chestProp.getPlayerUUID() != null) {
				Player afkPlayer = game.getPlayerByID(chestProp.getPlayerUUID());
				afkPlayer.setAfkPenalty(afkPlayer.getAfkPenalty() + 1);
			}
	}

	private void endGame()
	{
		setWinnerFlag();
		System.out.println("Winner: " + (game.isWinnerFlag()
				? "Vampire"
				: "Survivors")
		);

		game.setGameStatus(GameStatus.SUMMARY);
		server.setTick(0);
	}

	private void setWinnerFlag()
	{
		if (game.countSurvivors() <= 0)
			game.setWinnerFlag(Constants.WINNER_VAMP);
		else
			game.setWinnerFlag(Constants.WINNER_SURVIVOR);
	}

}
