package scenes;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;

import adt.Scene;
import handlers.GameHandler;
import handlers.SceneHandler;
import window.Windows;

public class Options extends Scene {
	private JButton goBack;
	private JButton nextSong;
	private JButton stopMusic;
	private JButton togglefullscreen;
	private JButton specifyRes;
	private JLabel specifyResLabel;
	private JLabel tutorial;
	private JLabel volumeTitle;
	private JSlider slider;

	public Options(double volume) {
		super("mainMenu");
		
		goBack = new JButton("Go back");
		nextSong = new JButton("Play another song instead!");
		stopMusic = new JButton("Turn off/on that music!!!");
		volumeTitle = new JLabel("Volumemixer:");
		togglefullscreen = new JButton("Fullscreen: " + true);
		specifyRes = new JButton("Specify resolution");
		specifyResLabel = new JLabel("Specified resolution: " + false);
		tutorial = new JLabel("<html><font color='white'>********** C O N T R O L S **********<br/>"+ "Turn engine over / off: T<br/>" + "Throttle: W<br/>"
				+ "Clutch: Space<br/>"
				+ "Neutral: N<br/>1st:&ensp;U<br/>2nd: J<br/>3rd:&ensp;I<br/>4th:&ensp;L<br/>5th:&ensp;P (!)<br/>6th:&ensp;L (!)<br/>"
				+ "NOS: E<br/>"
				+ "Brakes: S <br/><br/>If game SLOW use WINDOWED<br/>(not fullscreen)<br/><br/>(!) = May not apply to chosen car</font></html>");
		tutorial.setPreferredSize(new Dimension(500, 300));
		volumeTitle.setPreferredSize(new Dimension(150, 20));

		slider = new JSlider(SwingConstants.HORIZONTAL, 0, 100, (int) (volume * 100.0));
		slider.setMinorTickSpacing(1);
		slider.setMajorTickSpacing(10);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);

		slider.addChangeListener((ChangeEvent e) -> {
			JSlider source = (JSlider) e.getSource();

			GameHandler.volume = source.getValue() / 100.0;
			GameHandler.music.updateVolume();
		});
		togglefullscreen.addActionListener((ActionEvent e) -> {
			boolean temp = !SceneHandler.instance.isFullScreen();
			SceneHandler.instance.setFullScreen(temp);
			togglefullscreen.setText("Fullscreen: " + temp);
			if (!temp) {
				specifyRes.setEnabled(false);
				Object[] possibilities = { 720, 1080 };
				SceneHandler.instance.setHEIGHT((int) JOptionPane.showInputDialog(null, "Choose resolution",
						"Carznstuff", JOptionPane.PLAIN_MESSAGE, null, possibilities, 720));
				SceneHandler.instance.setSpecified(false);
				specifyResLabel.setText("Specified resolution: " + false);
			} else {
				specifyRes.setEnabled(true);
			}
		});

		specifyRes.addActionListener((ActionEvent e) -> {
			int sure = JOptionPane.showConfirmDialog(null, "Do you want the fullscreen resolution specified?");
			if (sure == 0) {
				SceneHandler.instance.setSpecified(true);
				String res;
				int result = 0;
				do {
					res = JOptionPane.showInputDialog("Write height in px (1080 or 720):");

					try {
						result = Integer.valueOf(res);
					} catch (Exception ex) {

						if (ex.getMessage().equals("null"))
							return;
						res = null;
						JOptionPane.showMessageDialog(null, "Not correctly done, sir");
					}

				} while (res == null);
				SceneHandler.instance.setHEIGHT(result);
				specifyResLabel.setText("Specified resolution: " + SceneHandler.instance.HEIGHT + "p");
			} else if (sure == 1) {
				SceneHandler.instance.setSpecified(false);
				specifyResLabel.setText("Specified resolution: " + false);
			} else {
				return;
			}

		});
		goBack.addActionListener((ActionEvent e) -> {
			SceneHandler.instance.changeScene(SceneHandler.instance.getLastScene());
			GameHandler.ba.playRegularBtn();
		});
		nextSong.addActionListener((ActionEvent e) -> GameHandler.music.playNext());
		stopMusic.addActionListener((ActionEvent e) -> GameHandler.music.playOrStop());

		
		add(goBack);
		add(nextSong);
		add(stopMusic);
		add(volumeTitle);
		add(slider);
		add(togglefullscreen);
		add(specifyResLabel);
		add(specifyRes);
		add(tutorial);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(backgroundImage, 0, 0, Windows.WIDTH, Windows.HEIGHT, null);
	}
}
