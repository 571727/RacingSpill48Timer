package scenes;

import elem.interactions.TopBar;
import engine.io.InputHandler;
import scenes.racing.EndScene;
import scenes.racing.FinishScene;
import scenes.racing.RaceScene;
import scenes.regular.LobbyScene;
import scenes.regular.MainMenuScene;
import scenes.regular.SetupScene;

public class SceneHandler {

	private Scene[] scenes;
	private int lastScene;
	private int currentScene;
	private boolean specified;

	public SceneHandler() {
		scenes = new Scene[7];
	}
	
	public void init(Scene options, TopBar topBar) {
		scenes[0] = new MainMenuScene(topBar);
		scenes[1] = options;
		scenes[2] = new SetupScene();
		scenes[3] = new LobbyScene();
		scenes[4] = new RaceScene();
		scenes[5] = new FinishScene();
		scenes[6] = new EndScene();
		
		for(Scene scene : scenes) {
			scene.init();
		}
		
	}

	/**
	 * FIXME Destroy all the meshes and shaders etc
	 */
	public void destroy() {
		for(Scene scene : scenes) {
			scene.destroy();
		}		
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