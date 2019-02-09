package scenes;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import adt.Scene;
import elem.Player;
import handlers.SceneHandler;
import handlers.ServerHandler;

public class MainMenu extends Scene {

	private JButton options;
	private JButton host;
	private JButton join;
	private Player player;
	private ServerHandler serverHandler;

	public MainMenu() {
		serverHandler = new ServerHandler();
		options = new JButton("Options");
		host = new JButton("Host");
		join = new JButton("Join");

		options.addActionListener((ActionEvent e) -> {
			SceneHandler.instance.changeScene(4);
		});
		host.addActionListener((ActionEvent e) -> {
			//init server and player and then go to lobby
			String name = JOptionPane.showInputDialog("What's your username?");
			serverHandler.createNew();
			player = new Player(name, 1);
			SceneHandler.instance.changeScene(1);
		});
		join.addActionListener((ActionEvent e) -> {
			//init some shit like ip and player and then go to lobby
			String name = JOptionPane.showInputDialog("What's your username?");
			String ip = JOptionPane.showInputDialog("What's the ip?");
			
			player = new Player(name, 0, ip);
			SceneHandler.instance.changeScene(1);
			

		});

		add(options);
		add(host);
		add(join);
	}

}
