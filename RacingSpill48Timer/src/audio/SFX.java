package audio;

public class SFX {
	private static MediaAudio sfx;

	public static void playSound(String url) {
		sfx = new MediaAudio("/sfx/" + url);
		sfx.play();
	}
}
