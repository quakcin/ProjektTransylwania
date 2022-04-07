package pl.gr14b.transylwania;

class ClientCreateServer
{
	private int port;

	ClientCreateServer (int port)
	{
		this.port = port;
		(new ClientSideServerThread()).start();
	}

	private class ClientSideServerThread extends Thread
	{
		@Override
		public void run ()
		{
			try
			{
				new Server(port);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}