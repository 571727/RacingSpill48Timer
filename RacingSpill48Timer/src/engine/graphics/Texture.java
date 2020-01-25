package engine.graphics;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import engine.utils.BufferUtils;

public class Texture {

	private int width, height;
	private String path;
	private int textureID;

	public Texture(String path) {
		this.path = path;
	}

	public int load() {
		int[] pixels = null;
		try {
			BufferedImage image = ImageIO.read(Texture.class.getResourceAsStream(path));
			this.width = image.getWidth();
			this.height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);

		} catch (IOException e) {
			e.printStackTrace();
		}

		int[] data = new int[width * height];
		for (int i = 0; i < width * height; i++) {
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);

			data[i] = a << 24 | b << 16 | g << 8 | r;
		}

		textureID = glGenTextures();

		glBindTexture(GL_TEXTURE_2D, textureID);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
		glBindTexture(GL_TEXTURE_2D, 0);

		return textureID;
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, textureID);
	}

	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public FloatBuffer createTextureBuffer(Vertex[] vertices) {
		FloatBuffer textureBuffer = MemoryUtil.memAllocFloat(vertices.length * 2);
		float[] textureData = new float[vertices.length * 2];
		for (int i = 0; i < vertices.length; i++) {
			textureData[i * 2] = vertices[i].getTextureCoord().x();
			textureData[i * 2 + 1] = vertices[i].getTextureCoord().y();
		}
		// Put data into buffer
		textureBuffer.put(textureData).flip();
		return textureBuffer;
	}

	public void destroy() {
		GL13.glDeleteTextures(textureID);
	}

}
