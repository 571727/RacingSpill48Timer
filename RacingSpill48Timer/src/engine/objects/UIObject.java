package engine.objects;

import org.lwjgl.nuklear.NkContext;

public abstract class UIObject {

	public abstract void layout(NkContext ctx, int x, int y);
}
