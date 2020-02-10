package engine.io;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_DECORATED;
import static org.lwjgl.glfw.GLFW.GLFW_DONT_CARE;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_DEBUG_CONTEXT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetWindowIcon;
import static org.lwjgl.glfw.GLFW.glfwSetWindowMonitor;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.ARBDebugOutput.GL_DEBUG_SEVERITY_LOW_ARB;
import static org.lwjgl.opengl.ARBDebugOutput.GL_DEBUG_SOURCE_API_ARB;
import static org.lwjgl.opengl.ARBDebugOutput.GL_DEBUG_TYPE_OTHER_ARB;
import static org.lwjgl.opengl.ARBDebugOutput.glDebugMessageControlARB;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.opengl.KHRDebug;
import org.lwjgl.system.Callback;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Platform;

import elem.Action;
import engine.math.Matrix4f;
import player_local.Player;

public class Window {

	public static int CURRENT_WIDTH, CURRENT_HEIGHT, INGAME_WIDTH, INGAME_HEIGHT, CLIENT_WIDTH, CLIENT_HEIGHT;

	private final int[] possibleHeights = { 480, 720, 900, 1080, 1152, 1440, 2160 };
	private Action closingProtocol;
	private boolean fullscreen;
	private String title;
	private Color clearColor;
	private long window;
	private Matrix4f projection;

	public Window(int width, int height, boolean fullscreen, String title, Color clearColor) {
		
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
		
		CURRENT_WIDTH = CLIENT_WIDTH;
		CURRENT_HEIGHT = CLIENT_HEIGHT;

		// TODO Turn this to ingame width and height.
		projection = Matrix4f.projection(70f, (float) CLIENT_WIDTH / (float) CLIENT_HEIGHT, 0.1f, 1000f);
	
	}

	public Callback init() {
		GLFWErrorCallback.createPrint().set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize glfw");
        }
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        if (Platform.get() == Platform.MACOSX) {
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        }
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);

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

		// Opengl
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

		glClearColor(clearColor.getRed(), clearColor.getGreen(), clearColor.getBlue(), 1.0f);

		// TESTING FIXME
		mouseState(true);
		
		return debugProc;
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
		
		
//		Example of nuklear use
//	        NkMouse mouse = ctx.input().mouse();
//	        if (mouse.grab()) {
//	            glfwSetInputMode(win, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
//	        } else if (mouse.grabbed()) {
//	            float prevX = mouse.prev().x();
//	            float prevY = mouse.prev().y();
//	            glfwSetCursorPos(win, prevX, prevY);
//	            mouse.pos().x(prevX);
//	            mouse.pos().y(prevY);
//	        } else if (mouse.ungrab()) {
//	            glfwSetInputMode(win, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
//	        }
//
//	        nk_input_end(ctx);
		
		
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

}
