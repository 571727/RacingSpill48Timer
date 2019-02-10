package scenes;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

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
	private RaceVisual visual;
	private RaceKeyHandler keys;
	private boolean running;
	
	public static int WIDTH;
	public static int HEIGHT; 

	public Race() {

	}

	public void initWindow() {

		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
		keys = new RaceKeyHandler(player.getCar());
		visual = new RaceVisual(player);
		WIDTH = device.getDisplayMode().getWidth();
		HEIGHT = device.getDisplayMode().getHeight();

		racingWindow = new JFrame();
		racingWindow.setTitle("The race");
		device.setFullScreenWindow(racingWindow);
//		racingWindow.setBounds(50, 50, 50, 50);
		racingWindow.setResizable(false);
		racingWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		racingWindow.setLocationRelativeTo(null);

		racingWindow.add(visual);
		racingWindow.addKeyListener(keys);
		
		racingWindow.setVisible(true);
		racingWindow.pack();
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

}
