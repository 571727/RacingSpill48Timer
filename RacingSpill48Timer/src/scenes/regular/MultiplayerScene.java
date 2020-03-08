package scenes.regular;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

import org.lwjgl.nuklear.NkContext;

import elem.interactions.RegularTopbar;
import elem.ui.UICollector;
import elem.ui.UINkImage;
import elem.ui.UIObject;
import engine.graphics.Renderer;
import engine.io.Window;
import scenes.adt.Scene;
import scenes.Scenes;
import scenes.adt.SceneGlobalFeatures;

public class MultiplayerScene extends Scene {

	private RegularTopbar topbar;

	public MultiplayerScene(SceneGlobalFeatures features, RegularTopbar topbar, NkContext ctx, long window) {
		super(features, null, "Multiplayer", ctx, window, 0, topbar.getHeight(), Window.CURRENT_WIDTH,
				Window.CURRENT_HEIGHT - topbar.getHeight());

		this.topbar = topbar;
		update();
	}

	@Override
	public void tick(double delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean keyInput(int keycode, int action) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void determineUIWindowFocusByMouse(double x, double y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseButtonInput(int button, int action, double x, double y) {
		boolean down = action != GLFW_RELEASE;

		if (down) {
			topbar.press(x, y);
			topbar.unpress();
		} else {
			topbar.release();
		}
	}

	@Override
	public void mousePosInput(double x, double y) {
		topbar.move(x, y);
		determineUIWindowFocusByMouse(x, y);
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
	public void render(NkContext ctx, Renderer renderer, long window) {
		renderUIBackground(ctx);
	}

	@Override
	public void renderUILayout(NkContext ctx, UICollector uic) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		topbar.setTitle(sceneName);
	}

	@Override
	protected void init(NkContext ctx, int x, int y, int width, int height) {
		backgroundImage = UINkImage.createNkImage("back/lobby.png");

	}

	@Override
	public void destroy() {
		removeAll();
	}

	@Override
	public void press() {
		// TODO Auto-generated method stub

	}

	@Override
	public UIObject getTopbar() {
		return topbar;
	}

}
