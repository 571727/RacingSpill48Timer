package elem.ui;

import java.util.HashMap;

public class UICollector {

	private HashMap<String, UIWindowObjects> mapUI = new HashMap<String, UIWindowObjects>();
	private String focus = "";

	public void add(String listname, UIObject o) {
		if (!mapUI.containsKey(listname)) {
			mapUI.put(listname, new UIWindowObjects());
		}

		mapUI.get(listname).add(o);
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

	public boolean isFocus(String listname) {
		return focus.equals(listname);
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
			focus = listname;
	}

	public void forceFocus(String listname) {
		focus = listname;
	}

	public String getFocus() {
		return focus;
	}

}
