package pl.gr14b.transylwania;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

class GameMechanics implements Serializable
{
	private Game game;
	GameMechanics (Game game)
	{
		this.game = game;
	}

	UUID playerJoinEvent (String playerNickName, ArrayList<Player> existingPlayers)
	{
		UUID newPlayerID = UUID.randomUUID();
		Player newPlayer = new Player(newPlayerID, playerNickName, existingPlayers);
		game.getPlayers().add(newPlayer);

		if (isNewPlayerSupposedToBeAGhost())
			newPlayer.setPlayerType(PlayerType.GHOST);

		return newPlayerID;
	}

	private boolean isNewPlayerSupposedToBeAGhost ()
	{
		return game.getGameStatus().equals(GameStatus.INTRO)
				|| game.getGameStatus().equals(GameStatus.KILLING)
				|| game.getGameStatus().equals(GameStatus.SUMMARY)
				;
	}

	int countSurvivors ()
	{
		int count = 0;
		for (Player p : game.getPlayers())
			if (p.getPlayerType().equals(PlayerType.SURVIVOR))
				count += 1;
		return count;
	}

	boolean isVampireConnected ()
	{
		for (Player p : game.getPlayers())
			if (p.getPlayerType().equals(PlayerType.VAMPIRE))
				return true;
		return false;
	}

	Player getVamp ()
	{
		for (Player p : game.getPlayers())
			if (p.getPlayerType().equals(PlayerType.VAMPIRE))
				return p;
		return null;
	}

	Player getNearestSurvivor (Player player)
	{
		Player nearest = null;

		for (Player p : game.getPlayers())
			if (p != player && p.getPlayerType().equals(PlayerType.SURVIVOR))
				if (nearest == null)
					nearest = p;
				else
				{
					double dist = p.getDist(player.getX(), player.getY());
					if (dist < nearest.getDist(player.getX(), player.getY()))
						nearest = p;
				}
		return nearest;
	}

	Player getNearestPlayerToPointTo ()
	{
		Player vamp = getVamp();
		Player surv = getNearestSurvivor(vamp);

		if (vamp == null || surv == null)
			return null;

		if (Chest.isPlayerHidden(game.getChests(), surv.getPlayerID()))
			return null;

		double maxVisDistance = Math.ceil((double) countSurvivors() / 3.5) * 810 + 500;
		double distToPlayer = vamp.getDist(surv.getX(), surv.getY());

		if (distToPlayer <= maxVisDistance)
			return surv;
		else
			return null;
	}

	ArrayList<Player> getPlayersNear (double dx, double dy, double dist)
	{
		ArrayList<Player> playersNear = new ArrayList<>();

		for (Player p : game.getPlayers())
			if (p.getDist(dx, dy) <= dist)
				playersNear.add(p);

		return playersNear;
	}

	void playSoundNear (double dx, double dy, double near, String sound)
	{
		for (Player p : getPlayersNear(dx, dy, near)) {
			p.setNextSoundInQue(sound);
			p.setForcingSynchronization(true);
		}
	}

	Player getPlayerByID (UUID id)
	{
		for (Player p : game.getPlayers())
			if (p.getPlayerID().equals(id))
				return p;
		return null;
	}

	Player getPlayer (UUID playerID)
	{
		for (Player p : game.getPlayers())
			if (p.getPlayerID().equals(playerID))
				return p;
		return null;
	}

	void resetGameServerWise ()
	{
		for (Player p : game.getPlayers())
			p.Reset(game.getPlayers());

		game.setWaitingTime((8 - game.getPlayers().size()) * 5);
		game.setGameTime(0);
		game.setGameStatus(GameStatus.LOBBY);
		game.setGlobalLight(0.6d);
		game.setGameMap(new GameMap(game));
	}

}
