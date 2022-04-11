package pl.gr14b.transylwania;

import java.io.Serializable;

class FlagPacket extends Packets implements Serializable
{
	GameStatus gameStatus;
	int waitingTime;
	int gameTime;
	int afkPenalty;
	boolean isWinner;

	FlagPacket
	(
			GameStatus gameStatus,
			int waitingTime,
			int gameTime,
			int afkPenalty,
			boolean isWinner
	)
	{
		this.gameStatus = gameStatus;
		this.waitingTime = waitingTime;
		this.gameTime = gameTime;
		this.afkPenalty = afkPenalty;
		this.isWinner = isWinner;
	}

}