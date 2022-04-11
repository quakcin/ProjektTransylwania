package pl.gr14b.transylwania;

import java.awt.*;
import java.awt.image.BufferedImage;

class PaintHUD extends GraphicsPainter
{
	PaintHUD(Client client)
	{
		super(client);
	}

	private boolean isChangeInLightnessSerious()
	{
		return Math.abs(client.getPrivateLight() - client.getGlobalLight()) > 0.1d;
	}

	private void updateLightness()
	{
		client.setPrivateLight(
				client.getPrivateLight() + (client.getGlobalLight() > client.getPrivateLight()
						? +0.0015d
						: -0.0015d
				)
		);
	}

	private void paintLightMask()
	{
		if (isChangeInLightnessSerious())
			updateLightness();

		g.drawImage(
				applyOpacityToAnImage(stuff.getLightMask(), client.getPrivateLight()),
				0, 0, width, height, null
		);
	}

	private BufferedImage applyOpacityToAnImage(BufferedImage originalImage, double opacity)
	{
		BufferedImage bufferedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = bufferedImage.createGraphics();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) opacity));
		g.drawImage(originalImage, 0, 0, null);
		g.dispose();
		return bufferedImage;
	}

	private void paintLobbyOverlay()
	{
		g.setFont(stuff.getFont().deriveFont(35f));
		g.setColor(Color.WHITE);
		g.drawString("OCZEKIWANIE NA", 50, 80);
		g.drawString("GRACZY (" + clientGame.getPlayers().size() + " / 8)", 50, 130);

		if (clientGame.getWaitingTime() != 40)
			g.drawString("" + clientGame.getWaitingTime() + "s", 50, height - 50);
	}

	private void paintKillingTimer()
	{
		g.setFont(stuff.getFont().deriveFont(35f));
		g.setColor(Color.WHITE);

		int minutes = Math.floorDiv(clientGame.getGameTime(), 60);
		int seconds = clientGame.getGameTime() - minutes * 60;
		if (minutes != 0)
			g.drawString("" + minutes + "m " + seconds + "s", 50, height - 50);
		else
			g.drawString("" + seconds + "s", 50, height - 50);
	}

	private void paintVampireHints()
	{
		if (clientGame.getPlayer().getPlayerType().equals(PlayerType.VAMPIRE))
		{
			vampDefaultArrow(g, offX, offY);
			for (Player p : clientGame.getPlayers())
				if (p.getAfkPenalty() > 15)
					pointArrow(g, offX, offY, p.getX(), p.getY());
		}
	}

	private void paintKillingOverlay()
	{
		paintKillingTimer();
		paintVampireHints();
	}

	private void paintSummaryOverlay()
	{
		if (clientGame.getGameStatus() == GameStatus.SUMMARY)
			g.drawImage(
					stuff.getSummary().get(clientGame.isWinnerFlag() ? 1 : 0),
					0, 0, width, height, null
			);
	}

	private void paintIntroOverlay()
	{
		client.setRoleCallTime(client.getRoleCallTime() - 1);

		if (client.getRoleCallTime() > 0 && !clientGame.getPlayer().getPlayerType().equals(PlayerType.GHOST))
			g.drawImage(stuff.getpType().get(clientGame.getPlayer().getPlayerType().equals(PlayerType.SURVIVOR) ? 1 : 0), 0, 0, width, height, null);
	}

	private void paintStageSpecificOverlay()
	{
		if (clientGame.getGameStatus().equals(GameStatus.LOBBY))
			paintLobbyOverlay();

		if (clientGame.getGameStatus().equals(GameStatus.KILLING))
			paintKillingOverlay();

		if (clientGame.getGameStatus().equals(GameStatus.SUMMARY))
			paintSummaryOverlay();

		//if (clientGame.getGameStatus().equals(GameStatus.INTRO)) //?
		paintIntroOverlay();
	}

	private void paintHealth()
	{
		// draw health
		if (clientGame.getPlayer().getPlayerType().equals(PlayerType.SURVIVOR))
			g.drawImage(
					stuff.getHealth().get(clientGame.getPlayer().getHealth()),
					width - 30 - 90,50, 90, 108,null
			);
	}

	@Override
	void paint()
	{
		paintLightMask();
		paintStageSpecificOverlay();
		paintHealth();
	}

	private void pointArrow(Graphics g, int offX, int offY, double dx, double dy)
	{
		// Draw Arrow here:
		Player player = clientGame.getPlayer();
		if (player.getDist(dx, dy) < 500)
			return;

		double ang = Math.atan2(player.getY() - dy, player.getX() - dx);
		double ax = Math.cos(ang) * 115;
		double ay = Math.sin(ang) * 115;
		// Rotate Arrow
		BufferedImage arrow = new BufferedImage(stuff.getArrow().getWidth(), stuff.getArrow().getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D arrGfx = arrow.createGraphics();
		arrGfx.rotate(ang + (90 * (3.1415 / 180)), arrow.getWidth() >> 1, arrow.getHeight() >> 1);
		arrGfx.drawImage(stuff.getArrow(), 0, 0, null);
		g.drawImage(arrow, (int) ax - 20 + offX, (int) ay - 20 + offY, 40, 40, null);
		arrGfx.dispose();
	}

	private void vampDefaultArrow(Graphics g, int offX, int offY)
	{
		Player pointTo = clientGame.getNearestPlayerToPointTo();
		if (pointTo != null)
			pointArrow(g, offX, offY, pointTo.getX(), pointTo.getY());
	}

}
