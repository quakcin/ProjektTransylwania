package pl.gr14b.transylwania;

import java.util.ArrayList;

class ServerBackgroundWorker
{
	private Game game;
	private int tick;

	ServerBackgroundWorker (Game game)
	{
		this.game = game;
		playersInQue = 0;
		tick = 0;
	}

	void Update ()
	{
		if (game.getGameStatus() == Game.GAME_STATUS_LOBBY)
			UpdateLobby();
		else if (game.getGameStatus() == Game.GAME_STATUS_INTRO)
			UpdateIntro();
		else if (game.getGameStatus() == Game.GAME_STATUS_KILLING)
			UpdateKilling();
		else if (game.getGameStatus() == Game.GAME_STATUS_SUMMARY)
			UpdateSummary();

		UpdateAlways();
		CollisionWatch();
		tick += 1;
	}

	private int playersInQue;

	private void UpdateLobby ()
	{
		int playersCount = game.getPlayers().size();
		if (playersCount != playersInQue)
		{
			// -- change waiting time!
			if (playersCount >= 2)
				game.setWaitingTime((8 - playersCount) * 5);
			else
				game.setWaitingTime(8 * 5);
			playersInQue = playersCount;
		}
		else
		{
			if (playersCount >= 2)
			{
				if (game.getWaitingTime() <= 0)
				{
					game.setGameStatus(Game.GAME_STATUS_INTRO);
					game.spreadOutLampsAroundTheMap();
					for (Player p : game.getPlayers())
						p.teleportToSpawn(game.getPlayers());
					game.setWaitingTime(5);
					tick = 1; // reset ticks!
				}
				else if (tick % 2 == 0) // FIXME: TIMER SHOULD BE SET TO 20
					game.setWaitingTime(game.getWaitingTime() - 1);
			}
		}

		// event when: player presses space:
		for (Player p : game.getPlayers())
			if (p.isSpacePressed())
			{
				// set to a random survivor icon
				p.randomizeCharacter();
				p.setSpacePressedDisabled(Game.LOBBY_CHANGE_PLAYER_MODEL__DELAY);
			}
	}

	private void CollisionWatch ()
	{
		ArrayList<Player> players = game.getPlayers();
		for (int i = 0; i < players.size(); i++)
			for (int j = i + 1; j < players.size(); j++)
			{
				Player player = game.getPlayers().get(i);
				Player otherPlayer = game.getPlayers().get(j);

				if (player.getPlayerType() == Player.PLAYER_TYPE_GHOST || otherPlayer.getPlayerType() == Player.PLAYER_TYPE_GHOST)
					continue;

				if (player.getDist(otherPlayer.getX(), otherPlayer.getY()) < 50)
				{
					double playerAngle = player.getAng();
					double otherPlayerAngle = otherPlayer.getAng();

					player.setAng(otherPlayerAngle);
					otherPlayer.setAng(playerAngle);

					player.Push(90, game);
					otherPlayer.Push(90, game);

					player.setAng(playerAngle);
					otherPlayer.setAng(otherPlayerAngle);

					player.setPlayerMoving(true);
					otherPlayer.setPlayerMoving(true);

					player.setForcingSynchronization(true);
					otherPlayer.setForcingSynchronization(true);
				}

			}
	}

