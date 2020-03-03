package engine.objects;

import static org.lwjgl.nuklear.Nuklear.nk_button_label;
import static org.lwjgl.nuklear.Nuklear.nk_widget_is_hovered;

import org.lwjgl.nuklear.NkContext;

import elem.Action;

public class UIButton extends UIObject {

	private String title;
	private Action pressedAction;
	private boolean hovered, pressed;
	private Action hoveredAction;

	public UIButton(String title) {
		this.title = title;
	}

	@Override
	public void layout(NkContext ctx) {

		if (nk_widget_is_hovered(ctx)) {
			if (!hovered) {
				hovered = true;

				// Play hover sfx
				if (hoveredAction != null)
					hoveredAction.run();
				else
					System.err.println(title + " does not have a hover action");
			}
		} else {
			hovered = false;
		}

		if (nk_button_label(ctx, title) && !pressed) {

			//
			// Action to be performed when the button is clicked
			//
			if (pressedAction != null)
				pressedAction.run();
			else
				System.err.println(title + " does not have a press action");
			pressed = true;

		}

	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPressedAction(Action action) {
		this.pressedAction = action;
	}

	public void setHoverAction(Action action) {
		this.hoveredAction = action;
	}

	public boolean isPressed() {
		return pressed;
	}

	public void unpress() {
		pressed = false;
	}
	
	public void press() {
		pressed = true;
	}

}
