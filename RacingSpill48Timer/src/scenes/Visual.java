package scenes;

import java.util.ArrayList;
import java.util.Random;

import engine.graphics.Renderer;

public abstract class Visual {

	private static final long serialVersionUID = 2040409868497970432L;
//	protected Font font = new Font("Calibri", 0, (int) (Window.WIDTH / 47.4f));
	protected Random r = new Random();
	protected ArrayList<VisualElement> visualElements;

	public abstract void tick(double delta);

	public abstract void render(Renderer renderer);

	public void addVisualElement(VisualElement btn) {
		visualElements.add(btn);
	}

	public void removeVisualElements() {
		visualElements.clear();
	}
}
