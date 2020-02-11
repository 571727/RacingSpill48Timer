package scenes;

import org.lwjgl.nuklear.NkContext;

import engine.graphics.Renderer;
import engine.objects.Camera;

public abstract class Scene {

	protected SceneChangeAction sceneChange;
	protected Visual visual;
	protected Camera camera;
	protected String sceneName;
	
	public Scene(Visual visual, Camera camera, String sceneName) {
		this.visual = visual;
		this.sceneName = sceneName; 
		this.camera = camera;
		
		visual.initNuklearVisual(sceneName);
	}
	
	public void setSceneChangeAction(SceneChangeAction sceneChange) {
		this.sceneChange = sceneChange;
	}

	public void render(NkContext ctx, Renderer renderer) {
		visual.render(renderer, camera, ctx);
	}
	
	public abstract void init();
	
	public abstract void tick(double delta);

	/**
	 * @return if it's NOT busy racing
	 */
	public abstract boolean keyInput(int keycode, int action);

	public abstract void mouseButtonInput(int button, int action, double x, double y);
	public abstract void mousePosInput(double x, double y);
	public abstract void mouseScrollInput(double x, double y);
	public abstract void mouseEnterWindowInput(boolean entered);
	public abstract void destroy();
	
}
