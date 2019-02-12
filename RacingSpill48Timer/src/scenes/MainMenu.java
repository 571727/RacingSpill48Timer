package scenes;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import adt.Scene;
import handlers.SceneHandler;
import handlers.ServerHandler;

public class MainMenu extends Scene {

	private JButton options;
	private JButton host;
	private JButton join;
	private JLabel title;
	private JLabel tutorial;
	private Thread thread;
	private ServerHandler serverHandler;
	private Lobby lobby;

	public MainMenu(Lobby lobby) {
		// Init variables
		serverHandler = new ServerHandler();
		options = new JButton("Options");
		host = new JButton("Host");
		join = new JButton("Join");
		this.lobby = lobby;
		title = new JLabel("Some racing game v.1.03");
		tutorial = new JLabel("<html>These are all the controls:<br/>"
				+ "Throttle: W<br/>"
				+ "Clutch: Space<br/>"
				+ "Shift: UP-LShift, DOWN-LCtrl<br/>"
				+ "NOS: E<br/>"
				+ "Brakes: S</html>");
		title.setPreferredSize(new Dimension(500,20));
		tutorial.setPreferredSize(new Dimension(500,150));
		
		// Eventlisteners
		options.addActionListener((ActionEvent e) -> {
			SceneHandler.instance.changeScene(4);
		});

		host.addActionListener((ActionEvent e) -> host());
		join.addActionListener((ActionEvent e) -> join());

		// Add to jpanel
		add(title);
		add(options);
		add(host);
		add(join);
		add(tutorial);
	}


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
		thread = new Thread(lobby);
		
		lobby.createNewLobby(name, 1, car, serverHandler, thread);
		
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
		thread = new Thread(lobby);
		
		lobby.joinNewLobby(name, 0, car, ip, thread);
		
		thread.start();
	}

	private String carSelection() {
		Object[] possibilities = { "M3", "Supra", "Mustang", "Bentley", "Skoda Fabia", "Corolla"};
		return (String) JOptionPane.showInputDialog(null, "Choose your car, mate", "Carznstuff",
				JOptionPane.PLAIN_MESSAGE, null, possibilities, "Supra");
	}

}
