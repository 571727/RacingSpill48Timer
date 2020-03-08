package scenes.adt;

import java.util.Stack;

import org.lwjgl.nuklear.NkContext;

import elem.Action;
import elem.ColorBytes;

public class SceneGlobalFeatures {

	private Stack<ColorBytes> backgroundColorCache = new Stack<ColorBytes>();
	private Action pressExitModal;
	private boolean showExitModal;

	public void setBackgroundColor(NkContext ctx) {
		ColorBytes bg = backgroundColorCache.peek();
		ctx.style().window().fixed_background().data().color().set(bg.r(), bg.g(), bg.b(), bg.a());
	}
	
	public void pushBackgroundColor(ColorBytes color) {
		backgroundColorCache.push(color);
	}

	public void popBackgroundColor() {
		backgroundColorCache.pop();
	}

	public boolean isExitModalVisible() {
		return showExitModal;
	}

	public void showExitModal() {
		showExitModal = true;
		pressExitModal.run();
	}
	
	public void hideExitModal() {
		showExitModal = false;
	}

	public void setPressExitModal(Action pressExitModal) {
		this.pressExitModal = pressExitModal;
	}
	
}
