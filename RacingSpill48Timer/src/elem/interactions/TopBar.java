package elem.interactions;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

public class TopBar {

	private double x, y;
	private boolean held;
	private long window;
	private int height;
	private PressAction pressedWithin;

	public TopBar(long window, int height, PressAction pressAction) {
		this.window = window;
		this.height = height;
		this.pressedWithin = pressAction;
	}

	public void press(double x, double y) {
		if (y < height) {
			pressedWithin.run(x, y);
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
			
			GLFW.glfwSetCursorPos(window, toX, toY);

			GLFW.glfwSetWindowPos(window, x, y);
		}
	}

	public int getHeight() {
		return height;
	}

	public boolean isHeld() {
		return held;
	}

	public void setHeld(boolean held) {
		this.held = held;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
}
