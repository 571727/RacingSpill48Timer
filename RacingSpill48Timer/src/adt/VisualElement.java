package adt;

import java.awt.Graphics;

import javax.swing.event.MouseInputAdapter;

public abstract class VisualElement extends MouseInputAdapter {

	public abstract void tick();
	public abstract void render(Graphics g);
	
}
