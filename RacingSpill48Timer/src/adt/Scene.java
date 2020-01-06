package adt;

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
	
	public abstract void init();

	public abstract void tick(double delta);
	public abstract void render();

	public abstract void keyInput(int keycode, int action);

	public abstract void mouseButtonInput(int button, int action);
	public abstract void mousePosInput(double x, double y);
	public abstract void mouseScrollInput(double x, double y);
	public abstract void mouseEnterWindowInput(boolean entered);
	
}
