package audio;

import handlers.GameHandler;

public class BgMusicListener {

	private MP3Audio[] music;
	private int playingIndex;
	private int amount;
	private boolean stopped;

	public BgMusicListener(int amount) {
		// Maybe use action for something later, cause it's awesome
		playingIndex = -1;

		music = new MP3Audio[amount];

		for (int i = 0; i < amount; i++) {
			music[i] = new MP3Audio("/music/music" + i);
		}

		this.amount = amount;
		if (amount != 0)
			playNext();
	}

	public void playNext() {

		if(stopped)
			return;
		
		if (playingIndex > -1 && music[playingIndex] != null && music[playingIndex].isPlaying())
			music[playingIndex].stop();

		playingIndex = (playingIndex + 1) % amount;
		music[playingIndex].play();
		updateVolume();

//		music[playingIndex].getMediaPlayer();
//		music[playingIndex].getMediaPlayer().setOnEndOfMedia(() -> playNext());
	}

	public void updateVolume() {
		if (playingIndex >= 0 && music[playingIndex] != null)
			music[playingIndex].setVolume(GameHandler.getMusicVolume());
	}

	public void playOrStop() {
		if (playingIndex >= 0 && music[playingIndex] != null && music[playingIndex].isPlaying()) {
			music[playingIndex].stop();
			stopped = true;
		}
		else {
			playNext();
			stopped = false;	
		}
	}

	public void stop() {
		if (playingIndex >= 0 && music[playingIndex] != null && music[playingIndex].isPlaying()) {
			music[playingIndex].stop();
		}
	}

}
