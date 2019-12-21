package elem;

import audio.RaceAudio;
import audio.SFX;
import startup.Main;

public class Car {

	private CarRep representation;
	private RaceAudio audio;
	private boolean gas;
	private boolean idle;
	private boolean brake;
	private boolean clutch;
	private boolean hasTurbo;
	private boolean upgradedGears;
	private boolean hasNOS;
	private boolean gearTooHigh;
	private boolean NOSON;
	private boolean engineOn;
	private boolean changed;
	private int nosBottleAmountLeft;
	private boolean soundBarrierBroken;
	private int soundBarrierSpeed;
	private long nosTimeLeft;
	private double speedLinear;
	private double speedActual;
	private double spdinc;
	private double distance;
	private double resistance;
	private int gear;
	private double rpm;
	private boolean audioActivated;
	private long tireGripTimeLeft;
	private double drag;
	private boolean TireGripON;
	private boolean sequentialShift;
	private boolean failedShift;
	private double spool;

	/**
	 * carName + "#" + hp + "#" + (totalWeight - weightloss) + "#" +
	 * nosStrengthStandard + "#" + totalGear + "#" + topSpeed + "#" +
	 * highestSpeedAchived;
	 */
	public Car(String[] cloneToServerString, int fromIndex) {
		representation = new CarRep(cloneToServerString, fromIndex);
	}

	public Car(String cartype, boolean audioActivated) {

		hasTurbo = false;
		hasNOS = false;
		gearTooHigh = false;
		this.audioActivated = audioActivated;

		soundBarrierSpeed = 1234;
		speedLinear = 0f;
		nosTimeLeft = 0;
		resistance = 1.0;
		drag = 1;

		String name;
		int nosTimeStandard;
		int nosBottleAmountStandard;
		double nosStrengthStandard;
		double hp;
		double weight;
		double speedTop;
		int rpmIdle;
		int rpmTop;
		int gearTop;
		int tireGripTimeStandard;
		double tireGripStrengthStandard;
		double tireGripAreaTop;
		double tireGripAreaBottom;
		int[] upgradeLVLs;
		double gearsbalance = 1.0;
		double maxValuePitch = 2;

		name = cartype;
		tireGripTimeStandard = 1000;
		tireGripStrengthStandard = 1;
		tireGripAreaTop = 24;
		tireGripAreaBottom = 4;
		nosStrengthStandard = 0;
		nosTimeStandard = 1500;
		nosBottleAmountStandard = 0;
		upgradeLVLs = new int[Upgrades.UPGRADE_NAMES.length];

		if (name.equals(Main.CAR_TYPES[0])) {
			rpmIdle = 1000;
			hp = 220;
			weight = 1400;
			gearTop = 5;
			rpmTop = 7800;
			speedTop = 245;
		} else if (name.equals(Main.CAR_TYPES[1])) {
			rpmIdle = 300;
			hp = 310;
			weight = 3207;
			gearTop = 5;
			rpmTop = 2500;
			speedTop = 172;
		} else if (name.equals(Main.CAR_TYPES[2])) {
			rpmIdle = 800;
			hp = 64;
			weight = 1090;
			gearTop = 5;
			rpmTop = 5500;
			speedTop = 162;
			nosBottleAmountStandard = 1;
			nosStrengthStandard = 0.6;
			maxValuePitch = 4;
		} else {
			rpmIdle = 1200;
			hp = 300;
			weight = 1549;
			gearTop = 6;
			rpmTop = 9200;
			speedTop = 259;
		}

		if (audioActivated)
			audio = new RaceAudio(name);

		representation = new CarRep(name, nosTimeStandard, nosBottleAmountStandard, nosStrengthStandard, hp, weight,
				speedTop, rpmIdle, rpmTop, gearTop, tireGripTimeStandard, tireGripStrengthStandard, tireGripAreaTop,
				tireGripAreaBottom, upgradeLVLs, gearsbalance, maxValuePitch);

	}

