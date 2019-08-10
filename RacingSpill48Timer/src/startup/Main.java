package startup;

import handlers.GameHandler;

public class Main {

	public static boolean DEBUG = false;
	public static String[] CAR_TYPES = { "M3", "Supra", "Mustang", "Bentley", "Skoda Fabia", "Corolla" };
	public static String[] AI_NAMES = { "Jens", "Benz", "Razor", "The Boi", "The Viper", "The Biper", "èŠ‚å¥�å¤§å¸ˆ", "Knut",
			"Pepsi", "Cola", "Cherry", "Sprite", "Apex Legend", "The Law", "Anime Lover", "noobmaster69", "TeaBottle",
			"Racerdude", "BestRacer97", "Niki Lauda", "PÃ¥l the Racer"};
	public static String[] DIFFICULTY_TYPES = {"Easy", "Normal", "Hard", "Godlike"};
	public static String[] AMOUNT_OF_AI;
	public static boolean[] AI_NAMES_TAKEN;
	public static final String GAME_NAME = "Jhoffis' Road Racers";
	public static final String[] RACE_AMOUNT = {String.valueOf(18), String.valueOf(9), String.valueOf(2)}; 
	

	public static void main(String[] args) {
		AMOUNT_OF_AI = new String[9];
		for(int i = 0; i < AMOUNT_OF_AI.length; i++) {
			AMOUNT_OF_AI[i] = String.valueOf(i);
		}
		new GameHandler(5);
	}
}
