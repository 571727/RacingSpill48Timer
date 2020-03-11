package elem.ui;

import java.util.HashMap;

public class UICollector {

	// Holder alle ui elementer i ett vindu
	private HashMap<String, UIWindowObjects> mapUI = new HashMap<String, UIWindowObjects>();
	private String windowFocus = "";

	// Holder en referanse til en knapp per vindu. Kan være null. Send inn actions
	// til knappene når de addes inn i systemet, slik at de kan selv si ifra om de
	// er hovered av mus eller ikke.
	private HashMap<String, UIButton> hoveredButton = new HashMap<String, UIButton>();

	/**
	 * Adds object to a focusable window if the object does not already exist.
	 */
	public void add(String listname, UIObject o) {
		if (!mapUI.containsKey(listname)) {
			mapUI.put(listname, new UIWindowObjects());
		}

		mapUI.get(listname).add(o);
	}

	/**
	 * Adds object like normal, but since it is a button it designates extra actions
	 * for changes in hoverstate
	 */
	public void add(String listname, UIButton btn) {
		add(listname, (UIObject) btn);
		btn.setChangeHoverButtonAction(() -> changeHoveredButton(listname, btn));
	}

	public void changeHoveredButton(String listname, UIButton newButton) {
		if (hoveredButton.containsKey(listname)) {
			UIButton lastButton = hoveredButton.get(listname);

			// Run actions
			lastButton.unhover();

			hoveredButton.remove(listname);
		}
		
		if (newButton != null)
			hoveredButton.put(listname, newButton);
	}

	public void remove(String listname, UIObject o) {
		UIWindowObjects uios = mapUI.get(listname);
		if (uios != null)
			uios.remove(o);
	}

	public UIObject get(String listname, int i) {
		UIWindowObjects uios = mapUI.get(listname);
		if (uios != null)
			return uios.get(i);
		return null;
	}

	public UIObject getFirst(String listname) {
		UIWindowObjects uios = mapUI.get(listname);
		if (uios != null)
			return uios.getFirst();
		return null;
	}

	public UIButton getHoveredButton(String listname) {
		return hoveredButton.get(listname);
	}

	public boolean isFocus(String listname) {
		return windowFocus.equals(listname);
	}

	public int size(String listname) {
		UIWindowObjects uios = mapUI.get(listname);
		if (uios != null)
			return uios.size();
		return -1;
	}

	public void clear() {
		mapUI.clear();
	}

	public void setFocus(String listname) {
		UIWindowObjects uios = mapUI.get(listname);
		if (uios != null)
			windowFocus = listname;
	}

	public void forceFocus(String listname) {
		windowFocus = listname;
	}

	public String getFocus() {
		return windowFocus;
	}

}
