package scenes.regular;

import org.lwjgl.nuklear.NkContext;

import audio.AudioHandler;
import audio.lowstuff.Audio;
import file_manipulation.ControlsSettings;
import file_manipulation.RegularSettings;
import scenes.Scene;
import scenes.regular.visual.OptionsVisual;

public class OptionsScene extends Scene {

	public OptionsScene() {
		super(new OptionsVisual(), null, "options");
	}

	public void init(RegularSettings settings, ControlsSettings controlsSettings, AudioHandler audio) {
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick(double delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean keyInput(int keycode, int action) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void mouseButtonInput(int button, int action, double x, double y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePosInput(double x, double y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseScrollInput(double x, double y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEnterWindowInput(boolean entered) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	

}
