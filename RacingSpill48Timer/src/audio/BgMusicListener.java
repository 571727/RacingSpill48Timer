package audio;

import java.util.Random;

import javax.sound.sampled.LineEvent;

import javafx.scene.media.AudioSpectrumListener;

public class BgMusicListener {

	private Random r = new Random();
	private int lastPlayed;
	private MediaAudio media;
	private String[] songs;

	public BgMusicListener(String[] songs) {
		// Maybe use action for something later, cause it's awesome
		lastPlayed = -1;
		this.songs = songs;
		media = new MediaAudio("/music/" + findRandomSong());

	}

	public void playAndChooseNextRandomly() {
		attempt();
	}

	public void turnOff() {
	}

	//Denne kj�rer bare to ganger
	private void attempt() {
		media.play();

		media.getMediaPlayer().setOnEndOfMedia(() -> media.playNewSong("/music/" + findRandomSong()));
	}

	private String findRandomSong() {
		int nextSong = 0;

		do {
			nextSong = r.nextInt(songs.length);
		} while (nextSong == lastPlayed);

		return songs[nextSong];
	}

}
