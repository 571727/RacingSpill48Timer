package elem;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import adt.Action;
import adt.VisualElement;

public class VisualButton implements VisualElement {

	private MovingAnimation img;
	private Action action;
	private boolean enabled;

	public VisualButton(String imgName, int amount, int x, int y, int size, int x2, int y2, int framesPerMovement,  Action action) {
		img = new MovingAnimation(imgName, amount, x, y, x2, y2, framesPerMovement);
		img.setSize(img.getWidth() * size, img.getHeight() * size);
		this.action = action;
		enabled = true;
	}

	@Override
	public void tick() {
		if (enabled)
			img.incrementMovement();
	}

	@Override
	public void render(Graphics g) {
		// FIXME not run after everybodydone
		if (enabled)
			g.drawImage(img.getFrame(), img.getX(), img.getY(), img.getWidth(), img.getHeight(), null);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
//		System.out.println("VISUAL BUTTON " + arg0);
		if (arg0.getKeyCode() == 10)
			action.doStuff();
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

}
