package pl.gr14b.transylwania;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuHostButton extends JButton implements ActionListener
{

	private MenuBackground menuBackground;

	MenuHostButton (MenuBackground menuBackground)
	{
		super("Host");
		this.menuBackground = menuBackground;
		configureStyle();
		addActionListener(this);
	}

	private void configureStyle ()
	{
		setBounds(Constants.MENU_HOST_BUTTON_HORIZONTAL_OFFSET, Constants.MENU_BUTTONS_VERTICAL_OFFSET + 20,  Constants.MENU_BUTTON_WIDTH, Constants.MENU_BUTTON_HEIGHT);
		setContentAreaFilled(false);
		setBorder(
				BorderFactory.createMatteBorder(
						Constants.MENU_BORDER_WEIGHT,
						Constants.MENU_BORDER_WEIGHT,
						Constants.MENU_BORDER_WEIGHT,
						Constants.MENU_BORDER_WEIGHT,
				new Color(
						Constants.MENU_COLOR_PRIME,
						Constants.MENU_COLOR_PRIME,
						Constants.MENU_COLOR_PRIME)
				)
		);
		setFont(
				menuBackground.getMenu().getFont().deriveFont(Font.PLAIN, Constants.MENU_BUTTON_FONT_SIZE)
		);
		setForeground(
				new Color(
						Constants.MENU_COLOR_PRIME,
						Constants.MENU_COLOR_PRIME,
						Constants.MENU_COLOR_PRIME)
		);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent)
	{
		MenuLocator menuLocator = new MenuLocator(menuBackground.getMenuNetworkBox().getText());

		(new Server(menuLocator.getPort())).start();

		try
		{
			new Client(menuBackground.getMenuNicknameBox().getText(), menuLocator.getIp(), menuLocator.getPort());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		menuBackground.getMenu().dispose();
	}
}
