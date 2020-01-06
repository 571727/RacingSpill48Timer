package handlers;

import java.util.ArrayList;

import main.RaceKeysSettings;
import player_local.Car;

public class RaceKeysHandler {

	private Car car;
	private ArrayList<Integer> keys;

	public void init(Car car, RaceKeysSettings keys) {
		this.car = car;
		this.keys = new ArrayList<Integer>();
		// init Key -s
		this.keys.add(keys.getThrottle());
		this.keys.add(keys.getBrake());
		this.keys.add(keys.getTurnLeft());
		this.keys.add(keys.getTurnRight());
		this.keys.add(keys.getClutch());
		this.keys.add(keys.getGearUp());
		this.keys.add(keys.getGearDown());
		this.keys.add(keys.getNOS());
		for (int i = 0; i < 7; i++) {
			this.keys.add(keys.getGear(i));
		}
	}

	public void updateKey(int key, int keycode) {
		this.keys.set(key, keycode);
	}

	public void keyPressed(int keycode) {

		if (keycode == keys.get(0))
			car.acc();
		else if (keycode == keys.get(1))
			car.brakeOn();
		else if (keycode == keys.get(2))
			car.turnLeftOn();
		else if (keycode == keys.get(3))
			car.turnRightOn();
		else if (keycode == keys.get(4))
			car.clutchOn();
		else if (keycode == keys.get(5))
			car.nos();

		// Gearbox
		else if (!car.getStats().isSequentialShift()) {
			for (int i = 0; i < 7; i++) {
				if (keycode == keys.get(i))
					car.shift(i);
			}
		} else {
			if (keycode == keys.get(6))
				car.shift(car.getStats().getGear() + 1);
			else if (keycode == keys.get(7))
				car.shift(car.getStats().getGear() - 1);
		}
	}

	public void keyReleased(int keycode) {
		if (keycode == keys.get(0)) 
			car.dcc();
		else if (keycode == keys.get(1)) 
			car.brakeOff();
		else if (keycode == keys.get(2)) 
			car.turnLeftOff();
		else if (keycode == keys.get(3)) 
			car.turnRightOff();
		else if (keycode == keys.get(4)) 
			car.clutchOff();
	}

}
