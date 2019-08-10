package scenes;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import adt.Scene;
import audio.ButtonAudio;
import handlers.GameHandler;
import handlers.SceneHandler;
import handlers.ServerHandler;
import startup.Main;

public class MainMenu extends Scene {

	private JButton options;
	private JButton host;
	private JButton join;
	private JLabel title;
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
		title = new JLabel(Main.GAME_NAME + " v.1.5");

		title.setPreferredSize(new Dimension(550, 20));

		// Eventlisteners
		options.addActionListener((ActionEvent e) -> {
			SceneHandler.instance.changeScene(4);
			GameHandler.ba.playRegularBtn();
		});
		host.addActionListener((ActionEvent e) -> {
			host();
			GameHandler.ba.playRegularBtn();
		});
		join.addActionListener((ActionEvent e) -> {
			join();
			GameHandler.ba.playRegularBtn();
		});

		// Add to jpanel
		add(title);
		add(options);
		add(host);
		add(join);

	}

	private void host() {
		// init server and player and then go to lobby
		// Register your username
		String name = username();
		if (name == null)
			return;

		// Register your car
		String car = carSelection();
		if (car == null)
			return;

		String amountOfAI = amountOfAISelection();
		if (amountOfAI == null)
			return;

		String diff;
		if (Integer.valueOf(amountOfAI) != 0) {
			diff = difficultySelection();
			if (diff == null)
				return;
		} else {
			diff = "0";
		}

		Main.AI_NAMES_TAKEN = new boolean[Main.AI_NAMES.length];

		serverHandler.createNew(Integer.valueOf(amountOfAI), Integer.valueOf(diff));

		SceneHandler.instance.changeScene(1);
		thread = new Thread(lobby);

		lobby.createNewLobby(name, 1, car, serverHandler, thread);

		thread.start();
	}

	private String difficultySelection() {
		String str = (String) JOptionPane.showInputDialog(null, "Choose difficulty, bro", "Carznstuff",
				JOptionPane.PLAIN_MESSAGE, null, Main.DIFFICULTY_TYPES, Main.DIFFICULTY_TYPES[0]);

		for (int i = 0; i < Main.DIFFICULTY_TYPES.length; i++) {
			if (Main.DIFFICULTY_TYPES[i].equals(str))
				str = String.valueOf(i * 100 / Main.DIFFICULTY_TYPES.length);
		}

		return str;
	}

	private String amountOfAISelection() {
		return (String) JOptionPane.showInputDialog(null, "How many AI?", "Carznstuff", JOptionPane.PLAIN_MESSAGE, null,
				Main.AMOUNT_OF_AI, Main.AMOUNT_OF_AI[0]);
	}

	private void join() {
		// init some shit like ip and player and then go to lobby
		// Register your username
		String name = username();
		if (name == null)
			return;

		// Register hosts ip
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

		// Register your car
		String car = carSelection();
		if (car == null)
			return;

		SceneHandler.instance.changeScene(1);
		thread = new Thread(lobby);

		lobby.joinNewLobby(name, 0, car, ip, thread);

		thread.start();
	}

	private String carSelection() {

		return (String) JOptionPane.showInputDialog(null, "Choose your car, mate", "Carznstuff",
				JOptionPane.PLAIN_MESSAGE, null, Main.CAR_TYPES, "Supra");
	}

	private String username() {
		String name = null;
		do {
			name = JOptionPane
					.showInputDialog("<html>What's your username?<br/> Don't use # and max 12 letters</html>");

			try {
				if (name.isEmpty() || name.contains("#") || name.length() > 12)
					JOptionPane.showMessageDialog(null, "Write your username properly please");
			} catch (NullPointerException e) {
				break;
			}

		} while (name.isEmpty() || name.contains("#") || name.length() > 12);
		return name;
	}

}
