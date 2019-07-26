package elem;

import audio.RaceAudio;

public class Car implements Cloneable {

	private boolean gas;
	private boolean idle;
	private boolean brake;
	private boolean clutch;
	private boolean hasTurbo;
	private boolean hasNOS;
	private boolean gearTooHigh;
	private boolean NOSON;
	private boolean engineOn;
	private boolean changed;
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
	private double resistance;
	private int gear;
	private int totalGear;
	private int totalRPM;
	private int rpm;
	private String carStyle;
	private RaceAudio audio;
	private int idleSpeed;

	public Car(String cartype) {

		hasTurbo = false;
		hasNOS = false;
		gearTooHigh = false;

		speedLinear = 0f;
		nosTimeLeft = 0;
		nosTimeToGive = 3000;
		nosTimeToGiveStandard = 3000;
		nosAmountLeft = 0;
		nosAmountLeftStandard = 0;
		nosStrength = 0;
		nosStrengthStandard = 0;
		topSpeed = 250;
		resistance = 1.0;
		idleSpeed = 1000;

		// Kanskje Lada der kj�relyden er hardbass.

		switch (cartype) {
		case "M3":
			hp = 300;
			totalWeight = 1549;
			totalGear = 6;
			totalRPM = 8000;
			break;
		case "Supra":
			hp = 220;
			totalWeight = 1400;
			totalGear = 5;
			totalRPM = 7800;
			break;
		case "Mustang":
			hp = 310;
			totalWeight = 1607;
			totalRPM = 7500;
			totalGear = 5;
			break;
		case "Bentley":
			hp = 650;
			totalRPM = 2500;
			totalWeight = 3048;
			totalGear = 4;
			break;
		case "Skoda Fabia":
			hp = 101;
			totalWeight = 950;
			totalRPM = 5500;
			totalGear = 5;
			break;
		case "Corolla":
			hp = 120;
			totalWeight = 1100;
			totalRPM = 6000;
			totalGear = 5;
			break;
		}
		setCarStyle(cartype.toLowerCase());
		audio = new RaceAudio(carStyle);

		updateSpeedInc();
//		System.out.println("Weightcalc: " + weightcalc +", spdinc: " + spdinc);
	}

	public void updateVolume() {
		audio.updateVolume();
	}

	public boolean isHasTurbo() {
		return hasTurbo;
	}

	public void setHasTurbo(boolean hasTurbo) {
		this.hasTurbo = hasTurbo;
	}

	public boolean isHasNOS() {
		return hasNOS;
	}

	public void setHasNOS(boolean hasNOS) {
		this.hasNOS = hasNOS;
	}

	public double getTopSpeed() {
		return topSpeed;
	}

	public void setTopSpeed(double topSpeed) {
		this.topSpeed = topSpeed;
	}

	public void updateSpeed() {
		if (engineOn) {
			changed = false;

			if (gas) {
				if (rpm < totalRPM - 60)
					rpm += hp * 1.5 * resistance;
				else
					rpm = totalRPM - 100;
			} else {
				if (rpm > idleSpeed)
					rpm -= hp * 1.15 * resistance;
				else
					rpm = idleSpeed;
			}

			if (!clutch && gear > 0 && idle && !gas) {
				// FIXME
				engineOn = false;
				checkIdle();

			} else if (gas && !clutch && gearCheck()) {

				if (speedLinear < ((gear - 1) * (500 / totalGear) - 35)) {
					speedLinear += spdinc / 6;
					gearTooHigh = true;
				} else {
					speedLinear += spdinc;
					gearTooHigh = false;
				}
				idle = false;

				if (nosTimeLeft > System.currentTimeMillis()) {
					speedLinear += nosStrength;
					NOSON = true;
				} else {
					NOSON = false;
				}
			} else {

				if (speedLinear > 0)
					speedLinear -= 0.5f;
				else
					speedLinear = 0;

				checkIdle();
			}

		} else {
			if (!changed) {
				changed = true;
				if (audio.isPlayingIdle())
					audio.stopAll();
				resetBooleans();
			}
		}
		if (brake) {

			if (speedLinear > 0)
				speedLinear -= spdinc;
			else
				speedLinear = 0;

			checkIdle();

		}

		speedActual = (-2 * Math.pow(speedLinear, 2) + 2000f * speedLinear) * (topSpeed / 500000f);
		int engineOnFactor = idleSpeed * (engineOn ? 1 : 0);
		double gearFactor = speedLinear / (gearMax() + 1);
		if (resistance == 0)
			rpm = (int) ((totalRPM - engineOnFactor) * gearFactor + engineOnFactor);
		// delt p� 72 fordi denne oppdateres hvert 50 millisek (1/3,6 * 1/20)
		distance += speedActual / 24;
	}

