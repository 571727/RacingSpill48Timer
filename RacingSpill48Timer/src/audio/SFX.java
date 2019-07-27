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
			sfx = new OggClip(new FileInputStream(SFX.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "/../res/sfx/" + url + ".ogg"));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
			
			float gain = (float) (GameHandler.volume * 3.5f);
			if (gain > 1)
				gain = 1;
			sfx.setGain(gain);
		
		sfx.play();
	}
}
