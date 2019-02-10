package scenes;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
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
	private int currentLength;
	private Random r;
	private boolean running;
	
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
		
		add(scrollPane);
		add(goToLobby);
	}

	public void initWindow() {

		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
		keys = new RaceKeyHandler(player.getCar());
		visual = new RaceVisual(player, this);
		WIDTH = device.getDisplayMode().getWidth();
		HEIGHT = device.getDisplayMode().getHeight();

		racingWindow = new JFrame();
		racingWindow.setTitle("The race");
		device.setFullScreenWindow(racingWindow);
//		racingWindow.setBounds(50, 50, 500, 500);
		racingWindow.setResizable(false);
		racingWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		racingWindow.setLocationRelativeTo(null);

		racingWindow.add(visual);
		racingWindow.addKeyListener(keys);
		racingWindow.requestFocus();
		
		racingWindow.setVisible(true);
		racingWindow.pack();
	}

	public void tick() {
		racingWindow.requestFocus();
		player.getCar().updateSpeed();
		checkDistanceLeft();
	}
	
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 20.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while (SceneHandler.instance.getCurrentScene().getClass().equals(Race.class)) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				delta--;
				
				visual.tick();
				tick();

			}
			frames++;
			visual.render();
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS RACEEE: " + frames);
				frames = 0;
			}
		}
	}
	
	public void checkDistanceLeft() {
		if(player.getCar().getDistance() >= currentLength) {
			//Push results and wait for everyone to finish. Then get a winner.
			
			
		}
	}
	
	/**
	 * Creates a new racetrack somewhere in the world and with some length of some type.
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

}
