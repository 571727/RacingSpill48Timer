package scenes.regular;

import org.lwjgl.nuklear.NkContext;

import audio.AudioHandler;
import elem.interactions.RegularTopbar;
import elem.ui.UIButton;
import elem.ui.UICollector;
import elem.ui.UIObject;
import engine.graphics.Renderer;
import engine.io.Window;
import file_manipulation.ControlsSettings;
import file_manipulation.RegularSettings;
import scenes.adt.Scene;
import scenes.game.GameScene;
import scenes.game.multiplayer.GameType;
import scenes.Scenes;
import scenes.adt.GlobalFeatures;

public class OptionsScene extends Scene {
	
	private RegularTopbar topbar;
	private UIButton gobackBtn;

	public OptionsScene(GlobalFeatures features, RegularTopbar topbar, NkContext ctx, long window) {
		super(features, null, "Options", ctx, window, 0, topbar.getHeight(), Window.CURRENT_WIDTH,
				Window.CURRENT_HEIGHT - topbar.getHeight());
		// TODO fullscreen checks? 
		this.topbar = topbar;
		
		gobackBtn = new UIButton(features.getGobackText());

		gobackBtn.setPressedAction(() -> {
			sceneChange.run(Scenes.PREVIOUS_REGULAR, true);
		});

		/*
		 * Add to a specific window
		 */

		add(sceneName, gobackBtn);

	}

	public void initOptions(RegularSettings settings, ControlsSettings controlsSettings, AudioHandler audio) {

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
