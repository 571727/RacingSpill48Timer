package scenes.regular.visual;

import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_TITLE;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_BORDER;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_MINIMIZABLE;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_CLOSABLE;
import static org.lwjgl.nuklear.Nuklear.NK_STATIC;
import static org.lwjgl.nuklear.Nuklear.NK_DYNAMIC;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_NO_SCROLLBAR;
import static org.lwjgl.nuklear.Nuklear.nk_begin;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_dynamic;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_begin;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_end;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_push;
import static org.lwjgl.nuklear.Nuklear.nk_group_begin;
import static org.lwjgl.nuklear.Nuklear.nk_group_end;

import java.util.ArrayList;

import org.lwjgl.nuklear.NkColor;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkRect;
import org.lwjgl.nuklear.NkVec2;

import elem.ColorBytes;
import engine.io.Window;
import engine.objects.UIObject;
import scenes.Visual;

public class MainMenuVisual extends Visual {

	private int hPadding;
	private float btnHeight;

	@Override
	public void init() {
		pushBackgroundColor(new ColorBytes(0, 0, 0, 0x66));
		setNuklearOptions(NK_WINDOW_TITLE | NK_WINDOW_BORDER | NK_WINDOW_MINIMIZABLE | NK_WINDOW_CLOSABLE | NK_WINDOW_NO_SCROLLBAR);

		btnHeight = Window.CURRENT_HEIGHT / 12;
		hPadding = Window.CURRENT_WIDTH / 8;
	}

	@Override
	protected void drawUILayout(NkContext ctx, ArrayList<UIObject> uios) {
		float height = 600; // The row is 30 pixels tall
		int itemsPerRow = 1; // How many widgets can go in one row

		pushBackgroundColor(new ColorBytes(0x00, 0x00, 0x00, 0x00));
		setBackgroundColor(ctx);
		
//		nk_layout_row_begin(ctx, NK_DYNAMIC, height, itemsPerRow);
		nk_layout_row_dynamic(ctx, Window.CURRENT_HEIGHT, 1);
		// Groups have the same options available as windows
		int options = NK_WINDOW_NO_SCROLLBAR;
		if(nk_group_begin(ctx, "My Group", options)) {

		  //
		  // The group contains rows and the rows contain widgets, put those here.
		  //
			for (int i = 0; i < uios.size(); i++) {
//				nk_layout_row_push(ctx, 0.4f);
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
		
	}

	@Override
	public void tick(double delta) {
	}

	@Override
	protected boolean begin(NkContext ctx, String windowTitle, NkRect rect, int windowOptions) {
		return nk_begin(ctx, windowTitle, rect, windowOptions);
	}

}
