package scenes.game.subscenes.regular;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.lwjgl.nuklear.NkContext;

import elem.ui.UICollector;
import engine.graphics.Renderer;
import scenes.game.player_local.Player;
import scenes.adt.Scene;
import scenes.adt.SceneGlobalFeatures;
import scenes.game.upgrade.Store;
import scenes.game.multiplayer.server.ServerHandler;

public class LobbyScene extends Scene {

	private Player player;
	private ServerHandler server;
	private Store store;
	private boolean everyoneReady;
	private int currentLength;
	private String currentPlace;
	
	private String chatText = "";
	private JTextField chatInput;
	private JLabel chatOutput;

	
	public LobbyScene(SceneGlobalFeatures features) {
		super(features, null, "Lobby");
	}
	
	@Override
	public void init() {
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
		sceneChange.run(-1);
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
	protected void initNuklearVisual(NkContext ctx, SceneGlobalFeatures features, String title, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

}
