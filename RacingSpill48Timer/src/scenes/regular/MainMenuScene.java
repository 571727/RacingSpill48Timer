package scenes.regular;

import org.lwjgl.glfw.GLFW;

import elem.interactions.TopBar;
import scenes.Scene;
import scenes.visual.MainMenuVisual;

public class MainMenuScene extends Scene {

	private TopBar topBar;

	public MainMenuScene(TopBar topBar) {
		super(new MainMenuVisual(), "MainMenu");
		this.topBar = topBar;
	}

	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void tick(double delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyInput(int keycode, int action) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub
		
	}

}
