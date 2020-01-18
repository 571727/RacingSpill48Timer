package engine.io;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import file_manipulation.ControlsSettings;
import scenes.Scene;
public class InputHandler {

	private double x, y;
	private Scene currentScene;
	private ControlsSettings keys;
	
	private GLFWCursorEnterCallback enterCallback;
	private GLFWKeyCallback keyCallback;
	private GLFWMouseButtonCallback mouseCallback;
	private GLFWCursorPosCallback posCallback;
	private GLFWScrollCallback scrollCallback;
	
	public InputHandler(Scene currentScene, long myWindow) {
		this.currentScene = currentScene;
		keys = new ControlsSettings();
		
		glfwSetCursorEnterCallback(myWindow, enterCallback = GLFWCursorEnterCallback.create((window, entered) -> {
			this.currentScene.mouseEnterWindowInput(entered);
		}));
		
		glfwSetKeyCallback(myWindow, keyCallback = GLFWKeyCallback.create((window, key, scancode, action, mods) -> {
			this.currentScene.keyInput(key, action);
		}));
		
		glfwSetMouseButtonCallback(myWindow, mouseCallback = GLFWMouseButtonCallback.create((window, button, action, mods) -> {
			this.currentScene.mouseButtonInput(button, action, x, y);
		}));
		
		glfwSetCursorPosCallback(myWindow, posCallback = GLFWCursorPosCallback.create((window, xpos, ypos) -> {
			x = xpos;
			y = ypos;
			this.currentScene.mousePosInput(xpos, ypos);
		}));
		
		glfwSetScrollCallback(myWindow, scrollCallback = GLFWScrollCallback.create((window, xoffset, yoffset) -> {
			this.currentScene.mouseScrollInput(xoffset, yoffset);
		}));
		
	}

	public void free() {
		enterCallback.free();
		keyCallback.free();
		mouseCallback.free();
		posCallback.free();
		scrollCallback.free();
	}

	public void setCurrent(Scene scene) {
		currentScene = scene;
	}
	
	public ControlsSettings getKeys() {
		return keys;
	}
	
	public GLFWCursorEnterCallback getEnterCallback() {
		return enterCallback;
	}

	public GLFWKeyCallback getKeyCallback() {
		return keyCallback;
	}

	public GLFWMouseButtonCallback getMouseCallback() {
		return mouseCallback;
	}

	public GLFWCursorPosCallback getPosCallback() {
		return posCallback;
	}

	public GLFWScrollCallback getScrollCallback() {
		return scrollCallback;
	}

}
