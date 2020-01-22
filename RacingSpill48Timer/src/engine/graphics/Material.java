package engine.graphics;

public class Material {

	private Texture texture;
	
	public Material(String path, int width, int height) {
		texture = new Texture(path);
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public void create() {
		texture.load();
	}

	public void destroy() {
		texture.destroy();
	}
	
}
