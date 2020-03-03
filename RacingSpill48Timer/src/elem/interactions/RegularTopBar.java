package elem.interactions;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwIconifyWindow;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_NO_SCROLLBAR;
import static org.lwjgl.nuklear.Nuklear.NK_TEXT_ALIGN_LEFT;
import static org.lwjgl.nuklear.Nuklear.NK_STATIC;
import static org.lwjgl.nuklear.Nuklear.nk_begin;
import static org.lwjgl.nuklear.Nuklear.nk_button_label;
import static org.lwjgl.nuklear.Nuklear.nk_end;
import static org.lwjgl.nuklear.Nuklear.nk_group_begin;
import static org.lwjgl.nuklear.Nuklear.nk_group_end;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_dynamic;
import static org.lwjgl.nuklear.Nuklear.nk_rect;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_static;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_begin;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_push;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_end;

import static org.lwjgl.nuklear.Nuklear.nk_label;

import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkRect;
import org.lwjgl.nuklear.NkVec2;

import engine.io.Window;
import engine.objects.UIButton;
import engine.objects.UIObject;
import main.Main;

public class RegularTopBar extends UIObject {

	private TopBar topbar;
	private String windowTitle;
	private String title;
	private NkRect rect;
	private UIButton minimizeBtn;
	private int options;

	public RegularTopBar(long window, int height) {

		PressAction pressAction = (double X, double Y) -> {
			// Move window
			topbar.setX(X);
			topbar.setY(Y);
			topbar.setHeld(true);
		};

		topbar = new TopBar(window, height, pressAction);

		// Layout settings
		windowTitle = "topbar";
		rect = NkRect.create();
		nk_rect(0, 0, Window.CURRENT_WIDTH, height, rect);
		options = NK_WINDOW_NO_SCROLLBAR;

		// Buttons
		minimizeBtn = new UIButton("M I N I M I Z E");

		minimizeBtn.setPressedAction(() -> glfwIconifyWindow(topbar.getWindow()));
	
	}

	public void setTitle(String title) {
		this.title = Main.GAME_NAME + " " + Main.GAME_VERSION + " - " + title;
	}

	public void layout(NkContext ctx) {
		if (nk_begin(ctx, windowTitle, rect, options)) {

			
			int height = getHeight() * 3 / 4;
			ctx.style().window().spacing().set(height, 0);
			ctx.style().window().padding().set(height, (int)(height / 3 * 0.65));
			nk_layout_row_dynamic(ctx, height, 3);

			nk_label(ctx, title, NK_TEXT_ALIGN_LEFT);

			//Empty space
			nk_label(ctx, "", NK_TEXT_ALIGN_LEFT);
			
			minimizeBtn.layout(ctx);
			
			
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

	public int getHeight() {
		return topbar.getHeight();
	}

	public String getName() {
		return windowTitle;
	}

	public void unpress() {
		minimizeBtn.unpress();
	}
}
