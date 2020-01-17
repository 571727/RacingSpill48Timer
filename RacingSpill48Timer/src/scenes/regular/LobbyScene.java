package scenes.regular;

import javax.swing.JLabel;
import javax.swing.JTextField;

import elem.interactions.Button;
import player_local.Player;
import scenes.Scene;
import scenes.upgrade.Store;
import scenes.visual.LobbyVisual;
import server.ServerHandler;

public class LobbyScene extends Scene {

	private Player player;
	private ServerHandler server;
	private Store store;
	private Button readyBtn;
	private Button goBackBtn;
	private Button optionsBtn;
	private boolean everyoneReady;
	private int currentLength;
	private String currentPlace;
	
	private String chatText = "";
	private JTextField chatInput;
	private JLabel chatOutput;

	
	public LobbyScene() {
		super(new LobbyVisual(), "Lobby");
	}
	
	public void init() {
		store.init(player);
	}
	
	@Override
	public void tick(double delta) {
		visual.tick(delta);
	}

	@Override
	public void keyInput(int keycode, int action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseButtonInput(int button, int action) {
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

}
