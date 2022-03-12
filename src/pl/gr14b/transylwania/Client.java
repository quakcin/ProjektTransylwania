package pl.gr14b.transylwania;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.Buffer;

public class Client extends JFrame implements KeyListener
{
	private Game clientGame;
	private ClientHandler clientHandler;
	private GameHandler gameHandler;
	private Boolean[] keyboardState;
	private Canvas canvas;
	private Stuff stuff;

	Client ()
	{
		super();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(800, 800));
		setSize(new Dimension(800, 800));
		pack();
		addKeyListener(this);
		canvas = new Canvas();
		add(canvas);
		setVisible(true);

		stuff = new Stuff();

		keyboardState = new Boolean[0xFF];
		for (int i = 0; i < 0xFF; i++)
			keyboardState[i] = false;

		try {
			clientHandler = new ClientHandler();
			clientHandler.start();

			gameHandler = new GameHandler();
			gameHandler.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void keyTyped(KeyEvent keyEvent) {}

	@Override
	public void keyPressed(KeyEvent keyEvent)
	{
		if (keyEvent.getKeyChar() >= 0xFF)
			return;
		keyboardState[keyEvent.getKeyChar()] = true;
	}

	@Override
	public void keyReleased(KeyEvent keyEvent)
	{
		if (keyEvent.getKeyChar() >= 0xFF)
			return;
		keyboardState[keyEvent.getKeyChar()] = false;
	}


	private class Canvas extends JPanel
	{
		Canvas ()
		{
			super();
			Redraw redraw = new Redraw();
			redraw.start();
		}
		@Override
		protected void paintComponent (Graphics g)
		{
			super.paintComponent(g);

			if (clientGame == null)
				return;

			if (clientGame.getPlayer() == null)
				return;

			g.drawImage(stuff.getBackground(), 0, 0, getBounds().width, getBounds().height, null);

			// Start Drawing

			final int offX = getBounds().width / 2;
			final int offY = getBounds().height / 2;

			for (Room r : clientGame.getRooms())
			{
				if (Math.abs(r.getX() - clientGame.getPlayer().getX() / 810) > 2 || Math.abs(r.getY() - clientGame.getPlayer().getY() / 810) > 2)
					continue;

				int rx = offX + (int) (clientGame.getPlayer().getX() - r.getX() * 810 - 810);
				int ry = offY + (int) (clientGame.getPlayer().getY() - r.getY() * 810 - 810);
				g.drawImage(stuff.getRooms().get(r.getTexture()), rx, ry, 810, 810, null);
			}

			// draw doors
			for (int y = 0; y < clientGame.MAP_SIZE; y++)
				for (int x = 0; x < clientGame.MAP_SIZE + 1; x++)
				{
					int index = x * (clientGame.MAP_SIZE) + y;

					if (clientGame.getVerticalDoors()[index])
					{
						int dx = x * 810 + 45;
						int dy = y * 810 - 450 + 90 + 810;
						g.drawImage(stuff.getDoorTexture(),offX + (int) (clientGame.getPlayer().getX() - dx), offY + (int) (clientGame.getPlayer().getY() - dy), 90, 90, null);
					}

				}

			// draw doors
			for (int y = 0; y < clientGame.MAP_SIZE + 1; y++)
				for (int x = 0; x < clientGame.MAP_SIZE ; x++)
				{
					int index = y * (clientGame.MAP_SIZE) + x;

					if (clientGame.getHorizontalDoors()[index])
					{
						int dx = x * 810 - 450 + 90 + 810;
						int dy = y * 810 + 45;
						g.drawImage(stuff.getDoorTexture(), offX + (int) (clientGame.getPlayer().getX() - dx), offY + (int) (clientGame.getPlayer().getY() - dy), 90, 90, null);
					}

				}

			// Draw Props
			for (Prop prop : clientGame.getProps())
				prop.Draw(g, offX, offY, stuff, clientGame);

			// Draw Lamp
			for (Lamp lamp : clientGame.getLamps())
				lamp.Draw(g, offX, offY, stuff, clientGame);

			// Draw Players

			for (Player p : clientGame.getPlayers())
			{
				if ((p.getPlayerType() == Player.PLAYER_TYPE_GHOST && p.getPlayerID() != clientGame.getPlayer().getPlayerID()) && clientGame.getPlayer().getPlayerType() != Player.PLAYER_TYPE_GHOST)
					continue;

				int cx = offX + (int) (clientGame.getPlayer().getX() - p.getX());
				int cy = offY + (int) (clientGame.getPlayer().getY() - p.getY());

				ImageIcon pfp = null;

				if (p.getPlayerType() == Player.PLAYER_TYPE_SURVIVOR)
					pfp = stuff.getSurvivors().get(p.getCharacter());
				else if (p.getPlayerType() == Player.PLAYER_TYPE_GHOST)
					pfp = stuff.getGhost();
				else if (p.getPlayerType() == Player.PLAYER_TYPE_VAMPIRE)
					pfp = stuff.getVamp();

				AffineTransform aft = AffineTransform.getRotateInstance(p.getAng() + 1.57075, pfp.getIconWidth() >> 1, pfp.getIconHeight() >> 1);
				AffineTransformOp aftop = new AffineTransformOp(aft, AffineTransformOp.TYPE_BILINEAR);

				BufferedImage bfi = new BufferedImage(pfp.getIconWidth(), pfp.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics bfig = bfi.createGraphics();
				pfp.paintIcon(null, bfig, 0, 0);

				g.drawImage(aftop.filter(bfi, null), cx - 40, cy - 40, 80, 80, null);


				if (clientGame.getGameStatus() == Game.GAME_STATUS_LOBBY) {
					//g.setFont(new Font("Arial", Font.BOLD, 20));
					g.setFont(stuff.getFont().deriveFont(20f));
					g.setColor(new Color(255, 221, 0));
					g.drawString(p.getNickName(), (int) (cx - (p.getNickName().length() * 5.5)), cy - 45);
				}
			}

			// draw light mask
			g.drawImage(stuff.getLightMask(), 0, 0, getBounds().width, getBounds().height, null);

			if (clientGame.getGameStatus() == Game.GAME_STATUS_LOBBY)
			{
				g.setFont(stuff.getFont().deriveFont(35f));
				g.setColor(Color.WHITE);
				g.drawString("OCZEKIWANIE NA", 50, 80);
				g.drawString("GRACZY (" + clientGame.getPlayers().size() + " / 8)", 50, 130);

				if (clientGame.getWaitingTime() != 40)
					g.drawString("" + clientGame.getWaitingTime() + "s", 50, getBounds().height - 50);
			}
			else if (clientGame.getGameStatus() == Game.GAME_STATUS_KILLING)
			{
				g.setFont(stuff.getFont().deriveFont(35f));
				g.setColor(Color.WHITE);

				int minutes = Math.floorDiv(clientGame.getGameTime(), 60);
				int seconds = clientGame.getGameTime() - minutes * 60;
				if (minutes != 0)
					g.drawString("" + minutes + "m " + seconds + "s", 50, getBounds().height - 50);
				else
					g.drawString("" + seconds + "s", 50, getBounds().height - 50);

				if (clientGame.getPlayer().getPlayerType() == Player.PLAYER_TYPE_VAMPIRE)
				{
					Player pointTo = clientGame.getNearestPlayerToPointTo();
					if (pointTo != null)
					{
						// Draw Arrow here:
						Player player = clientGame.getPlayer();
						double ang = Math.atan2(player.getY() - pointTo.getY(), player.getX() - pointTo.getX());
						double ax = Math.cos(ang) * 115;
						double ay = Math.sin(ang) * 115;
						// Rotate Arrow
						BufferedImage arrow = new BufferedImage(stuff.getArrow().getWidth(), stuff.getArrow().getHeight(), BufferedImage.TYPE_INT_ARGB);
						Graphics2D arrGfx = (Graphics2D) arrow.createGraphics();
						arrGfx.rotate(ang + (90 * (3.1415 / 180)), arrow.getWidth() >> 1, arrow.getHeight() >> 1);
						arrGfx.drawImage(stuff.getArrow(), 0, 0, null);
						g.drawImage(arrow, (int) ax - 20 + offX, (int) ay - 20 + offY, 40, 40, null);
						arrGfx.dispose();
					}
				}

			}

			// draw health
			if (clientGame.getPlayer().getPlayerType() == Player.PLAYER_TYPE_SURVIVOR)
				g.drawImage(stuff.getHealth().get(clientGame.getPlayer().getHealth()), getBounds().width - 30 - 90,50, 90, 108,null);

			g.dispose();
		}

		private class Redraw extends Thread
		{
			@Override
			public void run ()
			{
				while (true)
				{
					try {
						canvas.repaint();
						Thread.sleep(1000L / 60L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private class GameHandler extends Thread
	{
		@Override
		public void run()
		{
			long tick = 0;
			long chaseSongPlayTime = 0;

			try {
				while (true)
				{
					tick += 1;
					if (clientGame != null)
					{
						if (clientGame.getPlayer() != null)
						{
							clientGame.getPlayer().setPlayerMoving(false);
							if (keyboardState['w'])
							{
								clientGame.getPlayer().Push(16.3, clientGame);
								clientGame.getPlayer().setPlayerMoving(true);
							}
							if (keyboardState['s'])
								clientGame.getPlayer().Push(-16.3, clientGame);
							if (keyboardState['a'])
								clientGame.getPlayer().Turn(-10);
							if (keyboardState['d'])
								clientGame.getPlayer().Turn(10);
							if (keyboardState[' '])
								clientGame.getPlayer().setSpacePressedEnabled();

							clientGame.getPlayer().updateFlagStatusCounters();
						}

						for (Player p : clientGame.getPlayers())
							if (p != clientGame.getPlayer())
								if (p.isPlayerMoving())
								{
									if (p.getDist(clientGame.getPlayer().getX(), clientGame.getPlayer().getY()) < 800) {
										p.Push(16.3, clientGame);
										if ((tick + clientGame.getPlayers().indexOf(p) * 3) % 15L == 0)
											if (p.getPlayerType() == Player.PLAYER_TYPE_SURVIVOR)
												Stuff.playSound("survStepSound");
											else if (p.getPlayerType() == Player.PLAYER_TYPE_VAMPIRE)
												Stuff.playSound("vampStepSound");
											else if (p.getPlayerType() == Player.PLAYER_TYPE_GHOST)
												Stuff.playSound("ghostStepSound");
									}
								}

						if (tick % 13L == 0)
							if (clientGame.getPlayer().isPlayerMoving())
								if (clientGame.getPlayer().getPlayerType() == Player.PLAYER_TYPE_SURVIVOR)
									Stuff.playSound("selfStepSound");
								else if (clientGame.getPlayer().getPlayerType() == Player.PLAYER_TYPE_VAMPIRE)
									Stuff.playSound("vampStepSound");
								else if (clientGame.getPlayer().getPlayerType() == Player.PLAYER_TYPE_GHOST)
									Stuff.playSound("ghostStepSound");

						// Chase Song Handler / Mechanics
						Player vamp = clientGame.getVamp();
						if (vamp != null)
							if (vamp.getPlayerID() != clientGame.getPlayer().getPlayerID())
							{
								if (clientGame.getPlayer().getDist(vamp.getX(), vamp.getY()) > 900)
									chaseSongPlayTime = 0;
								else if (chaseSongPlayTime == 3)
									Stuff.playSound("chase");

								chaseSongPlayTime += 1;
								if (chaseSongPlayTime > 5 * 25 + 10)
									chaseSongPlayTime = 0;
							}

						// Sound que handler
						if (clientGame.getPlayer().getNextSoundInQue() != null)
						{
							Stuff.playSound(clientGame.getPlayer().getNextSoundInQue());
							clientGame.getPlayer().setNextSoundInQue(null);
						}
					}
					Thread.sleep(1000L / 25L);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private class ClientHandler extends Thread
	{
		private Socket socket;
		private ObjectOutputStream objectOutputStream;
		private ObjectInputStream objectInputStream;

		@Override
		public void run ()
		{
			try
			{
				// Wait a while before connection, JFrame likes to mess up the memory
				Thread.sleep(2000L);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					socket = new Socket("127.0.0.1", /*"26.106.248.14"*/ Server.PORT);
					socket.setTcpNoDelay(true);
					objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
					objectInputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
				}
				catch (Exception e)
				{
					e.printStackTrace();
					return;
				}
			}
			// Say Hello To The Server:
			Hello helloPacket = new Hello("Test Player");

			try
			{
				objectOutputStream.writeObject(helloPacket);
				objectOutputStream.flush();
				objectOutputStream.reset();

				Object serverResponse = objectInputStream.readObject();

				if (serverResponse instanceof Game)
				{
					// pray to the datagram gods it works:
					clientGame = (Game) serverResponse;
				}

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			// Carry On:
			while (socket.isConnected())
			{
				try
				{
					// Share our own state with the server
					objectOutputStream.writeObject(clientGame.getPlayer());
					objectOutputStream.flush();
					objectOutputStream.reset();

					// Sync our clientGame object with the server contents
					Object serverResponse = objectInputStream.readObject();

					if (serverResponse instanceof Game)
					{
						// Reroute references:
						Game serverGame = (Game) serverResponse;
						clientGame = (Game) serverGame;
					}
					else if (serverResponse instanceof PlayersListPacket)
					{
						Player oldPlayerState = clientGame.getPlayer();
						clientGame.setPlayers(((PlayersListPacket) serverResponse).getPlayers());

						if (oldPlayerState != null)
							if (clientGame.getPlayer().getDist(oldPlayerState.getX(), oldPlayerState.getY()) < 150) // FIXME: make this value smaller
							{
								clientGame.getPlayer().copyLocation(oldPlayerState);
							}

					}
					else if (serverResponse instanceof PropsAndStatsListPacket)
					{
						PropsAndStatsListPacket packet = ((PropsAndStatsListPacket) serverResponse);
						clientGame.setProps(packet.getProps());
						clientGame.setGameTime(packet.gameTime);
						clientGame.setWaitingTime(packet.waitingTime);
						clientGame.setGameStatus(packet.gameStatus);
					}
					else if (serverResponse instanceof LampsListPacket)
					{
						clientGame.setLamps(((LampsListPacket) serverResponse).getLamps());
					}


					clientGame.getPlayer().setForcingSynchronization(false);
					clientGame.getPlayer().NextPacket();
					Thread.sleep(1000L / 25L);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}

	}

	public static void main (String[] args)
	{
		new Client();
	}

}
