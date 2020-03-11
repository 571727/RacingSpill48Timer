package scenes.adt;

import java.util.Stack;

import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.Nuklear;

import elem.Action;
import elem.ColorBytes;
import elem.objects.GameObject;
import elem.ui.UIButton;
import elem.ui.UICollector;
import elem.ui.UIObject;

public class SceneGlobalFeatures {

	private Stack<ColorBytes> backgroundColorCache = new Stack<ColorBytes>();
	private UICollector uic = new UICollector();
	private Action pressExitModal;
	private boolean showExitModal;
	private boolean pressedHoveredButton;

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

	public void addUIC(String listname, UIObject uio) {
		uic.add(listname, uio);
	}

	public void addUIC(String listname, UIButton btn) {
		uic.add(listname, btn);
	}

	public void pressFindHoveredButtonUIC(String listname) {
		UIButton hoveredButton = uic.getHoveredButton(listname);
		runPressHoveredButtonUIC(hoveredButton);
	}

	public void runPressHoveredButtonUIC(UIButton hoveredButton) {
		if (hoveredButton != null && !pressedHoveredButton) {
			hoveredButton.runPressedAction();
			pressedHoveredButton = true;
		}
	}

	public void releaseHoveredButtonUIC() {
		pressedHoveredButton = false;
	}

	public void clearHoveredButtonUIC(String listname) {
		uic.changeHoveredButton(listname, null);
	}

	public void clearUIC() {
		uic.clear();
	}

	public UICollector getUIC() {
		return uic;
	}

}
