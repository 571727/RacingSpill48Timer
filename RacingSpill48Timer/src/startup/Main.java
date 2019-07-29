package startup;

import handlers.GameHandler;

public class Main {

	public static boolean DEBUG = false;
	public static String[] CAR_TYPES = { "M3", "Supra", "Mustang", "Bentley", "Skoda Fabia", "Corolla" };
	public static String[] AI_NAMES = { "Jens", "Benz", "Razor", "The Boi", "The Viper", "The Biper", "节奏大师", "Knut",
			"Pepsi", "Cola", "Cherry", "Sprite", "Apex Legend", "The Law", "Anime Lover", "noobmaster69", "TeaBottle",
			"Racerdude", "BestRacer97", "Niki Lauda", "Pål the Racer"};
	public static String[] DIFFICULTY_TYPES = {"Easy", "Normal", "Hard", "Godlike"};
	public static String[] AMOUNT_OF_AI;
	

	public static void main(String[] args) {
		AMOUNT_OF_AI = new String[9];
		for(int i = 0; i < AMOUNT_OF_AI.length; i++) {
			AMOUNT_OF_AI[i] = String.valueOf(i);
		}
		new GameHandler(5);
	}
}
