package pl.gr14b.transylwania;

import java.awt.*;

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
	static final int LAMP_BONUS = -10;
	static final int LAMP_PENALTY = 12;

	static final int MAP_SIZE = 5;

	static final long CLIENT_THREAD_DELAY = 2000L;

	static final long ODD_TICK_LONG_DELAY = 1000L / 25L;

	static final long SERVER_THREAD_YIELD = 1000L / 20L;

	static final double DEFAULT_PRIVATE_LIGHTNESS = 0.5d;
	static final double DEFAULT_GLOBAL_LIGHTNESS = 0.7d;

	static final int CHASE_MUSIC_RESET_TICK = 3;

	static final int CHASE_MUSIC_DURATION = 5 * 25 + 10;

	static final int CHASE_MUSIC_RANGE = 900;

	static final int CLOCK_TICKING_TRIGGER = 15;

	static final int SERVER_ODD_TICK = 25;

	static final int ROLE_CALL_DURATION = 25 * 4;

	static final double ROLE_CALL_LIGHTNESS = 0.75d;

	static final double PLAYER_DEFAULT_FORWARD_MOTION_SPEED = 16.3;

	static final double PLAYER_DEFAULT_BACKWARD_MOTION_SPEED = -12;

	static final double PLAYER_DEFAULT_TURNING_SPEED = 10;

	static final double CLIENT_ANIMATION_THROTTLE_LIMITER = 800;
	static final double SERVER_THROTTLING_LIMIT = 150;

	static final long RANDOM_TICK_OFFSET_LIMIT = 15L;
	static final int RANDOM_TICK_OFFSET_DELTA = 3;

	static final long PLAYER_STEP_TICK = 13L;

	static final long CLIENT_SCREEN_REFRESH_RATE = 1000L / 60L;

	static final int VERTICAL_DOORS_SPAWN_CHANCE = 75;
	static final int HORIZONTAL_DOORS_SPAWN_CHANCE = 80;

	static final int DEFAULT_ROOM_SIZE = 810;
	static final int DEFAULT_GRID_SIZE = 90;
	static final int DEFAULT_ROOM_PADDING = 20;
	static final int DEFAULT_ROOM_EDGE = 2 * Constants.DEFAULT_GRID_SIZE;
	static final int DEFAULT_HALF_ROOM_SIZE = 450; // warped perception
	static final int DEFAULT_HALF_ROOM_TRUE_SIZE = DEFAULT_ROOM_SIZE / 2;
	static final int MAP_CENTER = MAP_SIZE * DEFAULT_HALF_ROOM_TRUE_SIZE;

	static final int GHOST_MAX_DIST = (MAP_SIZE + 1) * DEFAULT_HALF_ROOM_TRUE_SIZE;


	static final int LAMP_COLLIDER = 100;
	static final int CHEST_COLLIDER = 400;

	static final int DEFAULT_LAMP_SIZE = 60;
	static final int LAMP_PLAYERS_DELTA = 5;

	static final int DEFAULT_CHEST_SIZE = 100;
	static final int CHEST_PLAYERS_DELTA = 6;

	static final int FAR_PLANE = 0xFFFFF;

	static final double PLAYER_ARROW_DELTA_X = 3.5;
	static final int PLAYER_ARROW_DELTA_Y = 500;

	static final int MAX_PLAYERS = 8;
	static final int WAITING_TIME_DELTA = 5;

	static final int MENU_WIDTH = 800;
	static final int MENU_HEIGHT = 810;

	static final int MENU_BUTTONS_VERTICAL_OFFSET = 480;
	static final int MENU_HOST_BUTTON_HORIZONTAL_OFFSET = 500;
	static final int MENU_JOIN_BUTTON_HORIZONTAL_OFFSET = 200;
	static final int MENU_LOCAL_BUTTON_HORIZONTAL_OFFSET = 350;
	static final int MENU_BUTTON_WIDTH = 100;
	static final int MENU_BUTTON_HEIGHT = 40;

	static final int MENU_COLOR_PRIME = 250;
	static final int MENU_BORDER_WEIGHT = 2;
	static final int MENU_BUTTON_FONT_SIZE = 22;

	static final int DEFAULT_PORT = 666;
	static final String DEFAULT_IP = "127.0.0.1";
	static final int LOCATOR_PORT_TOKEN_COUNT = 2;
	static final int LOCATOR_IP_TOKEN_MIN_LENGTH = 1;

	static final int MENU_BOX_BOUND_X = 200;
	static final int MENU_NICKNAME_BOX_BOUND_Y = 300;
	static final int MENU_ADDRESS_BOX_BOUND_Y = 380;
	static final int MENU_BOX_BOUND_WIDTH = 400;
	static final int MENU_BOX_BOUND_HEIGHT = 45;
	static final int MENU_BOX_FONT_SIZE = 28;
	static final int MENU_BOX_COLUMNS_COUNT = 40;

	static final double LIGHTNESS_DELTA = 0.0015d;

	static final float SCREEN_DEFAULT_FONT_SIZE = 35f;
	static final int SCREEN_PADDING = 50;

	static final int SCREEN_PLAYERS_COUNT_Y_OFFSET = 130;

	static final int SCREEN_HP_X_PADDING = 30 - 90;

	static final int MINUTE_IN_SECONDS = 60;

	static final int AFK_PENALTY_TIME = 15;

	static final int ARROW_ROTATION_OFFSET = 115;

	static final double DEG90 = 90 * (3.1415 / 180);
	static final double DEG180 = 180 * (3.1415 / 180);

	static final int ARROW_SIZE = 40;

	static final int DEFAULT_DOOR_OFFSET = 45;

	static final int DEFAULT_PLAYER_HEALTH = 3;

	static final int BLOOD_SPLASH_MIN_OFFSET = 15;
	static final int BLOOD_SPLASH_MAX_OFFSET = 15;

	static final int CHARACTER_SPRITE_COUNT = 8;

	static final int STAB_SOUND_SPREAD = DEFAULT_ROOM_SIZE * 6;
	static final int DEATH_SOUND_SPREAD = 2000;

	static final double PLAYER_SPRITE_FIXED_ANGLE = 1.57075;

	static final float PLAYER_NICKNAME_FONT_SIZE = 20f;
	static final int DEFAULT_NICK_OFFSET = 45;
	static final double DEFAULT_NICK_DELTA = 5.5;

	static final int PLAYER_SIZE = 80;
	static final int PLAYER_OFFSET = 40;
		static final int PLAYER_COLLIDER = 50;

	static final int PACKET_COUNT = 14;

	// static final int HORIZONTAL_DOOR_X_OFFSET = Constants.DEFAULT_ROOM_SIZE - Constants.DEFAULT_HALF_ROOM_SIZE + Constants.DEFAULT_GRID_SIZE + Constants.DEFAULT_HALF_ROOM_SIZE;
	static final int XY_DOOR_OFFSET = 450 + 90 + 810;

	static final int PUSH_ATTEMPTS = 10;
	static final int PUSH_ATTEMPTS_SPEED = 10;

	static final int CHEST_RENDER_SIZE = 70;
	static final int LAMP_RENDER_SIZE = 40;

	static final int LAMP_SOUND_SPREAD = 1000;

	static final int LAMP_USE_DELAY = 25;

	static final int ROOM_TEXTURE_COUNT = 11;

	static final int TICK_1SEC = 10;

	static final double[] GAME_TIMES_PER_PLAYERS = {3d, 4.5d, 5.5d, 6.3d, 7d, 7.5d, 8d};

	static final int INTRO_MUSIC_TICK = 56;

	static final int VAMP_ATTACK_RANGE = 100;
	static final int VAMP_AGRO_RANGE = 250;

	static final int CHEST_INTERACTION_SPACE_DELAY = 40;
	static final int LOBBY_MAX_WAITING_TIME = 40;

	static final int INTRO_PHASE_DURATION = 5;

	static final int ALLOW_NO_SYNC_DIST = 100;

	static final long SERVER_DATA_EXCHANGE_YIELD = 5L;

	static final int LAST_PACKET_ID = 10;

	static final int MAX_TEXTURE_COUNT = 0xFF;

	static int random (int mm, int mx)
	{
		return (int) Math.round(Math.random() * (mx - mm - 1)) + mm;
	}
}
