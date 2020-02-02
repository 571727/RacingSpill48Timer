package engine.graphics;

import java.awt.Color;

import engine.math.Vector2f;
import engine.math.Vector3f;

public class Vertex {

	private Vector3f position, normal;
	private Color color;
	private Vector2f textureCoord;
	
	
	public Vertex(Vector3f position, Vector2f textureCoord) {
		this(position, null, textureCoord);
	}

	public Vertex(Vector3f position, Vector3f normal, Vector2f textureCoord) {
		this(position, null, normal, textureCoord);
	}
	
	public Vertex(Vector3f position, Color color, Vector3f normal, Vector2f textureCoord) {
		this.position = position;
		this.color = color;
		this.textureCoord =textureCoord;
		this.normal = normal;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Color getColor() {
		return color;
	}

	public Vector2f getTextureCoord() {
		return textureCoord;
	}

	public Vector3f getNormal() {
		return normal;
	}
	
}
