package pl.gr14b.transylwania;

class Constants
{
	// -- Winner status
	static final boolean WINNER_VAMP = true;
	static final boolean WINNER_SURVIVOR = false;


	// -- In Game Flags and Constants
	static final int VAMP_ATTACK_DELAY = 30;
	static final int VAMP_ATTACK_MISS_DELAY = 15;
	static final int LOBBY_CHANGE_PLAYER_MODEL__DELAY = 15;

	// -- Parameters for game loop
	static final int LAMP_BONUS = 10;
	static final int LAMP_PENALTY = 12;

	static final int MAP_SIZE = 5;

	static int random (int mm, int mx)
	{
		return (int) Math.round(Math.random() * (mx - mm - 1)) + mm;
	}
}
