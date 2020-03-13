package scenes.game.subscenes.racing;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.nuklear.NkContext;

import elem.interactions.TransparentTopbar;
import elem.objects.Camera;
import elem.ui.UICollector;
import elem.ui.UIObject;
import engine.graphics.Renderer;
import engine.io.Window;
import scenes.adt.GlobalFeatures;
import scenes.adt.Scene;
import scenes.game.subscenes.SubScene;

public class RaceScene extends SubScene{

	private TransparentTopbar transparentTopbar;

	public RaceScene(GlobalFeatures features, TransparentTopbar transparentTopbar, NkContext ctx, long window) {
		super(features, new Camera(), "Race", ctx, window, 0, 0, Window.CURRENT_WIDTH, Window.CURRENT_HEIGHT);
		this.transparentTopbar = transparentTopbar;
	
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
			transparentTopbar.press(x, y);
		} else {
			transparentTopbar.release();
		}		
	}

	@Override
	public void mousePosInput(double x, double y) {
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
	public void tick(double delta) {
		camera.update();
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
	public void determineUIWindowFocusByMouse(double x, double y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void init(NkContext ctx, int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void press() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UIObject getTopbar() {
		return null;
	}




//	@Override
//	public void mouseButtonInput(int button, int action, double x, double y) {
//		
	}

//	@Override
//	public void mousePosInput(double x, double y) {
//		camera.rotateCameraMouseBased(x, y);		
//	}

	



