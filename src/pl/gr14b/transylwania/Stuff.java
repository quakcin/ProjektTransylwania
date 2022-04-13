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
	private PlayerStuff playerStuff;
	private MiscStuff miscStuff;
	private MapStuff mapStuff;

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

	Stuff () throws Exception
	{
		playerStuff = new PlayerStuff();
		miscStuff = new MiscStuff();
		mapStuff = new MapStuff();
	}

	static Font loadFont (String path) throws Exception
	{
		return
				Font.createFont(Font.TRUETYPE_FONT, new File(path));
	}

	static BufferedImage loadBufferedImage (String path) throws Exception
	{
		return
				ImageIO.read(new File(path));
	}

	static ArrayList<ImageIcon> loadImageIconsFromFormattedPath (String path)
	{
		ArrayList<ImageIcon> icons = new ArrayList<>();
		for (int i = 0; i < 0xFF; i++)
		{
			File fd = new File(String.format(path, i));
			if (!(fd.exists() && !fd.isDirectory()))
				break;
			icons.add(new ImageIcon(fd.getAbsolutePath()));
		}
		return icons;
	}

	static ArrayList<Image> loadImagesFromFormattedPath (String path) throws Exception
	{
		ArrayList<Image> icons = new ArrayList<>();
		for (int i = 0; i < 0xFF; i++)
		{
			File fd = new File(String.format(path, i));
			if (!(fd.exists() && !fd.isDirectory()))
				break;
			icons.add(ImageIO.read(fd));
		}
		return icons;
	}

	static ImageIcon loadGIF(String path) throws Exception
	{
			return
					new ImageIcon(new File(path).getAbsolutePath());
	}

	BufferedImage getLightMask()
	{
		return miscStuff.getLightMask();
	}

	ArrayList<Image> getRooms()
	{
		return mapStuff.getRooms();
	}

	BufferedImage getDoorTexture()
	{
		return mapStuff.getDoorTexture();
	}

	BufferedImage getBackground()
	{
		return miscStuff.getBackground();
	}

	Font getFont()
	{
		return miscStuff.getFont();
	}

	ImageIcon getGhost()
	{
		return playerStuff.getGhost();
	}

	ArrayList<Image> getHealth()
	{
		return miscStuff.getHealth();
	}

	ArrayList<ImageIcon> getDeadBodies()
	{
		return mapStuff.getDeadBodies();
	}


	ArrayList<ImageIcon> getBloodSplashes()
	{
		return mapStuff.getBloodSplashes();
	}

	ArrayList<ImageIcon> getLamps()
	{
		return mapStuff.getLamps();
	}

	BufferedImage getArrow()
	{
		return miscStuff.getArrow();
	}


	ArrayList<ImageIcon> getSurvivorsWalking()
	{
		return playerStuff.getSurvivorsWalking();
	}

	ArrayList<ImageIcon> getSurvivorsStanding()
	{
		return playerStuff.getSurvivorsStanding();
	}

	ImageIcon getVampWalking()
	{
		return playerStuff.getVampWalking();
	}

	ImageIcon getVampStanding()
	{
		return playerStuff.getVampStanding();
	}

	ImageIcon getVampAttacking()
	{
		return playerStuff.getVampAttacking();
	}

	ArrayList<Image> getSummary()
	{
		return miscStuff.getSummary();
	}

	ArrayList<Image> getPType()
	{
		return miscStuff.getPType();
	}

	public ImageIcon getChest()
	{
		return mapStuff.getChest();
	}
}
