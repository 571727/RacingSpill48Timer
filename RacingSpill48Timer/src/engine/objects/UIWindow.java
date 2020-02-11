package engine.objects;

import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_TITLE;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_BORDER;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_MINIMIZABLE;
import static org.lwjgl.nuklear.Nuklear.nk_begin;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_dynamic;
import static org.lwjgl.nuklear.Nuklear.nk_end;

import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkRect;
import org.lwjgl.system.MemoryStack;

public class UIWindow extends UIObject{

	public UIWindow(int x, int y) {
		super(x, y);
	}
	
	@Override
	public void layout(NkContext ctx) {
		try(MemoryStack stack = MemoryStack.stackPush()) {
		    // Create a rectangle for the window
		    NkRect rect = NkRect.mallocStack(stack);
		    rect.x(50).y(50).w(300).h(200);
		    // Begin the window
		    if(nk_begin(ctx, "Window Name", rect, NK_WINDOW_TITLE|NK_WINDOW_BORDER|NK_WINDOW_MINIMIZABLE)) {
		        // Add rows here
		        float rowHeight = 50;
		        int itemsPerRow = 1;
		        nk_layout_row_dynamic(ctx, rowHeight, itemsPerRow);
		    }
		    // End the window
		    nk_end(ctx);
		}
	}

}
