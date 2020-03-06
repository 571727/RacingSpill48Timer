package scenes;

import org.lwjgl.nuklear.NkContext;

import engine.graphics.Renderer;
import elem.objects.Camera;

public abstract class Scene implements SceneManipulationI {

	protected SceneChangeAction sceneChange;
	protected Visual visual;
	protected Camera camera;
	protected String sceneName;

	public Scene(Visual visual, Camera camera, String sceneName) {
		this.visual = visual;
		this.sceneName = sceneName;
		this.camera = camera;
	}

	public Scene(Visual visual, Camera camera, String sceneName, NkContext ctx, long window, int x, int y, int width,
			int height) {
		this(visual, camera, sceneName);
		visual.initNuklearVisual(ctx, sceneName, x, y, width, height);
	}

	public void setSceneChangeAction(SceneChangeAction sceneChange) {
		this.sceneChange = sceneChange;
	}

	public abstract void init();

	public abstract void destroy();

	@Override
	public void render(NkContext ctx, Renderer renderer, long window) {
		visual.render(renderer, camera, ctx, window);
	}

}
