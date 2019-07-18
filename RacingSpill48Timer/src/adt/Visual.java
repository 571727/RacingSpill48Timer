package adt;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;

import elem.Player;
import scenes.Race;

public abstract class Visual extends Canvas {

	private static final long serialVersionUID = 2040409868497970432L;
	protected BufferStrategy bs;
	protected Font font = new Font("Calibri", 0, (int) (Race.WIDTH / 47.4f));
	protected Random r = new Random();
	protected ArrayList<VisualElement> visualElements;

	public abstract void tick();

	public abstract void render(Graphics g);

	public abstract void setRace(Race race);

	public abstract void setPlayer(Player player);

	public abstract boolean hasAnimationsRunning();

	public void addVisualElement(VisualElement btn) {
		visualElements.add(btn);
	}

	public void removeVisualElements() {
		visualElements.clear();
	}
}
