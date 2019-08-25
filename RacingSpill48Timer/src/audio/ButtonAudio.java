package audio;

public class ButtonAudio {

	private MP3Audio startGame;
	private MP3Audio regularBtn;
	private MP3Audio ready;

	public ButtonAudio() {
		ready = new MP3Audio("/sfx/btn/ready");
		startGame = new MP3Audio("/sfx/btn/start_game");
		regularBtn = new MP3Audio("/sfx/btn/rbtn");
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
