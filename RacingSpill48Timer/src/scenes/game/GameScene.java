package scenes.game;

import org.lwjgl.nuklear.NkContext;

import elem.objects.Camera;
import elem.ui.UICollector;
import elem.ui.UIObject;
import engine.graphics.Renderer;
import scenes.adt.Scene;
import scenes.adt.SceneGlobalFeatures;

public class GameScene extends Scene{

	public GameScene() {
		super(null, null, "");
		// TODO Auto-generated constructor stub
	}

	public void createGame(int gameType) {
		
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
	public void tick(double delta) {
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
		// TODO Auto-generated method stub
		return null;
	}

}
