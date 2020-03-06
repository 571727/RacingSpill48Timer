package scenes.regular;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

import org.lwjgl.nuklear.NkContext;

import elem.interactions.RegularTopBar;
import engine.io.Window;
import scenes.Scene;
import scenes.Scenes;
import scenes.regular.visual.MainMenuVisual;

public class MultiplayerScene extends Scene {

	private RegularTopBar topbar;

	public MultiplayerScene(RegularTopBar topbar, NkContext ctx, long window) {
		super(new MainMenuVisual(), null, "MainMenu", ctx, window, 0, topbar.getHeight(), Window.CURRENT_WIDTH,
				Window.CURRENT_HEIGHT - topbar.getHeight());

		this.topbar = topbar;
		
	}

	@Override
	public void init() {
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
		} else {
		}
	}

	@Override
	public void mousePosInput(double x, double y) {
		topbar.move(x, y);
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
