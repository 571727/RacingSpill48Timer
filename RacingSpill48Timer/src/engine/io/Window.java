package engine.io;

import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.opengl.GL30C.*;
import org.lwjgl.stb.*;
import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.ARBDebugOutput.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.nuklear.NkAllocator;
import org.lwjgl.nuklear.NkBuffer;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkDrawNullTexture;
import org.lwjgl.nuklear.NkUserFont;
import org.lwjgl.nuklear.NkUserFontGlyph;
import org.lwjgl.nuklear.NkVec2;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.opengl.KHRDebug;
import org.lwjgl.system.Callback;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Platform;

import elem.Action;
import engine.graphics.Shader;
import engine.graphics.Texture;
import engine.math.Matrix4f;
import engine.utils.FileUtils;
import player_local.Player;

public class Window {

	public static int INGAME_WIDTH, INGAME_HEIGHT, CLIENT_WIDTH, CLIENT_HEIGHT;

	public static final int MAX_VERTEX_BUFFER  = 512 * 1024;
	public static final int MAX_ELEMENT_BUFFER = 128 * 1024;
	public static final NkAllocator ALLOCATOR = NkAllocator.create()
			.alloc((handle, old, size) -> nmemAllocChecked(size)).mfree((handle, ptr) -> nmemFree(ptr));
	private NkContext ctx = NkContext.create();
	private NkBuffer cmds = NkBuffer.create();
	private NkDrawNullTexture null_texture = NkDrawNullTexture.create();
	private static final int BUFFER_INITIAL_SIZE = 4 * 1024;
	private int vbo, vao, ebo;
	private int uniform_tex;
	private int uniform_proj;
	
	private NkUserFont default_font = NkUserFont.create();
	private final ByteBuffer ttf;
	

	private final int[] possibleHeights = { 480, 720, 900, 1080, 1152, 1440, 2160 };
	private Action closingProtocol;
	private boolean fullscreen;
	private String title;
	private Color clearColor;
	private long window;
	private Matrix4f projection;

	public Window(int width, int height, boolean fullscreen, String title, Color clearColor) {
		
		try {
            this.ttf = FileUtils.ioResourceToByteBuffer("fonts/BASKVILL.TTF", 512 * 1024);
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
		
		this.fullscreen = fullscreen;
		this.title = title;
		this.clearColor = clearColor;
		INGAME_WIDTH = width;
		INGAME_HEIGHT = height;

		// Set client size to one resolution lower than the current one
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int currHeight = gd.getDisplayMode().getHeight();

		for (int i = 0; currHeight > possibleHeights[i]; i++) {
			CLIENT_HEIGHT = possibleHeights[i];
		}
		CLIENT_WIDTH = CLIENT_HEIGHT * 16 / 9;

		// TODO Turn this to ingame width and height.
		projection = Matrix4f.projection(70f, (float) CLIENT_WIDTH / (float) CLIENT_HEIGHT, 0.1f, 1000f);
	}

	public void init() {
		GLFWErrorCallback.createPrint().set();

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);

		window = glfwCreateWindow(CLIENT_WIDTH, CLIENT_HEIGHT, title, NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the glfw window");

		// ICON
		GLFWImage icon = icon("/pics/icon.png");
		GLFWImage.Buffer icons = GLFWImage.malloc(1);
		icons.put(0, icon);
		glfwSetWindowIcon(window, icons);

		// Get the thread stack and push a new frame
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			setWindowInTheMiddle(pWidth.get(0), pHeight.get(0));
		}
//				Make the OpenGL context current
		glfwMakeContextCurrent(window);
//				Enable v-sync
		glfwSwapInterval(1);

//				Make the window visible
		glfwShowWindow(window);

		// Opengl
		GL.createCapabilities();

		GLCapabilities caps = GL.createCapabilities();
		Callback debugProc = GLUtil.setupDebugMessageCallback();

		if (caps.OpenGL43) {
			GL43.glDebugMessageControl(GL43.GL_DEBUG_SOURCE_API, GL43.GL_DEBUG_TYPE_OTHER,
					GL43.GL_DEBUG_SEVERITY_NOTIFICATION, (IntBuffer) null, false);
		} else if (caps.GL_KHR_debug) {
			KHRDebug.glDebugMessageControl(KHRDebug.GL_DEBUG_SOURCE_API, KHRDebug.GL_DEBUG_TYPE_OTHER,
					KHRDebug.GL_DEBUG_SEVERITY_NOTIFICATION, (IntBuffer) null, false);
		} else if (caps.GL_ARB_debug_output) {
			glDebugMessageControlARB(GL_DEBUG_SOURCE_API_ARB, GL_DEBUG_TYPE_OTHER_ARB, GL_DEBUG_SEVERITY_LOW_ARB,
					(IntBuffer) null, false);
		}

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glEnable(GL40.GL_CULL_FACE);
		GL40.glCullFace(GL40.GL_BACK);

		glClearColor(clearColor.getRed(), clearColor.getGreen(), clearColor.getBlue(), 1.0f);

		// TESTING FIXME
		mouseState(true);
	}



