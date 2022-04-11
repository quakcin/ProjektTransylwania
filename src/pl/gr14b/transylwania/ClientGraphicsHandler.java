package pl.gr14b.transylwania;

public class ClientGraphicsHandler extends Thread
{
	private Client client;
	private ClientGraphics clientGraphics;

	ClientGraphicsHandler (Client client)
	{
		this.client = client;
		clientGraphics = new ClientGraphics(client);
		client.add(clientGraphics);
		start();
	}

	@Override
	public void run ()
	{
		while (!client.isForceHalt())
			drawGameGraphics();
	}

	private void drawGameGraphics ()
	{
		syncRefreshRate();
		if (clientGraphics != null)
			clientGraphics.repaint();
	}

	private void syncRefreshRate ()
	{
		try
		{
			Thread.sleep(1000L / 60L);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
