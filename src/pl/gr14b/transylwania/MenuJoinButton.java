package pl.gr14b.transylwania;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuJoinButton extends JButton implements ActionListener
{

	private MenuBackground menuBackground;

	MenuJoinButton (MenuBackground menuBackground)
	{
		super("Join");
		this.menuBackground = menuBackground;
		configureStyle();
		addActionListener(this);
	}

	private void configureStyle ()
	{
		setBounds(
				Constants.MENU_JOIN_BUTTON_HORIZONTAL_OFFSET,
				Constants.MENU_BUTTONS_VERTICAL_OFFSET + 20,
				Constants.MENU_BUTTON_WIDTH,
				Constants.MENU_BUTTON_HEIGHT
		);
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
				new Color(Constants.MENU_COLOR_PRIME,
						Constants.MENU_COLOR_PRIME,
						Constants.MENU_COLOR_PRIME)
		);
	}


	@Override
	public void actionPerformed(ActionEvent actionEvent)
	{
		String nick = menuBackground.getMenuNicknameBox().getText();
		MenuLocator menuLocator = new MenuLocator(menuBackground.getMenuNetworkBox().getText());

		System.out.println("Joining " + menuLocator.getIp() + " on the port " + menuLocator.getPort() + " as " + nick);
		try {
			new Client(nick, menuLocator.getIp(), menuLocator.getPort());
		} catch (Exception e) {
			e.printStackTrace();
		}
		menuBackground.getMenu().dispose();
	}
}
