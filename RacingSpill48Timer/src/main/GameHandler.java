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
import engine.graphics.UIRender;
import engine.io.InputHandler;
import engine.io.UI;
import engine.io.Window;
import engine.utils.Timer;
import file_manipulation.RegularSettings;
import scenes.adt.Scene;
import scenes.SceneHandler;
import scenes.Scenes;
import scenes.adt.SceneGlobalFeatures;
import scenes.game.GameScene;
import scenes.regular.MainMenuScene;
import scenes.regular.MultiplayerScene;
import scenes.regular.OptionsScene;
import scenes.regular.SingleplayerScene;
import steam.SteamMain;

public class GameHandler {

	private boolean running;
	private RegularSettings settings;
	private AudioHandler audio;
	private SceneHandler sceneHandler;
	private Window window;
	private Timer timer;
	private InputHandler input;
	private Renderer renderer;
	private UI ui;

	private Callback debugProcCallback;
	private SteamMain steam;

	public GameHandler() {
		settings = new RegularSettings();
		audio = new AudioHandler(settings);
		timer = new Timer();
		sceneHandler = new SceneHandler();
		ui = new UI();
		steam = new SteamMain();
	}

	public void start(String checksum) {
		init();
		gameLoop();
		dispose();
	}

	private void init() {

		if (!steam.init())
			System.exit(0);

		// Before window setup - CLEAR
		window = new Window(settings.getWidth(), settings.getHeight(), settings.getFullscreen(), Main.GAME_NAME,
				Color.RED);
		debugProcCallback = window.init();

		// Method setupWindow(win) - CLEAR
		input = new InputHandler(window, ui.getNkContext());
		input.setCurrent(sceneHandler);
		// setupContext();
		renderer = new Renderer(window, ui.setupContext());
		// ret and continue
		ui.nkFont();

		// Get created nuklear for stuff
		RegularTopBar topbar = new RegularTopBar(window.getWindow(), Window.CLIENT_HEIGHT / 18);
		SceneGlobalFeatures features = new SceneGlobalFeatures();
		
		Scene[] scenes = new Scene[Scenes.AMOUNT_REGULAR];
		scenes[Scenes.MAIN_MENU] = new MainMenuScene(features, topbar, ui.getNkContext(), window.getWindow());
		scenes[Scenes.SINGLEPLAYER] = new SingleplayerScene(features, topbar, ui.getNkContext(), window.getWindow());
		scenes[Scenes.MULTIPLAYER] = new MultiplayerScene(features, topbar, ui.getNkContext(), window.getWindow());
		scenes[Scenes.OPTIONS] =  new OptionsScene(features);
		scenes[Scenes.GAME] = new GameScene();

		sceneHandler.init(scenes, features);
		sceneHandler.changeSceneAction();
		sceneHandler.changeScene(0);

		((OptionsScene)scenes[Scenes.OPTIONS]).init(settings, input.getKeys(), audio);

		timer.init();

		// Make the window visible
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
		steam.update();
		window.update();
		sceneHandler.tick(delta);
	}

	private void render() {
		sceneHandler.render(ui.getNkContext(), renderer, window.getWindow());
		renderer.renderNuklear(ui.getNkContext());
	}

	private void dispose() {
//		Terminate GLFW and free the error callback
		System.out.println("disposing");

		steam.destroy();

		input.destroy(window.getWindow());
		window.destroy();
		sceneHandler.destroy();
		if (debugProcCallback != null) {
			debugProcCallback.free();
		}
		renderer.destroy();

		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Main.GAME_VERSION == null) ? 0 : Main.GAME_VERSION.hashCode());
		result = prime * result + ((audio == null) ? 0 : audio.hashCode());
		result = prime * result + ((debugProcCallback == null) ? 0 : debugProcCallback.hashCode());
		result = prime * result + ((input == null) ? 0 : input.hashCode());
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
