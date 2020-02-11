package engine.objects;

import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkRect;
import org.lwjgl.system.MemoryStack;

import elem.Action;

import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_NO_INPUT;
import static org.lwjgl.nuklear.Nuklear.nk_begin;
import static org.lwjgl.nuklear.Nuklear.nk_button_label;
import static org.lwjgl.nuklear.Nuklear.nk_end;

public class UIButton extends UIObject {

	private String title;
	private Action action;

	public UIButton(String title) {
		this.title = title;
	}

	@Override
	public void layout(NkContext ctx) {
		if (nk_button_label(ctx, title)) {
			//
			// Action to be performed when the button is clicked
			//
			if (action != null)
				action.run();
			else
				System.err.println(title + " does not have a action");
		}
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setAction(Action action) {
		this.action = action;
	}
	
}
