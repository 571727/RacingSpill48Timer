package engine.objects;

import engine.graphics.Mesh;
import engine.graphics.Shader;
import engine.math.Vector3f;

public class GameObject {

	protected Vector3f position, rotation, scale;
	protected Mesh mesh;
	protected Shader shader;

	public GameObject(Mesh mesh, Shader shader) {
		position = new Vector3f(0, 0, 0);
		rotation = new Vector3f(0, 0, 0);
		scale = new Vector3f(1, 1, 1);
		this.mesh = mesh;
		this.shader = shader;
	}

	public GameObject(Vector3f position, Vector3f rotation, Vector3f scale, Mesh mesh, Shader shader) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.mesh = mesh;
		this.shader = shader;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public Vector3f getScale() {
		return scale;
	}

	public void setScale(Vector3f scale) {
		this.scale = scale;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}

	public Shader getShader() {
		return shader;
	}

	public void setShader(Shader shader) {
		this.shader = shader;
	}

}
