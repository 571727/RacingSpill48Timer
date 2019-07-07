package audio;

import java.io.File;

import handlers.GameHandler;
import javafx.beans.value.ObservableValue;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import scenes.RaceVisual;

public class MediaAudio {
	private Media hit;
	private MediaPlayer mediaPlayer;

	public MediaAudio(String file) {
		hit = new Media(MediaAudio.class.getResource(file + ".mp3").toString());
		mediaPlayer = new MediaPlayer(hit);
		setVolume();
	}
	
	public void setVolume() {
		mediaPlayer.setVolume(GameHandler.volume);
	}

	public void play() {
		mediaPlayer.play();
	}

	public void stop() {
		mediaPlayer.stop();
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
