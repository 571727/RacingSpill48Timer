package audio;

import java.util.Random;

import javax.sound.sampled.LineEvent;

import javafx.scene.media.AudioSpectrumListener;

public class BgMusicListener implements AudioSpectrumListener {

	private Random r = new Random();
	private int lastPlayed;
	private MediaAudio media;
	private String[] songs;

	public BgMusicListener(String[] songs) {
		// Maybe use action for something later, cause it's awesome
		lastPlayed = -1;
		this.songs = songs;
		media = new MediaAudio("music/music1");

	}

	public void playAndChooseNextRandomly() {
		attempt();
	}

	public void turnOff() {
	}

	private void attempt() {
		media.play();

		media.getMediaPlayer().setOnEndOfMedia(() -> media.playNewSong("music/" + findRandomSong()));
	}

	private String findRandomSong() {
		int nextSong = 0;

		do {
			nextSong = r.nextInt(songs.length);
		} while (nextSong == lastPlayed);

		return songs[nextSong];
	}

	@Override
	public void spectrumDataUpdate(double arg0, double arg1, float[] arg2, float[] arg3) {

	}

}
