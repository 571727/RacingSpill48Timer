package handlers;

import adt.Scene;
import adt.SceneChangeAction;
import scenes.LobbyScene;
import scenes.MainMenuScene;
import scenes.SetupScene;
import scenes.race.EndScene;
import scenes.race.FinishScene;
import scenes.race.RaceScene;

public class SceneHandler {

	private Scene[] scenes;
	private int lastScene;
	private int currentScene;
	private boolean specified;

	public SceneHandler() {
		scenes = new Scene[7];
	}
	
	public void init(Scene options) {
		scenes[0] = new MainMenuScene();
		scenes[1] = new SetupScene();
		scenes[2] = new LobbyScene();
		scenes[3] = new RaceScene();
		scenes[4] = options;
		scenes[5] = new FinishScene();
		scenes[6] = new EndScene();
	}

	public void changeSceneAction(InputHandler input) {
		SceneChangeAction sceneChange = (scenenr)->{
			if(scenenr == -1)
				scenenr = this.lastScene;
			changeScene(scenenr);
			input.setCurrent(scenes[currentScene]);
		};
		for(Scene scene : scenes) {
			scene.setSceneChangeAction(sceneChange);
		}
	}
	
	public void changeScene(int scenenr) {
		lastScene = currentScene;
		currentScene = scenenr;
	}

	public Scene getCurrentScene() {
		return scenes[currentScene];
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