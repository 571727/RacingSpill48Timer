package elem;

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
	
	public Animation (String frameName, int frameCount, int currentFrame) {
		
		this.currentFrame = currentFrame;
		this.frameName = frameName;
		this.frameCount = frameCount;
		frames = new BufferedImage[frameCount];
		
		//Hent bilde
		try {
			for (int i = 0; i < frames.length; i++) {
				frames[i] = ImageIO.read(Animation.class.getResourceAsStream("/pics/" + frameName + i + ".png"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		width = frames[0].getWidth();
		height = frames[0].getHeight();
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
	
	public void incrementCurrentFrame() {
		currentFrame = (currentFrame + 1) % frameCount;
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

}
