package audio;

import java.io.File;

import elem.RaceVisual;
import javafx.beans.value.ObservableValue;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

public class MediaAudio {
	private Media hit;
	private MediaPlayer mediaPlayer;

	public MediaAudio(String file) {
		hit = new Media(MediaAudio.class.getResource(file + ".mp3").toString());
		mediaPlayer = new MediaPlayer(hit);
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

	public void playNewSong(String file) {
		hit = new Media(MediaAudio.class.getResource(file + ".mp3").toString());
		mediaPlayer = new MediaPlayer(hit);
		
		mediaPlayer.play();
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
