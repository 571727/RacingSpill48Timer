package scenes.game.subscenes.regular;

import org.lwjgl.nuklear.NkContext;

import scenes.Scene;
import scenes.regular.visual.MultiplayerSetupVisual;

public class MultiplayerSetupScene extends Scene{

	public MultiplayerSetupScene() {
		super(new MultiplayerSetupVisual(), null,  "setup");
		// TODO Auto-generated constructor stub
	}

	public void init() {
		
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
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
