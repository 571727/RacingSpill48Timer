package file_manipulation;

import static org.lwjgl.glfw.GLFW.*;

public class ControlsSettings extends Settings {

	public ControlsSettings() {

		int[] gearList = { GLFW_KEY_N, GLFW_KEY_U, GLFW_KEY_J, GLFW_KEY_I, GLFW_KEY_K, GLFW_KEY_O, GLFW_KEY_L };

		if (super.init("keys.properties")) {
			// Set the standard keys to file ( WASD / arrows Space Shift Ctrl Enter )
			setThrottle(GLFW_KEY_W);
			setBrake(GLFW_KEY_S);
			setClutch(GLFW_KEY_SPACE);
			setNOS(GLFW_KEY_E);
			setStrutsAle(GLFW_KEY_Q);
			setBlowTurbo(GLFW_KEY_R);
			setGearUp(GLFW_KEY_RIGHT_SHIFT);
			setGearDown(GLFW_KEY_RIGHT_CONTROL);
			for (int i = 0; i < 7; i++) {
				setGear(gearList[i], i);
			}
		}
	}

	public void setThrottle(int v) {
		writeToLine("Throttle=" + v, 0);
	}

	public int getThrottle() {
		return getSettingInteger(0);
	}

	public void setBrake(int v) {
		writeToLine("Brake=" + v, 1);
	}

	public int getBrake() {
		return getSettingInteger(1);
	}

	public void setClutch(int v) {
		writeToLine("Clutch=" + v, 2);
	}

	public int getClutch() {
		return getSettingInteger(2);
	}

	public void setNOS(int v) {
		writeToLine("NOS=" + v, 3);
	}

	public int getNOS() {
		return getSettingInteger(3);
	}

	public void setGearUp(int v) {
		writeToLine("GearUp=" + v, 4);
	}

	public int getGearUp() {
		return getSettingInteger(4);
	}

	public void setStrutsAle(int v) {
		writeToLine("StrutsAle=" + v, 5);
	}

	public int getStrutsAle() {
		return getSettingInteger(5);
	}
	
	public void setBlowTurbo(int v) {
		writeToLine("BlowTurbo=" + v, 6);
	}

	public int getBlowTurbo() {
		return getSettingInteger(6);
	}
	
	public void setGearDown(int v) {
		writeToLine("GearDown=" + v, 7);
	}

	public int getGearDown() {
		return getSettingInteger(7);
	}

	public void setGear(int v, int gear) {
		writeToLine("Gear" + gear + "=" + v, 8 + gear);
	}

	public int getGear(int gear) {
		return getSettingInteger(8 + gear);
	}
}
