package engine.utils;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Timer {
	
	public static final double TARGET_FPS = 120;
	public static final double TARGET_TPS = 30;
	
	private double lastLoopTime;
	private double timeCount;
	private int fps;
	private int fpsCount;
	// ticks per second
	private int tps;
	private int tpsCount;
	
	public void init() {
		lastLoopTime = getTime();
	}
	
	public double getTime() {
		return glfwGetTime();
	}
	
	public double getDelta() {
		double time = getTime();
		double delta = time - lastLoopTime;
		lastLoopTime = time;
		timeCount += delta;
		return delta;
	}
	
	public void updateFPS() {
		fpsCount++;
	}
	
	public void updateTPS() {
		tpsCount++;
	}
	
	public void update() {
		if(timeCount > 1.0) {
			fps = fpsCount;
			fpsCount = 0;
			
			tps = tpsCount;
			tpsCount = 0;
			
			timeCount -= 1.0;
		}
	}
	
	public int getFPS() {
		return fps > 0 ? fps : fpsCount;
	}
	
	public int getTPS() {
		return tps > 0 ? tps :tpsCount;
	}
	
	public double getLastLoopTime() {
		return lastLoopTime;
	}
}
