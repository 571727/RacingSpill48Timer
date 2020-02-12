package scenes;

import org.lwjgl.nuklear.NkContext;

import elem.interactions.RegularTopBar;
import elem.interactions.TopBar;
import engine.io.InputHandler;
import scenes.racing.EndScene;
import scenes.racing.FinishScene;
import scenes.racing.RaceScene;
import scenes.regular.LobbyScene;
import scenes.regular.MainMenuScene;
import scenes.regular.SetupScene;

public class SceneHandler {

	private Scene[] scenes;
	private boolean specified;

	public SceneHandler() {
		scenes = new Scene[9];
	}

	public void init(Scene options, RegularTopBar topBar, NkContext ctx, long window) {
		scenes[Scenes.MAIN_MENU] = new MainMenuScene(topBar, ctx, window);
		scenes[Scenes.SINGLEPLAYER] = new MainMenuScene(topBar, ctx, window);
		scenes[Scenes.MULTIPLAYER] = new MainMenuScene(topBar,ctx,  window);
		scenes[Scenes.LOBBY] = new LobbyScene();
		scenes[Scenes.OPTIONS] = options;
		scenes[Scenes.SETUP_LOBBY] = new SetupScene();
		scenes[Scenes.RACE] = new RaceScene();
		scenes[Scenes.FINISH] = new FinishScene();
		scenes[Scenes.END] = new EndScene();

		for (Scene scene : scenes) {
			scene.init();
		}

	}

	/**
	 * FIXME Destroy all the meshes and shaders etc
	 */
	public void destroy() {
		for (Scene scene : scenes) {
			scene.destroy();
		}
	}

	public void changeSceneAction(InputHandler input) {
		SceneChangeAction sceneChange = (scenenr) -> {
			changeScene(scenenr);
			input.setCurrent(scenes[Scenes.CURRENT]);
		};
		for (Scene scene : scenes) {
			scene.setSceneChangeAction(sceneChange);
		}
	}

	public void changeScene(int scenenr) {
		Scenes.PREVIOUS = Scenes.CURRENT;
		Scenes.CURRENT = scenenr;
	}

	public Scene getCurrentScene() {
		return scenes[Scenes.CURRENT];
	}

	public Scene getLastScene() {
		return scenes[Scenes.PREVIOUS];
	}

	public boolean isSpecified() {
		return specified;
	}

	public void setSpecified(boolean specified) {
		this.specified = specified;
	}

}