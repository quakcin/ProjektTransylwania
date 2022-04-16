package pl.gr14b.transylwania;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Menu extends JFrame
{
	private Font font = null;

	Menu ()
	{
		super();
		configureStyle();
		loadMenuSpecificFont();
		add(new MenuBackground(this));
		setVisible(true);
	}

	private void loadMenuSpecificFont ()
	{
		try
		{
			font = Font.createFont(Font.TRUETYPE_FONT, new File("Stuff/SpecialElite-Regular.ttf"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void configureStyle ()
	{
		Dimension screenDimension
				= new Dimension(Constants.MENU_WIDTH, Constants.MENU_HEIGHT);
		setSize(screenDimension);
		setMinimumSize(screenDimension);
		pack();
		setTitle("Transylwania");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		setResizable(false);
		setLocationRelativeTo(null);
	}

	@Override
	public Font getFont() {
		return font;
	}


	public static void main (String[] args)
	{
		new Menu();
	}

}
