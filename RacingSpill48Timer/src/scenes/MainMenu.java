package scenes;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import adt.Scene;
import handlers.GameHandler;
import handlers.SceneHandler;
import handlers.ServerHandler;
import startup.Main;
import window.Windows;

public class MainMenu extends Scene {

	private JButton options;
	private JButton host;
	private JButton join;
	private JLabel title;
	private Thread thread;
	private ServerHandler serverHandler;
	private Lobby lobby;
	private JButton exit;

	public MainMenu(Lobby lobby) {
		super("mainMenu");

		// Init variables
		serverHandler = new ServerHandler();
		options = new JButton("Options and Controls");
		host = new JButton("Host");
		join = new JButton("Join");
		exit = new JButton("Quit Game");
		this.lobby = lobby;
		title = new JLabel("<html><font color='white'>" + Main.GAME_NAME + " v.1.6.0" + "</font></html>");

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
		exit.addActionListener((ActionEvent e) -> {
			GameHandler.ba.playRegularBtn();
			System.exit(0);
		});

		// Layout
		int bw = 200;
		int bh = 50;
		double margin = 0.8 * bh;

		setLayout(null);
		title.setFont(new Font("TimesRoman", Font.ITALIC, 48));
		title.setBounds(125, 50, Windows.WIDTH, 72);
		
		host.setBounds(Windows.WIDTH / 2 - bw / 2, (int) (4 * bh + 0 * margin), bw, bh);
		join.setBounds(Windows.WIDTH / 2 - bw / 2, (int) (5 * bh + 1 *margin), bw, bh);
		options.setBounds(Windows.WIDTH / 2 - bw / 2, (int) (6 * bh + 2 *margin), bw, bh);
		exit.setBounds(Windows.WIDTH / 2 - bw / 2, (int) (7 * bh + 3 *margin), bw, bh);

		// Add to jpanel
		add(title);
		add(host);
		add(join);
		add(options);
		add(exit);
	}

	private void host() {
		// init server and player and then go to lobby
		// Register your username
		String name = username();
		if (name == null)
			return;
		
		
		String gamemode = gameMode();
		if (gamemode == null)
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

		String amountOfRaces = amountOfRacesSelection();
		if (amountOfRaces == null)
			return;

		Main.AI_NAMES_TAKEN = new boolean[Main.AI_NAMES.length];

		serverHandler.createNew(Integer.valueOf(amountOfAI), Integer.valueOf(diff), gamemode);

		SceneHandler.instance.changeScene(1);
		thread = new Thread(lobby);

		lobby.createNewLobby(name, 1, car, serverHandler, thread, Integer.valueOf(amountOfRaces));

		thread.start();
	}

	private String gameMode() {
		
		//FIXME
		String[] arr = new String[Main.GAME_MODES.length];
		for(int i = 0; i < arr.length; i++) {
			arr[i] = Main.GAME_MODES[i].getName();
		}
		
		String str = (String) JOptionPane.showInputDialog(null, "Choose gamemode, bro", Main.GAME_NAME,
				JOptionPane.PLAIN_MESSAGE, null, arr, arr[0]);

		return str;
	}

	private String amountOfRacesSelection() {
		String str = (String) JOptionPane.showInputDialog(null, "Choose length, bro", Main.GAME_NAME,
				JOptionPane.PLAIN_MESSAGE, null, Main.RACE_AMOUNT, Main.RACE_AMOUNT[0]);

		return str;
	}

	private String difficultySelection() {
		String str = (String) JOptionPane.showInputDialog(null, "Choose difficulty, bro", Main.GAME_NAME,
				JOptionPane.PLAIN_MESSAGE, null, Main.DIFFICULTY_TYPES, Main.DIFFICULTY_TYPES[0]);

		for (int i = 0; i < Main.DIFFICULTY_TYPES.length; i++) {
			if (Main.DIFFICULTY_TYPES[i].equals(str))
				str = String.valueOf(i * 100 / Main.DIFFICULTY_TYPES.length);
		}

		return str;
	}

	private String amountOfAISelection() {
		return (String) JOptionPane.showInputDialog(null, "How many AI?", Main.GAME_NAME, JOptionPane.PLAIN_MESSAGE,
				null, Main.AMOUNT_OF_AI, Main.AMOUNT_OF_AI[0]);
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

		lobby.joinNewLobby(name, 0, car, ip, thread, -1);

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

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(backgroundImage, 0, 0, Windows.WIDTH, Windows.HEIGHT, null);
	}

}
