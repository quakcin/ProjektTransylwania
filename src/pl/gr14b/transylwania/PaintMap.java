package pl.gr14b.transylwania;

class PaintMap extends GraphicsPainter
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

	
	private boolean isRoomCloseEnough(Room r)
	{
		return !(
					 Math.abs(r.getX() - clientGame.getPlayer().getX() / Constants.DEFAULT_ROOM_SIZE) > 2
				|| Math.abs(r.getY() - clientGame.getPlayer().getY() / Constants.DEFAULT_ROOM_SIZE) > 2
		);
	}

	private void paintRooms()
	{
		for (Room r : clientGame.getRooms())
			if (isRoomCloseEnough(r))
				r.paintRoom(this);
	}


	private void paintVerticalDoors()
	{
		for (int y = 0; y < Constants.MAP_SIZE; y++)
			for (int x = 0; x < Constants.MAP_SIZE + 1; x++)
			{
				int index = x * (Constants.MAP_SIZE) + y;

				if (clientGame.getVerticalDoors()[index])
				{
					int dx = x
							* Constants.DEFAULT_ROOM_SIZE
							+ Constants.DEFAULT_DOOR_OFFSET;

					int dy = y
							* Constants.DEFAULT_ROOM_SIZE
							- Constants.DEFAULT_HALF_ROOM_SIZE
							+ Constants.DEFAULT_GRID_SIZE
							+ Constants.DEFAULT_ROOM_SIZE;

					g.drawImage(
							stuff.getDoorTexture(),
							offX + (int) (clientGame.getPlayer().getX() - dx),
							offY + (int) (clientGame.getPlayer().getY() - dy),
							Constants.DEFAULT_GRID_SIZE,
							Constants.DEFAULT_GRID_SIZE,
							null
					);
				}

			}
	}

	private void paintHorizontalDoors()
	{
		for (int y = 0; y < Constants.MAP_SIZE + 1; y++)
			for (int x = 0; x < Constants.MAP_SIZE; x++)
			{
				int index = y * (Constants.MAP_SIZE) + x;

				if (clientGame.getHorizontalDoors()[index])
				{
					int dx = x
							* Constants.DEFAULT_ROOM_SIZE
							- Constants.DEFAULT_HALF_ROOM_SIZE
							+ Constants.DEFAULT_GRID_SIZE
							+ Constants.DEFAULT_ROOM_SIZE;

					int dy = y
							* Constants.DEFAULT_ROOM_SIZE
							+ Constants.DEFAULT_DOOR_OFFSET;

					g.drawImage(
							stuff.getDoorTexture(),
							offX + (int) (clientGame.getPlayer().getX() - dx),
							offY + (int) (clientGame.getPlayer().getY() - dy),
							Constants.DEFAULT_GRID_SIZE,
							Constants.DEFAULT_GRID_SIZE,
							null
					);
				}

			}
	}



}
