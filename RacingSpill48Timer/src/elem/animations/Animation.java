package elem.animations;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Animation {

	protected double currentFrame;
	protected int frameCount;
	protected String frameName;
	protected BufferedImage[] frames;
	private int width;
	private int height;

	public Animation(String frameName, int frameCount) {
		this(frameName, frameCount, 0);
	}

	public Animation(String frameName, int frameCount, int currentFrame) {

		this.currentFrame = currentFrame;
		this.frameName = frameName;
		this.frameCount = frameCount;
		if(frameCount > 0)
		frames = new BufferedImage[frameCount];
		else 
			frames = new BufferedImage[1];
		// Hent bilde
		try {
			for (int i = 0; i < frames.length; i++) {
				String frameNameIndex = frameName;
				if (frameCount != -1)
					frameNameIndex += i;
				frames[i] = ImageIO.read(Animation.class.getResourceAsStream("/pics/" + frameNameIndex + ".png"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		width = frames[0].getWidth();
		height = frames[0].getHeight();
	}

	public int getHalfWidth() {
		return width / 2;
	}
	
	public int getHalfHeight() {
		return height / 2;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public BufferedImage getFrame() {
		return frames[(int) currentFrame];
	}

	public void incrementCurrentFrame(double tickFactor) {
		currentFrame = (currentFrame + (1 * tickFactor)) % frameCount;
	}

	public double getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(double currentFrame) {
		this.currentFrame = currentFrame;
	}

	public int getFrameCount() {
		return frameCount;
	}

	public void setFrameCount(int frameCount) {
		this.frameCount = frameCount;
	}

	public String getFrameName() {
		return frameName;
	}

	public void setFrameName(String frameName) {
		this.frameName = frameName;
	}

	public BufferedImage[] getFrames() {
		return frames;
	}

	public void setFrames(BufferedImage[] frames) {
		this.frames = frames;
	}

	public void scale(double amount) {
		width = (int) (width * amount);
		height = (int) (height * amount);
	}
}
