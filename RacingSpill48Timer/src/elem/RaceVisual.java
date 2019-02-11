package elem;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import scenes.Race;

public class RaceVisual extends Canvas {

	private Race race;
	private Player player;
	private GraphicsEnvironment env;
	private GraphicsDevice device;
	private GraphicsConfiguration config;
	private BufferedImage carImage;
	private BufferedImage[] backgroundImages;
	private boolean startCountDown;
	private boolean running;
	private long startTime;
	private float currentBackground;
	private int y;

	public RaceVisual(Player player, Race race) {
		this.player = player;
		this.race = race;
		env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = env.getDefaultScreenDevice();
		config = device.getDefaultConfiguration();
		backgroundImages = new BufferedImage[6];
		y = 0;
		currentBackground = 0;
		startTime = 0;
		startCountDown = false;
		try {

			carImage = ImageIO
					.read(RaceVisual.class.getResourceAsStream("/pics/" + player.getCar().getCarStyle() + ".png"));

			for (int i = 1; i <= backgroundImages.length; i++) {
				backgroundImages[i - 1] = ImageIO.read(RaceVisual.class.getResourceAsStream("/pics/road" + i + ".png"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void tick() {
		if (player.getCar().isGas() && !player.getCar().isClutch()) {
			y = -9;
		} else
			y = -2;

		currentBackground = ((currentBackground + player.getCar().getSpeedActual() / 100)
				% backgroundImages.length);
	}

	public void render() {

		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		
		g.drawImage(backgroundImages[(int) currentBackground], 0, 0, Race.WIDTH, Race.HEIGHT, null);

		
		
		g.drawImage(carImage, -8, y, Race.WIDTH + 16, Race.HEIGHT + 9, null);

		g.setColor(Color.green);
		g.drawString("Speed: " + String.valueOf(player.getCar().getSpeedActual()), 100, 100);
		g.drawString("Gear: " + String.valueOf(player.getCar().getGear()), 100, 150);
		g.drawString("Clutch: " + String.valueOf(player.getCar().isClutch()), 100, 200);

		g.drawString("Place: " + String.valueOf(race.getCurrentPlace()), 100, 250);
		g.drawString("Distance: " + String.valueOf(race.getCurrentLength()), 100, 300);
		g.drawString("Distance covered: " + String.valueOf(player.getCar().getDistance()), 100, 350);

		
		if(startCountDown && !running) {
			if(1000 > System.currentTimeMillis() - startTime) {
				g.setColor(Color.red);
				g.fillOval(Race.WIDTH / 2 - 100, Race.HEIGHT / 3 , 50, 50);
			} else if(2000 > System.currentTimeMillis() - startTime) {
				g.setColor(Color.red);
				g.fillOval(Race.WIDTH / 2 - 100, Race.HEIGHT / 3 , 50, 50);
				g.fillOval(Race.WIDTH / 2 - 25, Race.HEIGHT / 3 , 50, 50);
			} else if(3000 > System.currentTimeMillis() - startTime) {
				g.setColor(Color.red);
				g.fillOval(Race.WIDTH / 2 - 100, Race.HEIGHT / 3 , 50, 50);
				g.fillOval(Race.WIDTH / 2 - 25, Race.HEIGHT / 3 , 50, 50);
				g.fillOval(Race.WIDTH / 2 + 50, Race.HEIGHT / 3 , 50, 50);
			} else if(4000 > System.currentTimeMillis() - startTime) {
				g.setColor(Color.green);
				g.fillOval(Race.WIDTH / 2 - 100, Race.HEIGHT / 3 , 50, 50);
				g.fillOval(Race.WIDTH / 2 - 25, Race.HEIGHT / 3 , 50, 50);
				g.fillOval(Race.WIDTH / 2 + 50, Race.HEIGHT / 3 , 50, 50);
			}
		} else if (!running) {
			g.drawString("Wait until everyone is ready", Race.WIDTH / 2 - 100, Race.HEIGHT / 5);
		}
		
		
		g.dispose();
		bs.show();
	}

	public boolean isStartCountDown() {
		return startCountDown;
	}

	public void setStartCountDown(boolean startCountDown) {
		this.startCountDown = startCountDown;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

}
