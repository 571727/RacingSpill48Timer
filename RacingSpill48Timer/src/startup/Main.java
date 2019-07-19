package startup;

import handlers.GameHandler;

public class Main {

	public static boolean DEBUG = false;
	public static String[] CARTYPES = { "M3", "Supra", "Mustang", "Bentley", "Skoda Fabia", "Corolla" };
	public static String[] AINAMES = { "Jens", "Benz", "Razor", "The Boi", "The Viper", "The Biper", "节奏大师", "Knut",
			"Pepsi", "Cola", "Cherry", "Sprite", "Apex Legend", "The Law", "Anime Lover", "noobmaster69", "TeaBottle",
			"Racerdude" };

	public static void main(String[] args) {
		new GameHandler(5);
		
	}

}
