package scenes;

import java.awt.Label;

import javax.swing.JLabel;

import adt.Scene;
import handlers.GameHandler;

public class Lobby extends Scene {
//Holder på en id?
	
	private static JLabel label;
	
	public Lobby() {
		label = new JLabel("HELLLLLLLLLLLLLLLLOOOOO?!");
		add(label);
		
	}

	
	public static void update(String string) {
		label.setText(string);
		
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}
}
