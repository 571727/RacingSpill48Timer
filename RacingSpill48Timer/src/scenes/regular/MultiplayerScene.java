package scenes.regular;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_NO_SCROLLBAR;
import static org.lwjgl.nuklear.Nuklear.nk_begin;
import static org.lwjgl.nuklear.Nuklear.nk_end;
import static org.lwjgl.nuklear.Nuklear.nk_group_begin;
import static org.lwjgl.nuklear.Nuklear.nk_group_end;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_dynamic;
import static org.lwjgl.nuklear.Nuklear.nk_rect;
import static scenes.game.multiplayer.GameType.SINGLEPLAYER;

import java.util.ArrayList;

import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkRect;
import org.lwjgl.nuklear.NkVec2;
import org.lwjgl.nuklear.Nuklear;
import org.lwjgl.system.MemoryStack;

import elem.ColorBytes;
import elem.interactions.RegularTopbar;
import elem.ui.UIButton;
import elem.ui.UICollector;
import elem.ui.UINkImage;
import elem.ui.UIObject;
import engine.graphics.Renderer;
import engine.io.Window;
import main.Main;
import scenes.adt.Scene;
import scenes.Scenes;
import scenes.adt.GlobalFeatures;
import scenes.game.GameScene;
import scenes.game.multiplayer.GameType;

public class MultiplayerScene extends Scene {

	private UIButton joinOnlineBtn, createOnlineBtn, joinLANBtn, createLANBtn, gobackBtn, refreshBtn;
	private ArrayList<UIButton> lobbiesBtns;
	private RegularTopbar topbar;

	private int windowOptions;
	private int btnHeight;
	private int hPadding;
	private NkRect windowRect;
	private String lobbiesTitle;

	public MultiplayerScene(GlobalFeatures features, RegularTopbar topbar, NkContext ctx, long window) {
		super(features, null, "Multiplayer", ctx, window, 0, topbar.getHeight(), Window.CURRENT_WIDTH,
				Window.CURRENT_HEIGHT - topbar.getHeight());

		this.topbar = topbar;
		lobbiesTitle = features.getLobbiesText();
		lobbiesBtns = new ArrayList<UIButton>();

		joinOnlineBtn = new UIButton(features.getJoinOnlineText());
		createOnlineBtn = new UIButton(features.getCreateOnlineText());
		joinLANBtn = new UIButton(features.getJoinLANText());
		createLANBtn = new UIButton(features.getCreateLANText());
		gobackBtn = new UIButton(features.getGobackText());
		refreshBtn = new UIButton(features.getRefreshText());

		joinOnlineBtn.setPressedAction(() -> {
			sceneChange.run(Scenes.MAIN_MENU, true);
		});
		createOnlineBtn.setPressedAction(() -> {
			sceneChange.run(Scenes.MAIN_MENU, true);
		});
		joinLANBtn.setPressedAction(() -> {
			sceneChange.run(Scenes.MAIN_MENU, true);
		});
		createLANBtn.setPressedAction(() -> {
			GameScene game = (GameScene) sceneChange.run(Scenes.GAME, false);
			game.initGame(GameType.SINGLEPLAYER);
			sceneUpdate.run();
		});
		gobackBtn.setPressedAction(() -> {
			sceneChange.run(Scenes.MAIN_MENU, true);
		});
		refreshBtn.setPressedAction(() -> {
			sceneChange.run(Scenes.MAIN_MENU, true);
		});

		add(sceneName, joinOnlineBtn);
		add(sceneName, createOnlineBtn);
		add(sceneName, joinLANBtn);
		add(sceneName, createLANBtn);
		add(sceneName, gobackBtn);
		add(sceneName, refreshBtn);
		
		joinOnlineBtn.setNavigations(null, createOnlineBtn, null, joinLANBtn);
		createOnlineBtn.setNavigations(joinOnlineBtn, null, null, createLANBtn);
		joinLANBtn.setNavigations(null, createLANBtn, joinOnlineBtn, gobackBtn);
		createLANBtn.setNavigations(joinLANBtn, null, createOnlineBtn, refreshBtn);
		gobackBtn.setNavigations(null, refreshBtn, joinLANBtn, null);
		refreshBtn.setNavigations(gobackBtn, null, createLANBtn, null);
		
		
		// TODO fake TEST LOBBYS
		
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
				hoveredButton = joinOnlineBtn;
			
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
			for (UIButton btn : lobbiesBtns) {
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
		ctx.style().window().spacing().set(0, btnHeight / 2);

		features.setBackgroundColor(ctx);
		/*
		 * MAIN SHIT
		 */
		try (MemoryStack stack = MemoryStack.stackPush()) {
			if (nk_begin(ctx, sceneName, windowRect, windowOptions)) {
				
				/*
				 * Lobbies
				 */

				nk_layout_row_dynamic(ctx, Window.CURRENT_HEIGHT / 2, 1);

				// Groups have the same options available as windows
				int options = Nuklear.NK_WINDOW_BORDER | Nuklear.NK_WINDOW_TITLE;

				features.pushBackgroundColor(ctx, new ColorBytes(0x00, 0x00, 0x00, 0x66));

				if (nk_group_begin(ctx, lobbiesTitle, options)) {

					for (int i = 0; i < uic.size(lobbiesTitle); i++) {
						nk_layout_row_dynamic(ctx, btnHeight, 1); // nested row
						uic.get(lobbiesTitle, i).layout(ctx);
					}

					nk_group_end(ctx);
				}

				features.popBackgroundColor(ctx);

				/*
				 * Buttons
				 */

				NkVec2 spacing = NkVec2.mallocStack(stack);
				NkVec2 padding = NkVec2.mallocStack(stack);

				spacing.set(btnHeight / 2, btnHeight / 2);
				padding.set(btnHeight / 2, 0);

				Nuklear.nk_style_push_vec2(ctx, ctx.style().window().spacing(), spacing);
				Nuklear.nk_style_push_vec2(ctx, ctx.style().window().padding(), padding);

				nk_layout_row_dynamic(ctx, btnHeight, 2);
				joinOnlineBtn.layout(ctx);
				createOnlineBtn.layout(ctx);

				nk_layout_row_dynamic(ctx, btnHeight, 2);
				joinLANBtn.layout(ctx);
				createLANBtn.layout(ctx);

				nk_layout_row_dynamic(ctx, btnHeight, 2);
				gobackBtn.layout(ctx);
				refreshBtn.layout(ctx);

				Nuklear.nk_style_pop_vec2(ctx);
				Nuklear.nk_style_pop_vec2(ctx);

			}
			// End the window
			nk_end(ctx);
		}
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
		for (UIButton btn : lobbiesBtns) {
			btn.press();
		}
	}

	@Override
	public UIObject getTopbar() {
		return topbar;
	}

}
