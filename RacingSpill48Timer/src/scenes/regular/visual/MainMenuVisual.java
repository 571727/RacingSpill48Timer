package scenes.regular.visual;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_BORDER;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_CLOSABLE;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_MINIMIZABLE;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_NO_SCROLLBAR;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_TITLE;
import static org.lwjgl.nuklear.Nuklear.nk_begin;
import static org.lwjgl.nuklear.Nuklear.nk_button_label;
import static org.lwjgl.nuklear.Nuklear.nk_end;
import static org.lwjgl.nuklear.Nuklear.nk_group_begin;
import static org.lwjgl.nuklear.Nuklear.nk_group_end;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_dynamic;
import static org.lwjgl.nuklear.Nuklear.nk_rect;
import static org.lwjgl.nuklear.Nuklear.nk_layout_space_push;
import static org.lwjgl.nuklear.Nuklear.nk_image;
import static org.lwjgl.system.MemoryStack.stackPush;

import java.util.ArrayList;

import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkImage;
import org.lwjgl.nuklear.NkRect;
import org.lwjgl.nuklear.NkVec2;
import org.lwjgl.nuklear.Nuklear;
import org.lwjgl.system.MemoryStack;

import elem.ColorBytes;
import elem.interactions.RegularTopBar;
import elem.ui.UICollector;
import elem.ui.UIExitModal;
import elem.ui.UINkImage;
import elem.ui.UIObject;
import engine.io.Window;
import scenes.Visual;

//nk_input_begin(ctx);

//nk_layout_row_begin(ctx, NK_DYNAMIC, height, itemsPerRow);

//if (Nuklear.nk_window_is_collapsed(ctx, windowTitle)) {
//	GLFW.glfwIconifyWindow(window);
//	Nuklear.nk_window_collapse(ctx, windowTitle, Nuklear.NK_Window_);
//}
//nk_input_end(ctx);
//GLFW.glfwSetWindowShouldClose(window, true);
//nk_layout_row_push(ctx, 0.4f);

public class MainMenuVisual extends Visual {

	private int hPadding;
	private float btnHeight;
	private RegularTopBar topbar;
	private UIExitModal exitModal;
	private byte alpha = (byte) 0x22;
	private NkImage backgroundImage;

	public void init(RegularTopBar topbar, UIExitModal exitModal) {
		pushBackgroundColor(new ColorBytes(0x66, 0, 0, alpha));
		setNuklearOptions(NK_WINDOW_NO_SCROLLBAR);
		this.exitModal = exitModal;

		btnHeight = Window.CURRENT_HEIGHT / 12;
		hPadding = Window.CURRENT_WIDTH / 8;
		this.topbar = topbar;
		topbar.setTitle(sceneName);
		uic.add(topbar.getName(), topbar);

		backgroundImage = UINkImage.createNkImage("back/lobby.png");
//		topbar.setShowExitModal(true);
	}

	@Override
	public void determineUIWindowFocusByMouse(double x, double y) {
		if (exitModal.isShowExitModal()) {
			//Give all focus to the exitmodal
			uic.setFocus(exitModal.getName());
		} else if (y > topbar.getHeight()) {
			// Give focus to the menu.
			uic.setFocus(sceneName);
		} else {
			// Give focus to the topbar.
			uic.setFocus(topbar.getName());
		}
	}

	@Override
	protected void drawUILayout(NkContext ctx, UICollector uic) {

		Nuklear.nk_window_set_focus(ctx, uic.getFocus());

		NkRect rect = NkRect.create();
		nk_rect(0, 0, Window.CURRENT_WIDTH, Window.CURRENT_HEIGHT, rect);
		
		
		if (nk_begin(ctx, sceneName + "background", windowRect, windowOptions)) {
			nk_layout_row_dynamic(ctx, 100, 1);
			nk_image(ctx, backgroundImage);
		}
		nk_end(ctx);
		
		/*
		 * TOPBAR
		 */
		pushBackgroundColor(new ColorBytes(0x22, 0x22, 0x22, alpha));
		setBackgroundColor(ctx);

		topbar.layout(ctx);

		popBackgroundColor();
		setBackgroundColor(ctx);

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
				pushBackgroundColor(new ColorBytes(0x00, 0x00, 0x00, 0x00));
				setBackgroundColor(ctx);

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
			
			popBackgroundColor();
			setBackgroundColor(ctx);

		}
		// End the window
		nk_end(ctx);
		
		
		/*
		 * EXIT MODAL
		 */
		if (exitModal.isShowExitModal()) {
			pushBackgroundColor(new ColorBytes(0x00, 0x00, 0x00, 0x33));
			setBackgroundColor(ctx);

			exitModal.layout(ctx);

			popBackgroundColor();

		}
	}

	@Override
	public void tick(double delta) {
	}


}
