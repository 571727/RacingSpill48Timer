package handlers;


import adt.Scene;
import elem.Player;
import javafx.embed.swing.JFXPanel;
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
	private int lastScene;
	private int currentScene;
	private boolean fullscreen;
	private boolean specified;
	public int HEIGHT;
	public int WIDTH;
	
	public SceneHandler(int numScenes, Options options) {
		// Make this class static
		if (instance != null)
			// Destroy myself
			return;
		else
			instance = this;

		fullscreen = true;
		windows = new Windows(1280, 800, "A Smooth Cruise");
		scenes = new Scene[numScenes];
		
		scenes[2] = new FixCar();
		scenes[3] = new Race();
		scenes[4] = options;
		scenes[1] = new Lobby((Race) scenes[3], (FixCar) scenes[2]);
		scenes[0] = new MainMenu((Lobby) scenes[1]);
		
		JFXPanel fxPanel = new JFXPanel();
		windows.add(fxPanel);
	}
	
	public void addClosingListener(Player player) {
		windows.closing(player);
	}

	public void changeScene(int scenenr) {
		lastScene = currentScene;
		windows.remove(scenes[currentScene]);
		windows.add(scenes[scenenr]);
		currentScene = scenenr;

		windows.invalidate();
		windows.validate();
		
		windows.repaint();
		
	}
	
	public void justRemove() {
		windows.remove(scenes[currentScene]);

		windows.invalidate();
		windows.validate();
		
		windows.repaint();
	}

	public Scene getCurrentScene() {
		return scenes[currentScene];
	}
	
	public Windows getWindows() {
		return windows;
	}

	public void setWindows(Windows windows) {
		this.windows = windows;
	}
	
	public void setFullScreen(boolean b) {
		fullscreen = b;
	}

	public boolean isFullScreen() {
		return fullscreen;
	}

	public int getWIDTH() {
		return WIDTH;
	}

	public void setHEIGHT(int i) {
		HEIGHT = i;
		WIDTH = HEIGHT * 16 / 9;
	}

	public int getLastScene() {
		return lastScene;
	}

	public boolean isSpecified() {
		return specified;
	}

	public void setSpecified(boolean specified) {
		this.specified = specified;
	}

	public int getCurrentSceneID() {
		return currentScene;
	}

}