	public void updateVolume() {
		if (audioActivated)
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

	public double getSpeedTop() {
		return representation.getSpeedTop();
	}

	public void setSpeedTop(double topSpeed) {
		representation.setSpeedTop(Math.round(topSpeed));
	}

	public void updateSpeed(double tickFactor) {

		updateSpeedInc();
		double speedLinearChange = 0;

		if (engineOn) {
			changed = false;

			// MOVEMENT
			if (!clutch && gear > 0 && idle && !gas) {
				setEngineOn(false);
			} else if (gas && !clutch && gearCheck()) {

				speedLinearChange += accelerateCar(System.currentTimeMillis());
				idle = false;

			} else {
				speedLinearChange += decelerateCar();
				checkIdle();
			}

			// RPM
			updateRPM(tickFactor);

			// SOUND
			if (audioActivated) {
				audio.motorPitch(rpm, representation.getRpmTop(), representation.getMaxValuePitch());
				audio.turbospoolPitch(rpm, representation.getRpmTop());
				audio.straightcutgearsPitch(speedLinear, representation.getSpeedTop());
			}
		} else {
			if (!changed) {
				changed = true;
				resetBooleans();
			}

			speedLinearChange += decelerateCar();
		}

		if (brake) {
			speedLinearChange += brake();
			checkIdle();
		}

		speedLinear += speedLinearChange * tickFactor;

		calculateActualSpeed();

		calculateDrag();
		calculateDistance(tickFactor);
	}

	/**
	 * Updates RPM value based on engaged clutch and throttle
	 */
	public void updateRPM(double tickFactor) {
		double rpmChange = 0;

		if (resistance == 0) {

			// If clutch engaged
			int engineOnFactor = representation.getRpmIdle() * (engineOn ? 1 : 0);
			double gearFactor = speedLinear / (gearMax() + 1);
			rpm = (int) ((representation.getRpmTop() - engineOnFactor) * gearFactor + engineOnFactor);

		} else if (gas) {

			// Not engaged but throttle down
			if (rpm < representation.getRpmTop() - 60) {
				double rpmFactor = (representation.getRpmTop() * 0.8) + (rpm * 0.2);
				rpmChange = representation.getHp() * (rpmFactor / (double) representation.getRpmTop()) * resistance;
				if (spool < 1)
					spool += 0.1 * tickFactor;
			} else
				// Redlining
				rpm = representation.getRpmTop() - 100;

		} else {

			// Not engaged and throttle not down
			if (rpm > representation.getRpmIdle())
				rpmChange = -(representation.getHp() * 0.5 * resistance);
			else
				// Sets RPM to for instance 1000 rpm as standard.
				rpm = representation.getRpmIdle();
			spool = 0;
		}

		rpm = rpmChange * tickFactor + rpm;
	}

	private double brake() {
		double brake = 0;

		if (speedLinear > 0)
			brake = -spdinc;

		return brake;
	}

	public double decelerateCar() {
		double dec = 0;

		if (speedLinear > 0)
			dec = -0.5f;

		return dec;
	}

	public void calculateDrag() {
		drag = -Math.pow(speedActual / representation.getSpeedTop(), 5) + 1;
		if (drag < 0)
			drag = 0;

	}

	public void calculateActualSpeed() {
		double prev = speedActual;
		speedActual = (-2 * Math.pow(speedLinear, 2) + 2000f * speedLinear) * (representation.getSpeedTop() / 500000f);

		if (speedActual > representation.getHighestSpeedAchived())
			representation.setHighestSpeedAchived((int) speedActual);
		else if (speedActual < 0) {
			speedActual = 0;
			speedLinear = 0;
			return;
		}

		if (speedActual > soundBarrierSpeed && prev <= soundBarrierSpeed) {
			SFX.playMP3Sound("soundbarrier");
			if(!soundBarrierBroken) {
				nosBottleAmountLeft++;
				soundBarrierBroken = true;
			}
		}
	}

	public void calculateDistance(double tickFactor) {
		// 25 ticks per sec. kmh, distance in meters. So, x / 3.6 / 25.
		distance += (speedActual / 90) * tickFactor;
	}

	public double accelerateCar(long comparedTimeLeft) {
		double inc = 0;

		if (speedLinear < ((gear - 1) * (500 / representation.getGearTop()) - 35)) {
			// Shifted too early
			inc = spdinc / 6;
			gearTooHigh = true;

		} else {
			// Perfect shift
			inc = spdinc;
			gearTooHigh = false;
		}

		if (nosTimeLeft > comparedTimeLeft) {
			inc += representation.getNosStrengthStandard();
			NOSON = true;
		} else {
			NOSON = false;
		}

		if (tireGripTimeLeft > comparedTimeLeft) {
			inc += representation.getTireGripStrengthStandard();
			TireGripON = true;
		} else {
			TireGripON = false;
		}

		return inc;
	}

	private void checkIdle() {
		if (speedActual < 2 && !idle) {
			if (engineOn) {
				idle = true;
				if (audioActivated)
					audio.motorIdle();
			} else {
				rpm = 0;
				if (audioActivated)
					audio.stopAll();
			}
		}
	}

	public void tryGearBoost() {
		int rs = rightShift();
		if (rs == 2) {
			gearBoost(System.currentTimeMillis(), 1);
			SFX.playMP3Sound("tireboost");
		}
	}

	public void gearBoost(long comparedTimeValue, int divideTime) {
		tireGripTimeLeft = comparedTimeValue + representation.getTireGripTimeStandard() / divideTime;
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
		return gear * (500 / representation.getGearTop());
	}

	private boolean gearCheck() {
		if (isGearCorrect()) {
			return true;
		} else {
			if (audioActivated && gear > 0 || rpm + 100 >= representation.getRpmTop())
				audio.redline();
			return false;
		}
	}

	public boolean isGearCorrect() {
		return speedLinear < gearMax();
	}

	public boolean isTopGear() {
		return gear == representation.getGearTop();
	}

	public void acc() {
		if (!gas && engineOn) {
			gas = true;
			if (audioActivated)
				audio.motorAcc(hasTurbo);
			failedShift = false;
		}

	}

	public void dcc() {
		if (gas && engineOn) {
			gas = false;
			if (audioActivated) {
				if (hasTurbo)
					audio.turboBlowoff();
				audio.motorDcc();
			}
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
			if (audioActivated && gas) {
				if (hasTurbo)
					audio.turboBlowoff();
				audio.motorDcc();
			}
		}
	}

	public void clutchOff() {
		if (clutch) {
			clutch = false;
			if (gear > 0)
				resistance = 0.0;
			if (audioActivated && gas) {
				audio.motorAcc(hasTurbo);
			}
		}
	}

	public void shift(int gear) {
		if (gear <= representation.getGearTop() && gear >= 0) {
			if (clutch || sequentialShift) {

				if (!clutch && sequentialShift) {
					if (gear == 0)
						resistance = 1.0;
					else
						resistance = 0.0;
				}

				this.gear = gear;
				failedShift = false;
				if (audioActivated)
					audio.gearSound();
			} else if (audioActivated) {
				failedShift = true;
				audio.grind();
			}
		}
	}

	public boolean isAudioActivated() {
		return audioActivated;
	}

	public void setAudioActivated(boolean audioActivated) {
		this.audioActivated = audioActivated;
	}

	public void shiftUp() {
		shift(gear + 1);
	}

	public void shiftDown() {
		shift(gear - 1);
	}

	public void nos(long comparedTimeValue, int divideTime) {
		if (nosBottleAmountLeft > 0) {
			nosTimeLeft = comparedTimeValue + representation.getNosTimeStandard() / divideTime;
			nosBottleAmountLeft--;
			if (audioActivated)
				audio.nos();
		}
	}

	public void reset() {
		brake = false;
		failedShift = false;
		clutch = false;
		resetBooleans();
		engineOn = false;
		soundBarrierBroken = false;
		speedLinear = 0f;
		nosTimeLeft = 0;
		nosBottleAmountLeft = representation.getNosBottleAmountStandard();
		speedActual = 0;
		distance = 0;
		gear = 0;
		rpm = 0;
		tireGripTimeLeft = 0;
		resistance = 1.0;
		drag = 1;
		if (audioActivated) {
			audio.stopAll();
			audio.closeAll();
			if (representation.getUpgradeLVL(3) >= 1) {
				// Turbo
				hasTurbo = true;
			}

		}
		updateSpeedInc();
		sequentialShift = representation.isSequentialShift();
		upgradedGears = sequentialShift;
	}

	public boolean isSequentialShift() {
		return sequentialShift;
	}

	public void setSequentialShift(boolean sequentialShift) {
		this.sequentialShift = sequentialShift;
	}

	private void resetBooleans() {
		idle = false;
		gas = false;
		NOSON = false;
	}

	public void updateSpeedInc() {
		double w = representation.getWeight();
		double rpmCalc = 1;
		if (!representation.isClutchSuper())
			rpmCalc = (double) rpm / (double) representation.getRpmTop();

		double hp = representation.getHp();
		if (representation.doesSpool())
			hp += (representation.getTurboHP() * spool) - representation.getTurboHP();

		spdinc = 6 * (hp * rpmCalc / w * representation.getGearsbalance()) * drag;
	}

	public String showStats(int prevLVL, int nextLVL) {
		return representation.getStatsNew(prevLVL, nextLVL);
	}

	public String showStats() {
		return representation.getStatsCurrent();
	}

	public int rightShift() {
		int res = 0;
		if ((this.gear == 1 || this.gear == 0) && speedLinear < 2) {
			double rt = representation.getRpmTop();
			double top = representation.getTireGripAreaTop();
			double bot = representation.getTireGripAreaBottom();

			if (top == -1 && rpm > rt / 2 || (rpm < rt - rt / top && rpm > rt - rt / bot)) {
				res = 2;
			}
		}
		return res;
	}

	public void upgradeRightShift(double change) {
		representation.upgradeRightShift(change);
	}

	public void guarenteeRightShift() {
		representation.guarenteeRightShift();
	}

	public CarRep clone() throws CloneNotSupportedException {
		return representation.getCloneObject();
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
		return representation.getHp();
	}

	public void setHp(double hp) {
		representation.setHp(hp);
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

	public int getGearTop() {
		return representation.getGearTop();
	}

	public void setGearTop(int totalGear) {
		representation.getGearTop();
	}

	public int getRpmTop() {
		return representation.getRpmTop();
	}

	public void setRpmTop(int rpmTop) {
		representation.setRpmTop(rpmTop);
	}

	public String getCarName() {
		return representation.getName();
	}

	public void setCarName(String name) {
		representation.setName(name);
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getNosStrengthStandard() {
		return representation.getNosStrengthStandard();
	}

	public void setNosStrengthStandard(double nosStrengthStandard) {
		representation.setNosStrengthStandard(nosStrengthStandard);
		if (nosStrengthStandard > 0)
			setHasNOS(true);
	}

	public int getNosBottleAmountStandard() {
		return representation.getNosBottleAmountStandard();
	}

	public void setNosBottleAmountStandard(int nosBottleAmountStandard) {
		representation.setNosBottleAmountStandard(nosBottleAmountStandard);
	}

	/**
	 * @return radian that represents rpm from -180 to ca. 35 - 40 idk yet
	 */
	public double getTachometer() {
		return 235 * ((double) (rpm + 1) / (double) representation.getRpmTop()) - 203;
	}

	public int getNosBottleAmountLeft() {
		return nosBottleAmountLeft;
	}

	public void setNosBottleAmountLeft(int nosBottleAmountLeft) {
		this.nosBottleAmountLeft = nosBottleAmountLeft;
	}

	public boolean isGearTooHigh() {
		return gearTooHigh;
	}

	public void setGearTooHigh(boolean gearTooHigh) {
		this.gearTooHigh = gearTooHigh;
	}

	public boolean isNOSON() {
		return NOSON;
	}

	public void setNOSON(boolean nOSON) {
		NOSON = nOSON;
	}

	public int getRpm() {
		return (int) rpm;
	}

	public void setRpm(int rpm) {
		this.rpm = rpm;
	}

	public boolean isEngineOn() {
		return engineOn;
	}

	public void setEngineOn(boolean engineOn) {
		this.engineOn = engineOn;
		if (!engineOn) {
			rpm = 0;
			audio.stopMotor();
			checkIdle();

		} else if (audioActivated) {

			audio.startEngine();
		}

		if (speedActual <= 1 && gear > 0 && !clutch)
			failedShift = true;
		else
			failedShift = false;
	}

	public void setEngineOnActual(boolean b) {
		this.engineOn = b;
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

	public boolean isUpgradedGears() {
		return upgradedGears;
	}

	public void setUpgradedGears(boolean upgradedGears) {
		this.upgradedGears = upgradedGears;
	}

	public double getWeight() {
		return representation.getWeight();
	}

	public void setWeight(double weight) {
		representation.setWeight(weight);
	}

	public int getHighestSpeedAchived() {
		return representation.getHighestSpeedAchived();
	}

	public void setHighestSpeedAchived(int highestSpeedAchived) {
		representation.setHighestSpeedAchived(highestSpeedAchived);
	}

	public boolean isGearBoostON() {
		return TireGripON;
	}

	public void setGearBoostON(boolean gearBoostON) {
		this.TireGripON = gearBoostON;
	}

	public double getTireGripStrengthStandard() {
		return representation.getTireGripStrengthStandard();
	}

	public void setTireGripStrengthStandard(double tireGripStrengthStandard) {
		representation.setTireGripStrengthStandard(tireGripStrengthStandard);
	}

	public void iterateUpgradeLVL(int LVL) {
		representation.iterateUpgradeLVL(LVL);
	}

	public String getInfo() {
		return representation.getInfo();
	}

	public int getUpgradeLVL(int LVL) {
		return representation.getUpgradeLVL(LVL);
	}

	public CarRep getRepresentation() {
		return representation;
	}

	public void setRepresentation(CarRep representation) {
		this.representation = representation;
	}

	public RaceAudio getAudio() {
		return audio;
	}

	public void setAudio(RaceAudio audio) {
		this.audio = audio;
	}

	public void openLines() {
		if (audioActivated)
			audio.openLines(hasTurbo, upgradedGears);
	}

	public boolean isFailedShift() {
		return failedShift;
	}

}
