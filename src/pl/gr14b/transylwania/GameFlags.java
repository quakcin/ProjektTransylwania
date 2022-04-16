package pl.gr14b.transylwania;

import java.io.Serializable;

class GameFlags implements Serializable
{
	private GameStatus gameStatus;
	private int waitingTime;
	private int gameTime;
	private double globalLight;
	private boolean winnerFlag;

	GameFlags ()
	{
		globalLight = Constants.DEFAULT_GLOBAL_LIGHTNESS;
		waitingTime = 0;
		gameTime = 0;
		gameStatus = GameStatus.LOBBY;
		winnerFlag = false;
	}


	GameStatus getGameStatus() {
		return gameStatus;
	}

	void setGameStatus(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}

	int getWaitingTime() {
		return waitingTime;
	}

	void setWaitingTime(int waitingTime) {
		this.waitingTime = waitingTime;
	}

	int getGameTime() {
		return gameTime;
	}

	void setGameTime(int gameTime) {
		this.gameTime = gameTime;
	}

	double getGlobalLight() {
		return globalLight;
	}

	void setGlobalLight(double globalLight) {
		this.globalLight = globalLight;
	}

	boolean isWinnerFlag() {
		return winnerFlag;
	}

	void setWinnerFlag(boolean winnerFlag) {
		this.winnerFlag = winnerFlag;
	}
}
