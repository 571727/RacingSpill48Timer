package engine.graphics;

import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.nuklear.Nuklear.nk__draw_begin;
import static org.lwjgl.nuklear.Nuklear.nk__draw_next;
import static org.lwjgl.nuklear.Nuklear.nk_buffer_init;
import static org.lwjgl.nuklear.Nuklear.nk_buffer_init_fixed;
import static org.lwjgl.nuklear.Nuklear.nk_clear;
import static org.lwjgl.nuklear.Nuklear.nk_convert;
import static org.lwjgl.opengl.GL11C.GL_BLEND;
import static org.lwjgl.opengl.GL11C.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL11C.GL_NEAREST;
import static org.lwjgl.opengl.GL11C.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.GL_RGBA;
import static org.lwjgl.opengl.GL11C.GL_RGBA8;
import static org.lwjgl.opengl.GL11C.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11C.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11C.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11C.GL_UNSIGNED_SHORT;
import static org.lwjgl.opengl.GL11C.glBindTexture;
import static org.lwjgl.opengl.GL11C.glBlendFunc;
import static org.lwjgl.opengl.GL11C.glClearColor;
import static org.lwjgl.opengl.GL11C.glDisable;
import static org.lwjgl.opengl.GL11C.glDrawElements;
import static org.lwjgl.opengl.GL11C.glEnable;
import static org.lwjgl.opengl.GL11C.glGenTextures;
import static org.lwjgl.opengl.GL11C.glScissor;
import static org.lwjgl.opengl.GL11C.glTexImage2D;
import static org.lwjgl.opengl.GL11C.glTexParameteri;
import static org.lwjgl.opengl.GL11C.glViewport;
import static org.lwjgl.opengl.GL12C.GL_UNSIGNED_INT_8_8_8_8_REV;
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
import static org.lwjgl.opengl.GL15C.glGenBuffers;
import static org.lwjgl.opengl.GL15C.glMapBuffer;
import static org.lwjgl.opengl.GL15C.glUnmapBuffer;
import static org.lwjgl.opengl.GL20C.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glUniform1i;
import static org.lwjgl.opengl.GL20C.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20C.glUseProgram;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.nmemAllocChecked;
import static org.lwjgl.system.MemoryUtil.nmemFree;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Objects;

import org.lwjgl.nuklear.NkAllocator;
import org.lwjgl.nuklear.NkBuffer;
import org.lwjgl.nuklear.NkColorf;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkConvertConfig;
import org.lwjgl.nuklear.NkDrawCommand;
import org.lwjgl.nuklear.NkDrawNullTexture;
import org.lwjgl.nuklear.NkUserFont;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL40;
import org.lwjgl.system.MemoryStack;

import engine.io.UI;
import engine.io.Window;

public class UIRender {

	private Shader nkShader;
	private NkBuffer cmds = NkBuffer.create();
	private NkDrawNullTexture null_texture = NkDrawNullTexture.create();
	private int vbo, vao, ebo;
	private int uniform_tex;
	private int uniform_proj;
	private int width, height;
	
	public UIRender() {
		
		width = Window.CURRENT_WIDTH;
		height = Window.CURRENT_HEIGHT;
		
		nkShader = new Shader("nk");

		nk_buffer_init(cmds, UI.ALLOCATOR, UI.BUFFER_INITIAL_SIZE);
		nkShader.create();

		uniform_tex = nkShader.getUniformLocation("tex");
		uniform_proj = nkShader.getUniformLocation("ProjMtx");
		int attrib_pos = nkShader.getAttribLocation("Position");
		int attrib_uv =  nkShader.getAttribLocation("TexCoord");
		int attrib_col =  nkShader.getAttribLocation("Color");
		
		{
			// buffer setup
			vbo = glGenBuffers();
			ebo = glGenBuffers();
			vao = glGenVertexArrays();

			glBindVertexArray(vao);
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);

			glEnableVertexAttribArray(attrib_pos);
			glEnableVertexAttribArray(attrib_uv);
			glEnableVertexAttribArray(attrib_col);

			glVertexAttribPointer(attrib_pos, 2, GL_FLOAT, false, 20, 0);
			glVertexAttribPointer(attrib_uv, 2, GL_FLOAT, false, 20, 8);
			glVertexAttribPointer(attrib_col, 4, GL_UNSIGNED_BYTE, true, 20, 16);
		}

