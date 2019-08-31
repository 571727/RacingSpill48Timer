package elem;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import adt.VisualElement;

public class VisualString implements VisualElement {

	private int x, y, w, h;
	private Color bc, tc;
	private String[] strings;
	private ArrayList<Color> tcs;
	private float alpha;
	private Font font;

	public VisualString(int x, int y, int w, int h, Color bc, Color tc, Font font) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.bc = bc;
		this.tc = tc;
		this.font = font;
		alpha = 0.8f;
		tcs = new ArrayList<Color>();
		strings = new String[0];
	}

	@Override
	public void tick(double tickFactor) {
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		if (bc != null) {
			g2d.setColor(bc);
			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
			g2d.setComposite(ac);

			g2d.fillRect(x, y, w, h);
			g2d.setComposite(ac.derive(1f));
		}
		g2d.setFont(font);
		for (int i = 0; i < strings.length; i++) {
			g2d.setColor(tcs.get(i));
			g2d.drawString(strings[i], x + font.getSize(), y + (i + 1) * (font.getSize()));
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
		return (x >= this.x && x <= this.x + w) && (y >= this.y && y <= this.y + h);
	}

	@Override
	public void run() {

	}

	public String[] getStrings() {
		return strings;
	}

	public boolean setText(String string, String split, String resultColors) {
		boolean res = false;
		String[] strings = string.split(split);
		String[] colors = resultColors.split(split);
		if (colors.length == strings.length) {
			this.strings = strings;
			for (String c : colors) {
				switch (c) {
				case "won":
					tcs.add(new Color(5, 200, 13));
					break;
				case "ai":
					tcs.add(new Color(25, 29, 144));
					break;
				case "dnf":
					tcs.add(new Color(200, 21, 21));
					break;
				case "nf":
					tcs.add(Color.darkGray);
					break;
				default:
					tcs.add(tc);
					break;
				}
			}
			res = true;
		} 
		return res;
	}

}
