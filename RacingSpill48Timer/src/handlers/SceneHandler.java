package handlers;

import adt.Scene;
import scenes.FixCar;
import scenes.Lobby;
import scenes.MainMenu;
import scenes.Options;
import scenes.Race;
import window.Windows;

public class SceneHandler {

	public static SceneHandler instance;
	
	private Windows windows;
	private Scene[] scenes;
	private int currentScene;
	
	
	public SceneHandler(int numScenes) {
		// Make this class static
		if (instance != null)
			// Destroy myself
			return;
		else
			instance = this;

		windows = new Windows(600, 500, "Racing shit");
		scenes = new Scene[numScenes];

		scenes[0] = new MainMenu();
		scenes[1] = new Lobby();
		scenes[2] = new FixCar();
		scenes[3] = new Race();
		scenes[4] = new Options();
	}

	public void changeScene(int scenenr) {
		
		windows.remove(scenes[currentScene]);
		windows.add(scenes[scenenr]);
		currentScene = scenenr;
		
		windows.invalidate();
		windows.validate();
	}
}