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

public class GlobalFeatures {

	private String readyText, optionsText, gobackText, singleplayerText, multiplayerText, exitText, exitOKText,
			exitCancelText, createOnlineText, joinOnlineText, createLANText, joinLANText, refreshText, minimizeText,
			lobbiesText, exitLabelText;

	private Stack<ColorBytes> backgroundColorCache = new Stack<ColorBytes>();
	private UICollector uic = new UICollector();
	private Action pressExitModal;
	private boolean showExitModal;
	private boolean pressedHoveredButton;

	public GlobalFeatures() {
		setLanguage(0);
	}

	public void setLanguage(int lang) {
		switch (lang) {
		case 0:
			// English
			readyText = "Ready";
			optionsText = "Options and controls";
			gobackText = "Go back";
			singleplayerText = "Singleplayer";
			multiplayerText = "Multiplayer";
			exitText = "Exit";
			exitOKText = "O K, M F";
			exitCancelText = "C A N C E L  T H A T  S H I T";
			createOnlineText = "Create online lobby";
			createLANText = "Create lobby on the LAN";
			joinOnlineText = "Join selected lobby";
			joinLANText = "Join lobby via IP";
			refreshText = "Refresh";
			minimizeText = "M I N I M I Z E";
			lobbiesText = "Lobbies";
			exitLabelText = "Sure you wanna exit?";
			break;
		case 1:
			// vietnamese

			break;
		case 2:
			// german

			break;
		}
	}

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

	public void clearUIC(String listname) {
		uic.clear(listname);
	}

	public UICollector getUIC() {
		return uic;
	}

	public String getReadyText() {
		return readyText;
	}

	public String getOptionsText() {
		return optionsText;
	}

	public String getGobackText() {
		return gobackText;
	}

	public String getSingleplayerText() {
		return singleplayerText;
	}

	public String getMultiplayerText() {
		return multiplayerText;
	}

	public String getExitText() {
		return exitText;
	}

	public String getExitOKText() {
		return exitOKText;
	}

	public String getExitCancelText() {
		return exitCancelText;
	}

	public String getCreateOnlineText() {
		return createOnlineText;
	}

	public String getJoinOnlineText() {
		return joinOnlineText;
	}

	public String getCreateLANText() {
		return createLANText;
	}

	public String getJoinLANText() {
		return joinLANText;
	}

	public String getRefreshText() {
		return refreshText;
	}

	public String getMinimizeText() {
		return minimizeText;
	}

	public String getLobbiesText() {
		return lobbiesText;
	}

	public String getExitLabelText() {
		return exitLabelText;
	}

}
