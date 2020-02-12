package scenes;

import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_NO_INPUT;
import static org.lwjgl.nuklear.Nuklear.nk_begin;
import static org.lwjgl.nuklear.Nuklear.nk_end;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_dynamic;
import static org.lwjgl.nuklear.Nuklear.nk_rect;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkRect;
import org.lwjgl.system.MemoryStack;

import elem.ColorBytes;
import elem.interactions.RegularTopBar;
import engine.graphics.Renderer;
import engine.io.Window;
import engine.objects.Camera;
import engine.objects.GameObject;
import engine.objects.UIObject;
import main.Main;

public abstract class Visual {

	private static final long serialVersionUID = 2040409868497970432L;
	
//	protected Font font = new Font("Calibri", 0, (int) (Window.WIDTH / 47.4f));
	protected Stack<ColorBytes> backgroundColorCache = new Stack<ColorBytes>();
	protected Random rand = new Random();
	protected ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	protected ArrayList<UIObject> uiObjects = new ArrayList<UIObject>();

	private NkRect windowRect = null;
	private String windowTitle = "TITLE NOT SET";
	private int windowOptions;

	public abstract void init();

	public abstract void tick(double delta);

	protected abstract void drawUILayout(NkContext ctx, ArrayList<UIObject> uiObjects);

	protected abstract boolean begin(NkContext ctx, String windowTitle, NkRect rect, int windowOptions, long window);

	public void render(Renderer renderer, Camera camera, NkContext ctx , long window) {
		for (GameObject go : gameObjects) {
			renderer.renderMesh(go, camera);
		}
		// Begin the window
		if (begin(ctx, windowTitle, windowRect, windowOptions, window)) {

			setBackgroundColor(ctx);
			drawUILayout(ctx, uiObjects);

		}
		// End the window
		nk_end(ctx);

	}

	public void setBackgroundColor(NkContext ctx) {
		ColorBytes bg = backgroundColorCache.peek();
		ctx.style().window().fixed_background().data().color().set(bg.r(), bg.g(), bg.b(), bg.a());
	}

	public void initNuklearVisual(NkContext ctx, String title) {
		windowOptions = NK_WINDOW_NO_INPUT;
		windowRect = NkRect.create();
		nk_rect(0, 0, Window.CURRENT_WIDTH, Window.CURRENT_HEIGHT, windowRect);

		nk_begin(ctx, windowTitle, windowRect, windowOptions);
	    nk_end(ctx);
		
		this.windowTitle = Main.GAME_NAME + " " + Main.GAME_VERSION + " - " + title;
	}

	public void setNuklearOptions(int options) {
		windowOptions = options;
	}

	public void pushBackgroundColor(ColorBytes color) {
		backgroundColorCache.push(color);
	}

	public void popBackgroundColor() {
		backgroundColorCache.pop();
	}
	
	public void add(GameObject go) {
		gameObjects.add(go);
	}

	public void add(UIObject uio) {
		uiObjects.add(uio);
	}

	public void removeAll() {
		gameObjects.clear();
		uiObjects.clear();
	}


}
