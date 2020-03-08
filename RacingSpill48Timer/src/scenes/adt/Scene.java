package scenes.adt;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.nuklear.NkContext;
import elem.objects.Camera;
import elem.objects.GameObject;
import elem.ui.UICollector;
import elem.ui.UIObject;
import engine.graphics.Renderer;

public abstract class Scene implements ISceneManipulation {

	protected Random rand = new Random();
	protected SceneChangeAction sceneChange;
	protected SceneGlobalFeatures features;
	protected Camera camera;
	protected ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	protected UICollector uic = new UICollector();
	protected String sceneName = "TITLE NOT SET";

	public Scene(SceneGlobalFeatures features, Camera camera, String sceneName) {
		this.sceneName = sceneName;
		this.camera = camera;
	}

	public Scene(SceneGlobalFeatures features, Camera camera, String sceneName, NkContext ctx, long window, int x, int y, int width,
			int height) {
		this(features, camera, sceneName);
		initNuklearVisual(ctx, features, sceneName, x, y, width, height);
	}

	public void setSceneChangeAction(SceneChangeAction sceneChange) {
		this.sceneChange = sceneChange;
	}

	public abstract void renderUILayout(NkContext ctx, UICollector uic);
	
	public abstract void renderUIBackground(NkContext ctx);

	public abstract void determineUIWindowFocusByMouse(double x, double y);

	public abstract void init();

	protected abstract void initNuklearVisual(NkContext ctx, SceneGlobalFeatures features, String title, int x, int y, int width,
			int height);
	
	public abstract void destroy();

	public abstract void press();


	public void add(GameObject go) {
		gameObjects.add(go);
	}

	public void add(String listname, UIObject uio) {
		uic.add(listname, uio);
	}

	public void removeAll() {
		gameObjects.clear();
		uic.clear();
	}

	public UICollector getUIC() {
		return uic;
	}

}
