package elem;

import audio.RaceAudio;

public class Car {

	private boolean gas;
	private boolean idle;
	private boolean brake;
	private boolean clutch;
	private long nosTimeLeft;
	private long nosTimeToGive;
	private int nosTimeToGiveStandard;
	private int nosAmountLeft;
	private int nosAmountLeftStandard;
	private double nosStrength;
	private double nosStrengthStandard;
	private double speedLinear;
	private double speedActual;
	private double hp;
	private double weightloss;
	private double totalWeight;
	private double spdinc;
	private double distance;
	private double topSpeed;
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
		nosTimeToGiveStandard = 3000;
		nosAmountLeft = 1;
		nosAmountLeftStandard = 1;
		nosStrength = 3.0f;
		nosStrengthStandard = 3.0f;
		topSpeed = 400;

		// Kanskje Lada der kjørelyden er hardbass.

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
				idle = false;

				if (nosTimeLeft > System.currentTimeMillis())
					speedLinear += nosStrength;
			} else {

				if (speedLinear > 0)
					speedLinear -= 0.5f;
				else
					speedLinear = 0;

				if (speedActual < 2 && !idle) {
					idle = true;
					audio.motorIdle();
				}
			}

		} else {

			if (speedLinear > 0)
				speedLinear -= spdinc;
			else
				speedLinear = 0;

			if (speedActual < 2 && !idle) {
				idle = true;
				audio.motorIdle();
			}

		}

		speedActual = (-2 * Math.pow(speedLinear, 2) + 2000f * speedLinear) * (topSpeed / 500000f);

		// delt på 72 fordi denne oppdateres hvert 50 millisek (1/3,6 * 1/20)
		distance += speedActual / 24;
	}

	public double getSpeedLinear() {
		return speedLinear;
	}

	public void setSpeedLinear(double speedLinear) {
		this.speedLinear = speedLinear;
	}

	public double getSpeedActual() {
		return speedActual;
	}

	public void setSpeedActual(double speedActual) {
		this.speedActual = speedActual;
	}

	private boolean gearCheck() {
		return speedLinear < gear * (500 / totalGear);
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

	public void reset() {
		idle = false;
		gas = false;
		brake = false;
		clutch = false;
		speedLinear = 0f;
		nosTimeLeft = 0;
		nosTimeToGive = nosTimeToGiveStandard;
		nosAmountLeft = nosAmountLeftStandard;
		nosStrength = nosStrengthStandard;
		speedActual = 0;
		distance = 0;
		gear = 0;
		audio.stopAll();
	}

	public String showStats() {
		return "<html>" + carStyle.toUpperCase() + ": <br/>" + "HP: " + hp + "<br/>" + "Weight: "
				+ (totalWeight - weightloss) + "<br/>" + "NOS strength: " + nosStrengthStandard + "<br/>"
				+ "Amount of gears: " + totalGear + "<br/>" + "Max RPM: " + totalRPM;

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

	public double getSpeed() {
		return speedLinear;
	}

	public void setSpeed(double speed) {
		this.speedLinear = speed;
	}

	public double getHp() {
		return hp;
	}

	public void setHp(double hp) {
		this.hp = hp;
	}

	public double getWeightloss() {
		return weightloss;
	}

	public void setWeightloss(double weightloss) {
		this.weightloss = weightloss;
	}

	public double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(double totalWeight) {
		this.totalWeight = totalWeight;
	}

	public double getSpdinc() {
		return spdinc;
	}

	public void setSpdinc(double spdinc) {
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

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

}
