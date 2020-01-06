package adt;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import window.Window;

public abstract class Visual {

	private static final long serialVersionUID = 2040409868497970432L;
	protected Font font = new Font("Calibri", 0, (int) (Window.WIDTH / 47.4f));
	protected Random r = new Random();
	protected ArrayList<VisualElement> visualElements;

	public abstract void tick(double delta);

	public abstract void render();

	public void addVisualElement(VisualElement btn) {
		visualElements.add(btn);
	}

	public void removeVisualElements() {
		visualElements.clear();
	}
}
