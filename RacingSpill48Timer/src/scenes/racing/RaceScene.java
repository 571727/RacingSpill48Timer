package scenes.racing;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.nuklear.NkContext;

import engine.objects.Camera;
import scenes.Scene;
import scenes.racing.visual.RaceVisual;

public class RaceScene extends Scene{

	public RaceScene() {
		super(new RaceVisual(), new Camera(),"race");
	}

	@Override
	public void init() {
		visual.init();
	}

	@Override
	public void tick(NkContext ctx, double delta) {
		visual.tick(delta);
		camera.update();
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
//		if (action != GLFW.GLFW_RELEASE) {
//			topBar.press(x, y);
//		} else {
//			topBar.release();
//		}		
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
	public void destroy() {
		// TODO Auto-generated method stub
		
	}


}
