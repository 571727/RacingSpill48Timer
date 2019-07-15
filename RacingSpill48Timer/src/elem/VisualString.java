package elem;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import adt.Action;
import adt.VisualElement;

public class VisualString implements VisualElement {

	
	private MovingAnimation img;
	private String[] strings;

	public VisualString(String imgName, int x, int y, int size) {
		img.setSize(img.getWidth() * size, img.getHeight() * size);
	}

	@Override
	public void tick() {
	}

	@Override
	public void render(Graphics g) {
		// FIXME not run after everybodydone
			g.drawImage(img.getFrame(), img.getX(), img.getY(), img.getWidth(), img.getHeight(), null);
			int i = 0;
			for(String s : strings) {
				g.drawString(s, img.getX(), i * (img.getY() - img.getHeight()));
				i++;
			}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	@Override
	public boolean isWithin(int x, int y) {
		return (x >= img.getX() && x <= img.getX() + img.getWidth())
				&& (y >= img.getY() && x <= img.getY() + img.getHeight());
	}

	@Override
	public void run() {
		action.doStuff();
	}

	public void setEnabled(boolean b) {
		enabled = b;
	}

	public String[] getStrings() {
		return strings;
	}

	public void setStrings(String string, String split) {
		this.strings = string.split(split);
	}
}
