package pl.gr14b.transylwania;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Client extends JFrame implements KeyListener
{
	private boolean forceHalt;
	private Game clientGame;
	private Boolean[] keyboardState;

	private Stuff stuff;

	private double privateLight;
	private double globalLight;
	private int roleCallTime;

	private String nickName;
	private String ipAddress;
	private int port;

	Client (String nick, String ip, int port) throws Exception
	{
		super();

		this.nickName = nick;
		this.ipAddress = ip;
		this.port = port;

		setFlagsToDefault();
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

	private void setFlagsToDefault ()
	{
		this.forceHalt = false;
		privateLight = 0.5d;
		roleCallTime = 0;
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
		return forceHalt;
	}

	void setForceHalt(boolean forceHalt) {
		this.forceHalt = forceHalt;
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
		return privateLight;
	}

	void setPrivateLight(double privateLight) {
		this.privateLight = privateLight;
	}

	double getGlobalLight() {
		return globalLight;
	}

	void setGlobalLight(double globalLight) {
		this.globalLight = globalLight;
	}

	int getRoleCallTime() {
		return roleCallTime;
	}

	void setRoleCallTime(int roleCallTime) {
		this.roleCallTime = roleCallTime;
	}

	Boolean[] getKeyboardState() {
		return keyboardState;
	}


	String getNickName() {
		return nickName;
	}

	String getIpAddress() {
		return ipAddress;
	}

	int getPort() {
		return port;
	}
}
