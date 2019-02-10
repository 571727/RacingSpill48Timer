package handlers;

import audio.BgMusicListener;

public class GameHandler {

	public static BgMusicListener music;
	private String[] songs;

	public GameHandler(int numScenes) {

		new SceneHandler(numScenes);
		SceneHandler.instance.changeScene(0);
		
		songs = initSongs();
		
		music = new BgMusicListener(songs);
		GameHandler.music.playAndChooseNextRandomly();
		// Loop som kjører viss kode basert på scene, hvis i det hele tatt-

	}

	private String[] initSongs() {
		String[] temp = { "music1", "music2", "music3" };
		return temp;
	}

}
