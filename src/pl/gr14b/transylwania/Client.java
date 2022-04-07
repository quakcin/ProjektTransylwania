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

public class Client extends JFrame implements KeyListener
{
	private static final double LIGHT_DELTA = 0.0015d;
	private boolean forceHalt;
	private Game clientGame;
	private Boolean[] keyboardState;
	private Canvas canvas;
	private Stuff stuff;
	private double privateLight;
	private double globalLight;
	private int roleCallTime;

	// Meta
	private String nickName;
	private String ipAddress;
	private int port;

	Client (String nick, String ip, int port)
	{
		super();

		this.forceHalt = false;
		this.nickName = nick;
		this.ipAddress = ip;
		this.port = port;

		setTitle("Transylwania");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(800, 800));
		setSize(new Dimension(800, 800));
		pack();
		addKeyListener(this);
		canvas = new Canvas();
		add(canvas);
		setVisible(true);
		privateLight = 0.5d;
		roleCallTime = 0;

		stuff = new Stuff();

		keyboardState = new Boolean[0xFF];
		for (int i = 0; i < 0xFF; i++)
			keyboardState[i] = false;

		try {
			ClientHandler clientHandler = new ClientHandler();
			clientHandler.start();

			GameHandler gameHandler = new GameHandler();
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

			for (Chest chest : clientGame.getChests())
				chest.Draw(g, offX, offY, stuff, clientGame);

			// Draw Players

			boolean isHidden = clientGame.getPlayer().isHidden(clientGame.getChests());
			for (Player p : clientGame.getPlayers())
			{
				if (!isHidden)
					if ((p.getPlayerType() == Player.PLAYER_TYPE_GHOST && p.getPlayerID() != clientGame.getPlayer().getPlayerID()) && clientGame.getPlayer().getPlayerType() != Player.PLAYER_TYPE_GHOST)
						continue;

				if (Chest.isPlayerHidden(clientGame.getChests(), p.getPlayerID()))
					continue;

				int cx = offX + (int) (clientGame.getPlayer().getX() - p.getX());
				int cy = offY + (int) (clientGame.getPlayer().getY() - p.getY());

				ImageIcon pfp = null;

				if (p.getPlayerType() == Player.PLAYER_TYPE_SURVIVOR)
					pfp = p.isPlayerMoving() ? stuff.getSurvivorsWalking().get(p.getCharacter()) : stuff.getSurvivorsStanding().get(p.getCharacter());
				else if (p.getPlayerType() == Player.PLAYER_TYPE_GHOST)
					pfp = stuff.getGhost();
				else if (p.getPlayerType() == Player.PLAYER_TYPE_VAMPIRE)
					if (p.isSpacePressed())
						pfp = stuff.getVampAttacking();
					else if (p.isPlayerMoving())
						pfp = stuff.getVampWalking();
					else
						pfp = stuff.getVampStanding();

				assert pfp != null;
				AffineTransform aft = AffineTransform.getRotateInstance(p.getAng() + 1.57075, pfp.getIconWidth() >> 1, pfp.getIconHeight() >> 1);
				AffineTransformOp aftop = new AffineTransformOp(aft, AffineTransformOp.TYPE_BILINEAR);

				BufferedImage bfi = new BufferedImage(pfp.getIconWidth(), pfp.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics bfig = bfi.createGraphics();
				pfp.paintIcon(null, bfig, 0, 0);

				g.drawImage(aftop.filter(bfi, null), cx - 40, cy - 40, 80, 80, null);


				if (clientGame.getGameStatus() == GameStatus.LOBBY) {
					//g.setFont(new Font("Arial", Font.BOLD, 20));
					g.setFont(stuff.getFont().deriveFont(20f));
					g.setColor(new Color(255, 221, 0));
					g.drawString(p.getNickName(), (int) (cx - (p.getNickName().length() * 5.5)), cy - 45);
				}
			}

			// draw light mask
			if (Math.abs(privateLight - globalLight) > 0.1d)
				privateLight += (globalLight > privateLight) ? +LIGHT_DELTA : -LIGHT_DELTA;
			g.drawImage(getImageWithOpacity(stuff.getLightMask(), privateLight), 0, 0, getBounds().width, getBounds().height, null);

			if (clientGame.getGameStatus() == GameStatus.LOBBY)
			{
				g.setFont(stuff.getFont().deriveFont(35f));
				g.setColor(Color.WHITE);
				g.drawString("OCZEKIWANIE NA", 50, 80);
				g.drawString("GRACZY (" + clientGame.getPlayers().size() + " / 8)", 50, 130);

				if (clientGame.getWaitingTime() != 40)
					g.drawString("" + clientGame.getWaitingTime() + "s", 50, getBounds().height - 50);
			}
			else if (clientGame.getGameStatus() == GameStatus.KILLING)
			{
				g.setFont(stuff.getFont().deriveFont(35f));
				g.setColor(Color.WHITE);

				int minutes = Math.floorDiv(clientGame.getGameTime(), 60);
				int seconds = clientGame.getGameTime() - minutes * 60;
				if (minutes != 0)
					g.drawString("" + minutes + "m " + seconds + "s", 50, getBounds().height - 50);
				else
					g.drawString("" + seconds + "s", 50, getBounds().height - 50);

				if (clientGame.getPlayer().getPlayerType() == Player.PLAYER_TYPE_VAMPIRE) {
					VampDefaultArrow(g, offX, offY);
					for (Player p : clientGame.getPlayers())
						if (p.getAfkPenalty() > 15)
							PointArrowTo(g, offX, offY, p.getX(), p.getY());
				}


			}

			// draw health
			if (clientGame.getPlayer().getPlayerType() == Player.PLAYER_TYPE_SURVIVOR)
				g.drawImage(stuff.getHealth().get(clientGame.getPlayer().getHealth()), getBounds().width - 30 - 90,50, 90, 108,null);

			// draw summary
			if (clientGame.getGameStatus() == GameStatus.SUMMARY)
				g.drawImage(stuff.getSummary().get(clientGame.isWinnerFlag() ? 1 : 0), 0, 0, getWidth(), getHeight(), null);

			if (--roleCallTime > 0 && clientGame.getPlayer().getPlayerType() != Player.PLAYER_TYPE_GHOST)
				g.drawImage(stuff.getpType().get(clientGame.getPlayer().getPlayerType() == Player.PLAYER_TYPE_SURVIVOR ? 1 : 0), 0, 0, getWidth(), getHeight(), null);

			g.dispose();
		}

		private class Redraw extends Thread
		{
			@Override
			public void run ()
			{
				while (!forceHalt)
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
		
		BufferedImage getImageWithOpacity (BufferedImage originalImage, double opacity)
		{
			BufferedImage bufferedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = bufferedImage.createGraphics();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) opacity));
			g.drawImage(originalImage, 0, 0, null);
			g.dispose();
			return bufferedImage;
		}

