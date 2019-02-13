package scenes;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import adt.Scene;
import elem.Player;
import elem.RaceVisual;
import handlers.RaceKeyHandler;
import handlers.SceneHandler;

/**
 * Kjør thread på lobby når du skal tilbake
 * 
 * @author jonah
 *
 */
public class Race extends Scene implements Runnable {

	/**
	 * Generated value
	 */
	private static final long serialVersionUID = 7286654650311664681L;
	private Player player;
	private Lobby lobby;
	private JFrame racingWindow;
	private JButton goToLobby;
	private JScrollPane scrollPane;
	private JLabel results;
	private RaceVisual visual;
	private RaceKeyHandler keys;
	private String[] places;
	private String currentPlace;
	private Thread lobbyThread;
	private int currentLength;
	private long time;
	private long startTime;
	private long waitTime;
	private boolean running;
	private boolean everyoneDone;
	private boolean cheating;

	public static int WIDTH;
	public static int HEIGHT;

	public Race() {

		places = new String[4];
		places[0] = "Japan";
		places[1] = "America";
		places[2] = "Britain";
		places[3] = "Germany";

		currentPlace = places[0];

		results = new JLabel("Driving");
		goToLobby = new JButton("Go back to the lobby");
		goToLobby.setEnabled(false);

		scrollPane = new JScrollPane(results);
		scrollPane.setPreferredSize(new Dimension(500, 300));

		goToLobby.addActionListener((ActionEvent e) -> {
			// Reset some shit and go to lobby
			player.setReady(0);
			SceneHandler.instance.changeScene(1);

			lobbyThread = new Thread(lobby);
			lobbyThread.start();
		});

		add(scrollPane);
		add(goToLobby);
	}

	public void initWindow() {

		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
		keys = new RaceKeyHandler(player.getCar());

		racingWindow = SceneHandler.instance.getWindows();

		racingWindow.setVisible(false);
		racingWindow.dispose();
		if (SceneHandler.instance.isFullScreen()) {
			WIDTH = device.getDisplayMode().getWidth();
			HEIGHT = device.getDisplayMode().getHeight();
			racingWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
			racingWindow.setUndecorated(true);
		} else {
			WIDTH = SceneHandler.instance.WIDTH;
			HEIGHT = SceneHandler.instance.HEIGHT;
			racingWindow.setBounds(10, 10, WIDTH, HEIGHT);
			racingWindow.setLocationRelativeTo(null);
		}
		racingWindow.setVisible(true);

		visual = new RaceVisual(player, this);

		everyoneDone = false;
		cheating = false;
		running = false;
		time = -1;
		startTime = -1;
		waitTime = System.currentTimeMillis() + 3000;
		visual.setStartCountDown(false);

		racingWindow.add(visual);
		racingWindow.addKeyListener(keys);
		racingWindow.requestFocus();

		SceneHandler.instance.justRemove();
	}

	public synchronized void endMe() {
		try {
			lobbyThread.join();
			System.err.println("thread ended");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void tick() {
		if (racingWindow.isVisible())
			racingWindow.requestFocus();
		player.getCar().updateSpeed();
		checkDistanceLeft();

		// Controls countdown and cheating and such shait.
		if (visual != null) {
			if (!visual.isStartCountDown() && waitTime < System.currentTimeMillis()) {

				// Wait for 5 secounds before the race starts

				visual.setStartCountDown(true);
				waitTime = System.currentTimeMillis() + 3000;
				visual.setStartTime(System.currentTimeMillis());

			} else if (waitTime < System.currentTimeMillis() && !running) {

				// Wait for 3 secounds. Start countdown.

				startTime = System.currentTimeMillis();
				running = true;
				visual.setRunning(true);
			} else if (running) {

				// Stopwatch

				time = System.currentTimeMillis() - startTime;
			} else if (waitTime > System.currentTimeMillis()) {
				// CHEATING!!!
				if (player.getCar().getSpeedActual() > 2) {
					cheating = true;
					racingWindow.removeKeyListener(keys);
					player.getCar().reset();
				}
			}
		}

		if (cheating && waitTime < System.currentTimeMillis()) {
			closeWindow();
		}
	}

	@Override
	public void run() {
		endMe();

		long lastTime = System.nanoTime();
		double amountOfTicks = 20.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;

		initWindow();

		while (SceneHandler.instance.getCurrentScene().getClass().equals(Race.class) && !everyoneDone) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				delta--;

				if (visual != null) {
					visual.tick();
					tick();
				} else {
					updateResults();
				}

			}
			frames++;
			if (visual != null) {
				Graphics g = null;
				visual.render(g);
			}
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS RACEEE: " + frames);
				frames = 0;
			}
		}
	}

	private void updateResults() {
		everyoneDone = true;
		String outtext = player.updateRace(1, time);

		String[] outputs = outtext.split("#");

		String result = "<html>Tracklength: " + currentLength + " meters.<br/><br/>Players: <br/>";
		int n = 0;
		for (int i = 1; i < outputs.length; i++) {
			n++;

			switch (n) {

			case 1:
				result += outputs[i] + ", ";
				break;
			case 2:
				if (Integer.valueOf(outputs[i]) == 1) {
					result += "Finished, ";
				} else {
					result += "Not finished, ";
					everyoneDone = false;
				}
				break;
			case 3:
				if (Long.valueOf(outputs[i]) == -1)
					result += "DNF";
				else
					result += "Time: " + (Float.valueOf(outputs[i]) / 1000) + " seconds";
				break;
			case 4:
				if (Integer.valueOf(outputs[i - 2]) == 1)
					result += outputs[i];

				result += "<br/>";
				n = 0;
				break;
			}

		}
		result += "</html>";

		// Show all players on screen
		results.setText(result);
		// Disable start game button
		if (everyoneDone) {
			// Stop race aka make ready the next race
			player.stopRace();
			goToLobby.setEnabled(true);

		} else
			goToLobby.setEnabled(false);
	}

	public void checkDistanceLeft() {
		if (player.getCar().getDistance() >= currentLength) {
			// Push results and wait for everyone to finish. Then get a winner.
			closeWindow();
			player.getCar().reset();
		} else {
			player.updateRace(0, time);
		}
	}

	public void closeWindow() {
		racingWindow.remove(visual);
		visual = null;
		SceneHandler.instance.changeScene(3);
		racingWindow.removeKeyListener(keys);
		keys = null;
		racingWindow.setVisible(false);
		racingWindow.dispose();
		racingWindow.setUndecorated(false);
		racingWindow.setExtendedState(JFrame.NORMAL);
		racingWindow.setBounds(50, 50, 600, 500);
		racingWindow.setLocationRelativeTo(null);
		racingWindow.setVisible(true);
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Lobby getLobby() {
		return lobby;
	}

	public void setLobby(Lobby lobby) {
		this.lobby = lobby;
	}

	public String getCurrentPlace() {
		return currentPlace;
	}

	public void setCurrentPlace(String currentPlace) {
		this.currentPlace = currentPlace;
	}

	public int getCurrentLength() {
		return currentLength;
	}

	public void setCurrentLength() {
		currentLength = player.getTrackLength();
	}

	public boolean isCheating() {
		return cheating;
	}

	public void setCheating(boolean cheating) {
		this.cheating = cheating;
	}

	public void setLobbyThread(Thread lobbyThread) {
		this.lobbyThread = lobbyThread;
	}

}
