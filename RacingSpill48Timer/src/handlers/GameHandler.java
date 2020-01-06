package handlers;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import org.lwjgl.glfw.GLFWErrorCallback;

import audio.MusicAudio;
import handlers.InputHandler;
import main.Main;
import main.RegularSettings;
import window.Timer;
import window.Window;
import audio.Audio;
import audio.ButtonAudio;
import scenes.OptionsScene;

public class GameHandler {

	private boolean running;
	private RegularSettings settings;
	private Audio audio;
	private OptionsScene options;
	private SceneHandler sceneHandler;
	private Window window;
	private Timer timer;
	private InputHandler input;
	
	public GameHandler() {
		settings = new RegularSettings();
		audio = new Audio(settings);
		options = new OptionsScene();
		timer = new Timer();
		sceneHandler = new SceneHandler();
	}
	
	public void start(String checksum) {
		init();
		gameLoop();
		dispose();
	}

	private void init() {
		// All static methods
		GLFWErrorCallback.createPrint(System.err).set();

//		Init GLFW
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		window = new Window(settings.getWidth(), settings.getHeight(), settings.getFullscreen(), Main.GAME_NAME, Color.BLACK);
		window.init();

		sceneHandler.init(options);
		sceneHandler.changeScene(0);

		input = new InputHandler(sceneHandler.getCurrentScene(), window.getWindow());
		options.init(settings, input.getKeys(), audio);
		sceneHandler.changeSceneAction(input);
		
		timer.init();

		running = true;
	}

	private void gameLoop() {
		double delta;
		while (running) {
			if (window.isClosing()) 
				running = false;
			

			delta = timer.getDelta();

			//update game
			tick(delta);
			timer.updateTPS();
			
			//render the game
			render();
			timer.updateFPS();
			
			timer.update();
			
			window.update();
		}
	}

	private void tick(double delta) {
		sceneHandler.getCurrentScene().tick(delta);
	}

	private void render() {
		sceneHandler.getCurrentScene().render();
	}

	private void dispose() {
		System.out.println("disposing");
		input.free();
		window.destroy();
//		Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	/**
	 * FIXME create a checksum for this program so people have a hard time cheating
	 */
	@Override
	public int hashCode() {
		return 1;
	}
	
}
