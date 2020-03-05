package scenes;

import java.util.ArrayList;

import engine.io.InputHandler;

public class SceneHandler {

	private ArrayList<Scene> scenes;
	private boolean specified;

	public SceneHandler() {
		scenes = new ArrayList<Scene>();
	}

	public void init(Scene[] scenes) {
		for (Scene scene : scenes) {
			this.scenes.add(scene);
			scene.init();
		}
	}

	/**
	 * Destroy all the meshes and shaders etc
	 */
	public void destroy() {
		for (Scene scene : scenes) {
			scene.destroy();
		}
	}

	public void changeSceneAction(InputHandler input) {
		SceneChangeAction sceneChange = (scenenr) -> {
			changeScene(scenenr);
			input.setCurrent(scenes.get(Scenes.CURRENT));
		};
		for (Scene scene : scenes) {
			scene.setSceneChangeAction(sceneChange);
		}
	}

	public void changeScene(int scenenr) {
		Scenes.PREVIOUS = Scenes.CURRENT;
		Scenes.CURRENT = scenenr;
	}

	public Scene getCurrentScene() {
		return scenes.get(Scenes.CURRENT);
	}

	public Scene getLastScene() {
		return scenes.get(Scenes.PREVIOUS);
	}

	public boolean isSpecified() {
		return specified;
	}

	public void setSpecified(boolean specified) {
		this.specified = specified;
	}

}