package pl.gr14b.transylwania;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MenuBackground extends JPanel
{

	private Menu menu;
	private ImageIcon bgImage;

	private MenuNicknameBox menuNicknameBox;
	private MenuNetworkBox menuNetworkBox;

	private MenuJoinButton menuJoinButton;
	private MenuLocalButton menuLocalButton;
	private MenuHostButton menuHostButton;


	MenuBackground (Menu menu)
	{
		super();
		this.menu = menu;
		this.bgImage = new ImageIcon(new File("Stuff/menu.png").getAbsolutePath());
		setBounds(0, 0, Constants.MENU_WIDTH, Constants.MENU_WIDTH);
		setLayout(null);
		setBorder(null);
		createMenuComponents();
		addMenuComponentsToControls();
	}

	private void createMenuComponents ()
	{
		this.menuHostButton = new MenuHostButton(this);
		this.menuLocalButton = new MenuLocalButton(this);
		this.menuJoinButton = new MenuJoinButton(this);

		this.menuNetworkBox = new MenuNetworkBox(this);
		this.menuNicknameBox = new MenuNicknameBox(this);
	}

	private void addMenuComponentsToControls()
	{
		add(this.menuHostButton);
		add(this.menuLocalButton);
		add(this.menuJoinButton);

		add(this.menuNetworkBox);
		add(this.menuNicknameBox);
	}

	@Override
	public void paintComponent (Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(this.bgImage.getImage(), 0, 0, null);
	}

	public Menu getMenu()
	{
		return menu;
	}

	public ImageIcon getBgImage() {
		return bgImage;
	}

	public MenuNicknameBox getMenuNicknameBox() {
		return menuNicknameBox;
	}

	public MenuNetworkBox getMenuNetworkBox() {
		return menuNetworkBox;
	}

	public MenuJoinButton getMenuJoinButton() {
		return menuJoinButton;
	}

	public MenuLocalButton getMenuLocalButton() {
		return menuLocalButton;
	}

	public MenuHostButton getMenuHostButton() {
		return menuHostButton;
	}
}
