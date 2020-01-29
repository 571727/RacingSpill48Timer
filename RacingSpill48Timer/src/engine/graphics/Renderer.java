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
	
	public void renderNuklear(int AA, int max_vertex_buffer, int max_element_buffer) {
//		try (MemoryStack stack = stackPush()) {
//	        // setup global state
//	        glEnable(GL_BLEND);
//	        glBlendEquation(GL_FUNC_ADD);
//	        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//	        glDisable(GL_CULL_FACE);
//	        glDisable(GL_DEPTH_TEST);
//	        glEnable(GL_SCISSOR_TEST);
//	        glActiveTexture(GL_TEXTURE0);
//
//	        // setup program
//	        glUseProgram(prog);
//	        glUniform1i(uniform_tex, 0);
//	        glUniformMatrix4fv(uniform_proj, false, stack.floats(
//	            2.0f / width, 0.0f, 0.0f, 0.0f,
//	            0.0f, -2.0f / height, 0.0f, 0.0f,
//	            0.0f, 0.0f, -1.0f, 0.0f,
//	            -1.0f, 1.0f, 0.0f, 1.0f
//	        ));
//	        glViewport(0, 0, display_width, display_height);
//	    }
//
//	    {
//	        // convert from command queue into draw list and draw to screen
//
//	        // allocate vertex and element buffer
//	        glBindVertexArray(vao);
//	        glBindBuffer(GL_ARRAY_BUFFER, vbo);
//	        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
//
//	        glBufferData(GL_ARRAY_BUFFER, max_vertex_buffer, GL_STREAM_DRAW);
//	        glBufferData(GL_ELEMENT_ARRAY_BUFFER, max_element_buffer, GL_STREAM_DRAW);
//
//	        // load draw vertices & elements directly into vertex + element buffer
//	        ByteBuffer vertices = Objects.requireNonNull(glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY, max_vertex_buffer, null));
//	        ByteBuffer elements = Objects.requireNonNull(glMapBuffer(GL_ELEMENT_ARRAY_BUFFER, GL_WRITE_ONLY, max_element_buffer, null));
//	        try (MemoryStack stack = stackPush()) {
//	            // fill convert configuration
//	            NkConvertConfig config = NkConvertConfig.callocStack(stack)
//	                .vertex_layout(VERTEX_LAYOUT)
//	                .vertex_size(20)
//	                .vertex_alignment(4)
//	                .null_texture(null_texture)
//	                .circle_segment_count(22)
//	                .curve_segment_count(22)
//	                .arc_segment_count(22)
//	                .global_alpha(1.0f)
//	                .shape_AA(AA)
//	                .line_AA(AA);
//
//	            // setup buffers to load vertices and elements
//	            NkBuffer vbuf = NkBuffer.mallocStack(stack);
//	            NkBuffer ebuf = NkBuffer.mallocStack(stack);
//
//	            nk_buffer_init_fixed(vbuf, vertices/*, max_vertex_buffer*/);
//	            nk_buffer_init_fixed(ebuf, elements/*, max_element_buffer*/);
//	            nk_convert(ctx, cmds, vbuf, ebuf, config);
//	        }
//	        glUnmapBuffer(GL_ELEMENT_ARRAY_BUFFER);
//	        glUnmapBuffer(GL_ARRAY_BUFFER);
//
//	        // iterate over and execute each draw command
//	        float fb_scale_x = (float)display_width / (float)width;
//	        float fb_scale_y = (float)display_height / (float)height;
//
//	        long offset = NULL;
//	        for (NkDrawCommand cmd = nk__draw_begin(ctx, cmds); cmd != null; cmd = nk__draw_next(cmd, cmds, ctx)) {
//	            if (cmd.elem_count() == 0) {
//	                continue;
//	            }
//	            glBindTexture(GL_TEXTURE_2D, cmd.texture().id());
//	            glScissor(
//	                (int)(cmd.clip_rect().x() * fb_scale_x),
//	                (int)((height - (int)(cmd.clip_rect().y() + cmd.clip_rect().h())) * fb_scale_y),
//	                (int)(cmd.clip_rect().w() * fb_scale_x),
//	                (int)(cmd.clip_rect().h() * fb_scale_y)
//	            );
//	            glDrawElements(GL_TRIANGLES, cmd.elem_count(), GL_UNSIGNED_SHORT, offset);
//	            offset += cmd.elem_count() * 2;
//	        }
//	        nk_clear(ctx);
//	    }
//
//	    // default OpenGL state
//	    glUseProgram(0);
//	    glBindBuffer(GL_ARRAY_BUFFER, 0);
//	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
//	    glBindVertexArray(0);
//	    glDisable(GL_BLEND);
//	    glDisable(GL_SCISSOR_TEST);
	}

}
