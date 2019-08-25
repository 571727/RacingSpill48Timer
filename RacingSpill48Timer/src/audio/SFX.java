package audio;

import java.util.Random;


public class SFX {
	private static MP3Audio sfx;

	public static void playMP3Sound(String url) {
		playMP3(url, 1);
	}

	public static void playMP3SoundWithRandomPitch(String url) {
		playMP3(url, (new Random().nextInt(16) + 4) / 10.0);
	}
	
	private static void playMP3(String url, double rate) {
		sfx = new MP3Audio("/sfx/" + url);
		sfx.setVolume(1);
		sfx.setRate(rate);
		sfx.play();
	}

}
