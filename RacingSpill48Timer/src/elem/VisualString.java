package elem;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import adt.Action;
import adt.VisualElement;

public class VisualString implements VisualElement {

	private int x, y, w, h;
	private Color bc, tc;
	private String[] strings;
	private float alpha;
	private Font font = new Font("Calibri", 0, 16);
	
	public VisualString(int x, int y, int w, int h, Color bc, Color tc) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.bc = bc;
		this.tc = tc;
		alpha = 0.8f;
	}

	@Override
	public void tick() {
	}

	@Override
	public void render(Graphics g) {
		// FIXME not run after everybodydone
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(bc);
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		g2d.setComposite(ac);

		g2d.fillRect(x, y, w, h);
		g2d.setComposite(ac.derive(1f));
		g2d.setColor(tc);
		g2d.setFont(font);
		int i = 1;
		for (String s : strings) {
			g2d.drawString(s, x + font.getSize(), y + i * (font.getSize()));
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
		return (x >= this.x && x <= this.x + w)
				&& (y >= this.y && y <= this.y + h);
	}

	@Override
	public void run() {

	}

	public String[] getStrings() {
		return strings;
	}

	public void setText(String string, String split) {
		this.strings = string.split(split);
	}
}
