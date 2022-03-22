package pl.gr14b.transylwania;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class Stuff
{
	// Graphics
	private ArrayList<ImageIcon> survivorsWalking;
	private ArrayList<ImageIcon> survivorsStanding;
	private ArrayList<ImageIcon> bloodSplashes;
	private ArrayList<ImageIcon> deadBodies;
	private ArrayList<ImageIcon> lamps;

	private ArrayList<Image> rooms;
	private ArrayList<Image> health;
	private ArrayList<Image> summary;
	private ArrayList<Image> pType;
	private BufferedImage lightMask;
	private BufferedImage doorTexture;
	private BufferedImage background;
	private ImageIcon ghost;
	private ImageIcon vampWalking;
	private ImageIcon vampStanding;
	private ImageIcon vampAttacking;
	private ImageIcon chest;

	private BufferedImage arrow;
	private Font font;



	static int random (int mm, int mx)
	{
		return (int) Math.round(Math.random() * (mx - mm - 1)) + mm;
	}

	static int RandomBloodSplash ()
	{
		return random(0, 4);
	}
	static int RandomDeadBody ()
	{
		return random(0, 3);
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
		survivorsWalking = new ArrayList<>();
		survivorsStanding = new ArrayList<>();
		deadBodies = new ArrayList<>();
		bloodSplashes = new ArrayList<>();
		lamps = new ArrayList<>();

		rooms = new ArrayList<>();
		health = new ArrayList<>();

		survivorsWalking = LoadImageIcons("Stuff/survivor-%d.gif");
		survivorsStanding = LoadImageIcons("Stuff/search-%d.gif");
		deadBodies = LoadImageIcons("Stuff/body-%d.png");
		bloodSplashes = LoadImageIcons("Stuff/blood-%d.png");
		lamps = LoadImageIcons("Stuff/lamp-%d.gif");

		rooms = LoadImage("Stuff/room-%d.png");
		health = LoadImage("Stuff/hp-%d.png");
		summary = LoadImage("Stuff/summary-%d.png");
		pType = LoadImage("Stuff/ptype-%d.png");

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
			vampWalking = new ImageIcon(new File("Stuff/vamp.gif").getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			vampStanding = new ImageIcon(new File("Stuff/vampStanding.gif").getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			vampAttacking = new ImageIcon(new File("Stuff/vampAttacking.gif").getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			chest = new ImageIcon(new File("Stuff/chest.png").getAbsolutePath());
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
		ArrayList<ImageIcon> icons = new ArrayList<>();
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
		ArrayList<Image> icons = new ArrayList<>();
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

	ArrayList<Image> getHealth() {
		return health;
	}

	ArrayList<ImageIcon> getDeadBodies() {
		return deadBodies;
	}


	ArrayList<ImageIcon> getBloodSplashes() {
		return bloodSplashes;
	}

	ArrayList<ImageIcon> getLamps() {
		return lamps;
	}

	BufferedImage getArrow() {
		return arrow;
	}


	ArrayList<ImageIcon> getSurvivorsWalking() {
		return survivorsWalking;
	}

	ArrayList<ImageIcon> getSurvivorsStanding() {
		return survivorsStanding;
	}

	ImageIcon getVampWalking() {
		return vampWalking;
	}

	ImageIcon getVampStanding() {
		return vampStanding;
	}

	ImageIcon getVampAttacking() {
		return vampAttacking;
	}

	ArrayList<Image> getSummary() {
		return summary;
	}

	ArrayList<Image> getpType() {
		return pType;
	}

	public ImageIcon getChest() {
		return chest;
	}
}
