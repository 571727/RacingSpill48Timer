package engine.graphics;

import org.lwjgl.opengl.GL30;

import engine.io.Window;
import engine.math.Matrix4f;
import engine.objects.Camera;
import engine.objects.GameObject;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;

public class Renderer {
	
	private Window window;
	
	public Renderer(Window window) {
		this.window = window;
	}

	public void renderMesh(GameObject go, Camera camera) {
		// Bind vao
		GL30.glBindVertexArray(go.getMesh().getVAO());
		// Position
		GL30.glEnableVertexAttribArray(0);
		// shader color
		GL30.glEnableVertexAttribArray(1);
		// texture coord
		GL30.glEnableVertexAttribArray(2);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, go.getMesh().getIBO());
		
		// Deal with textures
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		go.getMesh().getTexture().bind();
		
		go.getShader().bind();
		
		//Set uniforms
		go.getShader().setUniform("model", Matrix4f.transform(go.getPosition(), go.getRotation(), go.getScale()));
		go.getShader().setUniform("view", Matrix4f.view(camera.getPosition(), camera.getRotation()));
		go.getShader().setUniform("projection", window.getProjectionMatrix());
		
		//Draw
		GL11.glDrawElements(GL11.GL_TRIANGLES, go.getMesh().getIndices().length, 
				GL11.GL_UNSIGNED_INT, 0);

		go.getShader().unbind();
		go.getMesh().getTexture().unbind();
		
		// Unbind
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

}
