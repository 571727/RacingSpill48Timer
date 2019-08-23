package audio;

public class ButtonAudio {

	private WavAudio startGame;
	private WavAudio regularBtn;
	private WavAudio ready;

	public ButtonAudio() {
		ready = new WavAudio("/sfx/btn/ready");
		startGame = new WavAudio("/sfx/btn/start_game");
		regularBtn = new WavAudio("/sfx/btn/rbtn");
	}

	public void playRegularBtn() {
		regularBtn.stop();
		regularBtn.setVolume(1);
		regularBtn.play();
	}

	public void startGame() {
		startGame.stop();
		startGame.setVolume(1);
		startGame.play();
	}

	public void playReady() {
		ready.stop();
		ready.setVolume(1);
		ready.play();		
	}
}
