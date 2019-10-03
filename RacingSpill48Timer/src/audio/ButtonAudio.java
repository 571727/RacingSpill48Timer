package audio;

import handlers.GameHandler;

public class ButtonAudio {

	private MP3Audio[] btns;

	public ButtonAudio() {
		btns = new MP3Audio[5];
		btns[0] = new MP3Audio("/sfx/btn/ready");
		btns[1] = new MP3Audio("/sfx/btn/start_game");
		btns[2] = new MP3Audio("/sfx/btn/rbtn");
		btns[3] = new MP3Audio("/sfx/redLight");
		btns[4] = new MP3Audio("/sfx/greenLight");
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

	public void playTrafficLight(boolean green) {
		if(!green)
			playNew(3);
		else
			playNew(4);
	}
	
	private void play(int btn) {
		btns[btn].stop();
		btns[btn].setVolume(GameHandler.getSfxVolume());
		btns[btn].play();
	}

	private void playNew(int btn) {
		btns[btn].playNewInstance(GameHandler.getSfxVolume());
	}
}
