package scenes.adt;

import org.lwjgl.nuklear.NkContext;

import elem.objects.Camera;
import elem.ui.UICollector;
import engine.graphics.Renderer;

public interface ISceneManipulation {


	/**
	 * @return if it's NOT busy racing
	 */
	public boolean keyInput(int keycode, int action);

	public void mouseButtonInput(int button, int action, double x, double y);

	public void mousePosInput(double x, double y);

	public void mouseScrollInput(double x, double y);

	public void mouseEnterWindowInput(boolean entered);

	public void tick(double delta);
	
	public abstract void render(NkContext ctx, Renderer renderer, long window);

}
