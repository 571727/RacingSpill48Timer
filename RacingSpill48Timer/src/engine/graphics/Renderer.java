package engine.graphics;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class Renderer {

	public void renderMesh(Mesh mesh, Shader shader) {
		// Bind
		GL30.glBindVertexArray(mesh.getVAO());
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.getIBO());
		
		shader.bind();
		
		//Draw
		GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getIndices().length, 
				GL11.GL_UNSIGNED_INT, 0);

		shader.unbind();
		
		// Unbind
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}

}
