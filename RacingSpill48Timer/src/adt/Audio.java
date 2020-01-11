package adt;
/**
 * Plays sound effect. Use only me for sound effects to keep it simple.
 * Add more as more is needed. 
 * @author jhoffis
 *
 */
public interface Audio {
	public void setVolume(double factor);

	public void play();

	public void stop();

	public void loop();

	public boolean isPlaying();

	public void setRate(double rate);
}
