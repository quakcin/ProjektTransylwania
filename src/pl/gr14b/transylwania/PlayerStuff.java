package pl.gr14b.transylwania;

import javax.swing.*;
import java.util.ArrayList;

class PlayerStuff
{
	private ArrayList<ImageIcon> survivorsStanding;
	private ArrayList<ImageIcon> survivorsWalking;
	private ImageIcon vampAttacking;
	private ImageIcon vampStanding;
	private ImageIcon vampWalking;
	private ImageIcon ghost;

	PlayerStuff () throws Exception
	{
		survivorsStanding = Stuff.loadImageIconsFromFormattedPath("Stuff/search-%d.gif");
		survivorsWalking = Stuff.loadImageIconsFromFormattedPath("Stuff/survivor-%d.gif");
		vampAttacking = Stuff.loadGIF("Stuff/vampAttacking.gif");
		vampStanding = Stuff.loadGIF("Stuff/vampStanding.gif");
		vampWalking = Stuff.loadGIF("Stuff/vamp.gif");
		ghost = Stuff.loadGIF("Stuff/ghost.gif");
	}

	public ArrayList<ImageIcon> getSurvivorsWalking() {
		return survivorsWalking;
	}

	public ArrayList<ImageIcon> getSurvivorsStanding() {
		return survivorsStanding;
	}

	public ImageIcon getVampWalking() {
		return vampWalking;
	}

	public ImageIcon getVampStanding() {
		return vampStanding;
	}

	public ImageIcon getVampAttacking() {
		return vampAttacking;
	}

	public ImageIcon getGhost() {
		return ghost;
	}
}
