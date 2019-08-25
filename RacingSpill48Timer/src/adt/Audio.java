package adt;

public interface Audio {
	public void setVolume(double factor);

	public void play();

	public void stop();

	public void loop();

	public boolean isPlaying();

	public void setRate(double rate);
}
