package audio;

import adt.Audio;
import handlers.GameHandler;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

public class MP3Audio implements Audio{
	private Media hit;
	private MediaPlayer mediaPlayer;
	private AudioClip loopPlayer;

	public MP3Audio(String file) {
		System.out.println("Finding file:");
		System.out.println("\"" + file  +"\"");
		System.out.println(MP3Audio.class.getResource(file + ".mp3").toString());
		hit = new Media(MP3Audio.class.getResource(file + ".mp3").toString());
		loopPlayer = new AudioClip(MP3Audio.class.getResource(file + ".mp3").toString());
		mediaPlayer = new MediaPlayer(hit);
		setVolume(1);
	}

	@Override
	public void setVolume(double factor) {
		mediaPlayer.setVolume(GameHandler.getMasterVolume() * factor);		
	}

	@Override
	public void setRate(double rate) {
		mediaPlayer.setRate(rate);
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