package scenes.regular;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

import org.lwjgl.nuklear.NkContext;

import elem.interactions.RegularTopBar;
import elem.ui.UICollector;
import engine.graphics.Renderer;
import engine.io.Window;
import scenes.adt.Scene;
import scenes.Scenes;
import scenes.adt.SceneGlobalFeatures;

public class MultiplayerScene extends Scene {

	private RegularTopBar topbar;

	public MultiplayerScene(SceneGlobalFeatures features, RegularTopBar topbar, NkContext ctx, long window) {
		super(features, null, "MainMenu", ctx, window, 0, topbar.getHeight(), Window.CURRENT_WIDTH,
				Window.CURRENT_HEIGHT - topbar.getHeight());

		this.topbar = topbar;
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePosInput(double x, double y) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void renderUILayout(NkContext ctx, UICollector uic) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void renderUIBackground(NkContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initNuklearVisual(NkContext ctx, SceneGlobalFeatures features, String title, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
