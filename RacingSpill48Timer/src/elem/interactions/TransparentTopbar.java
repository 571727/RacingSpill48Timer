package elem.interactions;

public class TransparentTopbar {

	private TopbarInteraction topbar;

	public TransparentTopbar(long window, int height) {

		PressAction pressAction = (double X, double Y) -> {
			// Move window
			topbar.setX(X);
			topbar.setY(Y);
			topbar.setHeld(true);
		};

		topbar = new TopbarInteraction(window, height, pressAction);
	}

	public void press(double x, double y) {
		topbar.press(x, y);
	}

	public void release() {
		topbar.release();
	}

	public void move(double x, double y) {
		topbar.move(x, y);
	}

	public int getHeight() {
		return topbar.getHeight();
	}

}
