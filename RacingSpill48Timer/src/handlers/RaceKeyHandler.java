package handlers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import elem.Car;

public class RaceKeyHandler implements KeyListener{
	
	private Car car;
	
	public RaceKeyHandler(Car car) {
		this.car = car;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == 87) {
			//W
			car.acc();
		}
		if(e.getKeyCode() == 83) {
			//S
			car.brakeOn();
		}
		if(e.getKeyCode() == 32) {
			//Space
			car.clutchOn();
		}
		
		if(e.getKeyCode() == 16) {
			//LShift
			car.shiftUp();
		}
		if(e.getKeyCode() == 17) {
			//LShift
			car.shiftDown();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == 87) {
			//W
			car.dcc();
		}
		if(e.getKeyCode() == 83) {
			//S
			car.brakeOff();
		}
		if(e.getKeyCode() == 32) {
			//Space
			car.clutchOff();
		}
		
		
		if(e.getKeyCode() == 69) {
			//E
			car.nos();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
