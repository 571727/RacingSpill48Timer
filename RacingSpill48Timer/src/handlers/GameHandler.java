package handlers;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

import java.awt.Color;

import org.lwjgl.glfw.GLFWErrorCallback;

import audio.Audio;
import elem.graphics.Mesh;
import elem.graphics.Renderer;
import elem.graphics.Vertex;
import elem.math.Vector3f;
import main.Main;
import main.RegularSettings;
import scenes.OptionsScene;
import window.Timer;
import window.Window;

public class GameHandler {

	private boolean running;
	private RegularSettings settings;
	private Audio audio;
	private OptionsScene options;
	private SceneHandler sceneHandler;
	private Window window;
	private Timer timer;
	private InputHandler input;
	private Renderer renderer;
	
	private Mesh testMesh = new Mesh(new Vertex[] {
			new Vertex(new Vector3f(-0.5f,0.5f, 0.0f)),
			new Vertex(new Vector3f(0.5f,0.5f, 0.0f)),
			new Vertex(new Vector3f(0.5f,-0.5f, 0.0f)),
			new Vertex(new Vector3f(-0.5f,-0.5f, 0.0f))
	}, new int[] {
			0, 1, 2,
			0, 3, 2
	});
	
	public GameHandler() {
		settings = new RegularSettings();
		audio = new Audio(settings);
		options = new OptionsScene();
		timer = new Timer();
		sceneHandler = new SceneHandler();
		renderer = new Renderer();
	}
	
	public void start(String checksum) {
		init();
		gameLoop();
		dispose();
	}

	private void init() {
		// All static methods
		GLFWErrorCallback.createPrint(System.err).set();

		//	Init GLFW
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		window = new Window(settings.getWidth(), settings.getHeight(), settings.getFullscreen(), Main.GAME_NAME, Color.BLACK);
		window.init();

		sceneHandler.init(options);
		sceneHandler.changeScene(0);

		input = new InputHandler(sceneHandler.getCurrentScene(), window.getWindow());
		window.initCallbacks(input);
		
		options.init(settings, input.getKeys(), audio);
		sceneHandler.changeSceneAction(input);
		
		timer.init();

		running = true;
		testMesh.create();
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
		renderer.renderMesh(testMesh);
		sceneHandler.getCurrentScene().render(renderer);
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
