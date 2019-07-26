package audio;

import java.io.FileInputStream;
import java.io.IOException;

import org.newdawn.easyogg.OggClip;

import handlers.GameHandler;

public class SFX {
	private static MediaAudio sfx;

	public static void playMP3Sound(String url) {
		sfx = new MediaAudio("/sfx/" + url);
		sfx.play();
	}

	public static void playOggSound(String url) {

		OggClip sfx = null;
		try {
			sfx = new OggClip(new FileInputStream("res/sfx/" + url + ".ogg"));
			float gain = (float) (GameHandler.volume * 3.5f);
			if (gain > 1)
				gain = 1;
			sfx.setGain(gain);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sfx.play();
	}
}
