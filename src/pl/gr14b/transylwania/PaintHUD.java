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
						? + Constants.LIGHTNESS_DELTA
						: - Constants.LIGHTNESS_DELTA
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
		g.setFont(
				stuff.getFont().deriveFont(Constants.SCREEN_DEFAULT_FONT_SIZE)
		);
		g.setColor(Color.WHITE);
		g.drawString("OCZEKIWANIE NA", Constants.SCREEN_PADDING, Constants.SCREEN_PADDING + 30);
		g.drawString(
				"GRACZY (" + clientGame.getPlayers().size() + " / 8)",
				Constants.SCREEN_PADDING, Constants.SCREEN_PLAYERS_COUNT_Y_OFFSET
		);

		if (clientGame.getWaitingTime() != 40)
			g.drawString(
					"" + clientGame.getWaitingTime() + "s",
					Constants.SCREEN_PADDING,
					height - Constants.SCREEN_PADDING
			);
	}

	private void paintKillingTimer()
	{
		g.setFont(
				stuff.getFont().deriveFont(Constants.SCREEN_DEFAULT_FONT_SIZE)
		);
		g.setColor(Color.WHITE);

		int minutes = Math.floorDiv(clientGame.getGameTime(), Constants.MINUTE_IN_SECONDS);
		int seconds = clientGame.getGameTime() - minutes * Constants.MINUTE_IN_SECONDS;
		if (minutes != 0)
			g.drawString(
					+ minutes + "m " + seconds + "s",
					Constants.SCREEN_PADDING,
					height - Constants.SCREEN_PADDING
			);
		else
			g.drawString(
					"" + seconds + "s",
					Constants.SCREEN_PADDING,
					height - Constants.SCREEN_PADDING
			);
	}

	private void paintVampireHints()
	{
		if (clientGame.getPlayer().getPlayerType().equals(PlayerType.VAMPIRE))
		{
			vampDefaultArrow(g, offX, offY);
			for (Player p : clientGame.getPlayers())
				if (p.getAfkPenalty() > Constants.AFK_PENALTY_TIME)
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

		if (canPaintIntroOverlay())
			g.drawImage(
					stuff.getPType().get(
							clientGame.getPlayer().getPlayerType().equals(PlayerType.SURVIVOR) ? 1 : 0
					),
					0, 0, width, height, null
			);
	}

	private boolean canPaintIntroOverlay ()
	{
		return client.getRoleCallTime() > 0
				&& !clientGame.getPlayer().getPlayerType().equals(PlayerType.GHOST);
	}

	private void paintStageSpecificOverlay()
	{
		if (clientGame.getGameStatus().equals(GameStatus.LOBBY))
			paintLobbyOverlay();

		if (clientGame.getGameStatus().equals(GameStatus.KILLING))
			paintKillingOverlay();

		if (clientGame.getGameStatus().equals(GameStatus.SUMMARY))
			paintSummaryOverlay();

		paintIntroOverlay();
	}

	private void paintHealth()
	{
		if (clientGame.getPlayer().getPlayerType().equals(PlayerType.SURVIVOR))
			g.drawImage(
					stuff.getHealth().get(
							clientGame.getPlayer().getHealth()
					),
				width - Constants.SCREEN_HP_X_PADDING,
					Constants.SCREEN_PADDING,
				90, 108,
					null
			);
	}

	@Override
	void paint()
	{
		paintLightMask();
		paintStageSpecificOverlay();
		paintHealth();
	}

	private BufferedImage getArrowImage ()
	{
		return new BufferedImage(
				stuff.getArrow().getWidth(),
				stuff.getArrow().getHeight(),
				BufferedImage.TYPE_INT_ARGB
		);
	}

	private Graphics2D rotateArrow (double ang, BufferedImage arrow)
	{
		Graphics2D arrGfx = arrow.createGraphics();
		arrGfx.rotate(
				ang + Constants.DEG90,
				arrow.getWidth() >> 1,
				arrow.getHeight() >> 1
		);
		arrGfx.drawImage(stuff.getArrow(), 0, 0, null);
		return arrGfx;
	}

	private void pointArrow(Graphics g, int offX, int offY, double dx, double dy)
	{
		// Draw Arrow here:
		Player player = clientGame.getPlayer();
		if (player.getDist(dx, dy) < Constants.PLAYER_ARROW_DELTA_Y)
			return;

		double ang = Math.atan2(player.getY() - dy, player.getX() - dx);
		double ax = Math.cos(ang) * Constants.ARROW_ROTATION_OFFSET;
		double ay = Math.sin(ang) * Constants.ARROW_ROTATION_OFFSET;
		// Rotate Arrow
		BufferedImage arrow = getArrowImage();
		Graphics2D arrGfx = rotateArrow(ang, arrow);
		g.drawImage(
				arrow,
				(int) ax - Constants.DEFAULT_ROOM_PADDING + offX,
				(int) ay - Constants.DEFAULT_ROOM_PADDING + offY,
				Constants.ARROW_SIZE,
				Constants.ARROW_SIZE,
				null
		);
		arrGfx.dispose();
	}

	private void vampDefaultArrow(Graphics g, int offX, int offY)
	{
		Player pointTo = clientGame.getNearestPlayerToPointTo();
		if (pointTo != null)
			pointArrow(g, offX, offY, pointTo.getX(), pointTo.getY());
	}

}
