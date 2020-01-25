package main;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

import java.awt.Color;

import org.lwjgl.glfw.GLFWErrorCallback;

import audio.AudioHandler;
import elem.interactions.TopBar;
import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Shader;
import engine.graphics.Vertex;
import engine.io.InputHandler;
import engine.io.Window;
import engine.math.Vector2f;
import engine.math.Vector3f;
import engine.objects.Camera;
import engine.objects.GameObject;
import engine.utils.Timer;
import file_manipulation.RegularSettings;
import scenes.SceneHandler;
import scenes.regular.OptionsScene;

public class GameHandler {

	private boolean running;
	private RegularSettings settings;
	private AudioHandler audio;
	private OptionsScene options;
	private SceneHandler sceneHandler;
	private Window window;
	private Timer timer;
	private InputHandler input;
	private Renderer renderer;

	public GameHandler() {
		settings = new RegularSettings();
		audio = new AudioHandler(settings);
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

		// Init GLFW
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		window = new Window(settings.getWidth(), settings.getHeight(), settings.getFullscreen(), Main.GAME_NAME,
				Color.RED);
		window.init();

		renderer = new Renderer(window);
		
		TopBar topBar = new TopBar(window.getWindow());

		sceneHandler.init(options, topBar);
		sceneHandler.changeScene(0);

		input = new InputHandler(sceneHandler.getCurrentScene(), window.getWindow());
		window.initCallbacks(input);

		options.init(settings, input.getKeys(), audio);
		sceneHandler.changeSceneAction(input);

		timer.init();

		running = true;

	}

	private void gameLoop() {
		double delta;
		while (running) {
			if (window.isClosing()) {
				running = false;
				break;
			}

			delta = timer.getDelta();

			// update game
			tick(delta);
			window.update();
			timer.updateTPS();

			// render the game
			render();
			timer.updateFPS();

			timer.update();
			window.swapBuffers();
		}
	}

	private void tick(double delta) {
		sceneHandler.getCurrentScene().tick(delta);
	}

	private void render() {
		sceneHandler.getCurrentScene().render(renderer);
	}

	private void dispose() {
//		Terminate GLFW and free the error callback
		System.out.println("disposing");
		input.free();
		window.destroy();
		sceneHandler.destroy();
		
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	/**
	 * FIXME create a checksum for this program so people have a hard time cheating
	 */
	@Override
	public int hashCode() {
//		int hash = settings.hashCode() * audio.hashCode() * options.hashCode() / timer.hashCode()
//				/ sceneHandler.hashCode() * renderer.hashCode() + new Player().hashCode() + new CarFuncs().hashCode()
//				+ new Bank().hashCode() + new CarRep().hashCode()
//				+ new RaceScene().hashCode() * Main.GAME_VERSION.hashCode() / Main.GAME_NAME.hashCode();
		return 1;
	}

}
