package audio;

import java.util.Random;

import javax.sound.sampled.LineEvent;

import javafx.scene.media.AudioSpectrumListener;

public class BgMusicListener {

	private Random r = new Random();
	private int lastPlayed;
	private MediaAudio media;
	private int songs;

	public BgMusicListener(int songs) {
		// Maybe use action for something later, cause it's awesome
		lastPlayed = -1;
		this.songs = songs;
		playAndChooseNextRandomly();
	}

	public void playAndChooseNextRandomly() {
		if(media != null && media.isPlaying())
			media.stop();
		media = new MediaAudio("/music/music" + findRandomSong());
		
		media.play();
		media.getMediaPlayer().setOnEndOfMedia(() -> playAndChooseNextRandomly());
	}

	public void updateVolume() {
		media.setVolume();
	}

	private int findRandomSong() {
		int nextSong = 1;

		do {
			nextSong = r.nextInt(songs) + 1;
		} while (nextSong == lastPlayed);
		lastPlayed = nextSong;
		return nextSong;
	}

	public void playOrStop() {
		if(media != null && media.isPlaying())
			media.stop();
		else
			playAndChooseNextRandomly();
	}

}
