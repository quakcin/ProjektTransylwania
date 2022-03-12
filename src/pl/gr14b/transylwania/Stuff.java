package pl.gr14b.transylwania;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.Buffer;
import java.util.ArrayList;

public class Stuff
{
	public static final int PLAYER_MODEL_COUNT = 3;

	// Graphics
	private ArrayList<ImageIcon> survivors;
	private ArrayList<ImageIcon> bloodSplashes;
	private ArrayList<ImageIcon> deadBodies;
	private ArrayList<ImageIcon> lamps;

	private ArrayList<Image> rooms;
	private ArrayList<Image> health;
	private BufferedImage lightMask;
	private BufferedImage doorTexture;
	private BufferedImage background;
	private ImageIcon ghost;
	private ImageIcon vamp;
	private BufferedImage arrow;
	private Font font;



	static int random (int mm, int mx)
	{
		return (int) Math.round(Math.random() * (mx - mm - 1)) + mm;
	}

	static int random (double mm, double mx)
	{
		return (int) (Math.round(Math.random() * (mx - mm - 1)) + mm);
	}

	static int RandomBloodSplash ()
	{
		return random(0, 4);
	}
	static int RandomDeadBody ()
	{
		return 0;
	}

	static void playSound (String name)
	{
		File file = new File("Stuff/" + name + ".wav");
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file.toURI().toURL());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	Stuff()
	{
		survivors = new ArrayList<ImageIcon>();
		deadBodies = new ArrayList<ImageIcon>();
		bloodSplashes = new ArrayList<ImageIcon>();
		lamps = new ArrayList<ImageIcon>();

		rooms = new ArrayList<Image>();
		health = new ArrayList<Image>();

		survivors = LoadImageIcons("Stuff/survivor-%d.gif");
		deadBodies = LoadImageIcons("Stuff/body-%d.png");
		bloodSplashes = LoadImageIcons("Stuff/blood-%d.png");
		lamps = LoadImageIcons("Stuff/lamp-%d.gif");

		rooms = LoadImage("Stuff/room-%d.png");
		health = LoadImage("Stuff/hp-%d.png");

		try {
			arrow = ImageIO.read(new File("Stuff/arrow.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			lightMask = ImageIO.read(new File("Stuff/lightmask.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			background = ImageIO.read(new File("Stuff/background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			doorTexture = ImageIO.read(new File("Stuff/door.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			ghost = new ImageIcon(new File("Stuff/ghost.gif").getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			vamp = new ImageIcon(new File("Stuff/vamp.png").getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("Stuff/PermanentMarker-Regular.ttf"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	private ArrayList<ImageIcon> LoadImageIcons (String path)
	{
		ArrayList<ImageIcon> icons = new ArrayList<ImageIcon>();
		for (int i = 0; i < 0xFF; i++)
		{
			File fd = new File(String.format(path, i));
			if (fd.exists() && !fd.isDirectory()) {
				System.out.printf("Loaded ImageIcon: %s\n", fd.getAbsolutePath());
				icons.add(new ImageIcon(fd.getAbsolutePath()));
			}
			else
			{
				System.out.printf("Splash does not exists: %s\n", fd.getAbsolutePath());
				break;
			}
		}
		return icons;
	}

	private ArrayList<Image> LoadImage (String path)
	{
		ArrayList<Image> icons = new ArrayList<Image>();
		for (int i = 0; i < 0xFF; i++)
		{
			File fd = new File(String.format(path, i));
			if (fd.exists() && !fd.isDirectory()) {
				System.out.printf("Loaded Image: %s\n", fd.getAbsolutePath());
				try {
					icons.add(ImageIO.read(fd));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else
			{
				System.out.printf("Splash does not exists: %s\n", fd.getAbsolutePath());
				break;
			}
		}
		return icons;
	}

	ArrayList<ImageIcon> getSurvivors ()
	{
		return survivors;
	}

	BufferedImage getLightMask() {
		return lightMask;
	}

	ArrayList<Image> getRooms() {
		return rooms;
	}

	BufferedImage getDoorTexture() {
		return doorTexture;
	}

	BufferedImage getBackground() {
		return background;
	}

	Font getFont() {
		return font;
	}

	ImageIcon getGhost() {
		return ghost;
	}

	ImageIcon getVamp() {
		return vamp;
	}

	ArrayList<Image> getHealth() {
		return health;
	}

	public ArrayList<ImageIcon> getDeadBodies() {
		return deadBodies;
	}


	public ArrayList<ImageIcon> getBloodSplashes() {
		return bloodSplashes;
	}

	public ArrayList<ImageIcon> getLamps() {
		return lamps;
	}

	public BufferedImage getArrow() {
		return arrow;
	}


}