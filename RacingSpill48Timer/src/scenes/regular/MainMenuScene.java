package scenes.regular;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.nuklear.NkContext;

import elem.interactions.TopBar;
import engine.objects.Camera;
import engine.objects.UIWindow;
import scenes.Scene;
import scenes.regular.visual.MainMenuVisual;

public class MainMenuScene extends Scene {

	private TopBar topBar;

	public MainMenuScene(TopBar topBar) {
		super(new MainMenuVisual(), new Camera(), "MainMenu");
		this.topBar = topBar;
	}

	@Override
	public void init() {
		visual.init();
	}

	@Override
	public void tick(NkContext ctx, double delta) {
		camera.update();
		visual.tick(delta);
//		somewindow.layout(ctx, 10, 10);
	}

	@Override
	public boolean keyInput(int keycode, int action) {
		if (action != GLFW.GLFW_RELEASE) {
			camera.move(keycode);
		} else {
			camera.moveHalt(keycode);
		}
		
		return true;
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
		camera.rotateCameraMouseBased(x, y);
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
