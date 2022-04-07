package pl.gr14b.transylwania;

public class ServerGameKillingStage extends ServerGameStage
{
	ServerGameKillingStage(ServerBackgroundThread serverBackgroundThread, GameStatus stageCode) {
		super(serverBackgroundThread, stageCode);
	}

	boolean isGameOver ()
	{
		return game.countSurvivors() <= 0
				|| game.isVampireConnected()
				|| game.getGameTime() <= 0;
	}

	void setWinnerFlag ()
	{
		if (game.countSurvivors() <= 0)
			game.setWinnerFlag(Game.WINNER_VAMP);

		if (game.isVampireConnected() || game.getGameTime() <= 0)
			game.setWinnerFlag(Game.WINNER_SURVIVOR);
	}

	void endGame ()
	{
		setWinnerFlag();
		System.out.println("Winner: " + (game.isWinnerFlag()
				? "Vampire"
				: "Survivors")
		);

		game.setGameStatus(GameStatus.SUMMARY);
		server.setTick(0);
	}

	void updateServerListeners ()
	{
		vampAttackListener();
		lampActionListener();
		chestActionListener();
		antiAFKFilter();
	}

	@Override
	void UpdateStage()
	{
		if (server.hasSecondPassed())
			game.setGameTime(game.getGameTime() - 1);

		else if (!isGameOver())
			updateServerListeners();

		else
			endGame();
	}

	private void lampActionListener()
	{
		for (Player p : game.getPlayers())
			if (p.getPlayerType() != Player.PLAYER_TYPE_GHOST && p.isSpacePressed())
			{
				Lamp lampInUse = game.getLampInUse(p);
				if (lampInUse != null)
					lampInUse.UseLamp(p, game);
			}
	}

	void chestAction (Player player)
	{
		Chest chestInUse = game.getChestInUse(player);
		if (chestInUse != null)
			chestInUse.UseChest(player, game);

		player.setSpacePressedDisabled(40); // FIXME: MAGIC
		player.setForcingSynchronization(true);
	}

	private void chestActionListener ()
	{
		for (Player player : game.getPlayers())
			if (player.getPlayerType() != Player.PLAYER_TYPE_GHOST && player.isSpacePressed())
				chestAction(player);
	}

	void vampSuccessfulAttack (Player vamp, Player prey)
	{
		prey.Damage(game);
		vamp.FacePlayer(prey);
		vamp.setSpacePressedDisabled(Game.VAMP_ATTACK_DELAY);
	}

	void vampFailedAttack (Player vamp, Player prey)
	{
		game.playSoundNear(vamp.getX(), vamp.getY(), 810 * 2, "vampMiss");
		vamp.setSpacePressedDisabled(Game.VAMP_ATTACK_MISS_DELAY);
		vamp.FacePlayer(prey);
	}

	void vampAttackPrey (Player vamp, Player prey)
	{
		double dist
				= prey.getDist(vamp.getX(), vamp.getY());

		if (dist < 100)
			vampSuccessfulAttack(vamp, prey);

		else if (dist < 250)
			vampFailedAttack(vamp, prey);

		else
			game.playSoundNear(vamp.getX(), vamp.getY(), 810 * 6, "vampScream");
	}

	void vampAttack (Player vamp)
	{
		Player prey = game.getNearestSurvivor(vamp);

		if (prey != null)
			vampAttackPrey(vamp, prey);

		vamp.setForcingSynchronization(true);
	}

	private void vampAttackListener ()
	{
		Player vamp = game.getVamp();
		if (vamp != null)
			if (vamp.isSpacePressed())
				vampAttack(vamp);
	}

	private void antiAFKFilter ()
	{
		if (server.getTick() % 20 != 0)
			return;

		for (Chest chest : game.getChests())
			if (chest.getPlayerUUID() != null) {
				Player afkPlayer = game.getPlayerByID(chest.getPlayerUUID());
				afkPlayer.setAfkPenalty(afkPlayer.getAfkPenalty() + 1);
			}
	}



}
