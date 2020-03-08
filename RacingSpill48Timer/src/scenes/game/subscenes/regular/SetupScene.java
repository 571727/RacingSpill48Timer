package scenes.game.subscenes.regular;

import org.lwjgl.nuklear.NkContext;

import elem.ui.UICollector;
import engine.graphics.Renderer;
import scenes.adt.Scene;
import scenes.adt.SceneGlobalFeatures;

public class SetupScene extends Scene{

	public SetupScene(SceneGlobalFeatures features) {
		super(features, null,  "setup");
		// TODO Auto-generated constructor stub
	}
	
	@Override
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
	public void determineUIWindowFocusByMouse(double x, double y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initNuklearVisual(NkContext ctx, SceneGlobalFeatures features, String title, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

}
