package pl.gr14b.transylwania;

public class ClientHandleFlagPacket extends ClientHandlePacket
{
	ClientHandleFlagPacket(Client client, String packetClass) {
		super(client, packetClass);
	}

	@Override
	void packetHandler(Object packet)
	{
		FlagPacket flagPacket = ((FlagPacket) packet);

		if (isPlayingTickingSound(flagPacket.waitingTime))
			Stuff.playSound("tick");

		copyFlags(flagPacket);
	}

	private void copyFlags (FlagPacket flagPacket)
	{
		clientGame.setGameTime(flagPacket.gameTime);
		clientGame.setWaitingTime(flagPacket.waitingTime);
		clientGame.setGameStatus(flagPacket.gameStatus);
		clientGame.setWinnerFlag(flagPacket.isWinner);
	}

	private boolean isPlayingTickingSound(int waitingTime)
	{
		return clientGame.getWaitingTime() != waitingTime
				&& clientGame.getGameStatus().equals(GameStatus.LOBBY);
	}
}
