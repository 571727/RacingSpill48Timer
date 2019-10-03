package scenes.visual;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import adt.Visual;
import adt.VisualElement;
import elem.Animation;
import elem.MovingAnimation;
import elem.PlacedAnimation;
import elem.Player;
import scenes.Race;
import startup.Main;

public class RaceVisual extends Visual {

	private Race race;
	private Player player;

	private PlacedAnimation[] gearButtons;
	private MovingAnimation nitrosButton;
	private MovingAnimation[] warningButtons;
	private MovingAnimation tireboost;
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
	private int ballCount = 0;
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
	private double blurSpeed;
	private double blurShake;

	private int amountRPMnumbers;
	private double angle;
	private double angleFrom;
	private double angleTo;
	private double angleDistance;
	private double distance;
	private Font rpmFont;
	private Color rpmColor;
	private int[] xRPM;
	private int[] yRPM;
	private String[] rpmStr;

	private Font NOSFont;
	private BufferedImage tireboostNone;

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

		// NUMBERS ON TACHOMETER
		angleFrom = -202;
		angleTo = 30;
		amountRPMnumbers = 8;
		angleDistance = (Math.abs(angleFrom) + Math.abs(angleTo));
		angle = angleDistance / amountRPMnumbers;
		distance = widthTachometer / 4.7;
		rpmFont = new Font("Calibri", 0, (int) (Race.WIDTH / 85.0f));
		NOSFont = new Font("Calibri", 0, (int) (Race.WIDTH / 12.0f));
		rpmColor = new Color(200, 185, 185);
		double xRPM = xTachopointer - (xTachopointer / 130);
		double yRPM = yTachopointer + (yTachopointer / 400);

		this.xRPM = new int[amountRPMnumbers + 1];
		this.yRPM = new int[amountRPMnumbers + 1];
		this.rpmStr = new String[amountRPMnumbers + 1];

		for (int i = 0; i <= amountRPMnumbers; i++) {
			// Go to center of tachometer
			// Find the angles and determine how far up/down and left/right from there with
			// a fixed distance.
			// AKA we know the hypotenuse and the angle in degrees.
			double angle = angleFrom + (this.angle * i);
			double radian = (angle * (Math.PI)) / 180;

			this.xRPM[i] = (int) (xRPM + (Math.cos(radian) * distance));
			this.yRPM[i] = (int) (yRPM + (Math.sin(radian) * distance));
			double rpmNum = player.getCar().getRpmTop() * ((this.angle * i) / angleDistance) / 1000.0;
			rpmStr[i] = String.format("%.1f", Math.round(rpmNum * 4) / 4.0);
		}

		// var 1.7 og 1.2
		xGear = (int) (xTachometer + widthTachometer / 1.7);
		yGear = (int) (yTachometer + heightTachometer / 1.15);
		xSpeed = xTachometer + widthTachometer / 10;
		ySpeed = (int) (yTachometer + heightTachometer / 1.2);
		xDistance = 100;
		yDistance = 100;

		blurShake = 3.0;
		blurSpeed = 220;

		y = 0;
		startTime = 0;
		startCountDown = false;

		// HELP
		gearButtons = new PlacedAnimation[9];
		for (int i = 0; i < gearButtons.length; i++) {
			gearButtons[i] = new PlacedAnimation("gearButtons" + i, -1, xTachometer, Race.HEIGHT - heightTachometer);
			gearButtons[i].scale(3);
		}
		warningButtons = new MovingAnimation[2];
		for (int i = 0; i < warningButtons.length; i++) {
			int padding = 0;
			if (i != 0) {
				padding = (int) (gearButtons[i - 1].getHeight() * 1.2);
			}
			int y = Race.HEIGHT - Race.HEIGHT / 10;
			warningButtons[i] = new MovingAnimation("warningButtons" + i, -1, Race.WIDTH / 2, y - padding,
					Race.WIDTH / 2, (int) (y - Race.HEIGHT / 32) - padding, 15);
			warningButtons[i].scale(3);
		}
		nitrosButton = new MovingAnimation("e", -1, (int) (xDistance * 2.4),
				(int) (Race.HEIGHT - NOSFont.getSize() * 1.85), (int) (xDistance * 2.4),
				(int) (Race.HEIGHT - NOSFont.getSize() * 1.7), 15);
		nitrosButton.scale(4);
		tireboost = new MovingAnimation("tireboost", -1, Race.WIDTH / 2, (int) (Race.HEIGHT / 2.3), Race.WIDTH / 2,
				(int) (Race.HEIGHT / 2.3 - Race.HEIGHT / 520), 3);
		tireboost.scale(3);

