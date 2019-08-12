package adt;


import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public abstract class Scene extends JPanel{

	/**
	 * Generated value
	 */
	private static final long serialVersionUID = 1768390057592575498L;
	protected BufferedImage backgroundImage;
	protected Visual currentVisual;

	public Scene(String sceneName) {
		
		try {
			backgroundImage = ImageIO.read(getClass().getResourceAsStream("/pics/back/" + sceneName + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Scene() {
	}
}
