package scenes;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.Nuklear;

import elem.ColorBytes;
import elem.ui.UICollector;
import elem.ui.UIExitModal;
import elem.ui.UIObject;
import engine.graphics.Renderer;
import scenes.adt.ISceneManipulation;
import scenes.adt.Scene;
import scenes.adt.SceneChangeAction;
import scenes.adt.SceneGlobalFeatures;

public class SceneHandler implements ISceneManipulation {

	private ArrayList<Scene> scenes;
	private SceneGlobalFeatures features;
	private UIExitModal exitModal;
	private UICollector uic;

	public SceneHandler() {
		scenes = new ArrayList<Scene>();
	}

	public void init(Scene[] scenes, SceneGlobalFeatures features, UIExitModal exitModal) {
		for (Scene scene : scenes) {
			this.scenes.add(scene);
		}

		this.features = features;
		this.exitModal = exitModal;
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
			return getCurrentScene();
		};
		for (Scene scene : scenes) {
			scene.setSceneChangeAction(sceneChange);
		}
	}

	public void changeScene(int scenenr) {
		Scenes.PREVIOUS_REGULAR = Scenes.CURRENT_REGULAR;
		Scenes.CURRENT_REGULAR = scenenr;

		getCurrentScene().update();
		getCurrentScene().press();
	}

	public Scene getCurrentScene() {
		return scenes.get(Scenes.CURRENT_REGULAR);
	}

	public Scene getLastScene() {
		return scenes.get(Scenes.PREVIOUS_REGULAR);
	}

	/*
	 * =========== SCENE MANIPULATION ===========
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

		String focus1 = null;
		String focus2 = null;
		UIObject topbar = getCurrentScene().getTopbar();

		/*
		 * EXIT MODAL
		 */
		if (features.isExitModalVisible()) {

			focus1 = exitModal.getName();
			focus2 = focus1;
			exitModal.layout(ctx);
			getCurrentScene().press();

		} else {
			focus1 = getCurrentScene().getUIC().getFocus();
			focus2 = topbar.getName();
		}

		Nuklear.nk_window_set_focus(ctx, focus1);

		getCurrentScene().render(ctx, renderer, window);

		Nuklear.nk_window_set_focus(ctx, focus2);
		if (topbar != null) {
			topbar.layout(ctx);
		}

	}

	@Override
	public boolean keyInput(int keycode, int action) {
		if (features.isExitModalVisible()) {
			
			if (keycode == GLFW.GLFW_KEY_UP || keycode == GLFW.GLFW_KEY_LEFT)
				
			else if (keycode == GLFW.GLFW_KEY_DOWN || keycode == GLFW.GLFW_KEY_RIGHT)
				
			else if (keycode == GLFW.GLFW_KEY_ENTER)
			
			return false;
		} else {
			return getCurrentScene().keyInput(keycode, action);
		}
	}

	@Override
	public void mouseButtonInput(int button, int action, double x, double y) {
		getCurrentScene().mouseButtonInput(button, action, x, y);
		exitModal.unpress();
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