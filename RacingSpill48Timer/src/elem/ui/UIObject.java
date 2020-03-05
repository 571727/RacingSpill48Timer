package elem.ui;

import org.lwjgl.nuklear.NkContext;

public abstract class UIObject {

	protected String name;
	
	public UIObject() {
		this("NONAME");
	}
	
	public UIObject(String name) {
		this.name = name;
	}
	
	public abstract void layout(NkContext ctx);

	public String getName() {
		return name;
	}
}
