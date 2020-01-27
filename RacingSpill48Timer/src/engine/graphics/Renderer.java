package engine.graphics;

import org.lwjgl.opengl.GL11;

import engine.io.Window;
import engine.math.Matrix4f;
import engine.objects.Camera;
import engine.objects.GameObject;

public class Renderer {
	
	private Window window;
	
	public Renderer(Window window) {
		this.window = window;
	}

	public void renderMesh(GameObject go, Camera camera) {
		// Bind
		go.getMesh().bind();
		go.getMesh().getTexture().bind();
		go.getShader().bind();
		
		//Set uniforms
		go.getShader().setUniform("model", Matrix4f.transform(go.getPosition(), go.getRotation(), go.getScale()));
		go.getShader().setUniform("view", Matrix4f.view(camera.getPosition(), camera.getRotation()));
		go.getShader().setUniform("projection", window.getProjectionMatrix());
		
		//Draw
		GL11.glDrawElements(GL11.GL_TRIANGLES, go.getMesh().getIndices().length, 
				GL11.GL_UNSIGNED_INT, 0);

		// Unbind
		go.getShader().unbind();
		go.getMesh().getTexture().unbind();
		go.getMesh().unbind();
	}

}
