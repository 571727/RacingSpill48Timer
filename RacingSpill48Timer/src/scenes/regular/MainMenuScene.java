package scenes.regular;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

import org.lwjgl.nuklear.NkContext;

import elem.interactions.RegularTopBar;
import engine.io.Window;
import engine.objects.UIButton;
import scenes.Scene;
import scenes.Scenes;
import scenes.regular.visual.MainMenuVisual;

public class MainMenuScene extends Scene {

	private UIButton singleplayerBtn, multiplayerBtn, optionsBtn, exitBtn;
	private RegularTopBar topBar;

	public MainMenuScene(RegularTopBar topBar, NkContext ctx, long window) {
		super(new MainMenuVisual(), null, "MainMenu", ctx, window, 0, topBar.getHeight(), Window.CURRENT_WIDTH, Window.CURRENT_HEIGHT - topBar.getHeight());

		this.topBar = topBar;
		singleplayerBtn = new UIButton("Singleplayer");
		multiplayerBtn = new UIButton("Multiplayer");
		optionsBtn = new UIButton("Options and controls");
		exitBtn = new UIButton("Exit");

		singleplayerBtn.setPressedAction(() -> {
			sceneChange.run(Scenes.SINGLEPLAYER);
		});
		multiplayerBtn.setPressedAction(() -> {
			sceneChange.run(Scenes.MULTIPLAYER);
		});
		optionsBtn.setPressedAction(() -> {
			sceneChange.run(Scenes.OPTIONS);
		});
		exitBtn.setPressedAction(() -> glfwSetWindowShouldClose(window, true));

		/*
		 * FIXME add to a specific window or something 
		 */
		
		visual.add(singleplayerBtn, sceneName);
		visual.add(multiplayerBtn, sceneName);
		visual.add(optionsBtn, sceneName);
		visual.add(exitBtn, sceneName);
	}

	@Override
	public void init() {
		((MainMenuVisual) visual).init(topBar);
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
		boolean down = action != GLFW_RELEASE;

		if (down) {
			topBar.press(x, y);
			singleplayerBtn.unpress();
			multiplayerBtn.unpress();
			optionsBtn.unpress();
			exitBtn.unpress();
		} else {
			topBar.release();
		}
	}

	@Override
	public void mousePosInput(double x, double y) {
		topBar.move(x, y);
		visual.determineUIWindowFocusByMouse(x, y);
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
