package audio;

public class BgMusicListener {

	private MediaAudio[] music;
	private int playingIndex;
	private int amount;

	public BgMusicListener(int amount) {
		// Maybe use action for something later, cause it's awesome
		playingIndex = -1;

		music = new MediaAudio[amount];

		for (int i = 0; i < amount; i++) {
			music[i] = new MediaAudio("/music/music" + i);
		}

		this.amount = amount;
		if (amount != 0)
			playNext();
	}

	public void playNext() {

		if (playingIndex > -1 && music[playingIndex] != null && music[playingIndex].isPlaying())
			music[playingIndex].stop();

		playingIndex = (playingIndex + 1) % amount;
		music[playingIndex].play();
		music[playingIndex].getMediaPlayer();
		updateVolume();

		music[playingIndex].getMediaPlayer().setOnEndOfMedia(() -> playNext());
	}

	public void updateVolume() {
		if (music[playingIndex] != null)
			music[playingIndex].setVolume(1.8);
	}

	public void playOrStop() {
		if (music[playingIndex] != null && music[playingIndex].isPlaying())
			music[playingIndex].stop();
		else
			playNext();
	}

}
