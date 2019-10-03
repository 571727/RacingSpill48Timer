package audio;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import adt.Audio;
import handlers.GameHandler;
import jaco.mp3.MP3;
import jaco.mp3.player.MP3Player;

public class MP3Audio implements Audio {
	private MP3 mediaPlayer;
	private File mediaFile;

	public MP3Audio(String file) {
		System.out.println("Finding file:");
		System.out.println("\"" + file + "\"");
		String name = file.split("/")[file.split("/").length - 1];

		InputStream input = getClass().getResourceAsStream(file + ".mp3");
		File tempFile = null;
		try {
			String outputFile = name + ".tmp";

			tempFile = new File(outputFile);
			if (!tempFile.exists()) {
				Files.copy(input, Paths.get(outputFile));
				tempFile = new File(outputFile);
			}

			tempFile.deleteOnExit();

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		mediaPlayer = new MP3(tempFile);
		mediaFile = tempFile;

		setVolume(1);
	}

	@Override
	public void setVolume(double factor) {
//		mediaPlayer.();
		setVolume(mediaPlayer, factor);
	}
	
	private void setVolume(MP3 player, double factor) {
		player.setVolume((int) (GameHandler.getMasterVolume() * GameHandler.getSfxVolume() * factor * 100.0));
	}

	@Override
	public void setRate(double rate) {
		// mediaPlayer.setRate(rate);
	}

	public void play() {
		if (mediaPlayer.isStopped())
			mediaPlayer.play();
	}

	public void stop() {
		if (isPlaying())
			mediaPlayer.stop();
	}

	public void loop() {
		mediaPlayer.play();
//		mediaPlayer.setOnEndOfMedia(new Runnable() {
//			public void run() {
//				mediaPlayer.seek(Duration.ZERO);
//			}
//		});
	}

	public boolean isPlaying() {
		return !mediaPlayer.isStopped();
	}

	public void playNewInstance(double factor) {
		MP3 temp = new MP3(mediaFile);
		setVolume(temp, factor);
		temp.play();
	}

//	public Media getHit() {
//		return hit;
//	}
//
//	public void setHit(Media hit) {
//		this.hit = hit;
//	}
//
//	public MediaPlayer getMediaPlayer() {
//		return mediaPlayer;
//	}
//
//	public void setMediaPlayer(MediaPlayer mediaPlayer) {
//		this.mediaPlayer = mediaPlayer;
//	}

}