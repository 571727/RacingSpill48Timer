package scenes;

import java.awt.event.ActionEvent;

import javax.swing.JButton;

import adt.Scene;
import handlers.GameHandler;
import handlers.SceneHandler;

public class Options extends Scene {
	private JButton goBack;
	private JButton nextSong;
	private JButton stopMusic;

	public Options() {

		goBack = new JButton("Go back to main menu");
		nextSong = new JButton("Play another song instead!");
		stopMusic = new JButton("Turn off/on that music!!!");
		
		goBack.addActionListener((ActionEvent e) -> SceneHandler.instance.changeScene(0));
		nextSong.addActionListener((ActionEvent e) -> GameHandler.music.playAndChooseNextRandomly());
		stopMusic.addActionListener((ActionEvent e) -> GameHandler.music.playOrStop());
		
		add(goBack);
		add(nextSong);
		add(stopMusic);
	}
}
