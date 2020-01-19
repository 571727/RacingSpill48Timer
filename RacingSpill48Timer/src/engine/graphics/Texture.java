package engine.graphics;

import static org.lwjgl.stb.STBImage.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;

import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

public class Texture {

	private int width, height, nrChannels;
	private String path;
	private ByteBuffer data;
	private int textureID;
	
	public Texture(String path, int width, int height, int nrChannels) {
		IntBuffer w, h, nrCh;
		w = MemoryUtil.memAllocInt(1);
		h = MemoryUtil.memAllocInt(1);
		nrCh = MemoryUtil.memAllocInt(1);

		data = stbi_load((CharSequence) path, w.put(width), h.put(height), nrCh.put(nrChannels), 0);

		if(data.get() == 0) {
			System.out.println("Didnt find texture at " + path);
		}
		
		this.path = path;
		this.width = width;
		this.height = height;
		this.nrChannels = nrChannels;
		
	}
	
	public int generate() {
		textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureID);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);	
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, data);
		GL30.glGenerateMipmap(GL_TEXTURE_2D);
		
		stbi_image_free(data);
		return textureID;
	}
	
	
	
}
