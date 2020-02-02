package engine.objects;

import engine.graphics.Mesh;
import engine.graphics.Shader;
import engine.io.ModelLoader;
import engine.math.Vector3f;

public class Model extends GameObject {

	public Model(String modelPath, String texturePath, String shaderName) {
		this(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), modelPath, texturePath, shaderName);
	}

	public Model(Vector3f position, Vector3f rotation, Vector3f scale, String modelPath, String texturePath,
			String shaderName) {
		this(position, rotation, scale, ModelLoader.loadModel("resources/models/" + modelPath, "/pics/" + texturePath),
				shaderName);
	}

	private Model(Vector3f position, Vector3f rotation, Vector3f scale, Mesh mesh, String shaderName) {
		super(position, rotation, scale, mesh, new Shader(shaderName));
	}

	public void create() {
		shader.create();
		mesh.create();
	}

	public void destroy() {
		shader.destroy();
		mesh.destroy();
	}

}
