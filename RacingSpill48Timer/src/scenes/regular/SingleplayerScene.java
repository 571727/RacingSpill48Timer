package scenes.regular;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_NO_INPUT;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_NO_SCROLLBAR;
import static org.lwjgl.nuklear.Nuklear.nk_begin;
import static org.lwjgl.nuklear.Nuklear.nk_end;
import static org.lwjgl.nuklear.Nuklear.nk_group_begin;
import static org.lwjgl.nuklear.Nuklear.nk_group_end;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_dynamic;
import static org.lwjgl.nuklear.Nuklear.nk_rect;
import static scenes.game.multiplayer.MultiplayerType.*;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkRect;
import org.lwjgl.nuklear.NkVec2;
import org.lwjgl.nuklear.Nuklear;

import elem.ColorBytes;
import elem.interactions.RegularTopbar;
import elem.ui.ButtonNavigation;
import elem.ui.UIButton;
import elem.ui.UICollector;
import elem.ui.UINkImage;
import elem.ui.UIObject;
import engine.graphics.Renderer;
import engine.io.Window;
import main.Main;
import scenes.adt.Scene;
import scenes.Scenes;
import scenes.adt.SceneGlobalFeatures;
import scenes.game.GameScene;


public class SingleplayerScene extends Scene {

	private UIButton gobackBtn;
	private UIButton[] btns;
	private RegularTopbar topbar;
	
	private int windowOptions;
	private int btnHeight;
	private int hPadding;
	private NkRect windowRect;

	public SingleplayerScene(SceneGlobalFeatures features, RegularTopbar topbar, NkContext ctx, long window) {
		super(features, null, "Singleplayer", ctx, window, 0, topbar.getHeight(), Window.CURRENT_WIDTH,
				Window.CURRENT_HEIGHT - topbar.getHeight());

		this.topbar = topbar;
		
		gobackBtn = new UIButton("Go back");
		gobackBtn.setPressedAction(() -> {
			sceneChange.run(Scenes.MAIN_MENU);
		});
		add(sceneName, gobackBtn);
		
		//FIXME Moneyrush button is missing
		
		btns = new UIButton[Main.GAME_MODES.length];
		for(int i = 0; i < btns.length; i++) {
			btns[i] = new UIButton("Play \"" + Main.GAME_MODES[i] + "\" mode");
			int gameType = i;
			btns[i].setPressedAction(() -> {
				GameScene game = (GameScene) sceneChange.run(Scenes.GAME);
//				game.createGame(gameType, SINGLEPLAYER);
			});
			add(sceneName, btns[i]);
			
		}
		
		//Navigation
		gobackBtn.setNavigations(null, null, null, btns[0]);

		for(int i = 0; i < btns.length; i++) {
			UIButton aboveBtn = null;
			UIButton belowBtn = null;
			
			if(i > 0)
				aboveBtn = btns[i - 1];
			else
				aboveBtn = gobackBtn;
			
			if (i + 1 < btns.length)
				belowBtn = btns[i + 1];
			
			btns[i].setNavigations(null, null, aboveBtn, belowBtn);
		}
		
		update();
	}

	@Override
	public void tick(double delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean keyInput(int keycode, int action) {
		if(action == 1) {
			// Downstroke for quicker input
			UIButton hoveredButton = features.getUIC().getHoveredButton(sceneName); 
			if(hoveredButton == null)
				hoveredButton = gobackBtn;
			
			generalHoveredButtonNavigation(hoveredButton, keycode);
		}
		
		return false;
	}

	@Override
	public void determineUIWindowFocusByMouse(double x, double y) {
		if (y > topbar.getHeight()) {
			// Give focus to the menu.
			features.getUIC().setFocus(sceneName);
		} else {
			// Give focus to the topbar.
			features.getUIC().setFocus(topbar.getName());
		}
	}

	@Override
	public void mouseButtonInput(int button, int action, double x, double y) {
		boolean down = action != GLFW_RELEASE;

		if (down) {
			topbar.press(x, y);
			gobackBtn.unpress();
			for(UIButton btn : btns) {
				btn.unpress();
			}
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
		renderUILayout(ctx, features.getUIC());
		renderUIBackground(ctx);
	}

	@Override
	public void renderUILayout(NkContext ctx, UICollector uic) {

		// Set the padding of the group
		NkVec2 padding = ctx.style().window().group_padding().set(hPadding, btnHeight);
		ctx.style().window().spacing().set(0, btnHeight / 2);

		features.setBackgroundColor(ctx);
		/*
		 * MAIN SHIT
		 */
		if (nk_begin(ctx, sceneName, windowRect, windowOptions)) {
			/*
			 * GROUP OF MAIN BUTTONS
			 */

			nk_layout_row_dynamic(ctx, Window.CURRENT_HEIGHT - topbar.getHeight(), 1);

			// Groups have the same options available as windows
			int options = NK_WINDOW_NO_SCROLLBAR;

			features.pushBackgroundColor(ctx, new ColorBytes(0x00, 0x00, 0x00, 0x00));

			if (nk_group_begin(ctx, "My Group", options)) {

				//
				// The group contains rows and the rows contain widgets, put those here.
				//
				for (int i = 0; i < uic.size(sceneName); i++) {
					nk_layout_row_dynamic(ctx, btnHeight, 1); // nested row
					uic.get(sceneName, i).layout(ctx);
				}

				// Unlike the window, the _end() function must be inside the if() block
				nk_group_end(ctx);
			}

			features.popBackgroundColor(ctx);
		}
		// End the window
		nk_end(ctx);
	}

	@Override
	public void update() {
		topbar.setTitle(sceneName);
	}

	@Override
	protected void init(NkContext ctx, int x, int y, int width, int height) {
		backgroundImage = UINkImage.createNkImage("back/lobby.png");
		
		btnHeight = Window.CURRENT_HEIGHT / 12;
		hPadding = Window.CURRENT_WIDTH / 8;

		windowOptions = Nuklear.NK_WINDOW_SCROLL_AUTO_HIDE;
		windowRect = NkRect.create();
		nk_rect(x, y, width, height, windowRect);

		nk_begin(ctx, sceneName, windowRect, windowOptions);
		nk_end(ctx);

		features.getUIC().forceFocus(sceneName);
	}

	@Override
	public void destroy() {
		removeAll();
	}

	@Override
	public void press() {
		gobackBtn.press();
		for(UIButton btn : btns) {
			btn.press();
		}
	}

	@Override
	public UIObject getTopbar() {
		return topbar;
	}

}
