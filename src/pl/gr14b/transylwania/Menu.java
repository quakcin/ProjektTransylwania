package pl.gr14b.transylwania;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Menu extends JFrame
{
	private JFrame mainFrame;
	private JTextField nickName;
	private JTextField networkData;

	Menu ()
	{
		super();
		mainFrame = this;
		setSize(new Dimension(800, 820));
		setMinimumSize(new Dimension(800, 820));
		pack();
		setTitle("Transylwania");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		setResizable(false);
		setLocationRelativeTo(null);

		Background panel = new Background();
		panel.setBounds(0, 0, 800, 800);
		panel.setLayout(null);
		panel.setBorder(null);
		add(panel);

		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("Stuff/SpecialElite-Regular.ttf"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		assert font != null;

		nickName = new JTextField(40);
		nickName.setBounds(200, 300, 400, 45);

		nickName.setText("Steve");
		nickName.setFont(font.deriveFont(Font.PLAIN, 28));
		nickName.setHorizontalAlignment(SwingConstants.CENTER);
		// nickName.setBackground(new Color(30, 33, 36));
		nickName.setOpaque(false);
		nickName.setForeground(new Color(250, 250, 250));
		nickName.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(250, 250, 250)));

		panel.add(nickName);

		networkData = new JTextField(40);
		networkData.setBounds(200, 380, 400, 45);
		networkData.setFont(font.deriveFont(Font.PLAIN, 28));
		networkData.setHorizontalAlignment(SwingConstants.CENTER);
		networkData.setText("127.0.0.1:666");
		networkData.setOpaque(false);
		networkData.setForeground(new Color(250, 250, 250));
		networkData.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(250, 250, 250)));
		panel.add(networkData);

		JButton joinButton = new JButton("Join");
		joinButton.setBounds(200, 480 + 20, 100, 40);
		joinButton.setContentAreaFilled(false);
		joinButton.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(250, 250, 250)));
		joinButton.setFont(font.deriveFont(Font.PLAIN, 22));
		joinButton.setForeground(new Color(250, 250, 250));
		panel.add(joinButton);

		joinButton.addActionListener(new JoinGameClick());

		JButton localButton = new JButton("Local");
		localButton.setBounds(350, 480 + 40, 100, 40);
		localButton.setContentAreaFilled(false);
		localButton.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(250, 250, 250)));
		localButton.setFont(font.deriveFont(Font.PLAIN, 22));
		localButton.setForeground(new Color(250, 250, 250));
		panel.add(localButton);
		localButton.addActionListener(new LocalGameClick());

		JButton hostButton = new JButton("Host");
		hostButton.setBounds(500, 480 + 20, 100, 40);
		hostButton.setContentAreaFilled(false);
		hostButton.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(250, 250, 250)));
		hostButton.setFont(font.deriveFont(Font.PLAIN, 22));
		hostButton.setForeground(new Color(250, 250, 250));
		panel.add(hostButton);
		hostButton.addActionListener(new HostGameClick());

		setVisible(true);
	}

	private static class Background extends JPanel
	{
		private ImageIcon bgImage;
		Background ()
		{
			this.bgImage = new ImageIcon(new File("Stuff/menu.png").getAbsolutePath());
		}
		@Override
		public void paintComponent (Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(this.bgImage.getImage(), 0, 0, null);
		}
	}

	public static void main (String[] args)
	{
		new Menu();
	}

	private class HostGameClick implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent actionEvent)
		{
			Locator locator = new Locator(networkData.getText());
			new ClientCreateServer(locator.port);
			new Client(nickName.getText(), locator.ip, locator.port);
			mainFrame.dispose();
		}
	}

	private class JoinGameClick implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent actionEvent)
		{
			String nick = nickName.getText();
			Locator locator = new Locator(networkData.getText());

			System.out.println("Joining " + locator.ip + " on the port " + locator.port + " as " + nick);
			new Client(nick, locator.ip, locator.port);
			mainFrame.dispose();
		}
	}

	private class LocalGameClick implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent actionEvent)
		{
			networkData.setText("172.0.0.1:666");
		}
	}

}

class Locator
{
	String ip;
	int port;
	Locator (String location)
	{
		String[] tokens = location.split(":");

		if (tokens.length >= 1)
			ip = tokens[0];
		else
			ip = "127.0.0.1";

		if (tokens.length != 2)
			port = Integer.parseInt(tokens[1]);
		else
			port = 666;
	}
}