		{
			// null texture setup
			int nullTexID = glGenTextures();

			null_texture.texture().id(nullTexID);
			null_texture.uv().set(0.5f, 0.5f);

			glBindTexture(GL_TEXTURE_2D, nullTexID);
			try (MemoryStack stack = stackPush()) {
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, 1, 1, 0, GL_RGBA, GL_UNSIGNED_INT_8_8_8_8_REV,
						stack.ints(0xFFFFFFFF));
			}
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		}

		glBindTexture(GL_TEXTURE_2D, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}
	
	public void setupRender() {
		try (MemoryStack stack = stackPush()) {
	        // setup global state
	        glEnable(GL_BLEND);
	        glBlendEquation(GL_FUNC_ADD);
	        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	        glDisable(GL_CULL_FACE);
	        glDisable(GL_DEPTH_TEST);
	        glEnable(GL_SCISSOR_TEST);
	        glActiveTexture(GL_TEXTURE0);

	        // setup program
	        nkShader.bind();
	        glUniform1i(uniform_tex, 0);
	        glUniformMatrix4fv(uniform_proj, false, stack.floats(
	            2.0f / width, 0.0f, 0.0f, 0.0f,
	            0.0f, -2.0f / height, 0.0f, 0.0f,
	            0.0f, 0.0f, -1.0f, 0.0f,
	            -1.0f, 1.0f, 0.0f, 1.0f
	        ));		
	        
	        glViewport(0, 0, Window.CURRENT_WIDTH, Window.CURRENT_HEIGHT);
	    }

	}
	
	public void bind(NkContext ctx, int AA, int max_vertex_buffer, int max_element_buffer) {
//		GL30.glBindVertexArray(vao);
//		
//		// Position
//		GL30.glEnableVertexAttribArray(0);
//		// shader color
//		GL30.glEnableVertexAttribArray(1);
//		// texture coord
//		GL30.glEnableVertexAttribArray(2);
//		
//		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		
		// convert from command queue into draw list and draw to screen

        // allocate vertex and element buffer
        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);

        glBufferData(GL_ARRAY_BUFFER, max_vertex_buffer, GL_STREAM_DRAW);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, max_element_buffer, GL_STREAM_DRAW);

        // load draw vertices & elements directly into vertex + element buffer
        ByteBuffer vertices = Objects.requireNonNull(glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY, max_vertex_buffer, null));
        ByteBuffer elements = Objects.requireNonNull(glMapBuffer(GL_ELEMENT_ARRAY_BUFFER, GL_WRITE_ONLY, max_element_buffer, null));
        try (MemoryStack stack = stackPush()) {
            // fill convert configuration
            NkConvertConfig config = NkConvertConfig.callocStack(stack)
                .vertex_layout(UI.VERTEX_LAYOUT)
                .vertex_size(20)
                .vertex_alignment(4)
                .null_texture(null_texture)
                .circle_segment_count(22)
                .curve_segment_count(22)
                .arc_segment_count(22)
                .global_alpha(1.0f)
                .shape_AA(AA)
                .line_AA(AA);

            // setup buffers to load vertices and elements
            NkBuffer vbuf = NkBuffer.mallocStack(stack);
            NkBuffer ebuf = NkBuffer.mallocStack(stack);

            nk_buffer_init_fixed(vbuf, vertices/*, max_vertex_buffer*/);
            nk_buffer_init_fixed(ebuf, elements/*, max_element_buffer*/);
            nk_convert(ctx, cmds, vbuf, ebuf, config);
        }
        glUnmapBuffer(GL_ELEMENT_ARRAY_BUFFER);
        glUnmapBuffer(GL_ARRAY_BUFFER);
				
	}
	
	public void draw(NkContext ctx) {
        // iterate over and execute each draw command
        float fb_scale_x = (float)Window.CURRENT_WIDTH / (float)width;
        float fb_scale_y = (float)Window.CURRENT_HEIGHT / (float)height;

        long offset = NULL;
        for (NkDrawCommand cmd = nk__draw_begin(ctx, cmds); cmd != null; cmd = nk__draw_next(cmd, cmds, ctx)) {
            if (cmd.elem_count() == 0) {
                continue;
            }
            glBindTexture(GL_TEXTURE_2D, cmd.texture().id());
            glScissor(
                (int)(cmd.clip_rect().x() * fb_scale_x),
                (int)((height - (int)(cmd.clip_rect().y() + cmd.clip_rect().h())) * fb_scale_y),
                (int)(cmd.clip_rect().w() * fb_scale_x),
                (int)(cmd.clip_rect().h() * fb_scale_y)
            );
            glDrawElements(GL_TRIANGLES, cmd.elem_count(), GL_UNSIGNED_SHORT, offset);
            offset += cmd.elem_count() * 2;
        }
        nk_clear(ctx);
	}

	public void unbind() {
		// default OpenGL state
	    glUseProgram(0);
	    glBindBuffer(GL_ARRAY_BUFFER, 0);
	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	    glBindVertexArray(0);
	    glDisable(GL_BLEND);
	    glDisable(GL_SCISSOR_TEST);
	    // Reset
	    glEnable(GL11.GL_DEPTH_TEST);
		glEnable(GL40.GL_CULL_FACE);
		GL40.glCullFace(GL40.GL_BACK);
	}

	public void destroy() {
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(ebo);
		GL30.glDeleteVertexArrays(vao);
		nkShader.destroy();
	}

}
