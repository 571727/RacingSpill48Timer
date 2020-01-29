package scenes.visual;

import engine.math.Vector3f;
import engine.objects.Model;
import engine.objects.Sprite;
import scenes.Visual;

public class MainMenuVisual extends Visual {
	Model model;

	@Override
	public void init() {
		Sprite object = new Sprite("e.png", "main");
		object.create();
		add(object);

		model = new Model("untitled.obj", "tireboost.png", "main");
		model.create();
		add(model);
	}

	@Override
	public void tick(double delta) {
		model.setRotation(Vector3f.addY(model.getRotation(), 1));
	}

}
