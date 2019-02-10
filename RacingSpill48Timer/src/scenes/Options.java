package scenes;

import java.awt.event.ActionEvent;

import javax.swing.JButton;

import adt.Scene;
import handlers.SceneHandler;

public class Options extends Scene {
	private JButton goBack;

	public Options() {

		goBack = new JButton("Go back to main menu");

		goBack.addActionListener((ActionEvent e) -> SceneHandler.instance.changeScene(0));

		add(goBack);
	}
}
