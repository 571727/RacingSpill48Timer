package window;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import elem.Player;

public class Windows extends JFrame {

	/**
	 * Generated value
	 */
	private static final long serialVersionUID = 8073737083600458127L;
	private WindowAdapter closingProtocol;

	public Windows(int width, int height, String title) {
		setTitle(title);
		setBounds(50, 50, width, height);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLocationRelativeTo(null);
		setResizable(false);

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
