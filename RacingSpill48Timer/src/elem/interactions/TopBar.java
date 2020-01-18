package elem.interactions;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import engine.io.Window;
import static org.lwjgl.glfw.GLFW.*;
public class TopBar {

	private double x, y;
	private boolean held;
	private long window;

	public TopBar(long window) {
		this.window = window;
	}
	
	public void press(double x, double y) {
		if (y < Window.CLIENT_HEIGHT / 26) {
			if(x > Window.CLIENT_WIDTH - (Window.CLIENT_WIDTH / 40)) {
				//FIXME exit button
				glfwSetWindowShouldClose(window, true);
			} else {
				// Move window
			this.x = x;
			this.y = y;
			held = true;
			}
		}
	}

	public void release() {
		held = false;
	}

	public void move(double toX, double toY) {
		if (held) {
			IntBuffer xb = BufferUtils.createIntBuffer(1);
			IntBuffer yb = BufferUtils.createIntBuffer(1);
			GLFW.glfwGetWindowPos(window, xb, yb);
			
			int x = (int) (xb.get() + (toX - this.x));
			int y = (int) (yb.get() + (toY - this.y));
			
			GLFW.glfwSetWindowPos(window, x, y);
		}
	}
}
