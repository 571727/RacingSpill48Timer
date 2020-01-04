package player_local;

import audio.RaceAudio;
import audio.SFX;
import elem.Upgrades;
import startup.Main;

public class Car {

	private CarFuncs funcs;
	private CarRep rep;
	private CarStats stats;
	private RaceAudio audio;
	

	/**
	 * carName + "#" + hp + "#" + (totalWeight - weightloss) + "#" +
	 * nosStrengthStandard + "#" + totalGear + "#" + topSpeed + "#" +
	 * highestSpeedAchived;
	 */
	public Car(String[] cloneToServerString, int fromIndex) {
		rep = new CarRep(cloneToServerString, fromIndex);
		init();
	}

	public Car(String cartype) {
		rep = CarsExpert.getRep(cartype);
		init();
	}
	
	public void init() {
		stats = new CarStats();
		funcs = new CarFuncs();
	}

	public void updateSpeed(double tickFactor) {

		funcs.updateSpeedInc(rep, stats);
		double speedLinearChange = 0;

		if (stats.isEngineOn()) {
			// MOVEMENT
				speedLinearChange += funcs.movement(rep, stats, audio);
			// RPM
			updateRPM(tickFactor);

			// SOUND
			if (audioActivated) {
				audio.motorPitch(rpm, representation.getRpmTop(), representation.getMaxValuePitch());
				audio.turbospoolPitch(rpm, representation.getRpmTop());
				audio.straightcutgearsPitch(speedLinear, representation.getSpeedTop());
			}
		} else {
				resetBooleans();

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

	public void setEngineOn() {
		audio.startEngine();
		stats.setEngineOn();
	}
	
	public void setEngineOff() {
		setEngineOff();
		audio.stopMotor();
		checkIdle();
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
			audio.stopAll();
			audio.closeAll();
			

		updateSpeedInc();
	}

	public boolean isSequentialShift() {
		return sequentialShift;
	}

	public void setSequentialShift(boolean sequentialShift) {
		this.sequentialShift = sequentialShift;
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
