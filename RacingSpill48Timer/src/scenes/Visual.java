package scenes;

import java.util.ArrayList;
import java.util.Random;

import engine.graphics.Renderer;
import engine.objects.Camera;
import engine.objects.GameObject;

public abstract class Visual {

	private static final long serialVersionUID = 2040409868497970432L;
//	protected Font font = new Font("Calibri", 0, (int) (Window.WIDTH / 47.4f));
	protected Random r = new Random();
	protected ArrayList<GameObject> objects = new ArrayList<GameObject>();

	public abstract void init();
	public abstract void tick(double delta);

	public void render(Renderer renderer, Camera camera) {
		for(GameObject go : objects) {
			renderer.renderMesh(go, camera);
		}
	}

	public void add(GameObject go) {
		objects.add(go);
	}

	public void removeAll() {
		objects.clear();
	}
}
