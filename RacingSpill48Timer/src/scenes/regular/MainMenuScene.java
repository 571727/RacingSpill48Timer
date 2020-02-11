package scenes.regular;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import org.lwjgl.nuklear.NkContext;

import elem.interactions.RegularTopBar;
import elem.interactions.TopBar;
import engine.objects.Camera;
import engine.objects.UIButton;
import engine.objects.UIWindow;
import scenes.Scene;
import scenes.regular.visual.MainMenuVisual;

public class MainMenuScene extends Scene {

	private RegularTopBar topBar;

	public MainMenuScene(RegularTopBar topBar, long window) {
		super(new MainMenuVisual(), null, "MainMenu");

		this.topBar = topBar;
		UIButton singleplayerBtn = new UIButton("Singleplayer");
		UIButton multiplayerBtn = new UIButton("Multiplayer");
		UIButton optionsBtn = new UIButton("Options and controls");
		UIButton exitBtn = new UIButton("Exit");

		exitBtn.setAction(() -> glfwSetWindowShouldClose(window, true));

		visual.add(topBar);
		visual.add(singleplayerBtn);
		visual.add(multiplayerBtn);
		visual.add(optionsBtn);
		visual.add(exitBtn);
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
		if (action != GLFW_RELEASE) {
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
