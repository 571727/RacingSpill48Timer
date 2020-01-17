package audio;

import main.GameHandler;

public class MusicAudio {

//	private MP3Audio[] music;
//	private MP3Audio store;
//	private int playingIndex;
//	private int amount;
//	private boolean stopped;

	public MusicAudio() {
		// Maybe use action for something later, cause it's awesome
//		playingIndex = -1;
//		int amount = 11;
//		music = new MP3Audio[amount];
//
//		store = new MP3Audio("/music/waltz_for_memory");
//		for (int i = 0; i < amount; i++) {
//			music[i] = new MP3Audio("/music/music" + i);
//		}
//
//		this.amount = amount;
//		if (amount != 0)
//			playNext();
	}
	
	public void playStore() {
//		System.out.println(store.getVolume());
//		store.play();
	}
	
	public void stopStore() {
//		store.pause();
	}

	public void playNext() {
//
//		if(stopped)
//			return;
//		
//		if (playingIndex > -1 && music[playingIndex] != null && music[playingIndex].isPlaying())
//			music[playingIndex].stop();
//
//		playingIndex = (playingIndex + 1) % amount;
//		music[playingIndex].play();
//		updateVolume();
//
////		music[playingIndex].getMediaPlayer();
//		music[playingIndex].getMediaPlayer().setOnEndOfMedia(() -> playNext());
	}

	public void updateVolume() {
//		if (playingIndex >= 0 && music[playingIndex] != null)
//			music[playingIndex].setVolume(GameHandler.getMusicVolume());
//		
//		store.setVolume(GameHandler.getMusicVolume());
	}

	public void playOrStop() {
//		if (playingIndex >= 0 && music[playingIndex] != null && music[playingIndex].isPlaying()) {
//			music[playingIndex].stop();
//			stopped = true;
//		}
//		else {
//			playNext();
//			stopped = false;	
//		}
	}

	public void stop() {
//		if (playingIndex >= 0 && music[playingIndex] != null && music[playingIndex].isPlaying()) {
//			music[playingIndex].stop();
//		}
	}

}
