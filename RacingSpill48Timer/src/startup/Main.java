package startup;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import adt.GameMode;
import game_modes.GolfLike;
import game_modes.PointRush;
import handlers.GameHandler;

public class Main {

	public static final boolean DEBUG = false;
	public static final String[] CAR_TYPES = { "M3", "Supra", "Mustang", "Bentley", "Skoda Fabia", "Corolla" };
	public static final String[] AI_NAMES = { "Jens", "Benz", "Razor", "The Boi", "The Viper", "The Biper", "èŠ‚å¥�å¤§å¸ˆ", "Knut",
			"Pepsi", "Cola", "Cherry", "Sprite", "Apex Legend", "The Law", "Anime Lover", "noobmaster69", "TeaBottle",
			"Racerdude", "BestRacer97", "Niki Lauda", "PÃ¥l the Racer"};
	public static final String[] DIFFICULTY_TYPES = {"Easy", "Normal", "Hard", "Godlike"};
	public static String[] AMOUNT_OF_AI;
	public static boolean[] AI_NAMES_TAKEN;
	public static final String GAME_NAME = "Jhoffis' Road Racer";
	public static final String[] RACE_AMOUNT = {String.valueOf(18), String.valueOf(9), String.valueOf(2)};
	public static final GameMode[] GAME_MODES = {new GolfLike(), new PointRush()}; 
	
	private static File file;
	private static List<String> lines;
	public static long DISCONNECTED_ID = -1;

	public static void main(String[] args) {
		AMOUNT_OF_AI = new String[9];
		for(int i = 0; i < AMOUNT_OF_AI.length; i++) {
			AMOUNT_OF_AI[i] = String.valueOf(i);
		}
		
		initDisconnectedID();
		
		new GameHandler(5);
	}


	public static void newDisconnectedID(Long valueOf) {
		
		int pos = 0;
		String line = "discID=" + valueOf;
		
		if (pos == lines.size()) {
			lines.add(line);
		} else {
			lines.add(pos, line);
		}
		
		try {
			Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
			readDisconnectedIDLines();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		DISCONNECTED_ID = getDisconnectedID();
		
	}

	public static long getDisconnectedID() {
		return Long.valueOf(lines.get(0).split("=")[1]);
	}
	
	private static void initDisconnectedID() {
		file = new File("racing.temp");
		try {

			if (!file.isFile()) {
				if (file.createNewFile()) {
					PrintWriter pw = new PrintWriter(file);
					pw.print(0);
					pw.flush();
					pw.close();
				}
			}
			readDisconnectedIDLines();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void readDisconnectedIDLines() throws IOException {
		lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
	}	
}
