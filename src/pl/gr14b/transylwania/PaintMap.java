package pl.gr14b.transylwania;

public class PaintMap extends GraphicsPainter
{
	PaintMap(Client client) {
		super(client);
	}

	@Override
	void paint()
	{
		paintRooms();
		paintHorizontalDoors();
		paintVerticalDoors();
	}

	
	boolean isRoomCloseEnough (Room r)
	{
		return !(
					 Math.abs(r.getX() - clientGame.getPlayer().getX() / 810) > 2
				|| Math.abs(r.getY() - clientGame.getPlayer().getY() / 810) > 2
		);
	}

	void paintRooms ()
	{
		for (Room r : clientGame.getRooms())
			if (isRoomCloseEnough(r))
				r.paintRoom(this);
	}


	// FIXME: L8ER

	void paintVerticalDoors()
	{
		for (int y = 0; y < clientGame.MAP_SIZE; y++)
			for (int x = 0; x < clientGame.MAP_SIZE + 1; x++)
			{
				int index = x * (clientGame.MAP_SIZE) + y;

				if (clientGame.getVerticalDoors()[index])
				{
					int dx = x * 810 + 45;
					int dy = y * 810 - 450 + 90 + 810;
					g.drawImage(stuff.getDoorTexture(),offX + (int) (clientGame.getPlayer().getX() - dx), offY + (int) (clientGame.getPlayer().getY() - dy), 90, 90, null);
				}

			}
	}

	void paintHorizontalDoors()
	{
		for (int y = 0; y < clientGame.MAP_SIZE + 1; y++)
			for (int x = 0; x < clientGame.MAP_SIZE; x++)
			{
				int index = y * (clientGame.MAP_SIZE) + x;

				if (clientGame.getHorizontalDoors()[index])
				{
					int dx = x * 810 - 450 + 90 + 810;
					int dy = y * 810 + 45;
					g.drawImage(stuff.getDoorTexture(), offX + (int) (clientGame.getPlayer().getX() - dx), offY + (int) (clientGame.getPlayer().getY() - dy), 90, 90, null);
				}

			}
	}



}
