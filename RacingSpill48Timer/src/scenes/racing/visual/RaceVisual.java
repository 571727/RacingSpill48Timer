package scenes.racing.visual;

import java.util.ArrayList;

import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkRect;

import engine.math.Vector3f;
import engine.objects.Model;
import engine.objects.Sprite;
import engine.objects.UIObject;
import scenes.Visual;

public class RaceVisual extends Visual {

	private Model model;
	
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

	@Override
	protected void drawUILayout(NkContext ctx, ArrayList<UIObject> uiObjects) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean begin(NkContext ctx, String windowTitle, NkRect rect, int windowOptions) {
		// TODO Auto-generated method stub
		return false;
	}


}
