package pl.gr14b.transylwania;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MiscStuff
{
	private ArrayList<Image> summary;
	private ArrayList<Image> health;
	private ArrayList<Image> pType;

	private BufferedImage background;
	private BufferedImage lightMask;
	private BufferedImage arrow;

	private Font font;

	MiscStuff () throws Exception
	{
		summary = Stuff.loadImagesFromFormattedPath("Stuff/summary-%d.png");
		health = Stuff.loadImagesFromFormattedPath("Stuff/hp-%d.png");
		pType = Stuff.loadImagesFromFormattedPath("Stuff/ptype-%d.png");

		background = Stuff.loadBufferedImage("Stuff/background.png");
		lightMask = Stuff.loadBufferedImage("Stuff/lightmask.png");
		arrow = Stuff.loadBufferedImage("Stuff/arrow.png");

		font = Stuff.loadFont("Stuff/PermanentMarker-Regular.ttf");
	}

	public ArrayList<Image> getSummary() {
		return summary;
	}

	public ArrayList<Image> getHealth() {
		return health;
	}

	public ArrayList<Image> getPType () {
		return pType;
	}

	public BufferedImage getBackground() {
		return background;
	}

	public BufferedImage getLightMask() {
		return lightMask;
	}

	public BufferedImage getArrow() {
		return arrow;
	}

	public Font getFont() {
		return font;
	}
}
