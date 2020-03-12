package scenes;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.Nuklear;

import elem.Action;
import elem.ColorBytes;
import elem.ui.UICollector;
import elem.ui.UIExitModal;
import elem.ui.UIObject;
import engine.graphics.Renderer;
import scenes.adt.ISceneManipulation;
import scenes.adt.Scene;
import scenes.adt.SceneChangeAction;
import scenes.adt.GlobalFeatures;

public class SceneHandler implements ISceneManipulation {

	private ArrayList<Scene> scenes;
	private GlobalFeatures features;
	private UIExitModal exitModal;

	public SceneHandler() {
		scenes = new ArrayList<Scene>();
	}

	public void init(Scene[] scenes, GlobalFeatures features, UIExitModal exitModal) {
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
		Action sceneUpdate = () -> {
			getCurrentScene().update();
			getCurrentScene().press();
		};

		SceneChangeAction sceneChange = (scenenr, update) -> {
			changeScene(scenenr);
			
			if(update)
				sceneUpdate.run();
			
			return getCurrentScene();
		};
		
		
		for (Scene scene : scenes) {
			scene.setSceneChangeAction(sceneChange);
			scene.setSceneUpdateAction(sceneUpdate);
		}
		
		sceneChange.run(Scenes.MAIN_MENU, true);
	}

	private void changeScene(int scenenr) {
		Scenes.PREVIOUS_REGULAR = Scenes.CURRENT_REGULAR;
		Scenes.CURRENT_REGULAR = scenenr;
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
			focus1 = features.getUIC().getFocus();
			if (topbar != null)
				focus2 = topbar.getName();
		}

		Nuklear.nk_window_set_focus(ctx, focus1);

		getCurrentScene().render(ctx, renderer, window);

		if (focus2 != null) {
			Nuklear.nk_window_set_focus(ctx, focus2);
			if (topbar != null) {
				topbar.layout(ctx);
			}
		}
	}

	@Override
	public boolean keyInput(int keycode, int action) {
		boolean res = false;

		if (features.isExitModalVisible()) {

			if (keycode == GLFW.GLFW_KEY_UP || keycode == GLFW.GLFW_KEY_LEFT)
				exitModal.getOkBtn().hover();
			else if (keycode == GLFW.GLFW_KEY_DOWN || keycode == GLFW.GLFW_KEY_RIGHT)
				exitModal.getCancelBtn().hover();
			else if (keycode == GLFW.GLFW_KEY_ENTER) {
				features.pressFindHoveredButtonUIC(exitModal.getName());
				features.clearHoveredButtonUIC(exitModal.getName());
			}

		} else {
			res = getCurrentScene().keyInput(keycode, action);
		}

		// Upstroke
		if (action == 0) {
			features.releaseHoveredButtonUIC();
		}

		return res;
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