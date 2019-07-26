package scenes;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import adt.Visual;
import adt.VisualElement;
import elem.Animation;
import elem.Player;
import startup.Main;

public class RaceVisual extends Visual {

	private Race race;
	private Player player;

	private BufferedImage carImage;
	private BufferedImage tachopointer;
	private BufferedImage tachometer;
	private BufferedImage fastness;
	private Animation background;
	private Animation nitros;

	private AffineTransform identity = new AffineTransform();
	private boolean startCountDown;
	private boolean running;
	private long startTime;
	private int x;
	private int y;
	private int width;
	private int height;
	private int baller = 0;
	private Color ballColor;
	private int widthTachometer;
	private int heightTachometer;
	private int xTachometer;
	private int yTachometer;
	private double scaleXTachopointer;
	private double scaleYTachopointer;
	private int xTachopointer;
	private int yTachopointer;
	private int xGear;
	private int yGear;
	private int xSpeed;
	private int ySpeed;
	private int xDistance;
	private int yDistance;
	private float blurSpeed;
	private int blurShake;

	public RaceVisual(Player player, Race race) {
		super();
		visualElements = new ArrayList<VisualElement>();
		this.race = race;
		this.player = player;

		widthTachometer = (int) (Race.WIDTH / 5f);
		heightTachometer = (int) (widthTachometer / 311f * 225f);
		xTachometer = Race.WIDTH - (widthTachometer + (Race.WIDTH / 24));
		yTachometer = Race.HEIGHT - (heightTachometer + (Race.HEIGHT / 24));

		xTachopointer = (int) (xTachometer + widthTachometer / 1.6);
		yTachopointer = (int) (yTachometer + heightTachometer / 1.9);
		scaleXTachopointer = widthTachometer / 380.0;
		scaleYTachopointer = heightTachometer / 270.0;
		// var 1.7 og 1.2
		xGear = (int) (xTachometer + widthTachometer / 1.7);
		yGear = (int) (yTachometer + heightTachometer / 1.15);
		xSpeed = (int) (xTachometer + widthTachometer / 10);
		ySpeed = (int) (yTachometer + heightTachometer / 1.2);
		xDistance = 100;
		yDistance = 100;

		blurShake = 3;
		blurSpeed = 220;

		y = 0;
		startTime = 0;
		startCountDown = false;

	}

	@Override
	public void tick() {
		if (player.getCar().isGas() && !player.getCar().isClutch()) {
			if (player.getCar().isNOSON()) {
				y = -15;
				x = -16;
				width = Race.WIDTH + 32;
				height = Race.HEIGHT + 16;
				nitros.setCurrentFrame(r.nextInt(4));
			} else {
				y = -9;
				x = -8;
				width = Race.WIDTH + 16;
				height = Race.HEIGHT + 9;
			}
		} else {
			y = -2;
			x = -8;
			width = Race.WIDTH + 16;
			height = Race.HEIGHT + 9;
		}

		background.setCurrentFrame(
				(background.getCurrentFrame() + player.getCar().getSpeedActual() / 100) % background.getFrameCount());
	}

	@Override
	public void render(Graphics g) {
		try {
			bs = this.getBufferStrategy();
			if (bs == null) {
				this.createBufferStrategy(3);
				return;
			}
			g = bs.getDrawGraphics();

			Graphics2D g2d = (Graphics2D) g;

			g2d.drawImage(background.getFrame(), 0, 0, Race.WIDTH, Race.HEIGHT, null);

			AffineTransform trans = new AffineTransform();
			shakeAndScaleImage(trans, carImage, x, y, width, height, (float) player.getCar().getSpeedActual(), blurSpeed / 2,
					blurSpeed * 1.5f, blurShake * 2);
			if (player.getCar().isIdle())
				rotateIdle(trans, (player.getCar().getRpm() / player.getCar().getTotalRPM() + 1), blurShake * 2);

			g2d.drawImage(carImage, trans, this);

			if (player.getCar().isNOSON()) {
				blur(g2d, nitros.getFrame(), 0, 0, Race.WIDTH, Race.HEIGHT,
						(float) player.getCar().getNosStrengthStandard(), 0f, 2.5f, blurShake);
			}

			if (player.getCar().getSpeedActual() > blurSpeed)
				blur(g2d, fastness, 0, 0, Race.WIDTH, Race.HEIGHT, (float) player.getCar().getSpeedActual(), blurSpeed,
						100f, blurShake);

			g2d.setFont(font);

			// DEBUG
			drawDebug(g2d, 300);

			// Prerace stuff
			drawRaceHUD(g2d);

			drawTachometer(g2d);

			drawInfoHUD(g2d);

			for (int i = 0; i < visualElements.size(); i++) {
				visualElements.get(i).render(g);
			}
		} finally {
			if (g != null) {
				g.dispose();
			}
		}
		bs.show();
		Toolkit.getDefaultToolkit().sync();
	}

