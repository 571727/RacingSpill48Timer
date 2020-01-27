package engine.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

public class Mesh {

	private Vertex[] vertices;
	private int[] indices;
	private Texture texture;
	// vao (vertex array object), pbo ((vertex) position buffer object), ibo
	// (indices buffer object), cbo (color buffer object) is the buffers of a vertex
	// to be sent to the gpu.
	private int vao, pbo, ibo, cbo, tbo;

	public Mesh(Vertex[] vertices, int[] indices, Texture texture) {
		this.vertices = vertices;
		this.indices = indices;
		this.texture = texture;
	}

	public void create() {

		vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);

		// make data readable by opengl
		FloatBuffer positionBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
		float[] positionData = new float[vertices.length * 3];
		for (int i = 0; i < vertices.length; i++) {
			positionData[i * 3] = vertices[i].getPosition().x();
			positionData[i * 3 + 1] = vertices[i].getPosition().y();
			positionData[i * 3 + 2] = vertices[i].getPosition().z();
		}
		// Put data into buffer
		positionBuffer.put(positionData).flip();

		pbo = storeData(positionBuffer, 0, 3);

		if (vertices[0].getColor() != null) {
			// make data readable by opengl
			FloatBuffer colorBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
			float[] colorData = new float[vertices.length * 3];
			for (int i = 0; i < vertices.length; i++) {
				colorData[i * 3] = new Random().nextInt(100) / 100f;
				colorData[i * 3 + 1] = new Random().nextInt(100) / 100f;
				colorData[i * 3 + 2] = new Random().nextInt(100) / 100f;
			}
			// Put data into buffer
			colorBuffer.put(colorData).flip();

			cbo = storeData(colorBuffer, 1, 3);
			// make data readable by opengl
		}
		
		tbo = storeData(texture.createTextureBuffer(vertices), 2, 2);

		IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
		indicesBuffer.put(indices).flip();

		ibo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

	}

	/**
	 * @return what the buffer id is
	 */
	private int storeData(FloatBuffer buffer, int index, int size) {
		// Create a opengl buffer
		int bufferID = GL15.glGenBuffers();

		// Bind our buffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 0, 0);

		// Unbind
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		return bufferID;
	}
	
	public void bind() {
		GL30.glBindVertexArray(vao);
		
		// Position
		GL30.glEnableVertexAttribArray(0);
		// shader color
		GL30.glEnableVertexAttribArray(1);
		// texture coord
		GL30.glEnableVertexAttribArray(2);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
				
	}

	public void unbind() {
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);		
	}

	public void destroy() {
		GL15.glDeleteBuffers(pbo);
		GL15.glDeleteBuffers(cbo);
		GL15.glDeleteBuffers(ibo);
		GL15.glDeleteBuffers(tbo);

		GL30.glDeleteVertexArrays(vao);
		texture.destroy();
	}

	public Vertex[] getVertices() {
		return vertices;
	}

	public int[] getIndices() {
		return indices;
	}

	public int getVAO() {
		return vao;
	}

	public int getPBO() {
		return pbo;
	}

	public int getIBO() {
		return ibo;
	}

	public int getCBO() {
		return cbo;
	}

	public int getTBO() {
		return tbo;
	}

	public Texture getTexture() {
		return texture;
	}

}
