package elem;

public class MovingAnimation extends PlacedAnimation{

	private int x1;
	private int y1;
	private int x2;
	private int y2;
	private boolean forwardDirection;
	private int framesPerMovement;
	private float movementSpeedX;
	private float movementSpeedY;
	private int movementIndex;
	
	public MovingAnimation(String frameName, int frameCount, int x, int y, int x2, int y2, int framesPerMovement) {
		super(frameName, frameCount, x, y);
		this.x1 = x;
		this.y1 = y;
		this.x2 = x2;
		this.y2 = y2;
		this.framesPerMovement = framesPerMovement;
		forwardDirection = true;
		movementSpeedX = ( x2 - x1 ) / framesPerMovement;
		movementSpeedY = ( y2 - y1 ) / framesPerMovement;
	}
	
	public void incrementMovement() {
		if(forwardDirection) {
			x += movementSpeedX;
			y += movementSpeedY;
		} else {
			x -= movementSpeedX;
			y -= movementSpeedY;
		}
		
		movementIndex++;
		if(movementIndex == framesPerMovement) {
			forwardDirection = !forwardDirection;
			movementIndex = 0;
		}
	}
	
	
	
}
