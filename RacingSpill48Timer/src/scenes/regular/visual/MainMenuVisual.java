package scenes.regular.visual;

import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_NO_SCROLLBAR;
import static org.lwjgl.nuklear.Nuklear.nk_begin;
import static org.lwjgl.nuklear.Nuklear.nk_end;
import static org.lwjgl.nuklear.Nuklear.nk_group_begin;
import static org.lwjgl.nuklear.Nuklear.nk_group_end;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_dynamic;

import java.util.ArrayList;

import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkVec2;

import elem.ColorBytes;
import elem.interactions.RegularTopBar;
import engine.io.Window;
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
		topbar.setTitle(windowTitle);
	}

	@Override
	protected void drawUILayout(NkContext ctx, ArrayList<UIObject> uios) {
		if (nk_begin(ctx, windowTitle, windowRect, windowOptions)) {

			setBackgroundColor(ctx);

			float height = 600; // The row is 30 pixels tall
			int itemsPerRow = 1; // How many widgets can go in one row

			pushBackgroundColor(new ColorBytes(0x00, 0x00, 0x00, 0x00));
			setBackgroundColor(ctx);

			nk_layout_row_dynamic(ctx, Window.CURRENT_HEIGHT, 1);
			// Groups have the same options available as windows
			int options = NK_WINDOW_NO_SCROLLBAR;
			if (nk_group_begin(ctx, "My Group", options)) {

				//
				// The group contains rows and the rows contain widgets, put those here.
				//
				for (int i = 0; i < uios.size(); i++) {
					uios.get(i).layout(ctx);
					nk_layout_row_dynamic(ctx, btnHeight, 1); // nested row
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

			// End the window
			nk_end(ctx);
		}
		
		topbar.layout(ctx);

	}

	@Override
	public void tick(double delta) {
	}

}
