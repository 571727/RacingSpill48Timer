package scenes;

import javax.swing.JLabel;

import adt.Scene;

public class Lobby extends Scene {
//Holder på en id?
	
	JLabel label;
	
	public Lobby() {
		label = new JLabel("HELLLLLLLLLLLLLLLLOOOOO?!");
		add(label);
	}
	
	public void update() {
		
	}
}
