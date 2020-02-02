package engine.objects;

import java.awt.Color;

import engine.graphics.Mesh;
import engine.graphics.Shader;
import engine.graphics.Vertex;
import engine.math.Vector2f;
import engine.math.Vector3f;
import engine.graphics.Texture;

public class Sprite extends GameObject {

	public Sprite(String spritePath, String shaderName) {
		this(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), spritePath, shaderName);
	}

	public Sprite(Vector3f position, Vector3f rotation, Vector3f scale, String spritePath, String shaderName) {
		this(position, rotation, scale, new Texture("/pics/" + spritePath), shaderName);
	}

	private Sprite(Vector3f position, Vector3f rotation, Vector3f scale, Texture sprite, String shaderName) {
		super(position, rotation, scale,
				new Mesh(new Vertex[] {
						new Vertex(new Vector3f(-0.5f * sprite.widthHeightRatio(), 0.5f, 0.0f),
								new Color(1.0f, 0.0f, 0.0f), null, new Vector2f(0.0f, 0.0f)),
						new Vertex(new Vector3f(-0.5f * sprite.widthHeightRatio(), -0.5f, 0.0f),
								new Color(0.0f, 1.0f, 0.0f), null, new Vector2f(0f, 1f)),
						new Vertex(new Vector3f(0.5f * sprite.widthHeightRatio(), -0.5f, 0.0f),
								new Color(0.0f, 0.0f, 1.0f), null, new Vector2f(1f, 1f)),
						new Vertex(new Vector3f(0.5f * sprite.widthHeightRatio(), 0.5f, 0.0f), new Color(1f, 1f, 0.0f),
								null, new Vector2f(1f, 0f)) },
						new int[] { 0, 1, 2, 0, 2, 3 }, sprite),
				new Shader(shaderName));
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
