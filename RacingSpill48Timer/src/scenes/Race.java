package scenes;

import java.awt.Color;
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
import audio.SFX;
import elem.Player;
import elem.RaceVisual;
import handlers.RaceKeyHandler;
import handlers.SceneHandler;
import window.Windows;

/**
 * Kj�r thread p� lobby n�r du skal tilbake
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
	private int raceLights;
	private boolean finished;

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
			if (!SceneHandler.instance.isSpecified()) {
				WIDTH = device.getDisplayMode().getWidth();
				HEIGHT = device.getDisplayMode().getHeight();
			} else {
				WIDTH = SceneHandler.instance.WIDTH;
				HEIGHT = SceneHandler.instance.HEIGHT;
			}
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
		finished = false;
		time = -1;
		startTime = -1;

		waitTime = System.currentTimeMillis() + 3000;

		visual.setStartCountDown(false);

		racingWindow.add(visual);
		racingWindow.addKeyListener(keys);
		racingWindow.requestFocus();

		SceneHandler.instance.justRemove();
	}

	public synchronized void joinThread() {
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
		if (visual != null && !running) {
			int raceLights = player.getStatusRaceLights();

			if (raceLights != this.raceLights) {

				this.raceLights = raceLights;

				// CONTROL LIGHTS
				if (raceLights == 4) {
					SFX.playSound("greenLight");
					waitTime = System.currentTimeMillis() + 1000;
					visual.setBallCount(3);
					visual.setBallColor(Color.GREEN);
					running = true;
					visual.setRunning(true);
				} else {
					SFX.playSound("redLight");
					visual.setBallColor(Color.RED);
					visual.setBallCount(raceLights);
				}

			}

			// CHEATING
			if (raceLights < 4 && player.getCar().getSpeedActual() > 2) {
				cheating = true;
				racingWindow.removeKeyListener(keys);
				player.getCar().reset();
			}
		} else if (raceLights == 4 && waitTime < System.currentTimeMillis()) {
			raceLights = 0;
			visual.setBallCount(raceLights);
			startTime = System.currentTimeMillis();
		}

		if (cheating) {
			// TODO
			finishRace(true);
		}
	}

	@Override
	public void run() {
		joinThread();

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

				if (visual != null)
					visual.tick();

				if (!finished)
					tick();
				else
					updateResults();

				player.pingServer();
			}
			frames++;
			if (visual != null) {
				Graphics g = null;
				visual.render(g);
				if (player.isInTheRace() == false) {
					player.inTheRace();
				}
			}
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS RACEEE: " + frames);
				frames = 0;
			}
		}

		player.outOfTheRace();
		lobby.setStarted(false);
		lobbyThread = new Thread(lobby);
		lobbyThread.start();

	}

	private void updateResults() {
		everyoneDone = true;
		String outtext = player.updateRaceLobby();

		String[] outputs = outtext.split("#");

		String result = "<html>Tracklength: " + currentLength + " meters.<br/><br/>Players: <br/>";
		int n = 0;
		boolean finished = false;
		for (int i = 1; i < outputs.length; i++) {
			n++;

			switch (n) {

			case 1:
				result += outputs[i] + ", ";
				break;
			case 2:
				if (Integer.valueOf(outputs[i]) == 1) {
					result += "Finished, ";
					finished = true;
				} else {
					result += "Not finished, ";
					everyoneDone = false;
					finished = false;
				}
				break;
			case 3:
				if (Long.valueOf(outputs[i]) == -1) {
					result += "DNF";
				} else if (finished || startTime == -1) {
					result += "Time: " + (Float.valueOf(outputs[i]) / 1000) + " seconds";
				} else {
					result += "Time: " + (System.currentTimeMillis() - startTime / 1000) + " seconds";
				}
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
			// Push results and wait for everyone to finish. Then get a winner.'
			finishRace(false);
		}
	}

	private void finishRace(boolean cheated) {
		System.out.println("Finished");
		
		player.finishRace();
		player.getCar().reset();
		finished = true;

		racingWindow.removeKeyListener(keys);
		keys = null;

		visual.playFinishScene(cheated);

		// Legg til knapper og sånt
	}

	public void closeWindow() {
		racingWindow.remove(visual);
		visual = null;
		player.setReady(0);
		SceneHandler.instance.changeScene(1);		
		racingWindow.setVisible(false);
		racingWindow.dispose();
		racingWindow.setUndecorated(false);
		racingWindow.setExtendedState(JFrame.NORMAL);
		racingWindow.setBounds(50, 50, Windows.WIDTH, Windows.HEIGHT);
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