		try {
			tireboostNone = ImageIO.read(this.getClass().getResourceAsStream("/pics/tireboost_none.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void tick(double tickFactor) {
		if (player.getCar().isGas() && !player.getCar().isClutch() && player.getCar().getGear() != 0) {

			int add = (int) (player.getCar().getSpdinc() * 14);
			y = -2 - add;
			x = -8 - add / 2;
			width = (int) (Race.WIDTH + 16 + add * 0.8);
			height = Race.HEIGHT + 9 + add / 2;

			if (player.getCar().isNOSON()) {
				y -= 15;
				x -= 16;
				width += 32;
				height += 16;
				nitros.setCurrentFrame(r.nextInt(4));
			}
			if (player.getCar().isGearBoostON()) {
				y -= 5;
				x -= 5;
				width += 16;
				height += 10;
			}
		} else {
			y = -2;
			x = -8;
			width = Race.WIDTH + 16;
			height = Race.HEIGHT + 9;
		}

		for (MovingAnimation ma : warningButtons) {
			ma.incrementMovement(tickFactor);
		}
		nitrosButton.incrementMovement(tickFactor);
		tireboost.incrementMovement(tickFactor);

		background
				.setCurrentFrame((background.getCurrentFrame() + (player.getCar().getSpeedActual() / 100) * tickFactor)
						% background.getFrameCount());
	}

	@Override
	public void render(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		g2d.drawImage(background.getFrame(), 0, 0, Race.WIDTH, Race.HEIGHT, null);

		AffineTransform trans = new AffineTransform();
		shakeAndScaleImage(trans, carImage, x, y, width, height, (float) player.getCar().getSpeedActual(),
				blurSpeed / 2, blurSpeed * 1.5f, blurShake * 2);
		if (player.getCar().isIdle())
			rotateIdle(trans, ((double) player.getCar().getRpm() / (double) player.getCar().getRpmTop() + 1),
					blurShake / 16);

		g2d.drawImage(carImage, trans, this);

		if (player.getCar().isNOSON()) {
			blur(g2d, nitros.getFrame(), 0, 0, Race.WIDTH, Race.HEIGHT,
					(float) player.getCar().getNosStrengthStandard(), 0f, 2.5f, blurShake);
		} else if (player.getCar().isGearBoostON()) {
			shakeAndScaleImage(trans, carImage, x, y, width, height, 1, 1, 1, blurShake * 2);
		}

		if (player.getCar().getSpeedActual() > blurSpeed)
			blur(g2d, fastness, 0, 0, Race.WIDTH, Race.HEIGHT, (float) player.getCar().getSpeedActual(), blurSpeed,
					100f, blurShake);

		g2d.setFont(font);

		// DEBUG
		if (Main.DEBUG)
			drawDebug(g2d, 300);

		// Prerace stuff
		drawRaceHUD(g2d);

		drawTachometer(g2d);
		drawRightShift(g2d);
		drawInfoHUD(g2d);

		for (int i = 0; i < visualElements.size(); i++) {
			visualElements.get(i).render(g);
		}

	}

	private void rotateIdle(AffineTransform trans, double comparedValue, double shake) {

		double finetuneShake = 16.0;
		comparedValue = Math.pow(comparedValue, 2);

		double ranShake = r.nextInt((int) (shake * 100 * comparedValue)) / (100 * finetuneShake);
		double rad = Math.toRadians(ranShake - (shake / (2 * finetuneShake)));
		trans.rotate(rad, width / 2, height - (height / 8));

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

		trans.translate((-shake + x) / imgW * 100, (-shake + y) / imgH * 100);
		trans.scale(width / imgW, height / imgH);
//		System.out.println(trans.getTranslateX() + ", " + trans.getTranslateY());
	}

	private double shake(double amount) {
		return r.nextInt((int) amount * 2) - amount;
	}

	private double shake(double shake, double alpha) {
		return shake(shake) * alpha;
	}

	private void blur(Graphics2D g2d, BufferedImage img, int x, int y, int width, int height, float comparedValue,
			double fromValue, double tillAdditionalValue, double shake) {
		double alpha = alpha(comparedValue, fromValue, tillAdditionalValue, shake);

		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha);
		g2d.setComposite(ac);

		double xShake = shake(shake, alpha);
		double yShake = shake(shake, alpha);

		g2d.drawImage(img, (int) (shake + x), (int) (-shake + y), (width + 2 * (int) shake) + (int) xShake,
				(height + 2 * (int) shake) + (int) yShake, null);
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
		g.drawString("resistence: " + String.valueOf(player.getCar().getResistance()), 100, 200 + h);

		g.drawString("Place: " + String.valueOf(race.getCurrentPlace()), 100, 250 + h);
		g.drawString("Distance: " + String.valueOf(race.getCurrentLength()), 100, 300 + h);
		g.drawString("Distance covered: " + String.valueOf(player.getCar().getDistance()), 100, 350 + h);
		g.drawString(String.valueOf(System.currentTimeMillis() - startTime), 100, 375 + h);
		g.drawString("RPM: " + player.getCar().getRpm(), xSpeed - 400, ySpeed);
	}

	private void drawRaceHUD(Graphics2D g) {

		if (!running && ballCount <= 0) {
			g.setColor(Color.WHITE);
			g.drawString("Wait until everyone is ready", Race.WIDTH / 2 - Race.WIDTH / 10, Race.HEIGHT / 6);
		}
		if (player.getCar().isGearTooHigh()) {
			g.setColor(Color.RED);
			g.drawString("YOU'VE SHIFTED TOO EARLY!!!", Race.WIDTH / 2 - Race.WIDTH / 10, Race.HEIGHT / 7);
		}
		if (race.isCheating()) {
			g.setColor(Color.RED);
			g.drawString("YOU WENT TOO EARLY!!!", Race.WIDTH / 2 - Race.WIDTH / 8, Race.HEIGHT / 8);
		}

		g.setColor(ballColor);
		for (int i = 0; i < ballCount; i++) {
			g.fillOval((int) (Race.WIDTH / 2
					+ (-tireboost.getHalfWidth() - (tireboost.getHeight() * 0.6) + (tireboost.getHalfWidth() * i))),
					Race.HEIGHT / 3, (int) (tireboost.getHeight() * 1.2), (int) (tireboost.getHeight() * 1.2));
		}

	}

	/**
	 * Angles, nitros, gear
	 */
	private void drawTachometer(Graphics2D g) {

		g.drawImage(tachometer, xTachometer, yTachometer, widthTachometer, heightTachometer, null);

		// Draw rpm numbers
		g.setFont(rpmFont);
		g.setColor(rpmColor);
		for (int i = 0; i <= amountRPMnumbers; i++) {
			g.drawString(rpmStr[i], xRPM[i], yRPM[i]);
		}
		g.setFont(font);
		g.setColor(Color.white);

		AffineTransform trans = new AffineTransform();
		trans.setTransform(identity);
		trans.translate(xTachopointer,
				yTachopointer - (tachopointer.getHeight() * (0.005 * player.getCar().getTachometer() + 0.85)));
		trans.scale(scaleXTachopointer, scaleYTachopointer);
		trans.rotate(Math.toRadians(player.getCar().getTachometer()));
		g.drawImage(tachopointer, trans, this);

		g.drawString(String.format("%.0f", player.getCar().getSpeedActual()), xSpeed, ySpeed);

		int currentGear = player.getCar().getGear();
		int nextGear = currentGear + 1;
		int prevGear = currentGear - 1;

		BufferedImage next = null;
		BufferedImage prev = null;

		if (!player.getCar().isSequentialShift()) {
			if (prevGear >= 0)
				prev = gearButtons[prevGear].getFrame();
			if (nextGear <= player.getCar().getGearTop())
				next = gearButtons[nextGear].getFrame();
		} else {
			prev = gearButtons[gearButtons.length - 2].getFrame();
			next = gearButtons[gearButtons.length - 1].getFrame();
		}

		if (player.getCar().getGear() > 0) {

			g.drawString(String.valueOf(player.getCar().getGear()), xGear, yGear);

			g.drawImage(prev, gearButtons[prevGear].getX() - (gearButtons[0].getWidth()), gearButtons[prevGear].getY(),
					gearButtons[prevGear].getHalfWidth(), gearButtons[prevGear].getHalfHeight(), null);

		} else {
			g.drawString("N", xGear, yGear);
		}

		if (nextGear <= player.getCar().getGearTop())
			g.drawImage(next, gearButtons[nextGear].getX(), gearButtons[nextGear].getY(),
					gearButtons[nextGear].getWidth(), gearButtons[nextGear].getHeight(), null);

	}

	private void drawRightShift(Graphics g) {
		int rs = player.getCar().rightShift();
		if (!race.isRunning()) {
			if (rs > 0) {
				g.drawImage(tireboost.getFrame(), tireboost.getX() - tireboost.getHalfWidth(), tireboost.getY(),
						tireboost.getWidth(), tireboost.getHeight(), null);
			} else {
				g.drawImage(tireboostNone, tireboost.getX() - tireboost.getHalfWidth(), tireboost.getY(),
						tireboost.getWidth(), tireboost.getHeight(), null);
			}
		}
	}

	private void drawInfoHUD(Graphics2D g) {
		g.setColor(Color.WHITE);
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

		// NOS
		g.drawString("NOS bottles left: ", xDistance, (int) (Race.HEIGHT - NOSFont.getSize() * 2.0));
		g.setFont(NOSFont);
		int bottles = player.getCar().getNosBottleAmountLeft();
		if (bottles > 0) {
			g.setColor(Color.GREEN);
			g.drawImage(nitrosButton.getFrame(), nitrosButton.getX() - nitrosButton.getHalfWidth(), nitrosButton.getY(),
					nitrosButton.getWidth(), nitrosButton.getHeight(), null);
		} else
			g.setColor(Color.RED);
		g.drawString(String.valueOf(bottles), xDistance, (int) (Race.HEIGHT - NOSFont.getSize() * 1.2));
		g.setColor(Color.WHITE);
		g.setFont(font);

		// HELP
		if (player.getCar().isEngineOn() == false) {
			g.drawImage(warningButtons[1].getFrame(), warningButtons[1].getX() - warningButtons[1].getHalfWidth(),
					warningButtons[1].getY(), warningButtons[1].getWidth(), warningButtons[1].getHeight(), null);
		}
		if (player.getCar().isFailedShift()) {
			g.drawImage(warningButtons[0].getFrame(), warningButtons[0].getX() - warningButtons[0].getHalfWidth(),
					warningButtons[0].getY(), warningButtons[0].getWidth(), warningButtons[0].getHeight(), null);
		}

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
		ballCount = i;
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