		void PointArrowTo (Graphics g, int offX, int offY, double dx, double dy)
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

		void VampDefaultArrow (Graphics g, int offX, int offY)
		{
			Player pointTo = clientGame.getNearestPlayerToPointTo();
			if (pointTo != null)
				PointArrowTo(g, offX, offY, pointTo.getX(), pointTo.getY());
		}
	}

	private class GameHandler extends Thread
	{
		@Override
		public void run()
		{
			long tick = 0;
			long chaseSongPlayTime = 0;

			try
			{
				while (!forceHalt)
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
								clientGame.getPlayer().Push(-12, clientGame);
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
												Stuff.playSound("survStep");
											else if (p.getPlayerType() == Player.PLAYER_TYPE_VAMPIRE)
												Stuff.playSound("vampStep");
											else if (p.getPlayerType() == Player.PLAYER_TYPE_GHOST)
												Stuff.playSound("ghostStep");
									}
								}

						if (tick % 13L == 0)
							if (clientGame.getPlayer().isPlayerMoving())
								if (clientGame.getPlayer().getPlayerType() == Player.PLAYER_TYPE_SURVIVOR)
									Stuff.playSound("selfStep");
								else if (clientGame.getPlayer().getPlayerType() == Player.PLAYER_TYPE_VAMPIRE)
									Stuff.playSound("vampStep");
								else if (clientGame.getPlayer().getPlayerType() == Player.PLAYER_TYPE_GHOST)
									Stuff.playSound("ghostStep");

						// Chase Song Handler / Mechanics
						Player vamp = clientGame.getVamp();
						if (vamp != null)
							if (vamp.getPlayerID() != clientGame.getPlayer().getPlayerID())
							{
								if (clientGame.getPlayer().getDist(vamp.getX(), vamp.getY()) > 900 || clientGame.getVamp().isHidden(clientGame.getChests()))
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

						if (clientGame.getGameStatus() == GameStatus.KILLING && (tick % 25) == 0)
							if (clientGame.getGameTime() > 0 && clientGame.getGameTime() <= 15)
								Stuff.playSound("tick");
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
		@Override
		public void run ()
		{
			Socket socket;
			ObjectOutputStream objectOutputStream;
			ObjectInputStream objectInputStream;
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
					socket = new Socket(ipAddress, port);
					socket.setTcpNoDelay(true);
					objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
					objectInputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
				}
				catch (Exception e)
				{
					JOptionPane.showMessageDialog(null, "Failed to connect to the server!");
					new Menu();
					forceHalt = true;
					setDefaultCloseOperation(EXIT_ON_CLOSE);
					dispose();
					e.printStackTrace();
					//noinspection ReturnInsideFinallyBlock
					return;
				}
			}
			// Say Hello To The Server:
			Hello helloPacket = new Hello(nickName);

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
			while (socket.isConnected() && !forceHalt)
			{
				try
				{
					// Share our own state with the server
					objectOutputStream.writeObject(clientGame.getPlayer());
					objectOutputStream.flush();
					objectOutputStream.reset();

					// Sync our clientGame object with the server contents
					Object serverResponse = objectInputStream.readObject();

					// Heuristics for known game changes
					GameStatus oldGameStatus = clientGame.getGameStatus();

					if (serverResponse instanceof Game)
					{
						// Reroute references:
						clientGame = (Game) serverResponse;
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
					else if (serverResponse instanceof PropsPacket)
					{
						PropsPacket packet = ((PropsPacket) serverResponse);
						clientGame.setProps(packet.getProps());
					}
					else if (serverResponse instanceof LampsListPacket)
					{
						LampsListPacket lampsListPacket = (LampsListPacket) serverResponse;
						clientGame.setLamps(lampsListPacket.getLamps());
						globalLight = lampsListPacket.getGlobalLight();
					}
					else if (serverResponse instanceof ChestsListPacket)
					{
						ChestsListPacket chestsListPacket = (ChestsListPacket) serverResponse;
						clientGame.setChests(chestsListPacket.getChests());
					}
					else if (serverResponse instanceof FlagPacket)
					{
						FlagPacket packet = ((FlagPacket) serverResponse);
						// See if anything has changed
						if (clientGame.getWaitingTime() != packet.waitingTime && clientGame.getGameStatus() == GameStatus.LOBBY)
							Stuff.playSound("tick");

						// Remember changes.
						clientGame.setGameTime(packet.gameTime);
						clientGame.setWaitingTime(packet.waitingTime);
						clientGame.setGameStatus(packet.gameStatus);
						clientGame.setWinnerFlag(packet.isWinner);
					}

					clientGame.getPlayer().setForcingSynchronization(false);
					clientGame.getPlayer().setForcingLocationSynchronization(false);
					clientGame.getPlayer().NextPacket();


					// More heuristics
					if (clientGame.getGameStatus() != oldGameStatus && clientGame.getGameStatus() == GameStatus.KILLING)
					{
						// set role call display timer
						roleCallTime = 25 * 4;
						if (clientGame.getPlayer().getPlayerType() == Player.PLAYER_TYPE_VAMPIRE)
							Stuff.playSound("vamp-theme");
						else
							Stuff.playSound("surv-theme");
					}

					Thread.sleep(1000L / 25L);
				}
				catch (Exception e)
				{
					setDefaultCloseOperation(EXIT_ON_CLOSE);
					JOptionPane.showMessageDialog(null, "You have been disconnected from the server!");
					forceHalt = true;
					dispose();
					e.printStackTrace();
					break;
				}
			}
		}

	}

	public static void main (String[] args)
	{
		new Client("dev", "127.0.0.1", 6969);
	}

}
