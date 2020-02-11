package engine.objects;

import org.lwjgl.nuklear.NkContext;

public abstract class UIObject {

	protected int x, y;
	
	public UIObject(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public abstract void layout(NkContext ctx);
}
