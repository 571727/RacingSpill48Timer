package audio;

import handlers.GameHandler;

public class ButtonAudio {

	private MP3Audio[] btns;

	public ButtonAudio() {
		btns = new MP3Audio[3];
		btns[0] = new MP3Audio("/sfx/btn/ready");
		btns[1] = new MP3Audio("/sfx/btn/start_game");
		btns[2] = new MP3Audio("/sfx/btn/rbtn");
	}

	public void playReady() {
		play(0);
	}

	public void startGame() {
		play(1);
	}

	public void playRegularBtn() {
		play(2);
	}

	private void play(int btn) {
		btns[btn].stop();
		btns[btn].setVolume(GameHandler.getSfxVolume());
		btns[btn].play();
	}
}
