package pl.gr14b.transylwania;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuLocalButton extends JButton implements ActionListener
{

	private MenuBackground menuBackground;

	MenuLocalButton (MenuBackground menuBackground)
	{
		super("Local");
		this.menuBackground = menuBackground;
		configureStyle();
		addActionListener(this);
	}

	private void configureStyle ()
	{
		setBounds(
				Constants.MENU_LOCAL_BUTTON_HORIZONTAL_OFFSET,
				Constants.MENU_BUTTONS_VERTICAL_OFFSET + 40,
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
		menuBackground.getMenuNetworkBox().setText("127.0.0.1:666");
	}
}
