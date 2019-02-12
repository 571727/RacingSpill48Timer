package audio;

import java.util.Random;

public class RaceAudio {

	private Random r = new Random();
	private MediaAudio gear;
	private MediaAudio motor;
	private MediaAudio turbo;
	private MediaAudio redline;
	private MediaAudio nos;
	private String carname;

	public RaceAudio(String carname) {
		// Maybe use action for something later, cause it's awesome
		this.carname = carname;
	}

	public void motorIdle() {

		try {
			if (motor != null && motor.isPlaying()) {
				motor.stop();
			}

			motor = new MediaAudio("/sfx/motorIdle" + carname);
			motor.getMediaPlayer().setOnEndOfMedia(() -> motorIdle());
			motor.play();
		} catch (Exception e) {

		}

	}

	//FIXME baser lyd p� turtall
	
	public void motorAcc() {
		if (motor != null && motor.isPlaying()) {
			motor.stop();
		}
		if (redline != null && redline.isPlaying()) {
			redline.stop();
		}

		motor = new MediaAudio("/sfx/motorAcc" + carname);
		motor.play();

		if (turbo != null && turbo.isPlaying()) {
			turbo.stop();
		}
	}

	public void motorDcc() {

//		motor = new MediaAudio("/sfx/motorDcc" + carname);
//		motor.play();

		if (motor != null && motor.isPlaying()) {
			motor.stop();
		}
	}
	
	public void redline() {
		if (motor != null && motor.isPlaying()) {
			motor.stop();
		}
		
		redline = new MediaAudio("/sfx/redline");
		redline.play();
	}

	public void nos() {
		nos = new MediaAudio("/sfx/nos");
		nos.play();
	}

	public void turboSurge() {

		int nextSfx = 0;
		nextSfx = r.nextInt(2) + 1;
		turbo = new MediaAudio("/sfx/turbosurge" + nextSfx);
		turbo.play();

		if (turbo != null && turbo.isPlaying())
			turbo.stop();
	}

	public void gearSound() {
		int nextSfx = 0;
		nextSfx = r.nextInt(4) + 1;
		gear = new MediaAudio("/sfx/gear" + nextSfx);
		gear.play();
	}

	public void stopAll() {
		if (motor != null && motor.isPlaying()) {
			motor.stop();
		}
		if (turbo != null && turbo.isPlaying()) {
			turbo.stop();
		}
		if (nos != null && nos.isPlaying()) {
			nos.stop();
		}
		if (redline != null && redline.isPlaying()) {
			redline.stop();
		}
	}


}
