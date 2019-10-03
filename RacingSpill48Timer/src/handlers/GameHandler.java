package handlers;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import audio.BgMusicListener;
import audio.ButtonAudio;
import scenes.Options;
import startup.Main;

public class GameHandler {

	public static BgMusicListener music;
	public static ButtonAudio ba;
	private static double masterVolume;
	private static double sfxVolume;
	private static double musicVolume;
	private static File file;
	private static List<String> lines;

	public GameHandler(int numScenes) {
		initSettings();
		masterVolume = getSettingAsDouble(1);
		sfxVolume = getSettingAsDouble(2);
		musicVolume = getSettingAsDouble(3);

		Options options = new Options();

		new SceneHandler(numScenes, options);
		SceneHandler.instance.changeScene(0);

		music = new BgMusicListener(0);
		ba = new ButtonAudio();
		// Loop som kj�rer viss kode basert p� scene, hvis i det hele tatt-

	}

	public static double getMasterVolume() {
		return masterVolume;
	}

	public static double getSfxVolume() {
		return sfxVolume;
	}

	public static double getMusicVolume() {
		return musicVolume;
	}

	public static void setMasterVolume(double val) {

		int pos = 1;
		String line = "masterVolume=" + val;
		
		
		if (pos != lines.size()) {
			lines.set(pos, line);
		} else {
			lines.add(line);
		}

		try {
			Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
			readSettingsLines();
		} catch (IOException e) {
			e.printStackTrace();
		}

		masterVolume = val;
	}

	public static void setSfxVolume(double val) {
		int pos = 2;
		String line = "sfxVolume=" + val;

		if (pos != lines.size()) {
			lines.set(pos, line);
		} else {
			lines.add(line);
		}

		try {
			Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
			readSettingsLines();
		} catch (IOException e) {
			e.printStackTrace();
		}

		sfxVolume = val;
	}

	public static void setMusicVolume(double val) {
		int pos = 3;
		String line = "musicVolume=" + val;
		
		if (pos != lines.size()) {
			lines.set(pos, line);
		} else {
			lines.add(line);
		}

		try {
			Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
			readSettingsLines();
		} catch (IOException e) {
			e.printStackTrace();
		}

		musicVolume = val;
	}

	public static void newDisconnectedID(Long valueOf) {

		int pos = 0;
		String line = "discID=" + valueOf;

		if (pos != lines.size()) {
			lines.set(pos, line);
		} else {
			lines.add(line);
		}

		try {
			Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
			readSettingsLines();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Main.DISCONNECTED_ID = getSettingAsLong(0);
	}

	private static long getSettingAsLong(int line) {
		if (lines.size() > line)
			return Long.valueOf(lines.get(line).split("=")[1]);
		return -1;
	}

	private static double getSettingAsDouble(int line) {
		if (lines.size() > line)
			return Double.valueOf(lines.get(line).split("=")[1]);
		return -1;
	}

	private void initSettings() {
		new File("racingmaybe_temp").mkdir();
		file = new File("racingmaybe_temp/settings.temp");
		try {

			
			if (!file.isFile()) {
				if (file.createNewFile()) {
					PrintWriter pw = new PrintWriter(file);
					pw.flush();
					pw.close();
				}
			}
			readSettingsLines();
		} catch (IOException e) {
			e.printStackTrace();
		}

		newSettings();
		Main.DISCONNECTED_ID = getSettingAsLong(0);
	}

	private void newSettings() {

		int pos = 0;
		String line = "discID=" + Main.DISCONNECTED_ID;
		if (pos >= lines.size()) {
			lines.add(line);
		}
		pos = 1;
		line = "masterVolume=" + 0.5;
		if (pos >= lines.size()) {
			lines.add(line);
		}
		pos = 2;
		line = "sfxVolume=" + 1.0;
		if (pos >= lines.size()) {
			lines.add(line);
		}
		pos = 3;
		line = "musicVolume=" + 1.0;
		if (pos >= lines.size()) {
			lines.add(line);
		}

		try {
			Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
			readSettingsLines();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void readSettingsLines() throws IOException {
		lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
	}

}
