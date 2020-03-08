package scenes.adt;

import java.util.Stack;

import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.Nuklear;

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
	
	public void pushBackgroundColor(NkContext ctx, ColorBytes color) {
		pushBackgroundColor(color);
		Nuklear.nk_style_push_color(ctx, ctx.style().window().fixed_background().data().color(),
				getBackgroundColorCache().peek().create());
	}

	public void pushBackgroundColor(ColorBytes color) {
		backgroundColorCache.push(color);
	}

	public void popBackgroundColor(NkContext ctx) {
		backgroundColorCache.pop();
		Nuklear.nk_style_pop_color(ctx);
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

	public Stack<ColorBytes> getBackgroundColorCache() {
		return backgroundColorCache;
	}

	
}
