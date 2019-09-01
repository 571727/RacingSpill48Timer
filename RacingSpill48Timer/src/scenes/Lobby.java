package scenes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import adt.Scene;
import audio.SFX;
import elem.Player;
import handlers.GameHandler;
import handlers.SceneHandler;
import handlers.ServerHandler;
import window.Windows;

public class Lobby extends Scene implements Runnable {

	private static final long serialVersionUID = -5861049279182231248L;
	private JLabel label;
	private JButton ready;
	private JButton store;
	private JButton start;
	private JButton goBack;
	private JButton options;
	private Player player;
	private boolean running;
	private ServerHandler server;
	private JScrollPane lobbyInfoScrollPane;
	private boolean everyoneReady;
	private Race race;
	private Store fixCarScene;
	private Thread thread;
	private boolean gameEnded;
	private Thread lobbyThread;
	private boolean started;
	private JLabel ipLabel;

	private String chatText = "";
	private JTextField chatInput;
	private JLabel chatOutput;
	private JScrollPane chatScrollPane;
	private int currentLength;
	private String currentPlace;
	private boolean placeChecked;
	private boolean fixCarChecked;

	public Lobby(Race race, Store fixCarScene) {
		super("lobby");
		// Init shit
		this.race = race;
		this.fixCarScene = fixCarScene;
		label = new JLabel("If you can read this, something wrong happend!!!", SwingConstants.CENTER);
		ready = new JButton("Ready?");
		store = new JButton("Store");
		start = new JButton("Start race");
		goBack = new JButton("Go back to Main Menu");
		options = new JButton("Options and Controls");
		lobbyInfoScrollPane = new JScrollPane(label);
		lobbyInfoScrollPane.setPreferredSize(new Dimension(500, 300));
		ipLabel = new JLabel();

		chatInput = new JTextField("Chat here...");
		chatOutput = new JLabel();
		chatScrollPane = new JScrollPane(chatOutput);
		chatScrollPane.setPreferredSize(new Dimension(500, 300));

		String ipLabelText = "<html><font color='white'>IPs: ";
		try {
			Enumeration enumIP = NetworkInterface.getNetworkInterfaces();

			while (enumIP.hasMoreElements()) {
				NetworkInterface n = (NetworkInterface) enumIP.nextElement();
				Enumeration ee = n.getInetAddresses();
				while (ee.hasMoreElements()) {
					InetAddress i = (InetAddress) ee.nextElement();
					ipLabelText += "<br/>" + i.getHostAddress();
				}
			}
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		ipLabelText += "</font></html>";
		ipLabel.setText(ipLabelText);

		// ActionListeners
		ready.addActionListener((ActionEvent e) -> {
			player.setReady((player.getReady() + 1) % 2);
			// Disable fix car button
			if (player.getReady() == 1)
				GameHandler.ba.playReady();
			else
				GameHandler.ba.playRegularBtn();
			store.setEnabled(player.getReady() == 0);
			options.setEnabled(player.getReady() == 0);
			player.updateReady();
		});
		store.addActionListener((ActionEvent e) -> {
			SFX.playMP3Sound("btn/open_store");
			SceneHandler.instance.changeScene(2);
			if (!fixCarChecked) {
				fixCarScene.init(player);
				fixCarChecked = true;
			}
			fixCarScene.updateText(currentPlace, currentLength);
		});
		goBack.addActionListener((ActionEvent e) -> {
			GameHandler.ba.playRegularBtn();
			goBack();
		});

		start.addActionListener((ActionEvent e) -> {
			if (everyoneReady) {
				// start the race
				player.startRace();
			}
		});

		options.addActionListener((ActionEvent e) -> {
			SceneHandler.instance.changeScene(4);
			GameHandler.ba.playRegularBtn();
		});

		// Chatpart:
		chatInput.setPreferredSize(new Dimension(200, 20));
		chatInput.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// Pressed enter
				if (chatInput.isFocusOwner() && !chatInput.getText().isEmpty())
					if (e.getKeyCode() == 10) {
						player.addChat(chatInput.getText());
						chatInput.setText("");
//						SceneHandler.instance.getWindows().requestFocus();
					}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

		});
		chatInput.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
			}

			@Override
			public void focusGained(FocusEvent e) {
				chatInput.setText("");
				chatInput.removeFocusListener(this);
			}
		});

		// Layout
		setLayout(null);

		int ipLabelSize = 250;
		int bw = 200;
		int bh = bw / 4;
		int margin = (int) (0.4 * bh);
		int marginW = (int) (1.5 * margin);
		int paneSizeW = Windows.WIDTH / 5 * 2;
		int paneSizeH = Windows.HEIGHT / 5 * 2;
		int chatInputH = 24;
		int btnX = (Windows.WIDTH - paneSizeW - marginW) / 2 - bw / 2 + paneSizeW + marginW;

		ipLabel.setFont(new Font("TimesRoman", Font.ITALIC, 16));
		ipLabel.setBounds(paneSizeW + 2 * marginW, margin, ipLabelSize, (int) (ipLabelSize * 0.5));

		start.setBounds(btnX, 4 * bh + 0 * margin, bw, bh);
		store.setBounds(btnX, 5 * bh + 1 * margin, bw, bh);
		ready.setBounds(btnX, 6 * bh + 2 * margin, bw, bh);
		options.setBounds(btnX, 7 * bh + 3 * margin, bw, bh);
		goBack.setBounds(btnX, 8 * bh + 4 * margin, bw, bh);

		lobbyInfoScrollPane.setBounds(marginW, margin, paneSizeW, paneSizeH);
		chatScrollPane.setBounds(marginW, Windows.HEIGHT - paneSizeH - 5 * margin, paneSizeW, paneSizeH);
		chatInput.setBounds(marginW, Windows.HEIGHT - 5 * margin + (chatInputH / 8), paneSizeW, chatInputH);

		// Add to JPanel
		add(lobbyInfoScrollPane);
		add(ipLabel);
		add(goBack, BorderLayout.SOUTH);
		add(options, BorderLayout.SOUTH);
		add(ready, BorderLayout.SOUTH);
		add(store, BorderLayout.SOUTH);
		add(start, BorderLayout.SOUTH);
		add(chatInput);
		add(chatScrollPane);
	}

	private void goBack() {
		player.getCar().reset();
		clearChat();
//		player.stopAllClientHandlerOperations();
		player.endClientHandler();
		player.leaveServer();
		player = null;
		fixCarChecked = false;
		if (server != null) {
			server.close();
			server = null;
		}
		SceneHandler.instance.changeScene(0);
	}

	/**
	 * 
	 * @param string - outtext from server
	 */
	public void update(String string) {
		try {
			if (string == null)
				return;

			everyoneReady = true;
			String[] outputs = string.split("#");

			String endgoal = player.getEndGoal();

			String result = "<html>Podium-position: " + (player.getPlacePodium() + 1) + "<br/>Next race: "
					+ currentPlace + ", " + currentLength + " m" + "<br/>" + endgoal + "<br/><br/>" + "Players: <br/>";
			player.setPlacePodium(Integer.parseInt(outputs[0]));
			int n = 0;
			for (int i = 1; i < outputs.length; i++) {
				n++;

				switch (n) {

				case 1:
					result += outputs[i] + ", ";
					break;
				case 2:
					if (Integer.valueOf(outputs[i]) == 1) {
						result += "Ready, ";
					} else {
						result += "Not ready, ";
						everyoneReady = false;
					}
					break;
				case 3:
					if (Integer.valueOf(outputs[i]) == 1)
						result += "Host, ";
					break;
				case 4:
					result += "Points: " + outputs[i];
					break;
				case 5:
					result += " -- PING: " + outputs[i] + " ms";
					break;
				case 6:
					if (Integer.valueOf(outputs[i]) == 1) {
						raceStarted();
					}
					result += "<br/>";
					break;
				case 7:
					result += outputs[i] + "<br/>";
					n = 0;
					break;
				}

			}
			result += "</html>";

			// Show all players on screen
			label.setText(result);

			// Disable start game button
			if (everyoneReady)
				start.setEnabled(true);
			else
				start.setEnabled(false);

			// Update chat
			String actualChatText = "<html>Chat:";
			String newText = player.getChat();
			if (newText != null) {

				// Adding text to the chatwindow
				chatText += "<br/>" + newText;

				/*
				 * TAUNT(s)
				 */
				String[] tauntCheck = newText.split(": ");
				if (tauntCheck.length > 1 && tauntCheck[1].startsWith("14")) {
					SFX.playMP3SoundWithRandomPitch("start_the_game_already");
				}
			}
			actualChatText += chatText;
			actualChatText += "</html>";
			chatOutput.setText(actualChatText);

			// Check with the server what the location and length is
			if (!placeChecked) {
				placeChecked = true;
				currentLength = player.getTrackLength();
				currentPlace = player.getCurrentPlace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void clearChat() {
		chatOutput.setText("");
		chatText = "";
	}

	public void endGame() {
		gameEnded = true;
		fixCarChecked = false;
		goBack();
	}

	/**
	 * @param name - username
	 * @param host - int value (0,1) represents boolean
	 */
	public void createNewLobby(String name, int host, String car, ServerHandler server, Thread lobbyThread,
			int amountOfRaces) {
		this.server = server;
		joinNewLobby(name, host, car, null, lobbyThread, amountOfRaces);
	}

	/**
	 * @param name - username
	 * @param host - int value (0,1) represents boolean
	 * @param ip
	 */
	public void joinNewLobby(String name, int host, String car, String ip, Thread lobbyThread, int amountOfRaces) {
		this.lobbyThread = lobbyThread;
		gameEnded = false;
		if (ip != null)
			player = new Player(name, host, car, ip);
		else {
			player = new Player(name, host, car);
		}
		player.joinServer();
		if (host == 1)
			player.createNewRaces(amountOfRaces);
		player.updateCarCloneToServer();
		player.startClientHandler();
		player.startPing();
		initButtonState();
		SceneHandler.instance.addClosingListener(player);
	}

	/**
	 * GUI for player
	 */
	private void initButtonState() {

		if (player.isHost()) {
			start.setVisible(true);
			ipLabel.setVisible(true);
		} else {
			start.setVisible(false);
			ipLabel.setVisible(false);
		}

	}

	private void raceStarted() {
		if (!started) {

			started = true;
			SceneHandler.instance.changeScene(3);

			race.setPlayer(player);
			race.setLobby(this);
			race.setLobbyThread(lobbyThread);
			race.setCurrentLength(currentLength);
			race.setCurrentPlace(currentPlace);

			player.setReady(0);
			player.updateReady();
			player.getCar().updateVolume();

			store.setEnabled(true);
			options.setEnabled(true);

			thread = new Thread(race);
			thread.start();
		}
	}

	/**
	 * Merely to update lobby 5 per second
	 */
	@Override
	public void run() {
		started = false;

		player.startUpdateLobby();

		long lastTime = System.nanoTime();
		double amountOfTicks = 5.0;
		double ns = 1000000000 / amountOfTicks;
		double nsr = 1000000000 / (amountOfTicks * 4);
		double delta = 0;
		double deltar = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while ((SceneHandler.instance.getCurrentScene().getClass().equals(Lobby.class)
				|| SceneHandler.instance.getCurrentScene().getClass().equals(Store.class)
				|| SceneHandler.instance.getCurrentScene().getClass().equals(Options.class)
				|| (SceneHandler.instance.getCurrentScene().getClass().equals(Race.class) && !started)) && !gameEnded) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			deltar += (now - lastTime) / nsr;
			lastTime = now;
			while (delta >= 1) {

				if (SceneHandler.instance.getCurrentScene().getClass().equals(Race.class)
						&& SceneHandler.instance.getWindows().isVisible())
					SceneHandler.instance.getWindows().requestFocus();

				if (!gameEnded && !started && player != null
						&& !SceneHandler.instance.getCurrentScene().getClass().equals(Race.class))
					update(player.updateLobby());
				delta--;
			}

			while (SceneHandler.instance.getCurrentScene().getClass().equals(Race.class) && deltar >= 1) {
				race.lobbyTick(1);
				race.render();
				deltar--;
			}

			try {
				Thread.sleep(4);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (player != null)
			player.stopUpdateLobby();

	}

	/*
	 * Getters and setters
	 */
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public Race getRace() {
		return race;
	}

	public void setRace(Race race) {
		this.race = race;
	}

	public Store getFixCarScene() {
		return fixCarScene;
	}

	public void setFixCarScene(Store fixCarScene) {
		this.fixCarScene = fixCarScene;
	}

	public boolean isPlaceChecked() {
		return placeChecked;
	}

	public void setPlaceChecked(boolean placeChecked) {
		this.placeChecked = placeChecked;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(backgroundImage, 0, 0, Windows.WIDTH, Windows.HEIGHT, null);
	}
}