	public void setupNkContext() {

		Shader nkShader = new Shader("nk");

		nk_buffer_init(cmds, ALLOCATOR, BUFFER_INITIAL_SIZE);
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
	
	public void nkFont() {
		int BITMAP_W = 1024;
		int BITMAP_H = 1024;

		int FONT_HEIGHT = 18;
		int fontTexID   = glGenTextures();

		STBTTFontinfo          fontInfo = STBTTFontinfo.create();
		STBTTPackedchar.Buffer cdata    = STBTTPackedchar.create(95);

		float scale;
		float descent;

		try (MemoryStack stack = stackPush()) {
		    stbtt_InitFont(fontInfo, ttf);
		    scale = stbtt_ScaleForPixelHeight(fontInfo, FONT_HEIGHT);

		    IntBuffer d = stack.mallocInt(1);
		    stbtt_GetFontVMetrics(fontInfo, null, d, null);
		    descent = d.get(0) * scale;

		    ByteBuffer bitmap = memAlloc(BITMAP_W * BITMAP_H);

		    STBTTPackContext pc = STBTTPackContext.mallocStack(stack);
		    stbtt_PackBegin(pc, bitmap, BITMAP_W, BITMAP_H, 0, 1, NULL);
		    stbtt_PackSetOversampling(pc, 4, 4);
		    stbtt_PackFontRange(pc, ttf, 0, FONT_HEIGHT, 32, cdata);
		    stbtt_PackEnd(pc);

		    // Convert R8 to RGBA8
		    ByteBuffer texture = memAlloc(BITMAP_W * BITMAP_H * 4);
		    for (int i = 0; i < bitmap.capacity(); i++) {
		        texture.putInt((bitmap.get(i) << 24) | 0x00FFFFFF);
		    }
		    texture.flip();

		    glBindTexture(GL_TEXTURE_2D, fontTexID);
		    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, BITMAP_W, BITMAP_H, 0, GL_RGBA, GL_UNSIGNED_INT_8_8_8_8_REV, texture);
		    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

		    memFree(texture);
		    memFree(bitmap);
		}

		default_font
		    .width((handle, h, text, len) -> {
		        float text_width = 0;
		        try (MemoryStack stack = stackPush()) {
		            IntBuffer unicode = stack.mallocInt(1);

		            int glyph_len = nnk_utf_decode(text, memAddress(unicode), len);
		            int text_len  = glyph_len;

		            if (glyph_len == 0) {
		                return 0;
		            }

		            IntBuffer advance = stack.mallocInt(1);
		            while (text_len <= len && glyph_len != 0) {
		                if (unicode.get(0) == NK_UTF_INVALID) {
		                    break;
		                }

		                /* query currently drawn glyph information */
		                stbtt_GetCodepointHMetrics(fontInfo, unicode.get(0), advance, null);
		                text_width += advance.get(0) * scale;

		                /* offset next glyph */
		                glyph_len = nnk_utf_decode(text + text_len, memAddress(unicode), len - text_len);
		                text_len += glyph_len;
		            }
		        }
		        return text_width;
		    })
		    .height(FONT_HEIGHT)
		    .query((handle, font_height, glyph, codepoint, next_codepoint) -> {
		        try (MemoryStack stack = stackPush()) {
		            FloatBuffer x = stack.floats(0.0f);
		            FloatBuffer y = stack.floats(0.0f);

		            STBTTAlignedQuad q       = STBTTAlignedQuad.mallocStack(stack);
		            IntBuffer        advance = stack.mallocInt(1);

		            stbtt_GetPackedQuad(cdata, BITMAP_W, BITMAP_H, codepoint - 32, x, y, q, false);
		            stbtt_GetCodepointHMetrics(fontInfo, codepoint, advance, null);

		            NkUserFontGlyph ufg = NkUserFontGlyph.create(glyph);

		            ufg.width(q.x1() - q.x0());
		            ufg.height(q.y1() - q.y0());
		            ufg.offset().set(q.x0(), q.y0() + (FONT_HEIGHT + descent));
		            ufg.xadvance(advance.get(0) * scale);
		            ufg.uv(0).set(q.s0(), q.t0());
		            ufg.uv(1).set(q.s1(), q.t1());
		        }
		    })
		    .texture(it -> it
		        .id(fontTexID));

		nk_style_set_font(ctx, default_font);
	}

