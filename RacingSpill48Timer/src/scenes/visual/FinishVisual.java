package scenes.visual;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import adt.Visual;
import adt.VisualElement;
import audio.SFX;
import elem.PlacedAnimation;
import elem.Player;
import scenes.Race;

public class FinishVisual extends Visual {

	private Race race;
	private Player player;

	private Queue<PlacedAnimation> finishedPlayers;
	private BufferedImage resBackground;
	private int resCarWidth;
	private int resCarHeight;
	private float resCarMovement;

	public FinishVisual(Player player, Race race) {
		this.race = race;
		this.player = player;

		resCarWidth = (int) (Race.WIDTH * 1.16f / 2);
		resCarHeight = (int) (Race.HEIGHT * 0.726f / 2);
		resCarMovement = Race.WIDTH / 7f;

		finishedPlayers = new ConcurrentLinkedQueue<PlacedAnimation>();

		visualElements = new ArrayList<VisualElement>();

	}

	@Override
	public void tick() {

		for (PlacedAnimation ma : finishedPlayers) {
			if (ma != null) {
				ma.moveX((int) resCarMovement);
				ma.incrementCurrentFrame();
				if (ma.getX() > Race.WIDTH)
					finishedPlayers.remove(ma);
			}
		}

		for (int i = 0; i < visualElements.size(); i++) {
			visualElements.get(i).tick();
		}

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
			g2d.drawImage(resBackground, 0, 0, Race.WIDTH, Race.HEIGHT, null);

			for (PlacedAnimation ma : finishedPlayers) {
				if (ma != null)
					if (ma.getX() + ma.getWidth() > 0)
						g2d.drawImage(ma.getFrame(), ma.getX(), ma.getY(), resCarWidth, resCarHeight, null);
			}

			for (int i = 0; i < visualElements.size(); i++) {
				visualElements.get(i).render(g);
			}

			if (g != null) {
				g.dispose();
			}
			bs.show();
			Toolkit.getDefaultToolkit().sync();

		} catch (Exception e) {
			System.err.println(e.getMessage() + "In visual");
		}

	}

	public void addFinish(String carname, int frameCount) {
		finishedPlayers.add(new PlacedAnimation("resCar" + carname, frameCount, -3 * resCarWidth,
				(int) (Race.HEIGHT - (Race.HEIGHT / 1.9f))));
		SFX.playMP3Sound("driveBy" + carname);
	}

	@Override
	public void setRace(Race race) {
		this.race = race;
	}

	@Override
	public void setPlayer(Player player) {
		this.player = player;
	}

	@Override
	public boolean hasAnimationsRunning() {
		return !finishedPlayers.isEmpty();
	}

	public void setBackground(BufferedImage resBackground) {
		this.resBackground = resBackground;
	}

}
