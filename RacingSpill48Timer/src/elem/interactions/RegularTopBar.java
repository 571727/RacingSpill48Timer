package elem.interactions;

import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_BORDER;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_CLOSABLE;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_MINIMIZABLE;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_NO_INPUT;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_NO_SCROLLBAR;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_TITLE;
import static org.lwjgl.nuklear.Nuklear.nk_begin;
import static org.lwjgl.nuklear.Nuklear.nk_end;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_dynamic;
import static org.lwjgl.nuklear.Nuklear.nk_rect;

import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkRect;

import engine.io.Window;
import engine.objects.UIObject;
import main.Main;

import static org.lwjgl.glfw.GLFW.*;

public class RegularTopBar extends UIObject {

	private TopBar topbar;
	private NkRect rect;
	private String windowTitle;
	private String title;
	private int options;

	public RegularTopBar(long window, int height) {

		PressAction pressAction = (double X, double Y) -> {
			if (X > Window.CLIENT_WIDTH - (Window.CLIENT_WIDTH / 40)) {
				// FIXME exit button
			} else {
				// Move window
				topbar.setX(X);
				topbar.setY(Y);
				topbar.setHeld(true);
			}
		};

		topbar = new TopBar(window, height, pressAction);

		
		//Layout settings
		windowTitle = "topbar";
		rect = NkRect.create();
		nk_rect(0, 0, Window.CURRENT_WIDTH, height, rect);
		options = NK_WINDOW_TITLE | NK_WINDOW_BORDER | NK_WINDOW_MINIMIZABLE | NK_WINDOW_CLOSABLE
				| NK_WINDOW_NO_SCROLLBAR;
		
	}
	
	public void setTitle(String title) {
		this.title = Main.GAME_NAME + " " + Main.GAME_VERSION + " - " + title;
	}

	public void layout(NkContext ctx) {
		if (nk_begin(ctx, windowTitle, rect, options)) {
			
		}
		nk_end(ctx);
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
