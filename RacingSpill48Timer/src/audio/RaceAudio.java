package audio;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import audio.audiocue.*;
import org.newdawn.easyogg.OggClip;

import handlers.GameHandler;
import javafx.scene.media.MediaPlayer;

public class RaceAudio implements AudioCueListener {

	private Random r = new Random();
	private MediaAudio[] gear;
	private OggClip idle;
	private AudioCue motorAcc;
	private MediaAudio[] turbo;
	private MediaAudio redline;
	private MediaAudio nos;
	private Runnable idlestopper = () -> idle.stop();
	private Thread thread;
	private int motorAccInstance;
	private AudioCue motorDcc;
	private int motorDccInstance;

	public RaceAudio(String carname) {
		// Maybe use action for something later, cause it's awesome
		gear = new MediaAudio[4];
		turbo = new MediaAudio[2];

		for (int i = 0; i < gear.length; i++) {
			gear[i] = new MediaAudio("/sfx/gear" + (i + 1));
		}

		for (int i = 0; i < turbo.length; i++) {
			turbo[i] = new MediaAudio("/sfx/turbosurge" + (i + 1));
		}

		try {
			idle = new OggClip(
					new FileInputStream(RaceAudio.class.getProtectionDomain().getCodeSource().getLocation().getPath()
							+ "/../res/sfx/motorIdle" + carname + ".ogg"));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		URL acc = this.getClass().getResource("/sfx/motorAcc" + carname + ".wav");
		URL dcc = this.getClass().getResource("/sfx/motorDcc" + carname + ".wav");
		try {
			motorAcc = AudioCue.makeStereoCue(acc, 3);
			motorDcc = AudioCue.makeStereoCue(dcc, 3);
		} catch (UnsupportedAudioFileException | IOException e1) {
			e1.printStackTrace();
		}
		motorAcc.setName("motorAcc");
		motorDcc.setName("motorDcc");
		motorAcc.addAudioCueListener(this);
		motorDcc.addAudioCueListener(this);

		try {
			motorAcc.open(2056);
			motorDcc.open(2056);
		} catch (IllegalStateException | LineUnavailableException e) {
			e.printStackTrace();
		}
		motorAccInstance = motorAcc.obtainInstance();
		motorDccInstance = motorDcc.obtainInstance();

		redline = new MediaAudio("/sfx/redline");
		nos = new MediaAudio("/sfx/nos");
	}

	public void updateVolume() {
		float gain = (float) (GameHandler.volume * 3.5f);
		if (gain > 1)
			gain = 1;
		idle.setGain(gain);
		motorAcc.setVolume(motorAccInstance, gain);
		motorDcc.setVolume(motorDccInstance, gain);
		for (MediaAudio t : turbo) {
			t.setVolume();
		}
		redline.setVolume();
		nos.setVolume();

	}

	public void motorIdle() {

		try {
			stopMotorAcc();
			stopMotorDcc();
			idle.loop();
		} catch (Exception e) {

		}
	}

	public boolean isPlayingIdle() {
		return !(idle.isPaused() || idle.stopped());
	}

	public void motorPitch(double rpm, double totalRPM) {
		double value;
		double maxValue = 2;
		rpm = maxValue * rpm;

		if (rpm > totalRPM * maxValue)
			value = maxValue;
		else if (rpm < 0)
			value = 0;
		else
			value = rpm / totalRPM;

		value = -0.05 * Math.pow(2, value) + 0.8 * value;
		motorAcc.setSpeed(motorAccInstance, value);
		motorDcc.setSpeed(motorDccInstance, value);
	}

	private void stopMotorAcc() {
		if (motorAcc != null && motorAcc.getIsPlaying(motorAccInstance)) {
			motorAcc.stop(motorAccInstance);
		}
	}

	private void stopMotorDcc() {
		if (motorDcc != null && motorDcc.getIsPlaying(motorDccInstance)) {
			motorDcc.stop(motorDccInstance);
		}
	}

	public void motorAcc() {
		stopMotorDcc();

		if (idle != null && isPlayingIdle()) {
			stopIdle();
		}
		if (redline != null && redline.isPlaying()) {
			redline.stop();
		}
		if (!motorAcc.getIsPlaying(motorAccInstance)) {
			motorAcc.setFramePosition(motorAccInstance, 0);
			motorAcc.setLooping(motorAccInstance, -1);
			motorAcc.start(motorAccInstance);
		}

		if (turbo != null && isMediaArrayPlaying(turbo)) {
			stopMediaArray(turbo);
		}
	}

	public void motorDcc() {
		stopMotorAcc();
		if (!motorDcc.getIsPlaying(motorDccInstance)) {
			motorDcc.setFramePosition(motorDccInstance, 0);
			motorDcc.setLooping(motorDccInstance, -1);
			motorDcc.start(motorDccInstance);
		}

	}

	public void redline() {
		stopMotorAcc();

		redline.play();
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

		gear[nextSfx].play();
	}

	private boolean isMediaArrayPlaying(MediaAudio[] arr) {
		for (MediaAudio ma : arr) {
			if (ma.isPlaying())
				return true;
		}
		return false;
	}

	private void stopMediaArray(MediaAudio[] arr) {
		for (MediaAudio ma : arr) {
			ma.stop();
		}
	}

	public void stopAll() {
		if (idle != null && isPlayingIdle()) {
			stopIdle();
		}
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
	}

	private void stopIdle() {
		thread = new Thread(idlestopper);
		thread.start();
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

}
