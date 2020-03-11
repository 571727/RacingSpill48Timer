package elem.ui;

import static org.lwjgl.nuklear.Nuklear.nk_widget_is_hovered;

import org.lwjgl.nuklear.NkColor;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkStyleButton;
import org.lwjgl.nuklear.Nuklear;

import elem.Action;
import elem.ColorBytes;

public class UIButton extends UIObject {

	private String title;
	private Action pressedAction, hoveredAction, changeHoverButtonAction;
	private boolean mouseHover, keyHover, pressed, hasRunHover, disabled; // TODO impl disabled look & func on button
	private NkColor normal, active, hover;
	private UIButton left, right, above, below;

	public UIButton(String title) {
		this.title = title;

		ColorBytes normal = new ColorBytes(0, 0, 0, 0);
		ColorBytes active = new ColorBytes(0x11, 0x11, 0x11, 0xff);
		ColorBytes hover = new ColorBytes(0x55, 0x55, 0x55, 0xdd);

		this.normal = normal.create();
		this.active = active.create();
		this.hover = hover.create();
	}

	@Override
	public void layout(NkContext ctx) {

		NkColor figuredNormalColor = null;
		ctx.style().button().hover().data().color().set(hover);
		ctx.style().button().active().data().color().set(active);

		/*
		 * Deal with hover stuff
		 */
		if (nk_widget_is_hovered(ctx)) {
			if (!mouseHover) {
				hover();

				mouseHover = true;
				keyHover = false;
			}
		} else if (mouseHover) {
			mouseHover = false;
		}

		boolean hovered = mouseHover || keyHover;

		if (hovered)
			figuredNormalColor = hover;
		else
			figuredNormalColor = normal;

		ctx.style().button().normal().data().color().set(figuredNormalColor);

		/*
		 * Deal with pressing stuff
		 */
		if (Nuklear.nk_button_label(ctx, title) && !pressed) {
			runPressedAction();
			pressed = true;
		}

	}

	public void runPressedAction() {
		if (pressedAction != null)
			pressedAction.run();
		else
			System.err.println(title + " does not have a press action");
	}

	public void runHoveredAction() {
		// Play hover sfx
		if (hoveredAction != null) {
			hoveredAction.run();
		} else
			System.err.println(title + " does not have a hover action");
	}

	public void hover() {
		if (keyHover == false) {
			keyHover = true;
			runHoveredAction();
			if (changeHoverButtonAction != null)
				changeHoverButtonAction.run();
		}
	}

	public void unhover() {
		keyHover = false;
		mouseHover = false;
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

	public void setChangeHoverButtonAction(Action changeHoverButtonAction) {
		this.changeHoverButtonAction = changeHoverButtonAction;
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

	public void setNavigations(UIButton left, UIButton right, UIButton above, UIButton below) {
		this.left = left;
		this.right = right;
		this.above = above;
		this.below = below;
	}

	public void hoverNavigate(ButtonNavigation nav) {
		switch (nav) {
		case LEFT:
			navigate(left);
			break;
		case RIGHT:
			navigate(right);
			break;
		case ABOVE:
			navigate(above);
			break;
		case BELOW:
			navigate(below);
			break;
		default:
			break;
		}
	}
	
	private void navigate(UIButton btn) {
		if(btn != null)
			btn.hover();
	}

}