	private void UpdateIntro ()
	{
		// FIXME: MAKE SURE THAT THERE IS AT LEAST TWO PLAYERS
		//        AND REFACTOR OLD RANDOMS INTO NEW ONES
		if (game.getWaitingTime() <= 0)
		{
			// Blow out the lamps then choose vampire
			game.setGameStatus(Game.GAME_STATUS_KILLING);
			Player newVampire = game.getPlayers().get((int) Math.round(Math.random() * (game.getPlayers().size() - 1)));
			newVampire.setPlayerType(Player.PLAYER_TYPE_VAMPIRE);
			newVampire.setForcingSynchronization(true);

			// Set new game time
			double[] timesPerPlayers = {3d, 4.5d, 5.5d, 6.3d, 7d, 7.5d, 8d};
			game.setGameTime((int) Math.round(timesPerPlayers[game.countSurvivors() - 1] * 60d)); // WARN: Might get over bounds

			// Blow out the lamps
			for (Lamp lamp : game.getLamps())
				lamp.BlowOut();
		}
		else if (tick % 20 == 0) // FIXME: Timing
		{
			game.setWaitingTime(game.getWaitingTime() - 1);
		}

		if (tick == 56) // .magic number.
		{
			game.playSoundNear(0, 0, 100000L, "intro");
			game.setGlobalLight(1.0d);
		}
	}

	private void UpdateKilling ()
	{
		if (game.countSurvivors() <= 0 || !game.isVampireConnected() || game.getGameTime() <= 0)
		{
			// NOW: Move to summary, either players are dead or vamp has dc'ed
			if (game.countSurvivors() <= 0)
				game.setWinnerFlag(Game.WINNER_VAMP);
			if (!game.isVampireConnected() || game.getGameTime() <= 0)
				game.setWinnerFlag(Game.WINNER_SURVIVOR);
			game.setGameStatus(Game.GAME_STATUS_SUMMARY);
			System.out.println("Winner: " + (game.isWinnerFlag() ? "Vampire" : "Survivors"));
			tick = 0;
		}
		else if (tick % 10 == 0) // FIXME: It should be 20, even thought 10 works perfectly fine
		{
			game.setGameTime(game.getGameTime() - 1);
		}

		// WARN: Order matters

		// -- in case vamp attacks
		vampAttackListener();

		// -- in case someone uses lamp
		lampActionListener();
	}

	private void UpdateSummary ()
	{
		if (tick > 60)
		{
			game.Reset();
		}
	}

	private void UpdateAlways ()
	{
		for (Player p : game.getPlayers())
			if (p.getPlayerType() == Player.PLAYER_TYPE_GHOST)
				if (p.getDist(game.MAP_SIZE * 405, game.MAP_SIZE * 405) > (game.MAP_SIZE + 1) * 405)
					p.teleportToSpawn(game.getPlayers());
	}

	private void lampActionListener ()
	{
		for (Player p : game.getPlayers())
			if (p.getPlayerType() != Player.PLAYER_TYPE_GHOST) // TODO: .Ensure not zombie, in case we add one.
				if (p.isSpacePressed())
				{
					Lamp lampInUse = game.getLampInUse(p);
					if (lampInUse != null)
							lampInUse.UseLamp(p, game);
					// Apply delay no matter what
					p.setSpacePressedDisabled(25 * 2);
					p.setForcingSynchronization(true);
				}
		// .Nice pyramid.
	}

	private void vampAttackListener ()
	{
		// Actual Game handler
		Player vamp = game.getVamp();

		if (vamp != null)
			if (vamp.isSpacePressed())
			{
				Player prey = game.getNearestSurvivor(vamp);
				if (prey != null)
				{
					double dist = prey.getDist(vamp.getX(), vamp.getY());
					if (dist < 100)
					{
						// Can attack
						prey.Damage(1, game);
						vamp.FacePlayer(prey);
						vamp.setSpacePressedDisabled(Game.VAMP_ATTACK_DELAY);
					}
					else if (dist < 250)
					{
						game.playSoundNear(vamp.getX(), vamp.getY(), 810 * 2, "vampMiss");
						vamp.setSpacePressedDisabled(Game.VAMP_ATTACK_MISS_DELAY);
						vamp.FacePlayer(prey);
					}
					else
					{
						game.playSoundNear(vamp.getX(), vamp.getY(), 810 * 6, "vampScream");
						// vamp.setSpacePressedDisabled(Game.VAMP_ATTACK_MISS_DELAY); -- Lamps apply delay
					}
				}
				vamp.setForcingSynchronization(true);
			}
	}

}
