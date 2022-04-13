package pl.gr14b.transylwania;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerLocation implements Serializable
{
	private int pushAttempts;
	private Player player;
	private double x;
	private double y;
	private double ang;

	PlayerLocation (Player player)
	{
		this.player = player;
		this.ang = 0;
	}


	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getAng() {
		return ang;
	}

	public void setAng(double ang) {
		this.ang = ang;
	}

	void push (double speed, Game clientGame)
	{
		if (Chest.isPlayerHidden(clientGame.getChests(), player.getPlayerID()))
			return;

		pushAttempts = 0;
		recursivePush(player.getPlayerFlags().getPlayerType().equals(PlayerType.VAMPIRE)? speed * 1.2 : speed, clientGame);
	}



	private boolean isCollidingWithWallAtPoint (int mx, int my)
	{
		return ((mx == 0 || mx == 8) && my != 4)
				|| ((my == 0 || my == 8) && mx != 4);
	}

	private boolean isCollidingWithWall (double vx, double vy)
	{
		for (int i = -1; i <= 1; i += 2)
			for (int j = -1; j <= 1; j += 2)
			{
				int mx = (int) ((vx + i * 15) / 90) % 9;
				int my = (int) ((vy + j * 15) / 90) % 9;

				if (isCollidingWithWallAtPoint(mx, my))
					return true;
			}
		return false;
	}

	private boolean isCollidingWithHorizontalDoors (double vx, double vy, Game clientGame)
	{
		for (int y = 0; y < Constants.MAP_SIZE + 1; y++)
			for (int x = 0; x < Constants.MAP_SIZE; x++)
			{
				int index = y * (Constants.MAP_SIZE) + x;
				int dx = x * 810 - 450 + 90 + 810;
				int dy = y * 810;
				double doorDist = Math.sqrt(Math.pow(vx - dx, 2) + Math.pow(vy - dy, 2));

				if (doorDist < 90 && clientGame.getHorizontalDoors()[index])
					return true;
			}
		return false;
	}

	private boolean isCollidingWithVerticalDoors (double vx, double vy, Game clientGame)
	{
		for (int y = 0; y < Constants.MAP_SIZE; y++)
			for (int x = 0; x < Constants.MAP_SIZE + 1; x++)
			{
				int index = x * (Constants.MAP_SIZE) + y;
				int dx = x * 810;
				int dy = y * 810 - 450 + 90 + 810;
				double doorDist = Math.sqrt(Math.pow(vx - dx, 2) + Math.pow(vy - dy, 2));

				if (doorDist < 90 && clientGame.getVerticalDoors()[index])
					return true;
			}
		return false;
	}

	private boolean isCollidingWithAnotherPlayer (double vx, double vy, Game clientGame)
	{
		for (Player p : clientGame.getPlayers())
			if (p.getPlayerID() != player.getPlayerID() && !p.getPlayerType().equals(PlayerType.GHOST))
				if (!Chest.isPlayerHidden(clientGame.getChests(), p.getPlayerID()))
					if (Math.sqrt(Math.pow(vx - p.getX(), 2) + Math.pow(vy - p.getY(), 2)) < 50)
						return true;

		return false;
	}


	private boolean isCollidingWithAnything (double vx, double vy, Game game)
	{
		return isCollidingWithWall(vx, vy)
				|| isCollidingWithAnotherPlayer(vx, vy, game)
				|| isCollidingWithHorizontalDoors(vx, vy, game)
				|| isCollidingWithVerticalDoors(vx, vy, game)
				;
	}

	private void performRecursivePushAttempt (Game clientGame)
	{
		rotatePlayerOtherWayAround();
		if (pushAttempts++ < 10)
			recursivePush(10, clientGame);
		rotatePlayerOtherWayAround();
	}

	private void rotatePlayerOtherWayAround ()
	{
		ang += 180 * (3.1415 / 180);
	}

	private void recursivePush (double speed, Game clientGame)
	{
		double vx = getX() + -1 * Math.cos(ang) * speed;
		double vy = getY() + -1 * Math.sin(ang) * speed;

		if (isCollidingWithAnything(vx, vy, clientGame) && !player.isPlayerAGhost())
		{
			performRecursivePushAttempt(clientGame);
			return;
		}

		x = vx;
		y = vy;
	}

	void turn (double delta) {
		ang += delta * (3.1415 / 180);
	}

	void copyLocation(Player otherPlayer) {
		x = otherPlayer.getX();
		y = otherPlayer.getY();
		ang = otherPlayer.getAng();
	}

	private void serverTeleport(double dx, double dy)
	{
		x = dx;
		y = dy;
		player.getPlayerFlags().setForcingSynchronization(true);
	}

	double getDist (double dx, double dy)
	{
		return Math.sqrt(Math.pow(dx - x, 2) + Math.pow(dy - y, 2));
	}

	void facePlayer (Player otherPlayer)
	{
		ang = Math.atan2(y - otherPlayer.getY(), x - otherPlayer.getX());
		player.setForcingSynchronization(true);
		player.setForcingLocationSynchronization(true);
	}

	void face (double dx, double dy)
	{
		ang = Math.atan2(y - dy, x - dx);
		player.setForcingSynchronization(true);
		player.setForcingLocationSynchronization(true);
	}

	private double distanceToNearestPlayer (ArrayList<Player> allPlayers)
	{
		double nearest = 0xFFFFFF;

		for (Player p : allPlayers)
			if (p.getPlayerID() != player.getPlayerID())
			{
				double dist = player.getDist(p.getX(), p.getY());
				if (dist < nearest)
					nearest = dist;
			}

		return nearest;
	}

	void teleportToSpawn (ArrayList<Player> allPlayers)
	{
		do
			teleportPlayerToRandomSpawnLocation();
		while (distanceToNearestPlayer(allPlayers) < 90);
	}

	private void teleportPlayerToRandomSpawnLocation ()
	{
		serverTeleport(2 * 810 + ((Math.random() * 6) + 1) * 100, 2 * 810 + ((Math.random() * 6) + 1) * 100);
	}

}
