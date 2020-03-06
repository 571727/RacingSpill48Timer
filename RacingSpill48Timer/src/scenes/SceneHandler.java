package scenes;

import java.util.ArrayList;

import org.lwjgl.nuklear.NkContext;

import engine.graphics.Renderer;
import engine.io.InputHandler;

public class SceneHandler implements SceneManipulationI{

	private ArrayList<Scene> scenes;

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

	public void changeSceneAction() {
		SceneChangeAction sceneChange = (scenenr) -> {
			changeScene(scenenr);
		};
		for (Scene scene : scenes) {
			scene.setSceneChangeAction(sceneChange);
		}
	}

	public void changeScene(int scenenr) {
		Scenes.PREVIOUS_REGULAR = Scenes.CURRENT_REGULAR;
		Scenes.CURRENT_REGULAR = scenenr;
	}

	public Scene getCurrentScene() {
		return scenes.get(Scenes.CURRENT_REGULAR);
	}

	public Scene getLastScene() {
		return scenes.get(Scenes.PREVIOUS_REGULAR);
	}
	
	/**
	 * TODO Add support for global / universal scene widgets : Modal, topbar etc
	 * IN ALL BELOW
	 */
	@Override
	public void tick(double delta) {
		getCurrentScene().tick(delta);
	}
	
	/**
	 * TODO set global theme here... If it has to be done each cycle
	 */
	@Override
	public void render(NkContext ctx, Renderer renderer, long window) {
		getCurrentScene().render(ctx, renderer, window);
	}

	@Override
	public boolean keyInput(int keycode, int action) {
		return getCurrentScene().keyInput(keycode, action);
	}

	@Override
	public void mouseButtonInput(int button, int action, double x, double y) {
		getCurrentScene().mouseButtonInput(button, action, x, y);
	}

	@Override
	public void mousePosInput(double x, double y) {
		getCurrentScene().mousePosInput(x, y);
	}

	@Override
	public void mouseScrollInput(double x, double y) {
		getCurrentScene().mouseScrollInput(x, y);
	}

	@Override
	public void mouseEnterWindowInput(boolean entered) {
		getCurrentScene().mouseEnterWindowInput(entered);
	}

}