	private void checkIdle() {
		if (speedActual < 2 && !idle) {
			if (engineOn) {
				idle = true;
				audio.motorIdle();
			}
		}
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

	private float gearMax() {
		return gear * (500 / totalGear);
	}

	private boolean gearCheck() {
		if (speedLinear < gearMax()) {

			return true;
		} else {
			audio.redline();
			return false;
		}
	}

	public void acc() {
		if (!gas && engineOn) {
			gas = true;
			audio.motorAcc();
		}

	}

	public void dcc() {
		if (gas && engineOn) {
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
			resistance = 1.0;
			if (gas) {
				audio.turboSurge();
				audio.motorDcc();
			}
		}
	}

	public void clutchOff() {
		if (clutch) {
			clutch = false;
			if (gear > 0)
				resistance = 0.0;
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
			if (gear == 0)
				resistance = 1.0;
			audio.gearSound();
		}
	}

	public void nos() {
		if (nosAmountLeft > 0) {
			nosTimeLeft = System.currentTimeMillis() + nosTimeToGive;
			audio.nos();
			nosAmountLeft--;
		}
	}

	public void reset() {
		brake = false;
		clutch = false;
		resetBooleans();
		engineOn = false;
		speedLinear = 0f;
		nosTimeLeft = 0;
		nosTimeToGive = nosTimeToGiveStandard;
		nosAmountLeft = nosAmountLeftStandard;
		nosStrength = nosStrengthStandard;
		speedActual = 0;
		distance = 0;
		gear = 0;
		rpm = 0;
		resistance = 1.0;
		audio.stopAll();
		updateSpeedInc();
	}

	private void resetBooleans() {
		idle = false;
		gas = false;
		NOSON = false;
	}

	public void updateSpeedInc() {
		double w = (totalWeight - weightloss);
		double weightcalc = (0.00000033 * Math.pow(w, 2) + 0.00019 * w + 0.3);
		spdinc = (hp / weightcalc) / 100f;
	}

	public String showStats() {
		return "<html>" + carStyle.toUpperCase() + ": <br/>" + "HP: " + hp + "<br/>" + "Weight: "
				+ (totalWeight - weightloss) + "<br/>" + "NOS strength: " + nosStrengthStandard + "<br/>"
				+ "Amount of gears: " + totalGear + "<br/>" + "Topspeed: " + topSpeed;

	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
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

	public double getNosStrengthStandard() {
		return nosStrengthStandard;
	}

	public void setNosStrengthStandard(double nosStrengthStandard) {
		this.nosStrengthStandard = nosStrengthStandard;
	}

	public int getNosAmountLeftStandard() {
		return nosAmountLeftStandard;
	}

	public void setNosAmountLeftStandard(int nosAmountLeftStandard) {
		this.nosAmountLeftStandard = nosAmountLeftStandard;
	}

	/**
	 * @return radian that represents rpm from -180 to ca. 35 - 40 idk yet
	 */
	public double getTachometer() {
		return rpm * 0.03 - 203;
	}

	public boolean isGearTooHigh() {
		return gearTooHigh;
	}

	public void setGearTooHigh(boolean gearTooHigh) {
		this.gearTooHigh = gearTooHigh;
	}

	public int getNosAmountLeft() {
		return nosAmountLeft;
	}

	public void setNosAmountLeft(int nosAmountLeft) {
		this.nosAmountLeft = nosAmountLeft;
	}

	public boolean isNOSON() {
		return NOSON;
	}

	public void setNOSON(boolean nOSON) {
		NOSON = nOSON;
	}

	public int getRpm() {
		return rpm;
	}

	public void setRpm(int rpm) {
		this.rpm = rpm;
	}

	public boolean isEngineOn() {
		return engineOn;
	}

	public void setEngineOn(boolean engineOn) {
		this.engineOn = engineOn;
	}

	public boolean isIdle() {
		return idle;
	}

	public void setIdle(boolean idle) {
		this.idle = idle;
	}

	public double getResistance() {
		return resistance;
	}

	public void setResistance(double resistance) {
		this.resistance = resistance;
	}
}
