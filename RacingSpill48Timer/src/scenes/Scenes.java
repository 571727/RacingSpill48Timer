package scenes;

public class Scenes {
	/*
	 * REGULAR indexes
	 */
	public static final int MAIN_MENU = 0, MULTIPLAYER = 1, OPTIONS = 2, GAME = 3, AMOUNT_REGULAR = 4,
			/*
			 * INGAME indexes
			 */
			SETUP_LOBBY = 0, RACE = 1, FINISH = 2, LOBBY = 3, END = 4, AMOUNT_GAME = 5;
	
	public static int PREVIOUS_REGULAR = MAIN_MENU, CURRENT_REGULAR = MAIN_MENU;
	public static int PREVIOUS_GAME = SETUP_LOBBY, CURRENT_GAME = SETUP_LOBBY;
}
