package audio;

public class ButtonAudio {

	private MediaAudio startGame;
	private MediaAudio regularBtn;
	private MediaAudio ready;

	public ButtonAudio() {
		ready = new MediaAudio("/sfx/btn/ready");
		startGame = new MediaAudio("/sfx/btn/start_game");
		regularBtn = new MediaAudio("/sfx/btn/rbtn");
	}

	public void playRegularBtn() {
		regularBtn.stop();
		regularBtn.play();
	}

	public void startGame() {
		startGame.stop();
		startGame.play();
	}

	public void playReady() {
		ready.stop();
		ready.play();		
	}
}