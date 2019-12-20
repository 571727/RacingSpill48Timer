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
	private boolean startedPlaying;

	public MP3Audio(String file) {
		System.out.println("Finding file:");
		System.out.println("\"" + file + "\"");
		String name = file.split("/")[file.split("/").length - 1];

		InputStream input = getClass().getResourceAsStream(file + ".mp3");
		File tempFile = null;
		try {
			String outputFile = "racingmaybe_temp/" + name + ".tmp";

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

		setVolume(GameHandler.getMusicVolume());
	}

	@Override
	public void setVolume(double factor) {
//		mediaPlayer.();
		setVolume(mediaPlayer, factor);
	}
	
	private void setVolume(MP3 player, double factor) {
		player.setVolume((int) (GameHandler.getMasterVolume() * factor * 100.0));
	}

	@Override
	public void setRate(double rate) {
		// mediaPlayer.setRate(rate);
	}

	public void play() {
		if (!mediaPlayer.isPlaying() && mediaPlayer.getVolume() != 0) {
			mediaPlayer.play();
			if(startedPlaying && mediaPlayer.getPosition() == 0) {
				//Reset
				startedPlaying = false;
				mediaPlayer = new MP3(mediaFile);
				play();
			}
			startedPlaying = true;
		}
	}

	public void stop() {
		if (isPlaying())
			mediaPlayer.stop();
	}

	public void loop() {
		
		play();
		
//		mediaPlayer.setOnEndOfMedia(new Runnable() {
//			public void run() {
//				mediaPlayer.seek(Duration.ZERO);
//			}
//		});
	}

	public boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}

	public void playNewInstance(double factor) {
		MP3 temp = new MP3(mediaFile);
		setVolume(temp, factor);
		temp.play();
	}

	public void pause() {
		mediaPlayer.pause();
	}

	public int getVolume() {
		return mediaPlayer.getVolume();
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