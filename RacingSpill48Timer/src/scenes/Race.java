package scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import adt.Scene;
import adt.Visual;
import audio.SFX;
import elem.Animation;
import elem.Player;
import elem.VisualButton;
import elem.VisualString;
import handlers.GameHandler;
import handlers.RaceKeyHandler;
import handlers.SceneHandler;
import scenes.visual.FinishVisual;
import scenes.visual.RaceVisual;
import scenes.visual.WinVisual;
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
	public static final int TICK_STD = 20;
	private Player player;
	private Lobby lobby;
	private JFrame racingWindow;
	private VisualString results;
	private RaceVisual raceVisual;
	private FinishVisual finishVisual;
	private WinVisual winVisual;
	private RaceKeyHandler keys;
	private String currentPlace;
	private Thread lobbyThread;
	private int currentLength;
	private long startTime;
	private long waitTime;
	private boolean running;
	private boolean everyoneDone;
	private boolean cheating;
	private int raceLights;
	private boolean finished;
	private boolean[] finishedPlayers;
	VisualButton goBackVisual;
	private boolean doneWithRace;

	private Animation background;
	private Animation nitros;
	private BufferedImage fastness;
	private BufferedImage tachopointer;
	private BufferedImage tachometer;
	private BufferedImage resBackground;
	private int races;

	public static int WIDTH;
	public static int HEIGHT;

	public Race() {

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

		GameHandler.ba.startGame();

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

		// FIXME her allokeres det for 100 spillere når det ikke trengs
		finishedPlayers = new boolean[20];
		for (int i = 0; i < finishedPlayers.length; i++) {
			finishedPlayers[i] = false;
		}

		raceVisual = new RaceVisual(player, this);
		raceVisual.setCarImage(findCarImage(player.getCar().getCarName()));
		raceVisual.setBackground(background);
		raceVisual.setFastness(fastness);
		raceVisual.setNitros(nitros);
		raceVisual.setTachometer(tachometer);
		raceVisual.setTachopointer(tachopointer);

		finishVisual = new FinishVisual(player, this);
		finishVisual.setBackground(resBackground);

		winVisual = new WinVisual();
		winVisual.setPlayer(player);

		everyoneDone = false;
		cheating = false;
		running = false;
		finished = false;
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

	public boolean isEveryoneDone() {
		return everyoneDone;
	}

	public void setEveryoneDone(boolean everyoneDone) {
		this.everyoneDone = everyoneDone;
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
					SFX.playMP3Sound("greenLight");
					waitTime = System.currentTimeMillis() + 1000;
					raceVisual.setBallCount(3);
					raceVisual.setBallColor(Color.GREEN);
					running = true;
					raceVisual.setRunning(true);
					player.getCar().tryGearBoost();
				} else {
					SFX.playMP3Sound("redLight");
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
		if (finished && currentVisual != null && !everyoneDone) {
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
		double amountOfTicks = TICK_STD;
		// TODO lagre fps i en textfil. Og gj�re slik at man kan endre verdien i
		// options.
		double[] fpsSteps = { 144, 120, 60, 30, 20, 16 };
		int fps = 3;
		int tolerance = 5;
		double nst = 1000000000 / amountOfTicks;
		double nsr = 1000000000 / fpsSteps[fps];
		double nsp = 1000000000 / (amountOfTicks / 20.0);
		double deltat = 0;
		double deltar = 0;
		double deltap = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;

		initWindow();

		while (SceneHandler.instance.getCurrentScene().getClass().equals(Race.class) && !finished) {
			long now = System.nanoTime();
			deltat += (now - lastTime) / nst;
			deltar += (now - lastTime) / nsr;
			deltap += (now - lastTime) / nsp;
			lastTime = now;
			// Ping
			while (deltap >= 1) {
				deltap--;
				player.pingServer();
				System.out.println("ping");
			}
			// Tick
			while (deltat >= 1) {
				deltat--;

				if (racingWindow.isVisible())
					racingWindow.requestFocus();

				visualTick();

				tick();
			}
			// Render
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
				System.out.println("FPS RACE: " + frames);
				if ((frames >= fpsSteps[fps] - tolerance && frames < fpsSteps[fps] + tolerance) == false
						&& fps < fpsSteps.length - 1 && racingWindow.isFocused()) {
					fps++;
					nsr = 1000000000 / fpsSteps[fps];
				}
				// Check if frames are ok.

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

	private BufferedImage findCarImage(String car) {
		for (int i = 0; i < Main.CAR_TYPES.length; i++) {
			if (Main.CAR_TYPES[i].toLowerCase().equals(car))
				try {
					return ImageIO.read(
							RaceVisual.class.getResourceAsStream("/pics/" + Main.CAR_TYPES[i].toLowerCase() + ".png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			;
		}
		return null;
	}

	private void updateResults() {
		everyoneDone = true;
		String outtext = player.updateRaceLobby();

		String[] outputs = outtext.split("#");

		String result = "Tracklength: " + currentLength + " meters.<br/>Players: <br/>";
		String resultColors = "<br/><br/>";
		int n = 0;
		int stageLength = 5;
		int playerIndex = 0;
		boolean finished = false;
		String color = "";
		for (int i = 1; i < outputs.length; i++) {
			n = i % stageLength;

			switch (n) {

			case 1:
				result += "     " + outputs[i] + ",<br/>";
				break;
			case 2:

				// Controlling whether player has finished or not

				if (i < stageLength)
					color = "won";
				else
					color = "regular";

				if (Integer.valueOf(outputs[i]) == 1) {
//					result += "Finished, ";
					finished = true;

					// Controlling animation of players finishing after a race
					boolean prevFinished;
					try {
						prevFinished = finishedPlayers[playerIndex];
					} catch (IndexOutOfBoundsException e) {
						prevFinished = false;
						finishedPlayers[playerIndex] = true;
					}

					if (!prevFinished) {
						if (Long.valueOf(outputs[i + 1]) != -1) {
							System.out.println("Finish car: " + outputs[i + 3] + ".");
							finishVisual.addFinish(outputs[i + 3], 1);
						}
						finishedPlayers[playerIndex] = true;
					}

				} else if (Integer.valueOf(outputs[i]) == 2) {
					// AI
//					result += "Finished, ";
					finished = true;
					color = "ai";

				} else {
//					result += "Not finished, ";
					everyoneDone = false;
					finished = false;
					color = "nf";
				}

				break;
			case 3:

				if (Long.valueOf(outputs[i]) == -1) {
					result += "DNF";
					color = "dnf";
				} else if (finished || startTime == -1) {
					result += "Time: " + (Float.valueOf(outputs[i]) / 1000) + " seconds";
				} else {
					result += "Time: " + (System.currentTimeMillis() - startTime / 1000) + " seconds";
				}
				break;
			case 4:
				if (Integer.valueOf(outputs[i - 2]) > 0)
					result += outputs[i];

				result += "<br/>";
				playerIndex++;

				resultColors += color;
				resultColors += "<br/>";
				resultColors += color;
				resultColors += "<br/>";

				break;
			}

		}

		if (currentVisual.hasAnimationsRunning())
			everyoneDone = false;

		if (!lastRace()) {
			// Show all players on screen
			results.setText(result, "<br/>", resultColors);
		} else {
			winVisual.setEveryoneDone(everyoneDone);
			winVisual.addVisualElement(goBackVisual);
		}
		if (everyoneDone) {
			// Stop race aka make ready the next race
			player.stopRace();
			goBackVisual.setEnabled(true);
			racingWindow.addKeyListener(goBackVisual);
			racingWindow.requestFocus();
			lobby.setPlaceChecked(false);
		} else
			// Disable start game button
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

		player.finishRace(System.currentTimeMillis() - startTime);
		player.getCar().reset();
		finished = true;

		racingWindow.removeKeyListener(keys);
		keys = null;

		try {
			goBackVisual = new VisualButton("goBack", 1, WIDTH - 100, HEIGHT - 100, 2, WIDTH - 100, HEIGHT - 120, 5,
					() -> {
						closeWindow();
						if (races == 0) {
							lobby.endGame();
							GameHandler.music.playNext();
						}
					});

			if (!lastRace()) {
				changeVisual(finishVisual);
				results = new VisualString((int) (WIDTH - WIDTH / 3.6f), (int) (HEIGHT / 24), (int) (WIDTH / 3.8f),
						(int) (HEIGHT / 1.5f), Color.white, Color.black, new Font("Calibri", Font.BOLD, Race.WIDTH / 90));

				finishVisual.addVisualElement(goBackVisual);
				finishVisual.addVisualElement(results);
			} else {
				changeVisual(winVisual);
			}

			SceneHandler.instance.justRemove();
			racingWindow.requestFocus();

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Legg til knapper og sånt
	}

	private boolean lastRace() {
		return races == 0;
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
			everyoneDone = false;
			racingWindow.removeKeyListener(goBackVisual);
			racingWindow.remove(currentVisual);
			finishVisual.removeVisualElements();
			raceVisual.removeVisualElements();
			raceVisual = null;
			finishVisual = null;
			currentVisual = null;
			player.setReady(0);
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

	public void setCurrentLength(int currentLength) {
		this.currentLength = currentLength;
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

	public int getRaces() {
		return races;
	}

	public void setRaces(int races) {
		this.races = races;
	}

}
