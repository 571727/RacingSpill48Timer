package scenes.visual;

import engine.graphics.Mesh;
import engine.graphics.Shader;
import engine.graphics.Texture;
import engine.graphics.Vertex;
import engine.io.ModelLoader;
import engine.math.Vector2f;
import engine.math.Vector3f;
import engine.objects.GameObject;
import engine.objects.Sprite;
import scenes.Visual;

public class MainMenuVisual extends Visual {
	GameObject go;

	@Override
	public void init() {
		Sprite object = new Sprite("e.png", "main");
		object.create();
		add(object);

		Mesh thing = null;
		try {
			thing = ModelLoader.loadModel("resources/models/untitled.obj", "/pics/tireboost.png");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		String shaderName = "main";
		Shader shader = new Shader("/shaders/" + shaderName + "Vertex.glsl",
				"/shaders/" + shaderName + "Fragment.glsl");
		thing.create();
		shader.create();
		go = new GameObject(thing, shader);
		add(go);
	}

	@Override
	public void tick(double delta) {
		go.setRotation(Vector3f.addY(go.getRotation(), 1));
	}

}
