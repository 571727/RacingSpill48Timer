package scenes;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.nuklear.NkContext;

import elem.interactions.RegularTopBar;
import engine.graphics.Renderer;
import engine.objects.Camera;
import engine.objects.GameObject;
import engine.objects.UIObject;

public abstract class Visual {

	private static final long serialVersionUID = 2040409868497970432L;
//	protected Font font = new Font("Calibri", 0, (int) (Window.WIDTH / 47.4f));
	protected Random r = new Random();
	protected ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	protected ArrayList<UIObject> uiObjects = new ArrayList<UIObject>();
	
	public abstract void init();
	public abstract void tick(double delta);

	public void render(Renderer renderer, Camera camera, NkContext ctx) {
		for(GameObject go : gameObjects) {
			renderer.renderMesh(go, camera);
		}
		
		for(UIObject uio : uiObjects) {
			uio.layout(ctx);
		}
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
