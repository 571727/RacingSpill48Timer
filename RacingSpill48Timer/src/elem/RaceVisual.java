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

	private Player player;
	private GraphicsEnvironment env;
	private GraphicsDevice device;
	private GraphicsConfiguration config;
	private BufferedImage image;
	private int y;

	public RaceVisual(Player player) {
		this.player = player;
		env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = env.getDefaultScreenDevice();
		config = device.getDefaultConfiguration();

		y = 0; 
		
		try {
			
			image = ImageIO.read(RaceVisual.class.getResourceAsStream("/pics/" + player.getCar().getCarStyle() + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void tick() {

	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.white);
		g.fillRect(0, 0, Race.WIDTH, Race.HEIGHT);
		
		g.drawImage(image, 0, y, Race.WIDTH, Race.HEIGHT, null);

//		if (clutch)
//			g.drawString("clutching", 500, 100);
//		else
//			g.drawString("not clutching", 500, 100);
//		g.drawString(String.valueOf(gear), 500, 150);

//		g.setColor(Color.green);
//		g.fillRect(0, 0, Race.WIDTH, Race.HEIGHT);
//		
//		g.setColor(Color.black);
//		g.drawString("Fart er " + player.getCar().getSpeed(), 200, 200);
		g.dispose();
		bs.show();
	}

}
