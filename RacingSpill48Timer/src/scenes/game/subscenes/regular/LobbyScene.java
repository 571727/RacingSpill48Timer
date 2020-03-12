package scenes.game.subscenes.regular;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.lwjgl.nuklear.NkContext;

import elem.interactions.LobbyTopbar;
import elem.ui.UICollector;
import elem.ui.UIObject;
import engine.graphics.Renderer;
import engine.io.Window;
import scenes.game.player_local.Player;
import scenes.game.subscenes.SubScene;
import scenes.adt.Scene;
import scenes.Scenes;
import scenes.adt.GlobalFeatures;
import scenes.game.upgrade.Store;
import scenes.game.multiplayer.server.ServerHandler;

public class LobbyScene extends SubScene {

	private Player player;
	private ServerHandler server;
	private Store store;
	private boolean everyoneReady;
	private int currentLength;
	private String currentPlace;

	private String chatText = "";
	private JTextField chatInput;
	private JLabel chatOutput;
	private LobbyTopbar lobbyTopbar;

	public LobbyScene(GlobalFeatures features, LobbyTopbar lobbyTopbar, NkContext ctx, long window) {
		super(features, null, "Lobby", ctx, window, 0, lobbyTopbar.getHeight(), Window.CURRENT_WIDTH,
				Window.CURRENT_HEIGHT - lobbyTopbar.getHeight());
		this.lobbyTopbar = lobbyTopbar;
	}

	@Override
	public void update() {
//		store.init(player);
	}

	private void goBack() {
		player.getCar().reset();
		clearChat();
//		player.stopAllClientHandlerOperations();
		player.endClientHandler();
		player.leaveServer();
		player = null;
		if (server != null) {
			server.close();
			server = null;
		}
		sceneChange.run(Scenes.PREVIOUS_REGULAR, true);
	}

	private void clearChat() {
		chatOutput.setText("");
		chatText = "";
	}

	@Override
	public void tick(double delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseButtonInput(int button, int action, double x, double y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePosInput(double x, double y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseScrollInput(double x, double y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEnterWindowInput(boolean entered) {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean keyInput(int keycode, int action) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void render(NkContext ctx, Renderer renderer, long window) {
		// TODO Auto-generated method stub

	}

	@Override
	public void renderUILayout(NkContext ctx, UICollector uic) {
		// TODO Auto-generated method stub

	}

	@Override
	public void renderUIBackground(NkContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void determineUIWindowFocusByMouse(double x, double y) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void init(NkContext ctx, int x, int y, int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void press() {
		// TODO Auto-generated method stub

	}

	@Override
	public UIObject getTopbar() {
		return lobbyTopbar;
	}

}
