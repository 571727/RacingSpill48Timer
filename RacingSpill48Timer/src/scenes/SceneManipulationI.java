package scenes;

import org.lwjgl.nuklear.NkContext;

import engine.graphics.Renderer;

public interface SceneManipulationI {
	public void render(NkContext ctx, Renderer renderer, long window);

	public void tick(double delta);

	/**
	 * @return if it's NOT busy racing
	 */
	public boolean keyInput(int keycode, int action);

	public void mouseButtonInput(int button, int action, double x, double y);

	public void mousePosInput(double x, double y);

	public void mouseScrollInput(double x, double y);

	public void mouseEnterWindowInput(boolean entered);

}
