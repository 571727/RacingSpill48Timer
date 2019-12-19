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
	private MP3Audio turbo;
	private MP3Audio redline;
	private MP3Audio nos;
	private MP3Audio engineStart;
	private MP3Audio grind;

	private double motorOverallVolume = 1;

	private AudioCue motorAcc;
	private int motorAccInstance;
	private AudioCue motorDcc;
	private int motorDccInstance;
	private AudioCue turbospool;
	private int turbospoolInstance;
	private AudioCue straightcutgears;
	private int straightcutgearsInstance;
	private float wavgain;
	private double turbospooling;

	public RaceAudio(String carname) {
		// Maybe use action for something later, cause it's awesome
		gear = new MP3Audio[4];

		for (int i = 0; i < gear.length; i++) {
			gear[i] = new MP3Audio("/sfx/gear" + i);
		}

		turbo = new MP3Audio("/sfx/turbosurge" + 0);
		grind = new MP3Audio("/sfx/grind");

		engineStart = new MP3Audio("/sfx/engine_start");

		URL acc = this.getClass().getResource("/sfx/motorAcc" + carname + ".wav");
		URL dcc = this.getClass().getResource("/sfx/motorDcc" + carname + ".wav");
		URL ts = this.getClass().getResource("/sfx/turbospool.wav");
		URL scg = this.getClass().getResource("/sfx/straightcutgears.wav");
		try {
			motorAcc = AudioCue.makeStereoCue(acc, 3);
			motorDcc = AudioCue.makeStereoCue(dcc, 3);
			turbospool = AudioCue.makeStereoCue(ts, 3);
			straightcutgears = AudioCue.makeStereoCue(scg, 3);
		} catch (UnsupportedAudioFileException | IOException e1) {
			e1.printStackTrace();
		}
		motorAcc.setName("motorAcc");
		motorDcc.setName("motorDcc");
		turbospool.setName("turbospool");
		straightcutgears.setName("straightcutgears");

		motorAcc.addAudioCueListener(this);
		motorDcc.addAudioCueListener(this);
		turbospool.addAudioCueListener(this);
		straightcutgears.addAudioCueListener(this);

		motorAccInstance = motorAcc.obtainInstance();
		motorDccInstance = motorDcc.obtainInstance();
		turbospoolInstance = turbospool.obtainInstance();
		straightcutgearsInstance = straightcutgears.obtainInstance();

		redline = new MP3Audio("/sfx/redline");
		nos = new MP3Audio("/sfx/nos");
	}

	public void updateVolume() {
		float gain = (float) (GameHandler.getMasterVolume() * 2.5f * GameHandler.getSfxVolume());
		wavgain = gain / 2;
		if (wavgain > 1) {
			gain = 1;
			wavgain = 1;
		}

		turbo.setVolume(GameHandler.getSfxVolume());

		for (MP3Audio g : gear) {
			g.setVolume(GameHandler.getSfxVolume());
		}

		redline.setVolume(GameHandler.getSfxVolume());
		nos.setVolume(GameHandler.getSfxVolume());

	}

	public void grind() {
		grind.playNewInstance(GameHandler.getSfxVolume());
	}

	public void motorIdle() {
		if (engineStart.isPlaying() == false)
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

		if (value > 1.0) {
			motorOverallVolume = 1;
		} else {
			motorOverallVolume = value;
		}

		value = -0.05 * Math.pow(2, value) + 0.8 * value;
		motorAcc.setSpeed(motorAccInstance, value);
		motorDcc.setSpeed(motorDccInstance, value);

		updateMotorVolume();
	}

	private void updateMotorVolume() {
		motorAcc.setVolume(motorAccInstance, wavgain * motorOverallVolume);
		motorDcc.setVolume(motorDccInstance, wavgain * motorOverallVolume);
	}

	private void stopMotorAcc() {
		if (motorAcc != null && motorAcc.getIsPlaying(motorAccInstance)) {
			motorAcc.stop(motorAccInstance);
		}
		stopTurbospool();
	}

	private void stopTurbospool() {
		turbospooling = 0;
		if (turbospool != null && turbospool.getIsPlaying(turbospoolInstance)) {
			turbospool.stop(turbospoolInstance);
		}
	}

	private void stopStraightcutgears() {
		if (straightcutgears != null && straightcutgears.getIsPlaying(straightcutgearsInstance)) {
			straightcutgears.stop(straightcutgearsInstance);
			straightcutgears.setVolume(straightcutgearsInstance, 0);
		}
	}

	private void stopMotorDcc() {
		if (motorDcc != null && motorDcc.getIsPlaying(motorDccInstance)) {
			motorDcc.stop(motorDccInstance);
		}
	}

	public void turbospool() {
		if (!turbospool.getIsPlaying(turbospoolInstance)) {
			turbospool.setFramePosition(turbospoolInstance, 0);
			turbospool.setLooping(turbospoolInstance, -1);
			turbospool.start(turbospoolInstance);
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
		turbospool.setSpeed(turbospoolInstance, value);
		turbospool.setVolume(turbospoolInstance,
				(value / maxValue) * turbospooling * GameHandler.getMasterVolume() * GameHandler.getSfxVolume());
	}

	public void straightcutgears() {
		if (!straightcutgears.getIsPlaying(straightcutgearsInstance)) {
			straightcutgears.setFramePosition(straightcutgearsInstance, 0);
			straightcutgears.setLooping(straightcutgearsInstance, -1);
			straightcutgears.start(straightcutgearsInstance);
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
		straightcutgears.setSpeed(straightcutgearsInstance, value);
		straightcutgears.setVolume(straightcutgearsInstance, value * wavgain);
	}

	public void motorAcc(boolean turboInstalled) {
		stopMotorDcc();
		if (turboInstalled) {
			turbospool();
		}

		if (redline != null && redline.isPlaying()) {
			redline.stop();
		}
		motorSound();
	}

	private void motorSound() {

		if (!motorAcc.getIsPlaying(motorAccInstance)) {
			motorAcc.setFramePosition(motorAccInstance, 0);
			motorAcc.setLooping(motorAccInstance, -1);
			motorAcc.start(motorAccInstance);
		}
	}

	public void motorDcc() {
		stopRedline();
		stopMotorAcc();
		stopTurbospool();
		if (!motorDcc.getIsPlaying(motorDccInstance)) {
			motorDcc.setFramePosition(motorDccInstance, 0);
			motorDcc.setLooping(motorDccInstance, -1);
			motorDcc.start(motorDccInstance);
		}

	}

	private void stopRedline() {
		if (redline.isPlaying()) {
			redline.stop();
		}
	}

	public void redline() {
		stopMotorAcc();
		if (!redline.isPlaying()) {
			redline.loop();
		}
	}

	public void nos() {
		nos.play();
	}

	public void turboBlowoff() {

		turbo.playNewInstance(1);
		if (turbo != null && turbo.isPlaying())
			turbo.stop();
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
				straightcutgears.setVolume(straightcutgearsInstance, 0);
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
		stopMotorDcc();
		stopEngine();

		if (nos != null && nos.isPlaying()) {
			nos.stop();
		}
		if (redline != null && redline.isPlaying()) {
			redline.stop();
		}

	}

	public void closeAll() {
		if (motorAcc.isRunning())
			motorAcc.close();
		if (motorDcc.isRunning())
			motorDcc.close();
		if (straightcutgears.isRunning())
			straightcutgears.close();
		if (turbospool.isRunning())
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
		stopEngine();
	}

	public void startEngine() {
		engineStart.play();
	}

	public void stopEngine() {
		engineStart.stop();
	}

}