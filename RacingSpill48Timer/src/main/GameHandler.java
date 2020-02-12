package main;

import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.nuklear.Nuklear.NK_WINDOW_NO_INPUT;
import static org.lwjgl.nuklear.Nuklear.nk_begin;
import static org.lwjgl.nuklear.Nuklear.nk_end;
import static org.lwjgl.nuklear.Nuklear.nk_layout_row_dynamic;

import java.awt.Color;

import org.lwjgl.nuklear.NkRect;
import org.lwjgl.system.Callback;
import org.lwjgl.system.MemoryStack;

import audio.AudioHandler;
import elem.interactions.RegularTopBar;
import elem.interactions.TopBar;
import engine.graphics.Renderer;
import engine.io.InputHandler;
import engine.io.UI;
import engine.io.Window;
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
	private UI ui;
	
	private Callback debugProcCallback;

	public GameHandler() {
		settings = new RegularSettings();
		audio = new AudioHandler(settings);
		options = new OptionsScene();
		timer = new Timer();
		sceneHandler = new SceneHandler();
		ui = new UI();
	}

	public void start(String checksum) {
		init();
		gameLoop();
		dispose();
	}

	private void init() {
		window = new Window(settings.getWidth(), settings.getHeight(), settings.getFullscreen(), Main.GAME_NAME,
				Color.RED);
		debugProcCallback = window.init();

		
		RegularTopBar topBar = new RegularTopBar(window.getWindow(), Window.CLIENT_HEIGHT / 24);
		sceneHandler.init(options, topBar, ui.getNkContext(), window.getWindow());
		sceneHandler.changeScene(0);

		
		input = new InputHandler(sceneHandler.getCurrentScene(), window, ui.getNkContext());
		renderer = new Renderer(window);
		ui.nkFont();
		
		options.init(settings, input.getKeys(), audio);
		sceneHandler.changeSceneAction(input);

		timer.init();

		//		Make the window visible
		glfwShowWindow(window.getWindow());
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
			window.update();
			
			tick(delta);
			timer.updateTPS();

			// draw the game
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
		sceneHandler.getCurrentScene().render(ui.getNkContext(), renderer, window.getWindow());
		renderer.renderNuklear(ui.getNkContext());
	}

	private void dispose() {
//		Terminate GLFW and free the error callback
		System.out.println("disposing");
		input.free(window.getWindow());
		window.destroy();
		sceneHandler.destroy();
		if (debugProcCallback != null) {
            debugProcCallback.free();
        }
		
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((audio == null) ? 0 : audio.hashCode());
		result = prime * result + ((debugProcCallback == null) ? 0 : debugProcCallback.hashCode());
		result = prime * result + ((input == null) ? 0 : input.hashCode());
		result = prime * result + ((options == null) ? 0 : options.hashCode());
		result = prime * result + ((renderer == null) ? 0 : renderer.hashCode());
		result = prime * result + (running ? 1231 : 1237);
		result = prime * result + ((sceneHandler == null) ? 0 : sceneHandler.hashCode());
		result = prime * result + ((settings == null) ? 0 : settings.hashCode());
		result = prime * result + ((timer == null) ? 0 : timer.hashCode());
		result = prime * result + ((ui == null) ? 0 : ui.hashCode());
		result = prime * result + ((window == null) ? 0 : window.hashCode());
		System.out.println(result);
		return result;
	}

}
