package scenes;

import java.awt.event.ActionEvent;

import javax.swing.JButton;

import adt.Scene;

public class MainMenu extends Scene {

	private JButton options;
	private JButton host;
	private JButton join;

	public MainMenu() {

		options = new JButton("Options");
		host = new JButton("Host");
		join = new JButton("Join");

		options.addActionListener((ActionEvent e) -> {
			
		});
		host.addActionListener((ActionEvent e) -> {
			//init some shit and then go to lobby
		});
		join.addActionListener((ActionEvent e) -> {
			//init some shit like ip and then go to lobby
		});

		add(options);
		add(host);
		add(join);
	}

}
