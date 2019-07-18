package scenes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import adt.Scene;
import adt.Visual;
import adt.VisualElement;
import audio.SFX;
import elem.Animation;
import elem.Player;
import elem.VisualButton;
import elem.VisualString;
import handlers.RaceKeyHandler;
import handlers.SceneHandler;
import startup.Main;
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
	private VisualString results;
	private Visual currentVisual;
	private RaceVisual raceVisual;
	private FinishVisual finishVisual;
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
	private boolean changingVisual;
	private boolean rendering;
	private ArrayList<Boolean> finishedPlayers;
	VisualButton goBackVisual;
	private boolean doneWithRace;
	private Animation background;
	private Animation nitros;
	private BufferedImage fastness;
	private BufferedImage tachopointer;
	private BufferedImage tachometer;
	private BufferedImage resBackground;
	public static int WIDTH;
	public static int HEIGHT;

	public Race() {

		places = new String[4];
		places[0] = "Japan";
		places[1] = "America";
		places[2] = "Britain";
		places[3] = "Germany";

		currentPlace = places[0];

		background = new Animation("road", 6);
		nitros = new Animation("nitros", 4);
		try {

			fastness = ImageIO.read(RaceVisual.class.getResourceAsStream("/pics/fastness.png"));

			tachopointer = ImageIO.read(RaceVisual.class.getResourceAsStream("/pics/tacho.png"));
			// 311 x 225 px
			tachometer = ImageIO.read(RaceVisual.class.getResourceAsStream("/pics/tachometer.png"));
			
			resBackground = ImageIO.read(RaceVisual.class.getResourceAsStream("/pics/back.jpg"));

		} catch (IOException e) {
			System.err.println("didn't find the picture you were looking for");
			e.printStackTrace();
		}

		
	}

	public void initWindow() {

		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
		keys = new RaceKeyHandler(player.getCar());

		racingWindow = SceneHandler.instance.getWindows();
		doneWithRace = false;

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

		finishedPlayers = new ArrayList<Boolean>();

		
		raceVisual = new RaceVisual(player, this);
		raceVisual.setBackground(background);
		raceVisual.setFastness(fastness);
		raceVisual.setNitros(nitros);
		raceVisual.setTachometer(tachometer);
		raceVisual.setTachopointer(tachopointer);
		finishVisual = new FinishVisual(player, this);
		finishVisual.setBackground(resBackground);

		everyoneDone = false;
		cheating = false;
		running = false;
		finished = false;
		time = -1;
		startTime = -1;

		raceVisual.setStartCountDown(false);

		try {
			changeVisual(raceVisual);
		} catch (Exception e) {
			e.printStackTrace();
		}

		racingWindow.addKeyListener(keys);
		SceneHandler.instance.justRemove();
		racingWindow.setFocusable(true);
		racingWindow.requestFocus();

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
		player.getCar().updateSpeed();
		checkDistanceLeft();

		// Controls countdown and cheating and such shait.
		if (raceVisual != null && !running) {
			int raceLights = player.getStatusRaceLights();

			if (raceLights != this.raceLights) {

				this.raceLights = raceLights;

				// CONTROL LIGHTS
				if (raceLights == 4) {
					SFX.playSound("greenLight");
					waitTime = System.currentTimeMillis() + 1000;
					raceVisual.setBallCount(3);
					raceVisual.setBallColor(Color.GREEN);
					running = true;
					raceVisual.setRunning(true);
				} else {
					SFX.playSound("redLight");
					raceVisual.setBallColor(Color.RED);
					raceVisual.setBallCount(raceLights);
				}

			}

			// CHEATING
			if (raceLights < 4 && player.getCar().getSpeedActual() > 2) {
				finishRace(true);
				racingWindow.removeKeyListener(keys);
				player.getCar().reset();
			}
		} else if (raceLights == 4 && waitTime < System.currentTimeMillis()) {
			raceLights = 0;
			raceVisual.setBallCount(raceLights);
			startTime = System.currentTimeMillis();
		}

	}

	public void lobbyTick() {
		visualTick();
		if (finished && currentVisual != null) {
			updateResults();
		}
	}

	public void visualTick() {
		if (currentVisual != null) {
			currentVisual.tick();
		}
	}

	public void visualRender() {
		if (currentVisual != null) {
			Graphics g = null;
			currentVisual.render(g);
		}
	}

	@Override
	public void run() {
		joinThread();

		long lastTime = System.nanoTime();
		double amountOfTicks = 20.0;
		double nst = 1000000000 / amountOfTicks;
		double nsr = 1000000000 / (amountOfTicks * 3.0);
		double deltat = 0;
		double deltar = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;

		initWindow();

		while (SceneHandler.instance.getCurrentScene().getClass().equals(Race.class) && !finished) {
			long now = System.nanoTime();
			deltat += (now - lastTime) / nst;
			deltar += (now - lastTime) / nsr;
			lastTime = now;
			while (deltat >= 1) {
				deltat--;

				if (racingWindow.isVisible())
					racingWindow.requestFocus();

				visualTick();

				tick();

				player.pingServer();
				
			}
			while (deltar >= 1) {
				deltar--;
				frames++;
				if (currentVisual != null) {
					Graphics g = null;
					currentVisual.render(g);
					if (player.isInTheRace() == false) {
						player.inTheRace();
					}
				}
			}
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS RACEEE: " + frames);
				frames = 0;
			}

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
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

		String result = "Tracklength: " + currentLength + " meters.<br/>Players: <br/>";
		int n = 0;
		int playerIndex = 0;
		boolean finished = false;
		for (int i = 1; i < outputs.length; i++) {
			n++;

			switch (n) {

			case 1:
				result += outputs[i] + ", ";
				break;
			case 2:

				// Controlling whether player has finished or not
				if (Integer.valueOf(outputs[i]) == 1) {
					result += "Finished, ";
					finished = true;
				} else {
					result += "Not finished, ";
					everyoneDone = false;
					finished = false;
				}

				// Controlling animation of players finishing after a race
				if (playerIndex + 1 < finishedPlayers.size()) {
					boolean prevFinished = finishedPlayers.get(playerIndex);

					if (prevFinished != finished) {
						if (Long.valueOf(outputs[i + 1]) != -1)
							finishVisual.addFinish();
						finishedPlayers.set(playerIndex, finished);
					}
				} else {
					finishedPlayers.add(playerIndex, finished);
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
				playerIndex++;
				break;
			}

		}

		if (currentVisual.hasAnimationsRunning())
			everyoneDone = false;

		// Show all players on screen
		results.setText(result, "<br/>");
		// Disable start game button
		if (everyoneDone) {
			// Stop race aka make ready the next race
			player.stopRace();
			goBackVisual.setEnabled(true);
			racingWindow.addKeyListener(goBackVisual);
			racingWindow.requestFocus();

		} else
			goBackVisual.setEnabled(false);
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

		try {
			changeVisual(finishVisual);
			results = new VisualString(WIDTH - 500, HEIGHT / 5, 480, HEIGHT / 2, Color.white, Color.black);

			goBackVisual = new VisualButton("goBack", 1, WIDTH - 100, HEIGHT - 100, 2, WIDTH - 100, HEIGHT - 120, 5,
					() -> {
						closeWindow();
					});

			finishVisual.addVisualElement(goBackVisual);
			finishVisual.addVisualElement(results);

			SceneHandler.instance.justRemove();
			racingWindow.requestFocus();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!cheated || Main.DEBUG)
			finishVisual.addFinish();

		// Legg til knapper og sånt
	}

	private void changeVisual(Visual newVisual) throws Exception {

		if (newVisual.equals(currentVisual)) {
			System.err.println("Same visual");
			throw new Exception();
		}

		racingWindow.add(newVisual);
		if (currentVisual != null)
			racingWindow.remove(currentVisual);

		currentVisual = newVisual;
	}

	public void closeWindow() {
		if (!doneWithRace) {
			doneWithRace = true;
			racingWindow.remove(currentVisual);
			raceVisual = null;
			finishVisual = null;
			currentVisual = null;
			player.setReady(0);
			racingWindow.removeKeyListener(goBackVisual);
			goBackVisual = null;
			SceneHandler.instance.changeScene(1);
			racingWindow.setVisible(false);
			racingWindow.dispose();
			racingWindow.setUndecorated(false);
			racingWindow.setExtendedState(JFrame.NORMAL);
			racingWindow.setBounds(50, 50, Windows.WIDTH, Windows.HEIGHT);
			racingWindow.setLocationRelativeTo(null);
			racingWindow.setVisible(true);
		}
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
