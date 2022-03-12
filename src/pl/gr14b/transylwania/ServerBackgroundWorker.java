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
				}
				else if (tick % 10 == 0) // FIXME: TIMER SHOULD BE SET TO 20
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
			for (int j = 0; j < players.size(); j++)
			{
				Player player = players.get(i);
				Player otherPlayer = players.get(j);

				if (player.getPlayerID().equals(otherPlayer.getPlayerID()) || otherPlayer.getPlayerType() == Player.PLAYER_TYPE_GHOST)
					continue;

				double oldPlayerAngle = player.getAng();
				double oldOtherPlayerAngle = otherPlayer.getAng();
				player.setAng(oldOtherPlayerAngle);
				otherPlayer.setAng(oldPlayerAngle);

				while (player.getDist(otherPlayer.getX(), otherPlayer.getY()) < 50)
				{
					player.Push(90, game);
					otherPlayer.Push(90, game);
				}
				player.setAng(oldOtherPlayerAngle);
				otherPlayer.setAng(oldPlayerAngle);

				player.setForcingSynchronization(true);
				otherPlayer.setForcingSynchronization(true);
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
			game.setGameTime(game.countSurvivors() * 4 * 60);
			for (Lamp lamp : game.getLamps())
				lamp.BlowOut();

			newVampire.setForcingSynchronization(true);
		}
		else if (tick % 20 == 0)
		{
			game.setWaitingTime(game.getWaitingTime() - 1);
		}
	}

	private void UpdateKilling ()
	{
		if (game.countSurvivors() <= 0 || !game.isVampireConnected() || game.getGameTime() <= 0)
		{
			// NOW: Move to summary, either players are dead or vamp has dc'ed
			game.setGameStatus(Game.GAME_STATUS_SUMMARY);
		}
		else if (tick % 20 == 0)
		{
			game.setGameTime(game.getGameTime() - 1);
		}

		// WARN: Order matters
		// -- in case someone uses lamp
		lampActionListener();

		// -- in case vamp attacks
		vampAttackListener();
	}

	private void UpdateSummary ()
	{
		game.Reset();
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
					if (dist < 95)
					{
						// Can attack
						prey.Damage(1, game);
						vamp.FacePlayer(prey);
					}
					else if (dist < 130)
						vamp.FacePlayer(prey);
				}
				vamp.setSpacePressedDisabled(Game.VAMP_ATTACK_DELAY);
				vamp.setForcingSynchronization(true);
			}
	}

}
