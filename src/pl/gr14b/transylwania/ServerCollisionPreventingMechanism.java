package pl.gr14b.transylwania;

import java.util.ArrayList;

public class ServerCollisionPreventingMechanism
{
	Game game;
	ServerCollisionPreventingMechanism (Game serverGame)
	{
		this.game = serverGame;
	}

	boolean isOneOfPlayersGhost (Player player, Player otherPlayer)
	{
		return player.getPlayerType() == Player.PLAYER_TYPE_GHOST
				|| otherPlayer.getPlayerType() == Player.PLAYER_TYPE_GHOST;
	}

	boolean isOneOfPlayersHidden (Player player, Player otherPlayer)
	{
		return Chest.isPlayerHidden(game.getChests(), player.getPlayerID())
				|| Chest.isPlayerHidden(game.getChests(), otherPlayer.getPlayerID());
	}

	boolean canPlayersCollide (Player player, Player otherPlayer)
	{
		return !(isOneOfPlayersGhost(player, otherPlayer) && isOneOfPlayersHidden(player, otherPlayer));
	}

	void pushAwayCollidingPlayers (Player player, Player otherPlayer)
	{
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

	void checkIfTwoPlayersAreColliding (Player player, Player otherPlayer)
	{
		if (canPlayersCollide(player, otherPlayer))
			pushAwayCollidingPlayers(player, otherPlayer);
	}

	void preventCollisions()
	{
		ArrayList<Player> players = game.getPlayers();
		for (int i = 0; i < players.size(); i++)
			for (int j = i + 1; j < players.size(); j++)
				checkIfTwoPlayersAreColliding(game.getPlayers().get(i), game.getPlayers().get(j));
		preventMapCollisions();
	}

	void preventMapCollisions ()
	{
		for (Player p : game.getPlayers())
			if (p.getPlayerType() == Player.PLAYER_TYPE_GHOST)
				if (p.getDist(game.MAP_SIZE * 405, game.MAP_SIZE * 405) > (game.MAP_SIZE + 1) * 405)
					p.teleportToSpawn(game.getPlayers());
	}
}