	private void rotateIdle(AffineTransform trans, double comparedValue, double shake) {

		double finetuneShake = 16.0;

		double ranShake = r.nextInt((int) (shake * comparedValue)) / finetuneShake;
		double rad = Math.toRadians(ranShake - (shake / (2 * finetuneShake)));
		trans.rotate(rad, width / 2, height - (height / 8));

		// TODO overgang fra risting til annet
	}

	private void shakeAndScaleImage(AffineTransform trans, BufferedImage img, int x, int y, int width, int height,
			double comparedValue, double fromValue, double tillAdditionalValue, double shake) {

		double alpha = alpha(comparedValue, fromValue, tillAdditionalValue, shake);
		trans.setTransform(identity);

		double imgW = img.getWidth();
		double imgH = img.getHeight();

		double xShake = shake(shake, alpha);
		double yShake = shake(shake, alpha);

		width = (int) (width + 2 * shake) + (int) xShake;
		height = (int) (height + 2 * shake) + (int) yShake;

		trans.scale(width / imgW, height / imgH);
		trans.translate((-shake + x) / imgW, (-shake + y) / imgH);
	}

	private double shake(double amount) {
		return r.nextInt((int) amount * 2) - amount;
	}

	private double shake(double shake, double alpha) {
		return shake(shake) * alpha;
	}

	private void blur(Graphics2D g2d, BufferedImage img, int x, int y, int width, int height, float comparedValue,
			double fromValue, double tillAdditionalValue, int shake) {
		double alpha = alpha(comparedValue, fromValue, tillAdditionalValue, shake);

		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha);
		g2d.setComposite(ac);

		double xShake = shake(shake, alpha);
		double yShake = shake(shake, alpha);

