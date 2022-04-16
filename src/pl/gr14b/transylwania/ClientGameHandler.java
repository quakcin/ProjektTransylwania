package pl.gr14b.transylwania;

import java.util.ArrayList;

public class ClientGameHandler extends Thread
{
	private Client client;
	private ArrayList<ClientGameComponent> gameComponents;
	private long tick = 0;

	ClientGameHandler (Client client)
	{
		this.client = client;
		gameComponents = new ArrayList<>();
		packGameComponents();
		start();
	}

	private void packGameComponents()
	{
		gameComponents.add(new ClientGameKeyboardComponent(client));
		gameComponents.add(new ClientGameThrottleAnimationsComponent(client));
		gameComponents.add(new ClientGameWalkingSoundsComponent(client));
		gameComponents.add(new ClientGameChaseSoundComponent(client));
		gameComponents.add(new ClientGameAudioQueComponent(client));
		gameComponents.add(new ClientGameFinalTicksComponent(client));
		gameComponents.add(new ClientGameIntroListenerComponent(client));
	}

	@Override
	public void run()
	{
		try
		{
			updateGameHandler();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void updateGameHandler() throws InterruptedException
	{
		while (!client.isForceHalt())
		{
			updateGameComponents();
			tick += 1;
			Thread.sleep(Constants.ODD_TICK_LONG_DELAY);
		}
	}

	private void updateGameComponents()
	{
		for (ClientGameComponent gameComponent : gameComponents)
			gameComponent.invokeComponent(tick);
	}
}
