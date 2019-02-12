package handlers;

import audio.BgMusicListener;
import scenes.Options;

public class GameHandler {

	public static BgMusicListener music;
	public static double volume;

	public GameHandler(int numScenes) {
		volume = 0.25;
		Options options = new Options(volume);
		
		new SceneHandler(numScenes, options);
		SceneHandler.instance.changeScene(0);
		
		
		music = new BgMusicListener(9);
		
		// Loop som kjører viss kode basert på scene, hvis i det hele tatt-

	}



}
