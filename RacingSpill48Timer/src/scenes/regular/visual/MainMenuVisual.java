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
import static org.lwjgl.system.MemoryStack.stackPush;

import java.util.ArrayList;

import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkRect;
import org.lwjgl.nuklear.NkVec2;
import org.lwjgl.nuklear.Nuklear;
import org.lwjgl.system.MemoryStack;

import elem.ColorBytes;
import elem.interactions.RegularTopBar;
import engine.io.Window;
import engine.objects.UICollector;
import engine.objects.UIObject;
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

	public void init(RegularTopBar topbar) {
		pushBackgroundColor(new ColorBytes(0, 0, 0, 0x66));
		setNuklearOptions(NK_WINDOW_NO_SCROLLBAR);

		btnHeight = Window.CURRENT_HEIGHT / 12;
		hPadding = Window.CURRENT_WIDTH / 8;
		this.topbar = topbar;
		topbar.setTitle(sceneName);
		uic.add(topbar.getName(), topbar);
	}
	
	@Override
	public void determineUIWindowFocusByMouse(double x, double y) {
		if(y > topbar.getHeight()) {
			// Give focus to the menu.
			uic.setFocus(sceneName);
		} else {
			// Give focus to the topbar.
			uic.setFocus(topbar.getName());
		}
	}

	@Override
	protected void drawUILayout(NkContext ctx, UICollector uic) {

		topbar.layout(ctx);

		Nuklear.nk_window_set_focus(ctx, uic.getFocus());
		
		if (nk_begin(ctx, sceneName, windowRect, windowOptions)) {

			setBackgroundColor(ctx);

			pushBackgroundColor(new ColorBytes(0x00, 0x00, 0x00, 0x00));
			setBackgroundColor(ctx);

			nk_layout_row_dynamic(ctx, Window.CURRENT_HEIGHT - topbar.getHeight(), 1);
			// Groups have the same options available as windows
			int options = NK_WINDOW_NO_SCROLLBAR;

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

			// Set the padding of the group
			NkVec2 padding = ctx.style().window().group_padding();
			padding.x(hPadding);
			padding.y(btnHeight);
			ctx.style().window().spacing().y(btnHeight / 2);

			popBackgroundColor();
			setBackgroundColor(ctx);

		}
		// End the window
		nk_end(ctx);

	}

	@Override
	public void tick(double delta) {
	}





}
