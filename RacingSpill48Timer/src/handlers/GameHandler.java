package handlers;

import audio.BgMusicListener;

public class GameHandler {

	public static BgMusicListener music;

	public GameHandler(int numScenes) {

		new SceneHandler(numScenes);
		SceneHandler.instance.changeScene(0);
		
		
		music = new BgMusicListener(9);
		// Loop som kj�rer viss kode basert p� scene, hvis i det hele tatt-

	}



}
