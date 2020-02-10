package player_local;

import audio.RaceAudio;
import scenes.upgrade.Upgrades;

public class Car {

	public static final String[] CAR_TYPES = { "Decentra", "Oldsroyal", "Fabulvania", "Thoroughbread", "Amourena", "Silvershield", "Devil's Rulebreaker", "Salty Jessica" };
	private CarStats stats;
	private CarFuncs funcs;
	private CarRep rep;
	private RaceAudio audio;

	/**
	 * carName + "#" + hp + "#" + (totalWeight - weightloss) + "#" +
	 * nosStrengthStandard + "#" + totalGear + "#" + topSpeed + "#" +
	 * highestSpeedAchived;
	 */
	public Car(String[] cloneToServerString, int fromIndex) {
		init();
		rep = new CarRep(cloneToServerString, fromIndex);
	}

	public Car(String cartype) {
		init();
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

		if (name.equals(CAR_TYPES[0])) {
			rpmIdle = 1000;
			hp = 220;
			weight = 1400;
			gearTop = 5;
			rpmTop = 7800;
			speedTop = 245;
		} else if (name.equals(CAR_TYPES[1])) {
			rpmIdle = 300;
			hp = 310;
			weight = 3207;
			gearTop = 5;
			rpmTop = 2500;
			speedTop = 172;

		} else if (name.equals(CAR_TYPES[2])) {
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

		rep = new CarRep(name, nosTimeStandard, nosBottleAmountStandard, nosStrengthStandard, hp, weight,
				speedTop, rpmIdle, rpmTop, gearTop, tireGripTimeStandard, tireGripStrengthStandard, tireGripAreaTop,
				tireGripAreaBottom, upgradeLVLs, gearsbalance, maxValuePitch);

		if (name.equals(CAR_TYPES[1])) {
			rep.guarenteeRightShift();
		}

	}
	
	public void init() {
		stats = new CarStats();
		funcs = new CarFuncs();
		audio = new RaceAudio();
	}

	public void reset() {
		stats.reset(rep);
	}

	public void updateSpeed(double tickFactor) {

		funcs.updateSpeed(stats, rep, tickFactor);
		audio.motorPitch(stats.getRpm(), rep.getRpmTop(), rep.getMaxValuePitch());
		audio.turbospoolPitch(stats.getRpm(), rep.getRpmTop());
		audio.straightcutgearsPitch(stats.getSpeed(), rep.getSpeedTop());

		if (stats.getRpm() + 100 >= rep.getRpmTop())
			audio.redline();
	}

	public void tryTireboost() {
		if (funcs.isTireboostRight(stats, rep)) {
			funcs.tireboost(stats, rep, System.currentTimeMillis(), 1);
			audio.playTireboost();
		}
	}

	public boolean isGearTop() {
		return stats.getGear() == rep.getGearTop();
	}

	public void acc() {
		if (!stats.isThrottle()) {
			stats.setThrottle(true);
			stats.setFailedShift(false);
			audio.motorAcc(stats.isHasTurbo());
		}
	}

	public void dcc() {
		if (stats.isThrottle()) {
			stats.setThrottle(false);
			if (stats.isHasTurbo())
				audio.turboBlowoff();
			audio.motorDcc();
		}
	}

	public void brakeOn() {
		if (!stats.isBrake()) {
			stats.setBrake(true);
		}
	}

	public void brakeOff() {
		if (stats.isBrake()) {
			stats.setBrake(false);
		}
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
			if (stats.isClutch() || stats.isSequentialShift()) {

				if (!stats.isClutch() || stats.isSequentialShift()) {
					if (gear == 0)
						stats.setResistance(1.0);
					else
						stats.setResistance(0.0);
				}

				stats.setGear(gear);
				stats.setFailedShift(false);
					audio.gearSound();
			} else {
				stats.setFailedShift(true);
				audio.grind();
			}
		}
	}

	public void shiftUp() {
		shift(stats.getGear() + 1);
	}

	public void shiftDown() {
		shift(stats.getGear() - 1);
	}

	public void nos() {
		funcs.nos(stats, rep, System.currentTimeMillis(), 1);
		audio.nos();
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

	public void setNosStrengthStandard(double nosStrengthStandard) {
		rep.setNosStrengthStandard(nosStrengthStandard);
		if (nosStrengthStandard > 0)
			stats.setNOSON(true);
	}

	/**
	 * @return radian that represents rpm from -180 to ca. 35 - 40 idk yet
	 */
	public double getTachometer() {
		return 235 * ((double) (stats.getRpm() + 1) / (double) rep.getRpmTop()) - 203;
	}

	public void strutsAle() {
		// TODO Auto-generated method stub
		
	}

	public void blowTurbo() {
		// TODO Auto-generated method stub
		
	}

	/*
	 * GETTERS AND SETTERS
	 */
	
	public CarStats getStats() {
		return stats;
	}

	public CarFuncs getFuncs() {
		return funcs;
	}

	public CarRep getRep() {
		return rep;
	}

	
}
