package engine.graphics;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;

public class Renderer {

	public void renderMesh(Mesh mesh, Shader shader) {
		// Bind vao
		GL30.glBindVertexArray(mesh.getVAO());
		// Position
		GL30.glEnableVertexAttribArray(0);
		// shader color
		GL30.glEnableVertexAttribArray(1);
		// texture coord
		GL30.glEnableVertexAttribArray(2);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.getIBO());
		
		// Deal with textures
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		mesh.getMaterial().getTexture().bind();
		
		shader.bind();
		
		//Draw
		GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getIndices().length, 
				GL11.GL_UNSIGNED_INT, 0);

		shader.unbind();
		mesh.getMaterial().getTexture().unbind();
		
		// Unbind
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

}
