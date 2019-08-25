package audio;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import audio.audiocue.AudioCue;
import audio.audiocue.AudioCueInstanceEvent;
import audio.audiocue.AudioCueListener;
import handlers.GameHandler;

public class RaceAudio implements AudioCueListener {

	private Random r = new Random();
	private MP3Audio[] gear;
	private MP3Audio[] turbo;
	private MP3Audio redline;
	private MP3Audio nos;
	private WavAudio motorAcc;
	private WavAudio motorDcc;
	private WavAudio turbospool;
	private WavAudio straightcutgears;
	private float wavgain;
	private double turbospooling;

	public RaceAudio(String carname) {
		// Maybe use action for something later, cause it's awesome
		gear = new MP3Audio[4];
		turbo = new MP3Audio[2];

		for (int i = 0; i < gear.length; i++) {
			gear[i] = new MP3Audio("/sfx/gear" + (i + 1));
		}

		for (int i = 0; i < turbo.length; i++) {
			turbo[i] = new MP3Audio("/sfx/turbosurge" + (i + 1));
		}

		motorAcc = new WavAudio("/sfx/motorAcc" + carname);
		motorDcc = new WavAudio("/sfx/motorDcc" + carname);
		turbospool = new WavAudio("/sfx/turbospool");
		straightcutgears = new WavAudio("/sfx/straightcutgears");
		
		redline = new MP3Audio("/sfx/redline");
		nos = new MP3Audio("/sfx/nos");
	}

	public void updateVolume() {
		float gain = (float) (GameHandler.volume * 3.5f);
		wavgain = gain / 2;
		if (wavgain > 1) {
			gain = 1;
			wavgain = 1;
		}
		motorAcc.setVolume(wavgain);
		motorDcc.setVolume(wavgain);

		for (MP3Audio t : turbo) {
			t.setVolume(1);
		}

		for (MP3Audio g : gear) {
			g.setVolume(1);
		}

		redline.setVolume(1);
		nos.setVolume(1);

	}

	public void motorIdle() {
		motorSound();
	}

	public void motorPitch(double rpm, double totalRPM, double maxValue) {
		double value;
		rpm = maxValue * rpm;

		if (rpm > totalRPM * maxValue)
			value = maxValue;
		else if (rpm < 0)
			value = 0;
		else
			value = rpm / totalRPM;

		value = -0.05 * Math.pow(2, value) + 0.8 * value;
		motorAcc.setRate(value);
		motorDcc.setRate(value);
	}

	private void stopMotorAcc() {
		if (motorAcc != null && motorAcc.isPlaying()) {
			motorAcc.stop();
		}
		stopTurbospool();
	}

	private void stopTurbospool() {
		turbospooling = 0;
		if (turbospool != null && turbospool.isPlaying()) {
			turbospool.stop();
		}
	}

	private void stopStraightcutgears() {
		if (straightcutgears != null && straightcutgears.isPlaying()) {
			straightcutgears.stop();
			straightcutgears.setVolume(0);
		}
	}

	private void stopMotorDcc() {
		if (motorDcc != null && motorDcc.isPlaying()) {
			motorDcc.stop();
		}
	}

	public void turbospool() {
		if (!turbospool.isPlaying()) {
			turbospool.setFramePosition(0);
			turbospool.loop();
		}
	}

	public void turbospoolPitch(double rpm, double totalRPM) {
		double value;
		double maxValue = 2;
		rpm = maxValue * rpm;

		if (rpm > totalRPM * maxValue)
			value = maxValue;
		else if (rpm < 0)
			value = 0;
		else
			value = rpm / totalRPM;

		turbospooling += 0.1 * rpm / totalRPM;
		if (turbospooling > 1) {
			turbospooling = 1;
		}

		value = -0.05 * Math.pow(2, value) + 0.8 * value;
		turbospool.setRate(value);
		turbospool.setVolume((value / maxValue) * turbospooling);
	}

	public void straightcutgears() {
		if (!straightcutgears.isPlaying()) {
			straightcutgears.setFramePosition(0);
			straightcutgears.loop();
		}
	}

	public void straightcutgearsPitch(double speed, double topSpeed) {
		double value;
		double maxValue = 0.2;
		speed = maxValue * speed;

		if (speed > topSpeed * maxValue)
			value = maxValue;
		else if (speed < 0)
			value = 0;
		else
			value = speed / topSpeed;

		value = -0.05 * Math.pow(2, value) + 4 * value;
		straightcutgears.setRate(value);
		straightcutgears.setVolume(value * wavgain);
	}

	public void motorAcc(boolean turboInstalled) {
		stopMotorDcc();
		if (turboInstalled) {
			turbospool();
			if (turbo != null && isMediaArrayPlaying(turbo)) {
				stopMediaArray(turbo);
			}
		}

		if (redline != null && redline.isPlaying()) {
			redline.stop();
		}
		motorSound();
	}

	private void motorSound() {
		if (!motorAcc.isPlaying()) {
			motorAcc.setFramePosition(0);
			motorAcc.loop();
		}
	}

	public void motorDcc() {
		stopMotorAcc();
		stopTurbospool();
		if (!motorDcc.isPlaying()) {
			motorDcc.setFramePosition(0);
			motorDcc.loop();
		}

	}

	public void redline() {
		stopMotorAcc();

		redline.loop();
	}

	public void nos() {
		nos.play();
	}

	public void turboSurge() {

		int nextSfx = 0;
		nextSfx = r.nextInt(2);

		turbo[nextSfx].play();

//		if (turbo != null && turbo.isPlaying())
//			turbo.stop();
	}

	public void gearSound() {
		int nextSfx = 0;
		nextSfx = r.nextInt(4);
		gear[nextSfx].stop();
		gear[nextSfx].play();
	}

	private boolean isMediaArrayPlaying(MP3Audio[] turbo2) {
		for (MP3Audio ma : turbo2) {
			if (ma.isPlaying())
				return true;
		}
		return false;
	}

	private void stopMediaArray(MP3Audio[] arr) {
		for (MP3Audio ma : arr) {
			ma.stop();
		}
	}

	public void openLines(boolean turbo, boolean gears) {
		try {
			motorAcc.open(2056);
			motorDcc.open(2056);
			if (turbo)
				turbospool.open(2056);
			if (gears) {
				straightcutgears.setVolume(0);
				straightcutgears.open(2056);
				straightcutgears();
			}
		} catch (IllegalStateException | LineUnavailableException e) {
			e.printStackTrace();
		}

	}

	public void stopAll() {

		stopStraightcutgears();
		stopMotorAcc();

		if (turbo != null && isMediaArrayPlaying(turbo)) {
			stopMediaArray(turbo);
		}
		if (nos != null && nos.isPlaying()) {
			nos.stop();
		}
		if (redline != null && redline.isPlaying()) {
			redline.stop();
		}

		if (motorAcc.isPlaying())
			motorAcc.close();
		if (motorDcc.isPlaying())
			motorDcc.close();
		if (straightcutgears.isPlaying())
			straightcutgears.close();
		if (turbospool.isPlaying())
			turbospool.close();

	}

	@Override
	public void audioCueOpened(long now, int threadPriority, int bufferSize, AudioCue source) {
		// TODO Auto-generated method stub

	}

	@Override
	public void audioCueClosed(long now, AudioCue source) {
		// TODO Auto-generated method stub

	}

	@Override
	public void instanceEventOccurred(AudioCueInstanceEvent event) {
		// TODO Auto-generated method stub

	}

	public void stopMotor() {
		stopMotorAcc();
		stopMotorDcc();
	}

}
