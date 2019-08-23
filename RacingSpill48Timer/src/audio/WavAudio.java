package audio;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.UnsupportedAudioFileException;

import audio.audiocue.AudioCue;
import audio.audiocue.AudioCueInstanceEvent;
import audio.audiocue.AudioCueListener;
import handlers.GameHandler;

public class WavAudio implements AudioCueListener {
	private AudioCue mediaPlayer;
	private int ins;

	public WavAudio(String file) {
		//TODO implement this instead of adding bullshit yourself in raceAudio feks
		String type = "wav";
		System.out.println("Finding file:");
		System.out.println("\"" + file + "\"");
		System.out.println(WavAudio.class.getResource(file + "." + type).toString());
		URL url = this.getClass().getResource("/sfx/" + file + "." + type);
		try {
			mediaPlayer = AudioCue.makeStereoCue(url, 3);
		} catch (UnsupportedAudioFileException | IOException e1) {
			e1.printStackTrace();
		}

		mediaPlayer.setName("straightcutgears");
		mediaPlayer.addAudioCueListener(this);

		ins = mediaPlayer.obtainInstance();

		setVolume(1);
	}

	public void setVolume(double factor) {
		double newVol = GameHandler.volume * factor;
		mediaPlayer.setVolume(ins, newVol);
	}

	public void play() {
		mediaPlayer.play(ins);
	}

	public void stop() {
		mediaPlayer.stop(ins);
	}

	public void loop() {
		mediaPlayer.setFramePosition(ins, 0);
		mediaPlayer.setLooping(ins, -1);
		mediaPlayer.start(ins);
	}

	public boolean isPlaying() {
		return mediaPlayer.isRunning();
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

	public void setRate(double rate) {
		mediaPlayer.setSpeed(ins, rate);
	}
}
