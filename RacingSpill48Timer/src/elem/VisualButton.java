package elem;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import adt.Action;
import adt.VisualElement;

public class VisualButton extends VisualElement implements KeyListener{

	private PlacedAnimation img;
	private Action action;
	
	public VisualButton(String imgName, int amount, int x, int y, int size, Action action) {
		img = new PlacedAnimation(imgName, amount, x, y);
		img.setSize(img.getWidth() * size, img.getHeight() * size);
		this.action = action;
	}
	
	@Override
	public void mouseClicked(MouseEvent me) {
		System.out.println("MOUSE1");
	}

	@Override
	public void mouseEntered(MouseEvent me) {
		System.out.println("MOUSE2");
	}

	@Override
	public void mouseExited(MouseEvent me) {
		System.out.println("MOUSE3");
	}

	@Override
	public void mousePressed(MouseEvent me) {
		if(me.getX() >= img.getX() &&  me.getX() <= img.getX() + img.getWidth()) {
			if(me.getY() >= img.getY() &&  me.getY() <= img.getY() + img.getHeight()) {
				action.doStuff();
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		System.out.println("MOUSE4");
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		//FIXME not run after everybodydone
		g.drawImage(img.getFrame(), img.getX(), img.getY(), img.getWidth(), img.getHeight(), null);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		System.out.println(arg0.getKeyCode());
		if(arg0.getKeyCode() == 10)
			action.doStuff();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
		
		
		
		
		
		
		
		
		
		
	}

}
