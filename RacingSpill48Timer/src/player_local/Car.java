package player_local;

import audio.RaceAudio;

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
		funcs = new CarFuncs(true);
	}

	public void updateSpeed(double tickFactor) {

		funcs.updateSpeedInc(rep, stats);
		double speedChange = 0;

		if (stats.isEngineOn()) {
			// MOVEMENT
			speedChange += funcs.determineSpeedChange(rep, stats, audio);

			// Check for new record
			if (stats.getSpeed() > rep.getHighestSpeedAchived())
				rep.setHighestSpeedAchived(stats.getSpeed());

			// RPM
			funcs.updateRPM(rep, stats, tickFactor);

			// SOUND
			audio.motorPitch(stats.getRpm(), rep.getRpmTop(), rep.getMaxValuePitch());
			audio.turbospoolPitch(stats.getRpm(), rep.getRpmTop());
			audio.straightcutgearsPitch(stats.getSpeed(), rep.getSpeedTop());

		} else {
			// GLIDING
			speedChange += funcs.decelerateCar(stats);
		}

		// BRAKING
		if (stats.isBrake()) {
			speedChange += funcs.brake(stats);
			funcs.checkIdle(stats, audio);
		}

		// FINALLY CHANGE SPEED OF CAR
		stats.setSpeed(speedChange * tickFactor);

		funcs.soundBarrier(stats, audio);
		funcs.airResistance(rep, stats);
		funcs.moveOverDistance(stats, tickFactor);
	}

	public void toggleEngineOn() {
		if (stats.isEngineOn())
			setEngineOff();
		else
			setEngineOn();
	}

	public void setEngineOn() {
		audio.startEngine();
		stats.setEngineOn();
	}

	public void setEngineOff() {
		stats.setEngineOff();
		audio.stopMotor();
		funcs.checkIdle(stats, audio);
	}

	public void tryTireboost() {
		if (funcs.rightShiftTireboost(rep, stats)) {
			funcs.tireboost(rep, stats, System.currentTimeMillis(), 1);
			audio.playTireboost();
		}
	}

	public void acc() {
		if (!stats.isThrottle() && stats.isEngineOn()) {
			stats.setThrottle(true);
			audio.motorAcc(stats.isHasTurbo());
			stats.setFailedShift(false);
		}
	}

	public void dcc() {
		if (stats.isThrottle() && stats.isEngineOn()) {
			stats.setThrottle(false);
			if (stats.isHasTurbo())
				audio.turboBlowoff();
			audio.motorDcc();
		}
	}

	public void brakeOn() {
		stats.setBrake(true);
	}

	public void brakeOff() {
		stats.setBrake(false);
	}

	public void turnLeftOn() {
		// TODO Auto-generated method stub

	}

	public void turnRightOn() {
		// TODO Auto-generated method stub

	}

	public void clutchOn() {
		if (!stats.isClutch()) {
			stats.setClutch(true);
			stats.setResistance(1.0);

			if (stats.isThrottle()) {
				if (stats.isHasTurbo())
					audio.turboBlowoff();
				audio.motorDcc();
			}
		}
	}

	public void clutchOff() {
		if (stats.isClutch()) {

			stats.setClutch(false);
			if (stats.getGear() > 0)
				stats.setResistance(0.0);

			if (stats.isThrottle()) {
				audio.motorAcc(stats.isHasTurbo());
			}
		}
	}

	public void shift(int gear) {
		if (gear <= rep.getGearTop() && gear >= 0) {
			if (stats.isClutch()) {
				funcs.changeGear(stats, audio, gear);
			} else if (stats.isSequentialShift()) {

				if (gear == 0)
					stats.setResistance(1.0);
				else
					stats.setResistance(0.0);

				funcs.changeGear(stats, audio, gear);
			}
		} else {
			stats.setFailedShift(true);
			audio.grind();
		}
	}

	public void nos() {
		nos(System.currentTimeMillis(), 1);
	}

	public void nos(long comparedTimeValue, int divideTime) {
		if (stats.getNosBottleAmountLeft() > 0) {

			stats.setNosTimeLeft(comparedTimeValue + rep.getNosTimeStandard() / divideTime);
			stats.removeNosBottleAmountLeft(1);
			audio.nos();

		}
	}

	public void reset() {
		audio.stopAll();
		audio.closeAll();
		stats.reset(rep);
	}

	public String showStats(int prevLVL, int nextLVL) {
		return rep.getStatsNew(prevLVL, nextLVL);
	}

	public String showStats() {
		return rep.getStatsCurrent();
	}

	public CarRep clone() throws CloneNotSupportedException {
		return rep.getCloneObject();
	}

	public String getCarName() {
		return rep.getName();
	}

	public void setCarName(String name) {
		rep.setName(name);
	}

	public double getNosStrengthStandard() {
		return rep.getNosStrengthStandard();
	}

	public void setNosStrengthStandard(double nosStrengthStandard) {
		rep.setNosStrengthStandard(nosStrengthStandard);
		if (nosStrengthStandard > 0)
			stats.setHasNOS(true);
	}

	/**
	 * @return radian that represents rpm from -180 to ca. 35 - 40 idk yet
	 */
	public double getTachometer() {
		return 235 * ((double) (stats.getRpm() + 1) / (double) rep.getRpmTop()) - 203;
	}

	public RaceAudio getAudio() {
		return audio;
	}

	public void setAudio(RaceAudio audio) {
		this.audio = audio;
	}

	public CarFuncs getFuncs() {
		return funcs;
	}

	public void setFuncs(CarFuncs funcs) {
		this.funcs = funcs;
	}

	public CarRep getRep() {
		return rep;
	}

	public void setRep(CarRep rep) {
		this.rep = rep;
	}

	public CarStats getStats() {
		return stats;
	}

	public void setStats(CarStats stats) {
		this.stats = stats;
	}

	public void turnLeftOff() {
		// TODO Auto-generated method stub

	}

	public void turnRightOff() {
		// TODO Auto-generated method stub

	}

}
