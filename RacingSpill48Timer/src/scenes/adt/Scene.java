package scenes.adt;

import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_NO_INPUT;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_NO_SCROLLBAR;
import static org.lwjgl.nuklear.Nuklear.nk_begin;
import static org.lwjgl.nuklear.Nuklear.nk_end;
import static org.lwjgl.nuklear.Nuklear.nk_image;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_dynamic;
import static org.lwjgl.nuklear.Nuklear.nk_style_pop_vec2;
import static org.lwjgl.nuklear.Nuklear.nk_style_push_vec2;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkImage;
import org.lwjgl.nuklear.NkRect;
import org.lwjgl.nuklear.NkVec2;
import org.lwjgl.nuklear.Nuklear;
import org.lwjgl.system.MemoryStack;

import elem.interactions.TopbarInteraction;
import elem.objects.Camera;
import elem.objects.GameObject;
import elem.ui.ButtonNavigation;
import elem.ui.UIButton;
import elem.ui.UICollector;
import elem.ui.UIObject;
import engine.io.Window;

public abstract class Scene implements ISceneManipulation {

	protected Random rand = new Random();
	protected SceneChangeAction sceneChange;
	protected SceneGlobalFeatures features;
	protected Camera camera;
	protected ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	protected String sceneName = "TITLE NOT SET";
	protected NkImage backgroundImage;

	public Scene(SceneGlobalFeatures features, Camera camera, String sceneName) {
		this.sceneName = sceneName;
		this.camera = camera;
		this.features = features;
	}

	public Scene(SceneGlobalFeatures features, Camera camera, String sceneName, NkContext ctx, long window, int x,
			int y, int width, int height) {
		this(features, camera, sceneName);
		init(ctx, x, y, width, height);
	}

	public void setSceneChangeAction(SceneChangeAction sceneChange) {
		this.sceneChange = sceneChange;
	}

	public abstract void renderUILayout(NkContext ctx, UICollector uic);

	public void renderUIBackground(NkContext ctx) {

		try (MemoryStack stack = MemoryStack.stackPush()) {
			NkRect rect = NkRect.mallocStack(stack);
			rect.x(-4).y(-4).w(Window.CURRENT_WIDTH + 8).h(Window.CURRENT_HEIGHT + 8);

			if (nk_begin(ctx, sceneName + "background", rect,
					NK_WINDOW_NO_SCROLLBAR | NK_WINDOW_NO_INPUT | Nuklear.NK_WINDOW_BACKGROUND)) {

				// Set own custom styling
				NkVec2 spacing = NkVec2.mallocStack(stack);
				NkVec2 padding = NkVec2.mallocStack(stack);

//					spacing.set(0, 0);
				padding.set(0, 0);

				nk_style_push_vec2(ctx, ctx.style().window().spacing(), spacing);
				nk_style_push_vec2(ctx, ctx.style().window().padding(), padding);

				// ctx.style().window().border(0);

				nk_layout_row_dynamic(ctx, Window.CURRENT_HEIGHT, 1);
				nk_image(ctx, backgroundImage);

				nk_style_pop_vec2(ctx);
				nk_style_pop_vec2(ctx);
			}
		}
		nk_end(ctx);

	}

	protected void generalHoveredButtonNavigation(UIButton hoveredButton, int keycode) {
		switch (keycode) {
		case GLFW.GLFW_KEY_LEFT:
			hoveredButton.hoverNavigate(ButtonNavigation.LEFT);
			break;
		case GLFW.GLFW_KEY_RIGHT:
			hoveredButton.hoverNavigate(ButtonNavigation.RIGHT);
			break;
		case GLFW.GLFW_KEY_UP:
			hoveredButton.hoverNavigate(ButtonNavigation.ABOVE);
			break;
		case GLFW.GLFW_KEY_DOWN:
			hoveredButton.hoverNavigate(ButtonNavigation.BELOW);
			break;
		case GLFW.GLFW_KEY_ENTER:
			features.runPressHoveredButtonUIC(hoveredButton);
			break;
		}
	}

	public abstract void determineUIWindowFocusByMouse(double x, double y);

	public abstract void update();

	protected abstract void init(NkContext ctx, int x, int y, int width, int height);

	public abstract void destroy();

	public abstract void press();

	public abstract UIObject getTopbar();

	public void add(GameObject go) {
		gameObjects.add(go);
	}

	public void add(String listname, UIObject uio) {
		features.addUIC(listname, uio);
	}

	public void add(String listname, UIButton btn) {
		features.addUIC(listname, btn);
	}

	public void removeAll() {
		gameObjects.clear();
		features.clearUIC();
	}

}
