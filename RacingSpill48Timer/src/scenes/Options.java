package scenes;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

import adt.Scene;
import handlers.GameHandler;
import handlers.SceneHandler;

public class Options extends Scene {
	private JButton goBack;
	private JButton nextSong;
	private JButton stopMusic;
	private JButton togglefullscreen;
	private JLabel volumeTitle;
	private JSlider slider;

	public Options(double volume) {

		goBack = new JButton("Go back to main menu");
		nextSong = new JButton("Play another song instead!");
		stopMusic = new JButton("Turn off/on that music!!!");
		volumeTitle = new JLabel("Volumemixer:");
		togglefullscreen = new JButton("Fullscreen: " + true);
		
		volumeTitle.setPreferredSize(new Dimension(150, 20));
		
		slider = new JSlider(JSlider.HORIZONTAL, 0, 100, (int) (volume * 200.0));
		slider.setMinorTickSpacing(1);
		slider.setMajorTickSpacing(10);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);

		slider.addChangeListener((ChangeEvent e) -> {
			JSlider source = (JSlider)e.getSource();
		
			GameHandler.volume = source.getValue() / 200.0;
			GameHandler.music.updateVolume();
	    });
		togglefullscreen.addActionListener((ActionEvent e) -> {
			boolean temp = !SceneHandler.instance.isFullScreen();
			SceneHandler.instance.setFullScreen(temp);
			togglefullscreen.setText("Fullscreen: " + temp);
			if(!temp) {
				Object[] possibilities = { 720, 1080};
			  SceneHandler.instance.setHEIGHT((int) JOptionPane.showInputDialog(null, "Choose resolution", "Carznstuff",
					JOptionPane.PLAIN_MESSAGE, null, possibilities, 720));
			}
		});
		goBack.addActionListener((ActionEvent e) -> SceneHandler.instance.changeScene(0));
		nextSong.addActionListener((ActionEvent e) -> GameHandler.music.playAndChooseNextRandomly());
		stopMusic.addActionListener((ActionEvent e) -> GameHandler.music.playOrStop());

		add(goBack);
		add(nextSong);
		add(stopMusic);
		add(volumeTitle);
		add(slider);
		add(togglefullscreen);
	}
}
