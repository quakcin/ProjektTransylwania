package pl.gr14b.transylwania;

class PaintPlayers extends GraphicsPainter
{
	PaintPlayers(Client client) {
		super(client);
	}

	@Override
	void paint()
	{
		for (Player player : clientGame.getPlayers())
			attemptToPainAPlayer(player);
	}

	private void attemptToPainAPlayer(Player p)
	{
		if (isPlayerHidden(p))
			return;

		if (isAGhostThatCannotBeVisibleByMainPlayer(p))
			return;

		p.drawPlayer(this);
	}

	private boolean isPlayerHidden(Player p)
	{
		return ChestProp.isPlayerHidden(clientGame.getChests(), p.getPlayerID());
	}

	private boolean isAGhostThatCannotBeVisibleByMainPlayer(Player p)
	{
		return !isMainPlayer(p)
				&& isPlayerAGhost(p)
				&& !isMainPlayerGhost()
				&& !isMainPlayerHidden();
	}

	private boolean isMainPlayerHidden()
	{
		return clientGame.getPlayer().isHidden(clientGame.getChests());
	}

	private boolean isMainPlayer(Player p)
	{
		return p.getPlayerID().equals(clientGame.getPlayer().getPlayerID());
	}

	private boolean isPlayerAGhost(Player p)
	{
		return p.getPlayerType().equals(PlayerType.GHOST);
	}

	private boolean isMainPlayerGhost()
	{
		return clientGame.getPlayer().getPlayerType().equals(PlayerType.GHOST);
	}


}