		g2d.drawImage(img, -shake + x, -shake + y, (width + 2 * shake) + (int) xShake,
				(height + 2 * shake) + (int) yShake, null);
		g2d.setComposite(ac.derive(1f));
	}

	private double alpha(double comparedValue, double fromValue, double tillAdditionalValue, double shake) {
		double alpha = (comparedValue - fromValue) / tillAdditionalValue;

		if (alpha > 1f)
			alpha = 1f;

		alpha += (shake(shake) / tillAdditionalValue) * alpha;

		if (alpha > 1f)
			alpha = 1f;
		else if (alpha < 0f) {
			alpha = 0f;
		}
		return alpha;
	}

	private void drawDebug(Graphics2D g, int h) {
		g.setColor(Color.green);
		g.drawString("SpeedActual: " + String.valueOf(player.getCar().getSpeedActual()), 100, 100 + h);
		g.drawString("Tachometer rotation: " + String.valueOf(player.getCar().getTachometer()), 100, 175 + h);
		g.drawString("SpeedLinear: " + String.valueOf(player.getCar().getSpeedLinear()), 100, 125 + h);
		g.drawString("Engine On: " + String.valueOf(player.getCar().isEngineOn()), 100, 150 + h);
		g.drawString("Clutch: " + String.valueOf(player.getCar().isClutch()), 100, 200 + h);

		g.drawString("Place: " + String.valueOf(race.getCurrentPlace()), 100, 250 + h);
		g.drawString("Distance: " + String.valueOf(race.getCurrentLength()), 100, 300 + h);
		g.drawString("Distance covered: " + String.valueOf(player.getCar().getDistance()), 100, 350 + h);
		g.drawString(String.valueOf(System.currentTimeMillis() - startTime), 100, 375 + h);
		g.drawString("RPM: " + player.getCar().getRpm(), xSpeed - 400, ySpeed);
	}

	private void drawRaceHUD(Graphics2D g) {
		if (!running) {
			g.drawString("Wait until everyone is ready", Race.WIDTH / 2 - 100, Race.HEIGHT / 6);
		}

		g.setColor(ballColor);

		if (player.getCar().isGearTooHigh())
			g.drawString("YOU'VE SHIFTED TOO EARLY!!!", Race.WIDTH / 2 - 100, Race.HEIGHT / 7);

		for (int i = 0; i < baller; i++) {
			g.fillOval(Race.WIDTH / 2 + (-100 + (75 * i)), Race.HEIGHT / 3, 50, 50);
		}

		if (race.isCheating()) {
			g.drawString("You went too early!!!", Race.WIDTH / 2 - 50, Race.HEIGHT / 8);
		}
	}

	/**
	 * Angles, nitros, gear
	 */
	private void drawTachometer(Graphics2D g) {

		g.drawImage(tachometer, xTachometer, yTachometer, widthTachometer, heightTachometer, null);

		AffineTransform trans = new AffineTransform();
		trans.setTransform(identity);
		trans.translate(xTachopointer,
				yTachopointer - (tachopointer.getHeight() * (0.005 * player.getCar().getTachometer() + 0.85)));
		trans.scale(scaleXTachopointer, scaleYTachopointer);
		trans.rotate(Math.toRadians(player.getCar().getTachometer()));
		g.drawImage(tachopointer, trans, this);

		g.setColor(Color.white);
		g.drawString(String.format("%.0f", player.getCar().getSpeedActual()), xSpeed, ySpeed);
		if (player.getCar().getGear() > 0)
			g.drawString(String.valueOf(player.getCar().getGear()), xGear, yGear);
		else
			g.drawString("N", xGear, yGear);
	}

	private void drawInfoHUD(Graphics2D g) {
		g.drawString("Place: " + String.valueOf(race.getCurrentPlace()), xDistance, yDistance);
		g.drawString("Distance: " + String.valueOf(race.getCurrentLength()), xDistance, yDistance + 50);
		g.drawString("Distance covered: " + String.format("%.0f", player.getCar().getDistance()), xDistance,
				yDistance + 100);
		if (System.currentTimeMillis() - startTime - 3000 < 1000000
				&& System.currentTimeMillis() - startTime - 3000 >= 0)
			g.drawString(
					"Time in sec: " + String.format("%.2f",
							Float.valueOf(System.currentTimeMillis() - startTime - 3000) / 1000),
					xDistance, yDistance + 150);

		g.drawString("NOS bottles left: " + String.valueOf(player.getCar().getNosAmountLeft()), xDistance,
				yDistance + 200);

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

	public Color getBallColor() {
		return ballColor;
	}

	public void setBallColor(Color ballColor) {
		this.ballColor = ballColor;
	}

	public void setBallCount(int i) {
		baller = i;
	}

	@Override
	public void setRace(Race race) {
		this.race = race;
	}

	@Override
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * TODO Does not matter here
	 */
	@Override
	public boolean hasAnimationsRunning() {
		return false;
	}

	public BufferedImage getCarImage() {
		return carImage;
	}

	public void setCarImage(BufferedImage carImage) {
		this.carImage = carImage;
	}

	public BufferedImage getTachopointer() {
		return tachopointer;
	}

	public void setTachopointer(BufferedImage tachopointer) {
		this.tachopointer = tachopointer;
	}

	public BufferedImage getTachometer() {
		return tachometer;
	}

	public void setTachometer(BufferedImage tachometer) {
		this.tachometer = tachometer;
	}

	public BufferedImage getFastness() {
		return fastness;
	}

	public void setFastness(BufferedImage fastness) {
		this.fastness = fastness;
	}

	public void setBackground(Animation background) {
		this.background = background;
	}

	public Animation getNitros() {
		return nitros;
	}

	public void setNitros(Animation nitros) {
		this.nitros = nitros;
	}

}
