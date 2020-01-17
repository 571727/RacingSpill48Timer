package audio;
/**
 * Plays sound effect. Use to avoid the complex nature of OpenAL
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
