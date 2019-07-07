package elem;

public class PlacedAnimation extends Animation {

	private int x;
	private int y;
	
	
	public PlacedAnimation(String frameName, int frameCount) {
		super(frameName, frameCount);
	}
	
	public PlacedAnimation(String frameName, int frameCount, int currentFrame) {
		super(frameName, frameCount, currentFrame);
	}
	
	public PlacedAnimation(String frameName, int frameCount, int x, int y) {
		super(frameName, frameCount);
		this.x = x;
		this.y = y;
	}
	
	public PlacedAnimation(String frameName, int frameCount, int currentFrame, int x, int y) {
		super(frameName, frameCount, currentFrame);
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void moveY(int y) {
		this.y += y;
	}
	
	public void moveX(int x) {
		this.x += x;
	}


}
