package elem;

import audio.RaceAudio;

public class Car {

	private boolean gas;
	private boolean brake;
	private boolean clutch;
	private long nosTimeLeft;
	private long nosTimeToGive;
	private int nosAmountLeft;
	private float nosStrength;
	private float speedLinear;
	private float speedActual;
	private float hp;
	private float weightloss;
	private float totalWeight;
	private float spdinc;
	private float distance;
	private int gear;
	private int totalGear;
	private int totalRPM;
	private String carStyle;
	private RaceAudio audio;

	public Car(String cartype) {

//		function oppdaterFart(evt)
//		{
//			likningfart = 
//		}

		speedLinear = 0f;
		nosTimeLeft = 0;
		nosTimeToGive = 3000;
		nosAmountLeft = 1;
		nosStrength = 3.0f;

		//Kanskje Lada der kjørelyden er hardbass.
		
		
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
		audio = new RaceAudio(carStyle);
		spdinc = (hp + (totalWeight - weightloss)) / 1000;
	}

	public void updateSpeed() {
		if (!brake) {

			if (gas && !clutch && gearCheck()) {
				speedLinear += spdinc;
				if (nosTimeLeft > System.currentTimeMillis())
					speedLinear += nosStrength;
			} else {

				if (speedLinear > 0)
					speedLinear -= 0.5f;
				else
					speedLinear = 0;
			}

		} else {

			if (speedLinear > 0)
				speedLinear -= spdinc;
			else
				speedLinear = 0;

		}

		speedActual = (float) ((-2 * Math.pow(speedLinear, 2) + totalRPM * speedLinear) / totalRPM);

		// delt på 72 fordi denne oppdateres hvert 50 millisek (1/3,6 * 1/20)
		distance += speedActual / 24;
	}

	public float getSpeedLinear() {
		return speedLinear;
	}

	public void setSpeedLinear(float speedLinear) {
		this.speedLinear = speedLinear;
	}

	public float getSpeedActual() {
		return speedActual;
	}

	public void setSpeedActual(float speedActual) {
		this.speedActual = speedActual;
	}

	private boolean gearCheck() {
		return speedActual < gear * 100;
	}

	public void acc() {
		if (!gas) {
			gas = true;
			audio.motorAcc();
		}

	}

	public void dcc() {
		if (gas) {
			gas = false;
			audio.turboSurge();
			audio.motorDcc();
		}
	}

	public void brakeOn() {
		if (!brake) {
			brake = true;
		}
	}

	public void brakeOff() {
		if (brake) {
			brake = false;
		}
	}

	public void clutchOn() {
		if (!clutch) {
			clutch = true;
			if (gas) {
				audio.turboSurge();
				audio.motorDcc();
			}
		}
	}

	public void clutchOff() {
		if (clutch) {
			clutch = false;
			if (gas) {
				audio.motorAcc();
			}
		}
	}

	public void shiftUp() {
		if (gear < totalGear && clutch) {
			gear++;
			audio.gearSound();
		}
	}

	public void shiftDown() {
		if (gear > 0 && clutch) {
			gear--;
			audio.gearSound();
		}
	}

	public void nos() {
		if (nosAmountLeft > 0) {
			nosTimeLeft = System.currentTimeMillis() + nosTimeToGive;
			audio.nos();
		}
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

	public float getSpeed() {
		return speedLinear;
	}

	public void setSpeed(float speed) {
		this.speedLinear = speed;
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

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

}
