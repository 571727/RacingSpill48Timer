package handlers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import elem.Car;

public class RaceKeyHandler implements KeyListener {

	private Car car;

	public RaceKeyHandler(Car car) {
		this.car = car;
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == 87) {
			// W
			car.acc();
		}
		if (e.getKeyCode() == 83) {
			// S
			car.brakeOn();
		}
		if (e.getKeyCode() == 32) {
			// Space
			car.clutchOn();
		}
		// Gearbox
		if (!car.isSequentialShift()) {
			switch (e.getKeyCode()) {
			case 85:
				// first gear
				car.shift(1);
				break;
			case 74:
				// second gear
				car.shift(2);
				break;
			case 73:
				// third gear
				car.shift(3);
				break;
			case 75:
				// fourth gear
				car.shift(4);
				break;
			case 79:
				// fifth gear
				car.shift(5);
				break;
			case 76:
				// sixth gear
				car.shift(6);
				break;
			case 78:
				// neutral
				car.shift(0);
				break;

			}
		} else {
			if (e.getKeyCode() == 16) {
				// LShift
				car.shiftUp();
			}
			if (e.getKeyCode() == 17) {
				// LShift
				car.shiftDown();
			}
		}
		
		if (e.getKeyCode() == 84) {
			// T
			car.setEngineOn(!car.isEngineOn());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 87) {
			// W
			car.dcc();
		}
		if (e.getKeyCode() == 83) {
			// S
			car.brakeOff();
		}
		if (e.getKeyCode() == 32) {
			// Space
			car.clutchOff();
		}

		if (e.getKeyCode() == 69) {
			// E
			car.nos();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
