package engine.graphics;

import static org.lwjgl.nuklear.Nuklear.NK_ANTI_ALIASING_ON;
import static org.lwjgl.nuklear.Nuklear.nk__draw_begin;
import static org.lwjgl.nuklear.Nuklear.nk__draw_next;
import static org.lwjgl.nuklear.Nuklear.nk_buffer_init_fixed;
import static org.lwjgl.nuklear.Nuklear.nk_clear;
import static org.lwjgl.nuklear.Nuklear.nk_convert;
import static org.lwjgl.opengl.GL11C.GL_BLEND;
import static org.lwjgl.opengl.GL11C.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11C.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11C.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11C.GL_UNSIGNED_SHORT;
import static org.lwjgl.opengl.GL11C.glBindTexture;
import static org.lwjgl.opengl.GL11C.glBlendFunc;
import static org.lwjgl.opengl.GL11C.glDisable;
import static org.lwjgl.opengl.GL11C.glDrawElements;
import static org.lwjgl.opengl.GL11C.glEnable;
import static org.lwjgl.opengl.GL11C.glScissor;
import static org.lwjgl.opengl.GL11C.glViewport;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.glActiveTexture;
import static org.lwjgl.opengl.GL14C.GL_FUNC_ADD;
import static org.lwjgl.opengl.GL14C.glBlendEquation;
import static org.lwjgl.opengl.GL15C.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15C.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15C.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL15C.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL15C.glBindBuffer;
import static org.lwjgl.opengl.GL15C.glBufferData;
import static org.lwjgl.opengl.GL15C.glMapBuffer;
import static org.lwjgl.opengl.GL15C.glUnmapBuffer;
import static org.lwjgl.opengl.GL20C.glUniform1i;
import static org.lwjgl.opengl.GL20C.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20C.glUseProgram;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;
import java.util.Objects;

import org.lwjgl.nuklear.NkBuffer;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkConvertConfig;
import org.lwjgl.nuklear.NkDrawCommand;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL40;
import org.lwjgl.system.MemoryStack;

import engine.io.UI;
import engine.io.Window;
import engine.math.Matrix4f;
import engine.objects.Camera;
import engine.objects.GameObject;

public class Renderer {
	
	private Window window;
	private UIRender nkUI;
	
	public Renderer(Window window, UIRender nkUI) {
		this.window = window;
		this.nkUI = nkUI;
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
	
	public void renderNuklear(NkContext ctx) {
		nkUI.setupRender();
		nkUI.bind(ctx, NK_ANTI_ALIASING_ON, UI.MAX_VERTEX_BUFFER, UI.MAX_ELEMENT_BUFFER);
		nkUI.draw(ctx);
		nkUI.unbind();
	}
	
	public void destroy() {
		nkUI.destroy();
	}
}
