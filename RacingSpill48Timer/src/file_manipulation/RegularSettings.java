package file_manipulation;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class RegularSettings extends Settings {

	private int lineWidth, lineHeight, lineFullscreen, lineDiscID;

	public RegularSettings() {

		lineWidth = 0;
		lineHeight = 1;
		lineFullscreen = 2;
		lineDiscID = 3;

		if (super.init("settings.properties")) {
			// Get resolution that is set to the computer right now.
			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			int width = gd.getDisplayMode().getWidth();
			int height = gd.getDisplayMode().getHeight();
			boolean fullscreen = true;

			setWidth(width);
			setHeight(height);
			setFullscreen(fullscreen);
		}
	}

	public void setWidth(int v) {
		writeToLine("Width=" + v, lineWidth);
	}

	public int getWidth() {
		return getSettingInteger(lineWidth);
	}

	public void setHeight(int v) {
		writeToLine("Height=" + v, lineHeight);
	}
	
	public int getHeight() {
		return getSettingInteger(lineHeight);
	}

	public void setFullscreen(boolean v) {
		writeToLine("Fullscreen=" + (v ? 1 : 0), lineFullscreen);
	}

	public boolean getFullscreen() {
		return getSettingBoolean(lineFullscreen);
	}

	public void setDiscID(long v) {
		writeToLine("DiscID=" + v, lineDiscID);
	}

	public long getDiscID() {
		return getSettingLong(lineDiscID);
	}

}
