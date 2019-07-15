package startup;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.newdawn.easyogg.OggClip;

import audio.MediaAudio;
import handlers.GameHandler;

public class Main {

	public static boolean DEBUG = true;

	public static void main(String[] args) {
		new GameHandler(5);
		
	}

}
