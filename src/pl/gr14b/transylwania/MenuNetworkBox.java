package pl.gr14b.transylwania;

import javax.swing.*;
import java.awt.*;

class MenuNetworkBox extends JTextField
{

	private MenuBackground menuBackground;

	MenuNetworkBox (MenuBackground menuBackground)
	{
		super(Constants.MENU_BOX_COLUMNS_COUNT);
		this.menuBackground = menuBackground;
		configureStyle();
	}

	private void configureStyle ()
	{
		setBounds(
				Constants.MENU_BOX_BOUND_X,
				Constants.MENU_ADDRESS_BOX_BOUND_Y,
				Constants.MENU_BOX_BOUND_WIDTH,
				Constants.MENU_BOX_BOUND_HEIGHT
		);
		setFont(
				menuBackground.getMenu().getFont().deriveFont(Font.PLAIN, Constants.MENU_BOX_FONT_SIZE)
		);
		setHorizontalAlignment(SwingConstants.CENTER);
		setText("127.0.0.1:666");
		setOpaque(false);
		setForeground(
				new Color(Constants.MENU_COLOR_PRIME,
						Constants.MENU_COLOR_PRIME,
						Constants.MENU_COLOR_PRIME)
		);
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
	}
}
