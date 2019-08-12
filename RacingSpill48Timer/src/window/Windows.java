package window;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import elem.Player;
import startup.Main;

public class Windows extends JFrame {

	/**
	 * Generated value
	 */
	private static final long serialVersionUID = 8073737083600458127L;
	private WindowAdapter closingProtocol;
	public static int WIDTH;
	public static int HEIGHT;

	public Windows(int width, int height, String title, Color color) {
		
		WIDTH = width;
		HEIGHT = height;
		
		setTitle(title);
		setBounds(50, 50, width, height);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		getContentPane().setBackground(color);
	}

	public void closing(Player player) {
		if (closingProtocol != null) {
			removeWindowListener(closingProtocol);
		}
		
		closingProtocol = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					player.leaveServer();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		
		addWindowListener(closingProtocol);
	}

}
