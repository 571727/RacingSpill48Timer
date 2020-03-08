package scenes;

import java.util.ArrayList;

import org.lwjgl.nuklear.NkContext;

import elem.ColorBytes;
import elem.ui.UIExitModal;
import engine.graphics.Renderer;
import scenes.adt.ISceneManipulation;
import scenes.adt.Scene;
import scenes.adt.SceneChangeAction;
import scenes.adt.SceneGlobalFeatures;

public class SceneHandler implements ISceneManipulation{

	private ArrayList<Scene> scenes;
	private SceneGlobalFeatures features;
	private UIExitModal exitModal;

	public SceneHandler() {
		scenes = new ArrayList<Scene>();
	}

	public void init(Scene[] scenes, SceneGlobalFeatures features) {
		for (Scene scene : scenes) {
			this.scenes.add(scene);
			scene.init();
		}
		
		this.features = features;
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
	
	/*
	 * ===========	SCENE MANIPULATION	===========
	 */
	
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
		
		/*
		 * EXIT MODAL
		 */
		if (features.isShowExitModal()) {
			features.pushBackgroundColor(new ColorBytes(0x00, 0x00, 0x00, 0x33));
			features.setBackgroundColor(ctx);

			exitModal.layout(ctx);

			features.popBackgroundColor();
		}
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