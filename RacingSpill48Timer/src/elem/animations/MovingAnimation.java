package elem.animations;

public class MovingAnimation extends PlacedAnimation {

	private double x1;
	private double y1;
	private int x2;
	private int y2;
	private boolean forwardDirection;
	private int framesPerMovement;
	private double movementSpeedX;
	private double movementSpeedY;
	private double movementIndex;

	public MovingAnimation(String frameName, int frameCount, int x, int y, int x2, int y2, int framesPerMovement) {
		super(frameName, frameCount, x, y);
		this.x1 = x;
		this.y1 = y;
		this.x2 = x2;
		this.y2 = y2;
		this.framesPerMovement = framesPerMovement;
		forwardDirection = true;
		movementSpeedX = (x2 - x1) / framesPerMovement;
		movementSpeedY = (y2 - y1) / framesPerMovement;
	}

	public void incrementMovement(double tickFactor) {
		if (forwardDirection) {
			x1 += movementSpeedX * tickFactor;
			y1 += movementSpeedY * tickFactor;
		} else {
			x1 -= movementSpeedX * tickFactor;
			y1 -= movementSpeedY * tickFactor;
		}
		
		x = (int) x1;
		y = (int) y1;
		
		movementIndex += tickFactor;
		if (movementIndex >= framesPerMovement) {
			forwardDirection = !forwardDirection;
			movementIndex = 0;
		}
	}

}
