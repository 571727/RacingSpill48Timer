package adt;

import java.awt.Graphics;
import java.awt.event.KeyListener;

public interface VisualElement extends KeyListener{

	public void tick();
	public void render(Graphics g);
	public boolean isWithin(int x, int y);
	public void run();
	
}
