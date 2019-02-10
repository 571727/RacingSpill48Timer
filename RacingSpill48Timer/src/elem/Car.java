package elem;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Car {

	private boolean gas;
	private boolean brake;
	private boolean clutch;
	private boolean nos;
	private float speed;
	private float hp;
	private float weightloss;
	private float totalWeight;
	private float spdinc;
	private int gear;
	private int totalGear;
	private int totalRPM;
	private String carStyle;

	public Car(String cartype) {
		
//		function oppdaterFart(evt)
//		{
//			likningfart = (-2 * (fart*fart) + girHogd * fart)/girLengd;
//		}
		speed = 0f;
		
		switch (cartype) {
		case "M3":
			hp = 338;
			totalWeight = 1549;
			totalGear = 6;
			totalRPM = 8000;
			setCarStyle("m3");
			break;
		case "Supra":
			hp = 220;
			totalWeight = 1400;
			totalGear = 5;
			totalRPM = 7800;
			setCarStyle("supra");
			break;
		case "Mustang":
			hp = 310;
			totalWeight = 1607;
			totalRPM = 7500;
			totalGear = 5;
			setCarStyle("mustang");
			break;
		case "Bentley":
			hp = 650;
			totalRPM = 2500;
			totalWeight = 3048;
			totalGear = 4;
			setCarStyle("bentley");
			break;
		case "Skoda Fabia":
			hp = 101;
			totalWeight = 950;
			totalRPM = 5500;
			totalGear = 5;
			setCarStyle("skoda");
			break;
		case "Corolla":
			hp = 120;
			totalWeight = 1100;
			totalRPM = 6000;
			totalGear = 5;
			setCarStyle("corolla");
			break;
		}
		
		
		
		spdinc = (hp + (totalWeight - weightloss)) / 1000;
	}

	public void acc() {
		// TODO Auto-generated method stub

	}

	public void brake() {
		// TODO Auto-generated method stub

	}

	public void clutch() {
		// TODO Auto-generated method stub

	}

	public void shiftUp() {
		// TODO Auto-generated method stub

	}

	public void shiftDown() {
		// TODO Auto-generated method stub

	}

	public void nos() {
		// TODO Auto-generated method stub

	}

	public boolean isGas() {
		return gas;
	}

	public void setGas(boolean gas) {
		this.gas = gas;
	}

	public boolean isBrake() {
		return brake;
	}

	public void setBrake(boolean brake) {
		this.brake = brake;
	}

	public boolean isClutch() {
		return clutch;
	}

	public void setClutch(boolean clutch) {
		this.clutch = clutch;
	}

	public boolean isNos() {
		return nos;
	}

	public void setNos(boolean nos) {
		this.nos = nos;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getHp() {
		return hp;
	}

	public void setHp(float hp) {
		this.hp = hp;
	}

	public float getWeightloss() {
		return weightloss;
	}

	public void setWeightloss(float weightloss) {
		this.weightloss = weightloss;
	}

	public float getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(float totalWeight) {
		this.totalWeight = totalWeight;
	}

	public float getSpdinc() {
		return spdinc;
	}

	public void setSpdinc(float spdinc) {
		this.spdinc = spdinc;
	}

	public int getGear() {
		return gear;
	}

	public void setGear(int gear) {
		this.gear = gear;
	}

	public int getTotalGear() {
		return totalGear;
	}

	public void setTotalGear(int totalGear) {
		this.totalGear = totalGear;
	}

	public int getTotalRPM() {
		return totalRPM;
	}

	public void setTotalRPM(int totalRPM) {
		this.totalRPM = totalRPM;
	}

	public String getCarStyle() {
		return carStyle;
	}

	public void setCarStyle(String carStyle) {
		this.carStyle = carStyle;
	}

}
