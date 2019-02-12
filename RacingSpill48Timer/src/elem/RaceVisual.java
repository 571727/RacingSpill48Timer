package elem;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
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
	private BufferedImage tachopointer;
	private BufferedImage tachometer;
	private AffineTransform identity = new AffineTransform();
	private boolean startCountDown;
	private boolean running;
	private long startTime;
	private double currentBackground;
	private int y;
	private int baller = 0;
	private int widthTachometer;
	private int heightTachometer;
	private int xTachometer;
	private int yTachometer;
	private double scaleXTachopointer;
	private double scaleYTachopointer;
	private int xTachopointer;
	private int yTachopointer;

	public RaceVisual(Player player, Race race) {
		this.player = player;
		this.race = race;

		widthTachometer = (int) (Race.WIDTH / 5f);
		heightTachometer = (int) (widthTachometer / 311f * 225f);
		xTachometer = Race.WIDTH - (widthTachometer + (Race.WIDTH / 24));
		yTachometer = Race.HEIGHT - (heightTachometer + (Race.HEIGHT / 24));

		xTachopointer = (int) (xTachometer + widthTachometer / 1.6);
		yTachopointer = (int) (yTachometer + heightTachometer / 1.9);
		scaleXTachopointer = widthTachometer / 380.0;
		scaleYTachopointer = heightTachometer / 270.0;

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

			tachopointer = ImageIO.read(RaceVisual.class.getResourceAsStream("/pics/tacho.png"));
			// 311 x 225 px
			tachometer = ImageIO.read(RaceVisual.class.getResourceAsStream("/pics/tachometer.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void tick() {
		if (player.getCar().isGas() && !player.getCar().isClutch()) {
			y = -9;
		} else
			y = -2;

		currentBackground = ((currentBackground + player.getCar().getSpeedActual() / 100) % backgroundImages.length);
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

		// DEBUG
		g.setColor(Color.green);
		g.drawString("SpeedActual: " + String.valueOf(player.getCar().getSpeedActual()), 100, 100);
		g.drawString("Tachometer rotation: " + String.valueOf(player.getCar().getTachometer()), 100, 175);
		g.drawString("SpeedLinear: " + String.valueOf(player.getCar().getSpeedLinear()), 100, 125);
		g.drawString("Gear: " + String.valueOf(player.getCar().getGear()), 100, 150);
		g.drawString("Clutch: " + String.valueOf(player.getCar().isClutch()), 100, 200);

		g.drawString("Place: " + String.valueOf(race.getCurrentPlace()), 100, 250);
		g.drawString("Distance: " + String.valueOf(race.getCurrentLength()), 100, 300);
		g.drawString("Distance covered: " + String.valueOf(player.getCar().getDistance()), 100, 350);
		g.drawString(String.valueOf(System.currentTimeMillis() - startTime), 100, 375);

		// Prerace stuff

		if (startCountDown) {
			if (1000 > System.currentTimeMillis() - startTime) {
				g.setColor(Color.red);
				baller = 1;
			} else if (2000 > System.currentTimeMillis() - startTime) {
				g.setColor(Color.red);
				baller = 2;
			} else if (3000 > System.currentTimeMillis() - startTime) {
				g.setColor(Color.red);
				baller = 3;
			} else if (4000 > System.currentTimeMillis() - startTime) {
				baller = 3;
			} else {
				baller = 0;
			}
		} else if (!running) {
			g.drawString("Wait until everyone is ready", Race.WIDTH / 2 - 100, Race.HEIGHT / 6);
		}

		for (int i = 0; i < baller; i++) {
			g.fillOval(Race.WIDTH / 2 + (-100 + (75 * i)), Race.HEIGHT / 3, 50, 50);
		}

		if (race.isCheating()) {
			g.drawString("You went too early!!!", Race.WIDTH / 2 - 50, Race.HEIGHT / 8);
		}

		// Tachometer stuff
		Graphics2D g2d = (Graphics2D) g;
		g.drawImage(tachometer, xTachometer, yTachometer, widthTachometer, heightTachometer, null);

		AffineTransform trans = new AffineTransform();
		trans.setTransform(identity);
		trans.translate(xTachopointer, yTachopointer - (tachopointer.getHeight() * (0.005 * player.getCar().getTachometer() + 0.85)));
		trans.scale(scaleXTachopointer, scaleYTachopointer);
		trans.rotate(Math.toRadians(player.getCar().getTachometer()));
		g2d.drawImage(tachopointer, trans, this);

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
