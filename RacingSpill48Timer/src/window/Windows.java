package window;

import javax.swing.JFrame;


public class Windows extends JFrame {

	/**
	 * Generated value
	 */
	private static final long serialVersionUID = 8073737083600458127L;

	public Windows(int width, int height, String title) {
		setBounds(50, 50, width, height);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);

	}

}
