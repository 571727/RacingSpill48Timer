package scenes;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import adt.Scene;
import elem.Player;
import handlers.GameHandler;
import handlers.SceneHandler;
import handlers.ServerHandler;

public class MainMenu extends Scene {

	private JButton options;
	private JButton host;
	private JButton join;
	private Thread thread;
	private ServerHandler serverHandler;
	private Lobby lobby;
	private Race race;

	public MainMenu(Lobby lobby, Race race) {
		// Init variables
		serverHandler = new ServerHandler();
		options = new JButton("Options");
		host = new JButton("Host");
		join = new JButton("Join");
		this.lobby = lobby;
		this.race = race;

		// Eventlisteners
		options.addActionListener((ActionEvent e) -> {
			SceneHandler.instance.changeScene(4);
		});

		host.addActionListener((ActionEvent e) -> host());
		join.addActionListener((ActionEvent e) -> join());

		// Add to jpanel
		add(options);
		add(host);
		add(join);

	}

	/**
	 * FIXME om man trykker cancel går man videre med null verdier.
	 */

	private void host() {
		// init server and player and then go to lobby
		//Register your username
		String name;
		do {
			name = JOptionPane.showInputDialog("What's your username? Don't use #");

			try {
				if (name.isEmpty() || name.contains("#"))
					JOptionPane.showMessageDialog(null, "Write your username properly please");
			} catch (NullPointerException e) {
				return;
			}

		} while (name.isEmpty() || name.contains("#"));
		
		//Register your car
		String car = carSelection();
		if (car == null)
			return;

		serverHandler.createNew();

		SceneHandler.instance.changeScene(1);

		lobby.createNewLobby(name, 1, car, serverHandler);
		thread = new Thread(lobby);
		thread.start();
	}

	private void join() {
		// init some shit like ip and player and then go to lobby
		//Register your username
		String name;
		do {
			name = JOptionPane.showInputDialog("What's your username? Don't use #");

			try {
				if (name.isEmpty() || name.contains("#"))
					JOptionPane.showMessageDialog(null, "Write your username properly please");
			} catch (NullPointerException e) {
				return;
			}

		} while (name.isEmpty() || name.contains("#"));
		
		//Register hosts ip
		String ip;
		do {
			ip = JOptionPane.showInputDialog("What's the ip?");

			try {
				if (ip.isEmpty())
					JOptionPane.showMessageDialog(null, "Write the ip please");
			} catch (NullPointerException e) {
				return;
			}

		} while (ip.isEmpty());
		
		//Register your car
		String car = carSelection();
		if (car == null)
			return;
		
		SceneHandler.instance.changeScene(1);

		lobby.joinNewLobby(name, 0, car, ip);
		thread = new Thread(lobby);
		thread.start();
	}

	private String carSelection() {
		Object[] possibilities = { "M3", "Supra", "Mustang", "Bentley", "Skoda Fabia", "Corolla",
				"Just Bought This New Lamborghini Here" };
		return (String) JOptionPane.showInputDialog(null, "Choose your car, mate", "Carznstuff",
				JOptionPane.PLAIN_MESSAGE, null, possibilities, "Supra");
	}

}
