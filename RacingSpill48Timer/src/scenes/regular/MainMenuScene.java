package scenes.regular;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_NO_INPUT;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_NO_SCROLLBAR;
import static org.lwjgl.nuklear.Nuklear.nk_begin;
import static org.lwjgl.nuklear.Nuklear.nk_end;
import static org.lwjgl.nuklear.Nuklear.nk_group_begin;
import static org.lwjgl.nuklear.Nuklear.nk_group_end;
import static org.lwjgl.nuklear.Nuklear.nk_image;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_dynamic;
import static org.lwjgl.nuklear.Nuklear.nk_rect;

import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkImage;
import org.lwjgl.nuklear.NkRect;
import org.lwjgl.nuklear.NkVec2;
import org.lwjgl.nuklear.NkWindow;
import org.lwjgl.nuklear.Nuklear;

import elem.ColorBytes;
import elem.interactions.RegularTopBar;
import elem.objects.GameObject;
import engine.graphics.Renderer;
import engine.io.Window;
import elem.ui.UIButton;
import elem.ui.UICollector;
import elem.ui.UIExitModal;
import elem.ui.UINkImage;
import scenes.adt.Scene;
import scenes.Scenes;
import scenes.adt.SceneGlobalFeatures;
/**
 * 
 * @author Jens Benz
 *
 */
public class MainMenuScene extends Scene {

	private UIButton singleplayerBtn, multiplayerBtn, optionsBtn, exitBtn; 
	private RegularTopBar topbar;
	
	private int windowOptions;
	private int btnHeight;
	private int hPadding;
	private NkImage backgroundImage;
	private NkRect windowRect;

	public MainMenuScene(SceneGlobalFeatures features, RegularTopBar topbar, NkContext ctx, long window) {
		super(features, null, "MainMenu", ctx, window, 0, topbar.getHeight(), Window.CURRENT_WIDTH,
				Window.CURRENT_HEIGHT - topbar.getHeight());

		this.topbar = topbar;
		singleplayerBtn = new UIButton("Singleplayer");
		multiplayerBtn = new UIButton("Multiplayer");
		optionsBtn = new UIButton("Options and controls");
		exitBtn = new UIButton("Exit");

		singleplayerBtn.setPressedAction(() -> {
			sceneChange.run(Scenes.SINGLEPLAYER);
		});
		multiplayerBtn.setPressedAction(() -> {
			sceneChange.run(Scenes.MULTIPLAYER);
		});
		optionsBtn.setPressedAction(() -> {
			sceneChange.run(Scenes.OPTIONS);
		});
		exitBtn.setPressedAction(() -> features.showExitModal());

		/*
		 * Add to a specific window
		 */

		add(sceneName, singleplayerBtn);
		add(sceneName, multiplayerBtn);
		add(sceneName, optionsBtn);
		add(sceneName, exitBtn);
		
	}

	@Override
	public void init() {
		features.pushBackgroundColor(new ColorBytes(0x66, 0, 0, 0x66));
		windowOptions = NK_WINDOW_NO_SCROLLBAR;

		btnHeight = Window.CURRENT_HEIGHT / 12;
		hPadding = Window.CURRENT_WIDTH / 8;
		
		topbar.setTitle(sceneName);
		uic.add(topbar.getName(), topbar);

		backgroundImage = UINkImage.createNkImage("back/lobby.png");
//		topbar.setShowExitModal(true);
		
		windowRect = NkRect.create();
		nk_rect(0, topbar.getHeight(), Window.CLIENT_WIDTH, Window.CLIENT_HEIGHT - topbar.getHeight(), windowRect);

	    uic.forceFocus(sceneName);
	}

	@Override
	public void tick(double delta) {
	}

	@Override
	public boolean keyInput(int keycode, int action) {
		return false;
	}

	@Override
	public void mouseButtonInput(int button, int action, double x, double y) {
		boolean down = action != GLFW_RELEASE;

		if (down) {
			topbar.press(x, y);
			singleplayerBtn.unpress();
			multiplayerBtn.unpress();
			optionsBtn.unpress();
			exitBtn.unpress();
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
	public void destroy() {
		removeAll();
	}

	/*
	 *========= VISUALIZATION ==========
	 */
	
	
	@Override
	public void render(NkContext ctx, Renderer renderer, long window) {
//		for (GameObject go : gameObjects) {
//			renderer.renderMesh(go, camera);
//		}
		// Begin the window
		renderUIBackground(ctx);
		renderUILayout(ctx, uic);
	}

	@Override
	public void renderUILayout(NkContext ctx, UICollector uic) {

//		NkRect rect = NkRect.create();
//		nk_rect(0, 0, Window.CURRENT_WIDTH, Window.CURRENT_HEIGHT, rect);
		
		
		// Set the padding of the group
		NkVec2 padding = ctx.style().window().group_padding().set(hPadding, btnHeight);
		ctx.style().window().spacing().set(0, btnHeight / 2);

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
			
			if (nk_group_begin(ctx, "My Group", options)) {
				features.pushBackgroundColor(new ColorBytes(0x00, 0x00, 0x00, 0x00));
				features.setBackgroundColor(ctx);

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
			
			features.popBackgroundColor();
			features.setBackgroundColor(ctx);

		}
		// End the window
		nk_end(ctx);
				
	}

	@Override
	public void renderUIBackground(NkContext ctx) {
		if (nk_begin(ctx, sceneName + "background", windowRect, windowOptions)) {
			nk_layout_row_dynamic(ctx, 100, 1);
			nk_image(ctx, backgroundImage);
		}
		nk_end(ctx);		
	}

	@Override
	public void determineUIWindowFocusByMouse(double x, double y) {
		if (y > topbar.getHeight()) {
			// Give focus to the menu.
			uic.setFocus(sceneName);
		} else {
			// Give focus to the topbar.
			uic.setFocus(topbar.getName());
		}		
	}
	@Override
	public void initNuklearVisual(NkContext ctx, SceneGlobalFeatures features, String title, int x, int y, int width, int height) {
		this.features = features;
		this.sceneName = title;
		windowOptions = NK_WINDOW_NO_INPUT;
		windowRect = NkRect.create();
		nk_rect(x, y, width, height, windowRect);

		nk_begin(ctx, sceneName, windowRect, windowOptions);
	    nk_end(ctx);
		
	    uic.forceFocus(sceneName);
	}

	@Override
	public void press() {
		singleplayerBtn.press();
		multiplayerBtn.press();
		optionsBtn.press();
		exitBtn.press();
	}

}
