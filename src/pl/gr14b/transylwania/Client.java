package pl.gr14b.transylwania;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Client extends JFrame implements KeyListener
{

	private Game clientGame;
	private Boolean[] keyboardState;
	private Stuff stuff;
	private ClientCredits clientCredits;
	private ClientFlags clientFlags;

	Client (String nick, String ip, int port) throws Exception
	{
		super();

		this.clientCredits =
				new ClientCredits(nick, ip, port);

		this.clientFlags =
				new ClientFlags();

		initializeKeyboardStateArray();
		setWindowFrameOptions();
		createHandlers();

		stuff = new Stuff();
	}

	private void createHandlers ()
	{
		new ClientGameHandler(this);
		new ClientGraphicsHandler(this);
		new ClientConnectionHandler(this);
	}

	private void setWindowFrameOptions ()
	{
		setTitle("Transylwania");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(800, 800));
		setSize(new Dimension(800, 800));
		addKeyListener(this);
		setVisible(true);
	}


	private void initializeKeyboardStateArray()
	{
		keyboardState = new Boolean[0xFF];
		for (int i = 0; i < 0xFF; i++)
			keyboardState[i] = false;
	}

	private boolean isValidKey(int keyCode)
	{
		return keyCode > 0xFF
				|| keyCode < 0;
	}

	@Override
	public void keyTyped(KeyEvent keyEvent) {}

	@Override
	public void keyPressed(KeyEvent keyEvent)
	{
		if (isValidKey(keyEvent.getKeyChar()))
			return;
		keyboardState[keyEvent.getKeyChar()] = true;
	}

	@Override
	public void keyReleased(KeyEvent keyEvent)
	{
		if (isValidKey(keyEvent.getKeyChar()))
			return;
		keyboardState[keyEvent.getKeyChar()] = false;
	}

	boolean isForceHalt() {
		return clientFlags.isForceHalt();
	}

	void setForceHalt(boolean forceHalt) {
		clientFlags.setForceHalt(forceHalt);
	}

	public Game getClientGame() {
		return clientGame;
	}

	public void setClientGame(Game clientGame) {
		this.clientGame = clientGame;
	}

	public Stuff getStuff() {
		return stuff;
	}

	public void setStuff(Stuff stuff) {
		this.stuff = stuff;
	}

	double getPrivateLight() {
		return clientFlags.getPrivateLight();
	}

	void setPrivateLight(double privateLight) {
		clientFlags.setPrivateLight(privateLight);
	}

	double getGlobalLight() {
		return clientFlags.getGlobalLight();
	}

	void setGlobalLight(double globalLight) {
		clientFlags.setGlobalLight(globalLight);
	}

	int getRoleCallTime() {
		return clientFlags.getRoleCallTime();
	}

	void setRoleCallTime(int roleCallTime) {
		clientFlags.setRoleCallTime(roleCallTime);
	}

	Boolean[] getKeyboardState() {
		return keyboardState;
	}


	String getNickName()
	{
		return clientCredits.getNickName();
	}

	String getIpAddress()
	{
		return clientCredits.getIpAddress();
	}

	int getPort()
	{
		return clientCredits.getPort();
	}
}
