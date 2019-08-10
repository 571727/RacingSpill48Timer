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
	private double currentWeight;
	private double spdinc;
	private double distance;
	private double topSpeed;
	private double resistance;
	private int gear;
	private int totalGear;
	private int totalRPM;
	private int rpm;
	private String carName;
	private RaceAudio audio;
	private int idleSpeed;
	private double gearsbalance;
	private boolean upgradedGears;
	private boolean audioActivated;
	private double maxValuePitch;
	private int highestSpeedAchived;
	private long gearBoostTime;
	private double gearBoost;
	private boolean gearBoostON;
	private double gearBoostSTD;
	private double top;
	private double mid;
	private double bot;

	/**
	 * carName + "#" + hp + "#" + (totalWeight - weightloss) + "#" +
	 * nosStrengthStandard + "#" + totalGear + "#" + topSpeed + "#" +
	 * highestSpeedAchived;
	 */
	public Car(String[] cloneToServerString, int fromIndex) {
		updateServerClone(cloneToServerString, fromIndex);
	}

	public Car(String cartype, boolean audioActivated) {

		hasTurbo = false;
		hasNOS = false;
		gearTooHigh = false;
		this.audioActivated = audioActivated;

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
		gearsbalance = 1.0;
		idleSpeed = 1000;
		gearBoostSTD = 1;
		top = 24;
		mid = 4;
		bot = 2.6;

		// Kanskje Lada der kj�relyden er hardbass.

		maxValuePitch = 2;

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
			hp = 64;
			totalWeight = 1090;
			totalRPM = 5500;
			totalGear = 5;
			maxValuePitch = 4;
			break;
		case "Corolla":
			hp = 120;
			totalWeight = 1100;
			totalRPM = 6000;
			totalGear = 5;
			break;
		}
//		hp = 1600;
		setCarName(cartype.toLowerCase());
		if (audioActivated)
			audio = new RaceAudio(carName);

