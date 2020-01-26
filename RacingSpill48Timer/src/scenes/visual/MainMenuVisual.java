package scenes.visual;

import engine.objects.Sprite;
import scenes.Visual;

public class MainMenuVisual extends Visual{

	@Override
	public void init() {
		Sprite object = new Sprite("tireboost.png", "main");
		object.create();
		add(object);
	}
	
	@Override
	public void tick(double delta) {
		// TODO Auto-generated method stub
		
	}

}
