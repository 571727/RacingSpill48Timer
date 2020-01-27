package engine.io;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL40;
import org.lwjgl.system.MemoryStack;

import elem.Action;
import engine.graphics.Texture;
import engine.math.Matrix4f;
import player_local.Player;

public class Window {

	public static int INGAME_WIDTH, INGAME_HEIGHT, CLIENT_WIDTH, CLIENT_HEIGHT;

	private final int[] possibleHeights = {
			480, 720, 900, 1080, 1152, 1440, 2160
	};
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
		
		for(int i = 0; currHeight > possibleHeights[i]; i++) {
			CLIENT_HEIGHT = possibleHeights[i];
		}
		CLIENT_WIDTH = CLIENT_HEIGHT * 16 / 9;
		
		//TODO Turn this to ingame width and height.
		projection = Matrix4f.projection(70f, (float) CLIENT_WIDTH / (float) CLIENT_HEIGHT, 0.1f, 1000f);
	}

	public void init() {
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
		
		window = glfwCreateWindow(CLIENT_WIDTH, CLIENT_HEIGHT, title, NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the glfw window");

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

		GL.createCapabilities();

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glEnable(GL40.GL_CULL_FACE);
		GL40.glCullFace(GL40.GL_BACK);
		
		
		glClearColor(clearColor.getRed(), clearColor.getGreen(), clearColor.getBlue(), 1.0f);
		mouseState(true);
	}

	public void initCallbacks(InputHandler input) {
		glfwSetCursorEnterCallback(window, input.getEnterCallback());
		glfwSetKeyCallback(window, input.getKeyCallback());
		glfwSetMouseButtonCallback(window, input.getMouseCallback());
		glfwSetCursorPosCallback(window, input.getPosCallback());
		glfwSetScrollCallback(window, input.getScrollCallback());
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
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear the framebuffer
		glfwPollEvents();
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
