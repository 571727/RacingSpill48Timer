package scenes.regular;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.nuklear.NkContext;

import elem.interactions.RegularTopBar;
import elem.interactions.TopBar;
import engine.objects.Camera;
import engine.objects.UIWindow;
import scenes.Scene;
import scenes.regular.visual.MainMenuVisual;

public class MainMenuScene extends Scene {

	private RegularTopBar topBar;

	public MainMenuScene(RegularTopBar topBar) {
		super(new MainMenuVisual(), null, "MainMenu");
		this.topBar = topBar;
		visual.add(topBar);
	}

	@Override
	public void init() {
		visual.init();
	}

	@Override
	public void tick(double delta) {
		visual.tick(delta);
	}

	@Override
	public boolean keyInput(int keycode, int action) {
		return false;
	}

	@Override
	public void mouseButtonInput(int button, int action, double x, double y) {
		if (action != GLFW.GLFW_RELEASE) {
			topBar.press(x, y);
		} else {
			topBar.release();
		}
	}

	@Override
	public void mousePosInput(double x, double y) {
		topBar.move(x, y);
	}

	@Override
	public void mouseScrollInput(double x, double y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEnterWindowInput(boolean entered) {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		
	}

}
