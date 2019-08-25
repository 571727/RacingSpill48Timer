package audio;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import adt.Audio;
import audio.audiocue.AudioCue;
import audio.audiocue.AudioCueInstanceEvent;
import audio.audiocue.AudioCueListener;
import handlers.GameHandler;

public class WavAudio implements AudioCueListener, Audio {
	private AudioCue mediaPlayer;
	private int ins;

	public WavAudio(String file) {
		//TODO implement this instead of adding bullshit yourself in raceAudio feks
		String type = "wav";
		System.out.println("Finding file:");
		System.out.println("\"" + file + "\"");
		URL url = this.getClass().getResource(file + "." + type);
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
	
	public void setRate(double rate) {
		mediaPlayer.setSpeed(ins, rate);
	}

	public void setFramePosition(int i) {
		mediaPlayer.setFramePosition(ins, i);		
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

	public void open(int i) {
		try {
			mediaPlayer.open(i);
		} catch (IllegalStateException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		mediaPlayer.close();
	}

	

	
}
