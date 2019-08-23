package audio;

import handlers.GameHandler;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

public class MediaAudio {
	private Media hit;
	private MediaPlayer mediaPlayer;

	public MediaAudio(String file) {
		System.out.println("Finding file:");
		System.out.println("\"" + file  +"\"");
		System.out.println(MediaAudio.class.getResource(file + ".mp3").toString());
		hit = new Media(MediaAudio.class.getResource(file + ".mp3").toString());
		mediaPlayer = new MediaPlayer(hit);
		setVolume(1);
	}

	public void setVolume(double factor) {
		double newVol = GameHandler.volume * factor;
		mediaPlayer.setVolume(newVol);
	}

	public void play() {
		mediaPlayer.play();
	}

	public void stop() {
		mediaPlayer.stop();
	}

	public void loop() {
		mediaPlayer.play();
		mediaPlayer.setOnEndOfMedia(new Runnable() {
			@Override
			public void run() {
				mediaPlayer.seek(Duration.ZERO);
			}
		});
	}

	public boolean isPlaying() {
		return mediaPlayer.getStatus().equals(Status.PLAYING);
	}

	public Media getHit() {
		return hit;
	}

	public void setHit(Media hit) {
		this.hit = hit;
	}

	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	public void setMediaPlayer(MediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
	}
}
