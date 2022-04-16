package pl.gr14b.transylwania;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class ClientConnectionHandler extends Thread
{

	private Socket socket;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;

	private Client client;
	private boolean isFistPacket;

	private ArrayList<ClientHandlePacket> packetsHandler;

	ClientConnectionHandler (Client client)
	{
		this.client = client;
		this.isFistPacket = true;

		this.packetsHandler = new ArrayList<>();
		packPackets();

		openSocket();
		start();
	}

	@Override
	public void run ()
	{
		delayThreadInOrderToSyncInWithSwingLibrary();

		while (isGameStillRunning())
			backgroundConnectionHandler();
	}

	private boolean isGameStillRunning()
	{
		return socket.isConnected()
				&& !client.isForceHalt();
	}

	private void packPackets()
	{
		packetsHandler.add(new ClientHandleGamePacket(client, Game.class.toString()));
		packetsHandler.add(new ClientHandleFlagPacket(client, FlagPacket.class.toString()));
		packetsHandler.add(new ClientHandleChestListPacket(client, ChestsListPacket.class.toString()));
		packetsHandler.add(new ClientHandleLampListPacket(client, ClientHandleLampListPacket.class.toString()));
		packetsHandler.add(new ClientHandlePlayersListPacket(client, PlayersListPacket.class.toString()));
		packetsHandler.add(new ClientHandlePropsPacket(client, ClientHandlePropsPacket.class.toString()));
	}

	private void backgroundConnectionHandler ()
	{
		try
		{
			updateConnection();
			Thread.sleep(Constants.ODD_TICK_LONG_DELAY);
		}
		catch (Exception e)
		{
			throwClientConnectionThrottledErrorDialog(
					"You have been disconnected from the server!"
			);
		}
	}

	private void openSocket()
	{
		try
		{
			socket = new Socket(client.getIpAddress(), client.getPort());
			socket.setTcpNoDelay(true);
			objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			objectInputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void updateConnection() throws Exception
	{
		if (!isFistPacket)
			handlePackets();

		else
			handleFirstPacket();
	}


	private void sendPlayerObjectToTheServer () throws Exception
	{
		objectOutputStream.writeObject(client.getClientGame().getPlayer());
		objectOutputStream.flush();
		objectOutputStream.reset();
	}

	private void handleServerResponse () throws Exception
	{
		Object serverResponse = objectInputStream.readObject();

		for (ClientHandlePacket packet : packetsHandler)
			if (packet.isPacket(serverResponse.getClass().toString()))
			{
				packet.handlePacket(serverResponse);
				return;
			}
	}

	private void resetPlayerFlags ()
	{
		client.getClientGame().getPlayer().setForcingSynchronization(false);
		client.getClientGame().getPlayer().setForcingLocationSynchronization(false);
		client.getClientGame().getPlayer().nextPacket();
	}

	private void handlePackets () throws Exception
	{
		sendPlayerObjectToTheServer();
		handleServerResponse();
		resetPlayerFlags();
	}


	private void throwClientConnectionThrottledErrorDialog(String connectionErrorMessage)
	{
		JOptionPane.showMessageDialog(null, connectionErrorMessage);
		new Menu();
		client.setForceHalt(true);
		client.setDefaultCloseOperation(EXIT_ON_CLOSE);
		client.dispose();
		client.setForceHalt(true);
	}

	private void handleFirstPacket()
	{
		Hello helloPacket =
				new Hello(client.getNickName());

		try
		{
			objectOutputStream.writeObject(helloPacket);
			objectOutputStream.flush();
			objectOutputStream.reset();

			Object serverResponse = objectInputStream.readObject();

			if (serverResponse instanceof Game)
				client.setClientGame((Game) serverResponse);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throwClientConnectionThrottledErrorDialog(
					"Failed to say hello!"
			);
		}

		isFistPacket = false;
	}

	private void delayThreadInOrderToSyncInWithSwingLibrary()
	{
		try
		{
			Thread.sleep(Constants.CLIENT_THREAD_DELAY);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
