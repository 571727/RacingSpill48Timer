package elem.interactions;

import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_NO_INPUT;
import static org.lwjgl.nuklear.Nuklear.nk_begin;
import static org.lwjgl.nuklear.Nuklear.nk_end;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_dynamic;

import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkRect;
import org.lwjgl.system.MemoryStack;

import engine.io.Window;
import engine.objects.UIObject;
import static org.lwjgl.glfw.GLFW.*;

public class RegularTopBar extends UIObject {

	private TopBar topbar;

	public RegularTopBar(int x, int y, long window, int height) {
		super(x, y);

		PressAction pressAction = (double X, double Y) -> {
			if (X > Window.CLIENT_WIDTH - (Window.CLIENT_WIDTH / 40)) {
				// FIXME exit button
				glfwSetWindowShouldClose(window, true);
			} else {
				// Move window
				topbar.setX(X);
				topbar.setY(Y);
				topbar.setHeld(true);
			}
		};
		
		topbar = new TopBar(window, height, pressAction);

	}

	public void layout(NkContext ctx) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			// Create a rectangle for the window
			NkRect rect = NkRect.mallocStack(stack);
			rect.x(x).y(y).w(Window.CURRENT_WIDTH).h(topbar.getHeight());
			// Begin the window
			if (nk_begin(ctx, "Window Name", rect, NK_WINDOW_NO_INPUT)) {
				// Add rows here
				float rowHeight = 50;
				int itemsPerRow = 1;
				nk_layout_row_dynamic(ctx, rowHeight, itemsPerRow);
			}
			// End the window
			nk_end(ctx);
		}
	}

	public void press(double x, double y) {
		topbar.press(x, y);
	}

	public void release() {
		topbar.release();
	}

	public void move(double x, double y) {
		topbar.move(x, y);
	}

}
