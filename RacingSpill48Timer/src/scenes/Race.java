package scenes;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

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
	private Random r;
	private boolean running;
	private boolean everyoneDone;
	private boolean cheating;

	public static int WIDTH;
	public static int HEIGHT;

	public Race() {
		r = new Random();

		places = new String[4];
		places[0] = "Japan";
		places[1] = "America";
		places[2] = "Britain";
		places[3] = "Germany";

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

		visual = new RaceVisual(player, this);

		everyoneDone = false;
		cheating = false;
		running = false;
		time = -1;
		startTime = -1;
		waitTime = System.currentTimeMillis() + 5000;
		visual.setStartCountDown(false);

		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
		keys = new RaceKeyHandler(player.getCar());

		WIDTH = device.getDisplayMode().getWidth();
		HEIGHT = device.getDisplayMode().getHeight();

		racingWindow = new JFrame();
		racingWindow.setTitle("The race");
		device.setFullScreenWindow(racingWindow);
//		racingWindow.setBounds(50, 50, 500, 500);
		racingWindow.setResizable(false);
		racingWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		racingWindow.setLocationRelativeTo(null);
		
		racingWindow.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					player.leaveServer();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		

		racingWindow.add(visual);
		racingWindow.addKeyListener(keys);
		racingWindow.requestFocus();

		racingWindow.setVisible(true);
		racingWindow.pack();

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
		long lastTime = System.nanoTime();
		double amountOfTicks = 20.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
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
			if (visual != null)
				visual.render();
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

		String result = "<html>Players: <br/>";
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
				result += "Time: " + outputs[i] + "<br/>";
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
		}
	}

	public void closeWindow() {
		visual = null;
		racingWindow.setVisible(false);
		racingWindow.dispose();
	}

	/**
	 * Creates a new racetrack somewhere in the world and with some length of some
	 * type.
	 */
	public void randomizeConfiguration() {
		currentPlace = places[r.nextInt(places.length)];

		currentLength = 1000 * (r.nextInt(4) + 1);
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

	public void setCurrentLength(int currentLength) {
		this.currentLength = currentLength;
	}

	public boolean isCheating() {
		return cheating;
	}

	public void setCheating(boolean cheating) {
		this.cheating = cheating;
	}

}