//		System.out.println("Weightcalc: " + weightcalc +", spdinc: " + spdinc);
	}

	public void updateServerClone(String[] values, int fromIndex) {
		carName = values[fromIndex + 0];
		setHp(Double.valueOf(values[fromIndex + 1]));
		setCurrentWeight(Double.valueOf(values[fromIndex + 2]));
		setNosStrengthStandard(Double.valueOf(values[fromIndex + 3]));
		setTotalGear(Integer.valueOf(values[fromIndex + 4]));
		setTopSpeed(Double.valueOf(values[fromIndex + 5]));
		setHighestSpeedAchived(Integer.valueOf(values[fromIndex + 6]));
		setGearBoostSTD(Double.valueOf(values[fromIndex + 7]));
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
					rpm += hp * ((double) totalRPM / 9000) * resistance;
				else
					rpm = totalRPM - 100;
			} else {
				if (rpm > idleSpeed)
					rpm -= hp * 0.5 * resistance;
				else
					rpm = idleSpeed;
			}
			audio.motorPitch(rpm, totalRPM, maxValuePitch);
			audio.turbospoolPitch(rpm, totalRPM);
			audio.straightcutgearsPitch(speedLinear, topSpeed);

			if (!clutch && gear > 0 && idle && !gas) {
				setEngineOn(false);
			} else if (gas && !clutch && gearCheck()) {

				accelerateCar();
				idle = false;

				if (nosTimeLeft > System.currentTimeMillis()) {
					speedLinear += nosStrength;
					NOSON = true;
				} else {
					NOSON = false;
				}

				if (gearBoostTime > System.currentTimeMillis()) {
					speedLinear += gearBoost;
					gearBoostON = true;
				} else {
					gearBoostON = false;
				}
			} else {

				decelerateCar();

				checkIdle();
			}

		} else {
			if (!changed) {
				changed = true;
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

		calculateActualSpeed();
		calculateDistance();
		updateSpeedInc();
	}

	public void decelerateCar() {
		if (speedLinear > 0)
			speedLinear -= 0.5f;
		else
			speedLinear = 0;
	}

	public void calculateActualSpeed() {
		speedActual = (-2 * Math.pow(speedLinear, 2) + 2000f * speedLinear) * (topSpeed / 500000f);
		int engineOnFactor = idleSpeed * (engineOn ? 1 : 0);
		double gearFactor = speedLinear / (gearMax() + 1);
		if (resistance == 0)
			rpm = (int) ((totalRPM - engineOnFactor) * gearFactor + engineOnFactor);
		if (speedActual > highestSpeedAchived)
			highestSpeedAchived = (int) speedActual;
	}

	public void calculateDistance() {
		// delt p� 72 fordi denne oppdateres hvert 50 millisek (1/3,6 * 1/20)
		distance += speedActual / 24;
	}

	public void accelerateCar() {
		if (speedLinear < ((gear - 1) * (500 / totalGear) - 35)) {
			speedLinear += spdinc / 6;
			gearTooHigh = true;

		} else {
			speedLinear += spdinc;
			gearTooHigh = false;
		}
	}

	private void checkIdle() {
		if (speedActual < 2 && !idle) {
			if (engineOn) {
				idle = true;
				audio.motorIdle();
			} else {
				rpm = 0;
				audio.stopAll();
			}
		}
	}

	public void tryGearBoost() {
		int rs = rightShift();
		if (rs == 2) {
			// Best boost
			gearBoostTime = System.currentTimeMillis() + 1000;
			gearBoost = gearBoostSTD;
		} else if (rs == 1) {
			// Good boost
			gearBoostTime = System.currentTimeMillis() + 1000;
			gearBoost = gearBoostSTD / 2;
		} else {
			// No boost
			gearBoostTime = 0;
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
		if (isGearCorrect()) {
			return true;
		} else {
			audio.redline();
			return false;
		}
	}

	public boolean isGearCorrect() {
		return speedLinear < gearMax();
	}

	public boolean isTopGear() {
		return gear == totalGear;
	}

	public void acc() {
		if (!gas && engineOn) {
			gas = true;
			audio.motorAcc(hasTurbo);
		}

	}

	public void dcc() {
		if (gas && engineOn) {
			gas = false;
			if (hasTurbo)
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
			audio.clutch();
			if (gas) {
				if (hasTurbo)
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
				audio.motorAcc(hasTurbo);
			}
		}
	}

	public void shift(int gear) {
		if (gear <= totalGear && clutch) {
			if (this.gear == 1 && gear == 2) {

			}
			this.gear = gear;

			if (audioActivated)
				audio.gearSound();
		}
	}

	public boolean isAudioActivated() {
		return audioActivated;
	}

	public void setAudioActivated(boolean audioActivated) {
		this.audioActivated = audioActivated;
	}

	public void shiftUp() {
		shift(gear++);
	}

	public void shiftDown() {
		shift(gear--);
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
		gearBoostTime = 0;
		gearBoost = 0;
		resistance = 1.0;
		if (audioActivated)
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
		double rpmCalc = (double) rpm / (double) totalRPM;
		spdinc = (hp * rpmCalc / weightcalc) / 100f * gearsbalance;
	}

	public String showStats() {
		return "<html>" + carName.toUpperCase() + ": <br/>" + "HP: " + hp + "<br/>" + "Weight: "
				+ (totalWeight - weightloss) + "<br/>" + "NOS strength: " + nosStrengthStandard + "<br/>"
				+ "Amount of gears: " + totalGear + "<br/>" + "Topspeed: " + topSpeed + "<br/>Tiregrip: "
				+ gearBoostSTD;

	}

	public String cloneToServerString() {
		return carName + "#" + hp + "#" + (totalWeight - weightloss) + "#" + nosStrengthStandard + "#" + totalGear + "#"
				+ topSpeed + "#" + highestSpeedAchived + "#" + gearBoostSTD;
	}

	public int rightShift() {
		int res = 0;
		if ((this.gear == 1 || this.gear == 0) && speedLinear < 2) {
			double tr = totalRPM;

			if (rpm < tr - tr / top && rpm > tr - tr / mid) {
				res = 2;
			} else if (rpm < tr - tr / mid && rpm > tr - tr / bot) {
				res = 1;
			}
		}
		return res;
	}

	public void upgradeRightShift(double change) {
		top = top * change;
		mid = mid * (1 - Math.abs(1 - change));
		bot = bot * (1 - Math.abs(1 - change));
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
		this.weightloss = Math.round(weightloss);
		setCurrentWeight();
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

	public String getCarName() {
		return carName;
	}

	public void setCarName(String carName) {
		this.carName = carName;
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
		if (nosStrengthStandard > 0)
			setHasNOS(true);
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
		return 235 * ((double) (rpm + 1) / (double) totalRPM) - 203;
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
		if (engineOn) {
			audio.openLines(hasTurbo, upgradedGears);
		} else {
			checkIdle();
			rpm = 0;
		}
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

	public double getGearsbalance() {
		return gearsbalance;
	}

	public void setGearsbalance(double gearsbalance) {
		this.gearsbalance = gearsbalance;
	}

	public boolean isUpgradedGears() {
		return upgradedGears;
	}

	public void setUpgradedGears(boolean upgradedGears) {
		this.upgradedGears = upgradedGears;
	}

	public double getCurrentWeight() {
		return currentWeight;
	}

	public void setCurrentWeight(double currentWeight) {
		this.currentWeight = currentWeight;
	}

	public void setCurrentWeight() {
		this.currentWeight = totalWeight - weightloss;
	}

	public int getHighestSpeedAchived() {
		return highestSpeedAchived;
	}

	public void setHighestSpeedAchived(int highestSpeedAchived) {
		this.highestSpeedAchived = highestSpeedAchived;
	}

	public boolean isGearBoostON() {
		return gearBoostON;
	}

	public void setGearBoostON(boolean gearBoostON) {
		this.gearBoostON = gearBoostON;
	}

	public double getGearBoostSTD() {
		return gearBoostSTD;
	}

	public void setGearBoostSTD(double gearBoostSTD) {
		this.gearBoostSTD = gearBoostSTD;
	}

}
