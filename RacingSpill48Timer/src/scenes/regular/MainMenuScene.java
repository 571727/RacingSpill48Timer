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
import static org.lwjgl.nuklear.Nuklear.nk_style_pop_vec2;
import static org.lwjgl.nuklear.Nuklear.nk_style_push_vec2;

import org.lwjgl.nuklear.NkColor;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkImage;
import org.lwjgl.nuklear.NkRect;
import org.lwjgl.nuklear.NkVec2;
import org.lwjgl.nuklear.NkWindow;
import org.lwjgl.nuklear.Nuklear;
import org.lwjgl.system.MemoryStack;

import elem.ColorBytes;
import elem.interactions.RegularTopbar;
import elem.objects.GameObject;
import engine.graphics.Renderer;
import engine.io.Window;
import elem.ui.UIButton;
import elem.ui.UICollector;
import elem.ui.UIExitModal;
import elem.ui.UINkImage;
import elem.ui.UIObject;
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
	private RegularTopbar topbar;

	private int windowOptions;
	private int btnHeight;
	private int hPadding;
	private NkRect windowRect;

	public MainMenuScene(SceneGlobalFeatures features, RegularTopbar topbar, NkContext ctx, long window) {
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
		
		update();
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
	 * ========= VISUALIZATION ==========
	 */

	@Override
	public void render(NkContext ctx, Renderer renderer, long window) {
//		for (GameObject go : gameObjects) {
//			renderer.renderMesh(go, camera);
//		}
		// Begin the window
		renderUILayout(ctx, uic);
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
	public void update() {
		topbar.setTitle(sceneName);
	}

	@Override
	public void init(NkContext ctx, int x, int y, int width, int height) {
		features.pushBackgroundColor(new ColorBytes(0, 0, 0, 0));
		backgroundImage = UINkImage.createNkImage("back/lobby.png");

		btnHeight = Window.CURRENT_HEIGHT / 12;
		hPadding = Window.CURRENT_WIDTH / 8;

		windowOptions = NK_WINDOW_NO_SCROLLBAR | NK_WINDOW_NO_INPUT;
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

	@Override
	public UIObject getTopbar() {
		return topbar;
	}

}
