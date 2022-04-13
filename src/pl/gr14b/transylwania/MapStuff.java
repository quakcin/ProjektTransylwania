package pl.gr14b.transylwania;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MapStuff
{
	private ArrayList<ImageIcon> bloodSplashes;
	private ArrayList<ImageIcon> deadBodies;
	private ArrayList<ImageIcon> lamps;
	private ArrayList<Image> rooms;

	private BufferedImage doorTexture;
	private ImageIcon chest;

	MapStuff () throws Exception
	{
		bloodSplashes = Stuff.loadImageIconsFromFormattedPath("Stuff/blood-%d.png");
		deadBodies = Stuff.loadImageIconsFromFormattedPath("Stuff/body-%d.png");
		lamps = Stuff.loadImageIconsFromFormattedPath("Stuff/lamp-%d.gif");
		rooms = Stuff.loadImagesFromFormattedPath("Stuff/room-%d.png");

		doorTexture = Stuff.loadBufferedImage("Stuff/door.png");
		chest = Stuff.loadGIF("Stuff/chest.png");

	}

	public ArrayList<ImageIcon> getBloodSplashes() {
		return bloodSplashes;
	}

	public ArrayList<ImageIcon> getDeadBodies() {
		return deadBodies;
	}

	public ArrayList<ImageIcon> getLamps() {
		return lamps;
	}

	public ArrayList<Image> getRooms() {
		return rooms;
	}

	public BufferedImage getDoorTexture() {
		return doorTexture;
	}

	public ImageIcon getChest() {
		return chest;
	}
}
