package engine.objects;

import static org.lwjgl.glfw.GLFW.glfwIconifyWindow;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.nuklear.Nuklear.NK_TEXT_ALIGN_LEFT;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_NO_SCROLLBAR;
import static org.lwjgl.nuklear.Nuklear.nk_begin;
import static org.lwjgl.nuklear.Nuklear.nk_end;
import static org.lwjgl.nuklear.Nuklear.nk_group_begin;
import static org.lwjgl.nuklear.Nuklear.nk_group_end;
import static org.lwjgl.nuklear.Nuklear.nk_label;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_dynamic;
import static org.lwjgl.nuklear.Nuklear.nk_rect;

import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkRect;
import org.lwjgl.nuklear.NkVec2;
import org.lwjgl.nuklear.Nuklear;

import elem.Action;
import elem.interactions.PressAction;
import engine.io.Window;

public class UIExitModal extends UIObject{

	private String windowTitle;
	private String exitLabel;
	private UIButton okBtn, cancelBtn;
	private NkRect rect;
	private int options;
	private boolean visible;
	private boolean showExitModal;
	
	public UIExitModal(Action okAction, Action cancelAction) {
		super("ExitModal");
		windowTitle = getName();
		exitLabel = "Sure you wanna to exit?";
		
		rect = NkRect.create();
		nk_rect(0, 0, Window.CURRENT_WIDTH, Window.CURRENT_HEIGHT, rect);
		options = NK_WINDOW_NO_SCROLLBAR;

		//Buttons
		okBtn = new UIButton("O K,  M F");
		cancelBtn = new UIButton("C A N C E L  T H A T  S H I T");
		
		okBtn.setPressedAction(okAction);
		cancelBtn.setPressedAction(cancelAction);

	}
	
	@Override
	public void layout(NkContext ctx) {
		if (nk_begin(ctx, windowTitle, rect, options)) {

			
			NkVec2 padding = ctx.style().window().group_padding();
			padding.x(100);
			padding.y(50);
			
			int height = Window.CURRENT_HEIGHT * 2/ 5;
			int heightElements =  height / 4;
			
			// Move group down a bit
			nk_layout_row_dynamic(ctx, height / 2, 1);
			
			//Height of group
			nk_layout_row_dynamic(ctx, height, 1);

			if (nk_group_begin(ctx, "ExitGroup", options)) {

				nk_layout_row_dynamic(ctx, heightElements, 1);

				nk_label(ctx, exitLabel, NK_TEXT_ALIGN_LEFT);
				
				nk_layout_row_dynamic(ctx, heightElements, 2);
				okBtn.layout(ctx);
				cancelBtn.layout(ctx);

				// Unlike the window, the _end() function must be inside the if() block
				nk_group_end(ctx);
			}
			
		}
		nk_end(ctx);
	}

	public void unpress() {
		okBtn.unpress();
		cancelBtn.unpress();
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
		
		if(visible) {
			okBtn.press();
			cancelBtn.press();
		}
	}
	
	public boolean isShowExitModal() {
		return showExitModal;
	}

	public void setShowExitModal(boolean showExitModal) {
		this.showExitModal = showExitModal;
	}

}
