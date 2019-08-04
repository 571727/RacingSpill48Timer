package audio;

public class SFX {
	private static MediaAudio sfx;

	public static void playMP3Sound(String url) {
		sfx = new MediaAudio("/sfx/" + url);
		sfx.play();
	}

}
