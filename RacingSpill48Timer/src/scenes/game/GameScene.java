package scenes.game;

import org.lwjgl.nuklear.NkContext;

import static scenes.Scenes.*;

import elem.Action;
import elem.interactions.LobbyTopbar;
import elem.interactions.RegularTopbar;
import elem.interactions.TopbarInteraction;
import elem.interactions.TransparentTopbar;
import elem.ui.UICollector;
import elem.ui.UIObject;
import engine.graphics.Renderer;
import scenes.adt.GlobalFeatures;
import scenes.adt.Scene;
import scenes.adt.SceneChangeAction;
import scenes.game.gamefeat.GameFeatures;
import scenes.game.gamefeat.HostFeatures;
import scenes.game.gamefeat.JoinFeatures;
import scenes.game.gamefeat.SingleFeatures;
import scenes.game.multiplayer.GameType;
import scenes.game.subscenes.SubScene;
import scenes.game.subscenes.racing.EndScene;
import scenes.game.subscenes.racing.FinishScene;
import scenes.game.subscenes.racing.RaceScene;
import scenes.game.subscenes.regular.LobbyScene;
import scenes.game.subscenes.regular.SetupScene;

public class GameScene extends Scene {

	private GameFeatures gameFeat;
	private SetupScene setupScene;
	private LobbyScene lobbyScene;
	private RaceScene raceScene;
	private EndScene endScene;
	private FinishScene finishScene;
	private Scene currentScene;

	private RegularTopbar regularTopbar;
	private LobbyTopbar lobbyTopbar;
	private TransparentTopbar transparentTopbar;
	private NkContext ctx;
	private long window;

	public GameScene(GlobalFeatures features, RegularTopbar regularTopbar, LobbyTopbar lobbyTopbar,
			TransparentTopbar transparentTopbar, NkContext ctx, long window) {
		super(features, null, "", ctx, window, 0, 0, 0, 0);

		this.regularTopbar = regularTopbar;
		this.lobbyTopbar = lobbyTopbar;
		this.transparentTopbar = transparentTopbar;
		this.ctx = ctx;
		this.window = window;
	}

	/**
	 * @param multiplayerStatusType
	 */
	public void initGame(GameType type) {

		// New scenes so that the old shit is removed
		setupScene = new SetupScene(features, regularTopbar, ctx, window);
		lobbyScene = new LobbyScene(features, lobbyTopbar, ctx, window);
		raceScene = new RaceScene(features, transparentTopbar, ctx, window);
		endScene = new EndScene(features, transparentTopbar, ctx, window);
		finishScene = new FinishScene(features, transparentTopbar, ctx, window);

		// FIXME må kjøre destroy her om du går vekk fra spillet...

		gameFeat = null;
		// GameMode later

		currentScene = setupScene;

		Action destroyAction = () -> destroy();
		Action subSceneUpdate = () -> {
			currentScene.update();
			currentScene.press();
		};
		SceneChangeAction subSceneChange = (scenenr, update) -> {
			
			SubScene scene = null;
			
			switch(scenenr) {
			case RACE:
				currentScene = raceScene;
				break;
			case FINISH:
				currentScene = finishScene;
				break;
			case LOBBY:
				currentScene = lobbyScene;
				break;
			case END:
				currentScene = endScene;
				break;
			}
			
			if(update)
				subSceneUpdate.run();
			
			return scene;
		};
		
		switch (type) {
		case HOSTING:
			gameFeat = new HostFeatures(destroyAction);
			break;
		case SINGLEPLAYER:
			gameFeat = new SingleFeatures(destroyAction, sceneChange, subSceneChange, subSceneUpdate);

			break;
		case JOINING:
			// this depends on where the game is. If new: setup, if running lobby, osv.
			gameFeat = new JoinFeatures((feat) -> setGameFeatures(feat), destroyAction);

			break;
		}

		setGameFeatures(gameFeat);
		
	}

	private void setGameFeatures(GameFeatures gameFeat) {
		setupScene.setGameFeatures(gameFeat);
		lobbyScene.setGameFeatures(gameFeat);
		raceScene.setGameFeatures(gameFeat);
		endScene.setGameFeatures(gameFeat);
		finishScene.setGameFeatures(gameFeat);
	}

	@Override
	public boolean keyInput(int keycode, int action) {
		return currentScene.keyInput(keycode, action);
	}

	@Override
	public void mouseButtonInput(int button, int action, double x, double y) {
		currentScene.mouseButtonInput(button, action, x, y);
	}

	@Override
	public void mousePosInput(double x, double y) {
		currentScene.mousePosInput(x, y);
	}

	@Override
	public void mouseScrollInput(double x, double y) {
		currentScene.mouseScrollInput(x, y);
	}

	@Override
	public void mouseEnterWindowInput(boolean entered) {
		currentScene.mouseEnterWindowInput(entered);
	}

	@Override
	public void tick(double delta) {
		currentScene.tick(delta);
	}

	@Override
	public void render(NkContext ctx, Renderer renderer, long window) {
		currentScene.render(ctx, renderer, window);
	}

	@Override
	public void renderUILayout(NkContext ctx, UICollector uic) {
		currentScene.renderUILayout(ctx, uic);
	}

	@Override
	public void determineUIWindowFocusByMouse(double x, double y) {
		currentScene.determineUIWindowFocusByMouse(x, y);
	}

	@Override
	public void update() {
		currentScene.update();
	}

	@Override
	protected void init(NkContext ctx, int x, int y, int width, int height) {
	}

	@Override
	public void destroy() {
		if (setupScene != null) {
			// If setup does not exist, nothing does

			gameFeat.leave(false);
			
			setupScene.destroy();
			lobbyScene.destroy();
			raceScene.destroy();
			endScene.destroy();
			finishScene.destroy();

			setupScene = null;
			lobbyScene = null;
			raceScene = null;
			endScene = null;
			finishScene = null;
			currentScene = null;
		}
	}

	@Override
	public void press() {
		if (currentScene != null)
			currentScene.press();
	}

	@Override
	public UIObject getTopbar() {
		return currentScene.getTopbar();
	}

}