	public void initClosingProtocol(Player player) {
		closingProtocol = () -> {
			try {
				player.leaveServer();
			} catch (Exception ex) {
				System.err.println("FAILED TO LEAVE BY CLOSING WINDOW");
			}
		};
	}

	public void update() {
		
		glfwPollEvents();
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear the framebuffer
		
	}

	public void swapBuffers() {
		glfwSwapBuffers(window);
	}

	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
		if (fullscreen) {
			// switch to full screen
			glfwSetWindowMonitor(window, glfwGetPrimaryMonitor(), 0, 0, INGAME_WIDTH, INGAME_HEIGHT, GLFW_DONT_CARE);
		} else {
			// switch to windowed
			glfwSetWindowMonitor(window, NULL, 0, 0, INGAME_WIDTH, INGAME_HEIGHT, GLFW_DONT_CARE);
			setWindowInTheMiddle(INGAME_WIDTH, INGAME_HEIGHT);
		}
	}

	private void setWindowInTheMiddle(int pWidth, int pHeight) {
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

		glfwSetWindowPos(window, (vidmode.width() - pWidth) / 2, (vidmode.height() - pHeight) / 2);

	}

	public void setSize(int width, int height) {
		INGAME_WIDTH = width;
		INGAME_HEIGHT = height;
		setFullscreen(fullscreen);
	}

	public void destroy() {
		if (closingProtocol != null)
			closingProtocol.run();
		glfwDestroyWindow(window);
	}

	public boolean isClosing() {
		return glfwWindowShouldClose(window);
	}

	private GLFWImage icon(String path) {

		BufferedImage image = null;
		try {
			image = ImageIO.read(Window.class.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}

		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				int colorSpace = image.getRGB(j, i);
				buffer.put((byte) ((colorSpace << 8) >> 24));
				buffer.put((byte) ((colorSpace << 16) >> 24));
				buffer.put((byte) ((colorSpace << 24) >> 24));
				buffer.put((byte) (colorSpace >> 24));
			}
		}
		buffer.flip();
		final GLFWImage result = GLFWImage.create();
		result.set(image.getWidth(), image.getHeight(), buffer);
		return result;
	}

	public void mouseState(boolean lock) {
		glfwSetInputMode(window, GLFW_CURSOR, lock ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL);
	}

	public Matrix4f getProjectionMatrix() {
		return projection;
	}

	public long getWindow() {
		return window;
	}

	public boolean isFullscreen() {
		return fullscreen;
	}

	public NkContext getNkCtx() {
		return ctx;
	}

}
