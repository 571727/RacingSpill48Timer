package engine.objects;

import java.util.ArrayList;

public class UIWindowObjects {

	private boolean focus;
	private ArrayList<UIObject> uios = new ArrayList<UIObject>();
	
	public void add(UIObject o) {
		uios.add(o);
	}

	public void remove(UIObject o) {
		uios.remove(o);
	}
	
	public UIObject get(int i) {
		return uios.get(i);
	}
	
	public UIObject getFirst() {
		return uios.get(0);
	}

	public boolean isFocus() {
		return focus;
	}

	public void setFocus(boolean focus) {
		this.focus = focus;
	}

	public int size() {
		return uios.size();
	}
	
	
}
