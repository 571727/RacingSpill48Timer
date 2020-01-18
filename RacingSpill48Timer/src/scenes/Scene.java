package scenes;

import engine.graphics.Renderer;

public abstract class Scene {

	protected SceneChangeAction sceneChange;
	protected Visual visual;
	protected String sceneName;
	
	public Scene(Visual visual, String sceneName) {
		this.visual = visual;
		this.sceneName = sceneName; 
	}
	
	public void setSceneChangeAction(SceneChangeAction sceneChange) {
		this.sceneChange = sceneChange;
	}

	public void render(Renderer renderer) {
		visual.render(renderer);
	}
	
	public abstract void tick(double delta);

	public abstract void keyInput(int keycode, int action);

	public abstract void mouseButtonInput(int button, int action, double x, double y);
	public abstract void mousePosInput(double x, double y);
	public abstract void mouseScrollInput(double x, double y);
	public abstract void mouseEnterWindowInput(boolean entered);
	public abstract void destroy();
	
}
