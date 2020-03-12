package elem.ui;

import static org.lwjgl.glfw.GLFW.glfwIconifyWindow;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.nuklear.Nuklear.NK_TEXT_ALIGN_LEFT;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_NO_SCROLLBAR;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_NO_INPUT;
import static org.lwjgl.nuklear.Nuklear.nk_begin;
import static org.lwjgl.nuklear.Nuklear.nk_end;
import static org.lwjgl.nuklear.Nuklear.nk_group_begin;
import static org.lwjgl.nuklear.Nuklear.nk_group_end;
import static org.lwjgl.nuklear.Nuklear.nk_label;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_dynamic;
import static org.lwjgl.nuklear.Nuklear.nk_rect;
import static org.lwjgl.nuklear.Nuklear.nk_style_pop_vec2;
import static org.lwjgl.nuklear.Nuklear.nk_style_push_vec2;

import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkRect;
import org.lwjgl.nuklear.NkVec2;
import org.lwjgl.nuklear.Nuklear;
import org.lwjgl.system.MemoryStack;

import elem.Action;
import elem.ColorBytes;
import elem.interactions.PressAction;
import engine.io.Window;
import scenes.adt.GlobalFeatures;

public class UIExitModal extends UIObject {

	private GlobalFeatures features;
	private String windowTitle;
	private String exitLabel;
	private UIButton okBtn, cancelBtn;
	private int options;
	private boolean visible;
	private boolean showExitModal;

	public UIExitModal(GlobalFeatures features, Action okAction, Action cancelAction) {
		super("ExitModal");
		windowTitle = getName();
		exitLabel = features.getExitLabelText();

		options = NK_WINDOW_NO_SCROLLBAR | NK_WINDOW_NO_INPUT;

		this.features = features;

		// Buttons
		okBtn = new UIButton(features.getExitOKText());
		cancelBtn = new UIButton(features.getExitCancelText());

		okBtn.setPressedAction(okAction);
		cancelBtn.setPressedAction(cancelAction);
		
		features.addUIC(getName(), okBtn);
		features.addUIC(getName(), cancelBtn);

	}

	@Override
	public void layout(NkContext ctx) {

		try (MemoryStack stack = MemoryStack.stackPush()) {
			// Create a rectangle for the window
			NkRect rect = NkRect.mallocStack(stack);
			rect.x(0).y(0).w(Window.CURRENT_WIDTH).h(Window.CURRENT_HEIGHT);

			features.pushBackgroundColor(ctx, new ColorBytes(0x00, 0x00, 0x00, 0x66));

			if (nk_begin(ctx, windowTitle, rect, options)) {
				// Set own custom styling
				NkVec2 spacing = NkVec2.mallocStack(stack);
				NkVec2 padding = NkVec2.mallocStack(stack);
				
				spacing.set(50, 0);
				padding.set(100, 50);
				
				nk_style_push_vec2(ctx, ctx.style().window().spacing(), spacing);
				nk_style_push_vec2(ctx, ctx.style().window().group_padding(), padding);

				int height = Window.CURRENT_HEIGHT * 2 / 5;
				int heightElements = height / 4;

				// Move group down a bit
				nk_layout_row_dynamic(ctx, height / 2, 1);

				// Height of group
				nk_layout_row_dynamic(ctx, height, 1);

				features.pushBackgroundColor(ctx, new ColorBytes(0x00, 0x00, 0x00, 0xFF));

				if (nk_group_begin(ctx, "ExitGroup", options)) {

					nk_layout_row_dynamic(ctx, heightElements, 1);
					nk_label(ctx, exitLabel, NK_TEXT_ALIGN_LEFT);

					nk_layout_row_dynamic(ctx, heightElements, 2);
					okBtn.layout(ctx);
					cancelBtn.layout(ctx);

					// Unlike the window, the _end() function must be inside the if() block
					nk_group_end(ctx);
				}

				features.popBackgroundColor(ctx);
				
				// Reset styling
				nk_style_pop_vec2(ctx);
				nk_style_pop_vec2(ctx);

			}
			nk_end(ctx);

			features.popBackgroundColor(ctx);
		}
	}

	public void unpress() {
		okBtn.unpress();
		cancelBtn.unpress();
	}

	public void press() {
		okBtn.press();
		cancelBtn.press();
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;

		if (visible) {
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

	public UIButton getOkBtn() {
		return okBtn;
	}

	public void setOkBtn(UIButton okBtn) {
		this.okBtn = okBtn;
	}

	public UIButton getCancelBtn() {
		return cancelBtn;
	}

	public void setCancelBtn(UIButton cancelBtn) {
		this.cancelBtn = cancelBtn;
	}

}
