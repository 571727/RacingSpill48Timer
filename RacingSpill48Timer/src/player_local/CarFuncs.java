package player_local;

public class CarFuncs {

	private int soundBarrierSpeed;

	public CarFuncs() {
		soundBarrierSpeed = 1234;
	}

	public void updateSpeed(CarStats stats, CarRep rep, double tickFactor) {

		double speedInc = speedInc(stats, rep);
		double speedChange = 0;

		// MOVEMENT
		if (stats.isThrottle() && !stats.isClutch() && isGearCorrect(stats, rep))
			speedChange = accelerateCar(stats, rep, speedInc, System.currentTimeMillis());
		else if (stats.isBrake())
			speedChange = brake(stats, speedInc);
		else
			speedChange = decelerateCar(stats);

		// RPM
		updateRPM(stats, rep, tickFactor);

		stats.addSpeed(speedChange * tickFactor);

		calculateDrag(stats, rep);
		calculateDistance(stats, tickFactor);
	}

	private double speedInc(CarStats stats, CarRep rep) {
		double w = rep.getWeight();
		double rpmCalc = 1;
		if (!rep.isClutchSuper())
			rpmCalc = stats.getRpm() / rep.getRpmTop();

		double hp = rep.getHp();
		if (rep.doesSpool())
			hp += (rep.getTurboHP() * stats.getSpool()) - rep.getTurboHP();

		return 6 * (hp * rpmCalc / w * rep.getGearsbalance()) * stats.getDrag();
	}

	private boolean isGearCorrect(CarStats stats, CarRep rep) {
		return stats.getSpeed() < gearMax(stats, rep);
	}

	private double gearMax(CarStats stats, CarRep rep) {
		return stats.getGear() * (500 / rep.getGearTop());
	}

	private double accelerateCar(CarStats stats, CarRep rep, double speedInc, long comparedTimeLeft) {
		double inc = 0;

		if (stats.getSpeed() < ((stats.getGear() - 1) * (500 / rep.getGearTop()) - 35)) {
			// Shifted too early
			inc = speedInc / 6;
			stats.setGearTooHigh(true);

		} else {
			// Perfect shift
			inc = speedInc;
			stats.setGearTooHigh(false);
		}

		if (stats.getNosTimeLeft() > comparedTimeLeft) {
			inc += rep.getNosStrengthStandard();
			stats.setNOSON(true);
		} else {
			stats.setNOSON(false);
		}

		if (stats.getTireboostTimeLeft() > comparedTimeLeft) {
			inc += rep.getTireboostStrengthStandard();
			stats.setTireboostON(true);
		} else {
			stats.setTireboostON(false);
		}

		return inc;
	}

	private double brake(CarStats stats, double speedInc) {
		double brake = 0;

		if (stats.getSpeed() > 0)
			brake = -speedInc;

		return brake;
	}

	private double decelerateCar(CarStats stats) {
		double dec = 0;

		if (stats.getSpeed() > 0)
			dec = -0.5f;

		return dec;
	}

	/**
	 * Updates RPM value based on engaged clutch and throttle
	 */
	private void updateRPM(CarStats stats, CarRep rep, double tickFactor) {
		double rpmChange = 0;

		if (stats.getResistance() == 0) {

			// If clutch engaged
			double gearFactor = stats.getSpeed() / (gearMax(stats, rep) + 1);
			stats.setRpm(rep.getRpmTop() * gearFactor);

		} else if (stats.isThrottle()) {

			// Not engaged but throttle down
			if (stats.getRpm() < rep.getRpmTop() - 60) {

				double rpmFactor = (rep.getRpmTop() * 0.8) + (stats.getRpm() * 0.2);
				rpmChange = rep.getHp() * (rpmFactor / (double) rep.getRpmTop()) * stats.getResistance();

				if (stats.getSpool() < 1)
					stats.addSpool(0.1 * tickFactor);
			} else
				// Redlining
				stats.setRpm(rep.getRpmTop() - 100);

		} else {

			// Not engaged and throttle not down
			if (stats.getRpm() > rep.getRpmIdle())
				rpmChange = -(rep.getHp() * 0.5 * stats.getResistance());
			else
				// Sets RPM to for instance 1000 rpm as standard.
				stats.setRpm(rep.getRpmIdle());
			stats.setSpool(0);
		}

		stats.addRpm(rpmChange * tickFactor);
	}

	private void calculateDrag(CarStats stats, CarRep rep) {
		double drag = -Math.pow(stats.getSpeed() / rep.getSpeedTop(), 5) + 1;
		if (drag < 0)
			drag = 0;
		stats.setDrag(drag);
	}

	private void calculateDistance(CarStats stats, double tickFactor) {
		// 25 ticks per sec. kmh, distance in meters. So, x / 3.6 / 25.
		stats.addDistance((stats.getSpeed() / 90) * tickFactor);
	}
	
	public boolean isTireboostRight(CarStats stats, CarRep rep) {
		if ((stats.getGear() == 1 || stats.getGear() == 0) && stats.getSpeed() < 2) {
			double rt = rep.getRpmTop();
			double top = rep.getTireboostAreaTop();
			double bot = rep.getTireboostAreaBottom();

			if (top == -1 && stats.getRpm() > rt / 2 || (stats.getRpm()< rt - rt / top && stats.getRpm() > rt - rt / bot)) {
				return true;
			}
		}
		return false;
	}
	
	public void tireboost(CarStats stats, CarRep rep, long comparedTimeValue, int divideTime) {
		stats.setTireboostTimeLeft(comparedTimeValue + rep.getTireboostTimeStandard() / divideTime);
	}

	public void nos(CarStats stats, CarRep rep, long comparedTimeValue, int divideTime) {
		if (stats.getNosBottleAmountLeft() > 0) {
			stats.setNosTimeLeft(comparedTimeValue + rep.getNosTimeStandard() / divideTime);
			stats.decreaseNosBottleAmountLeft();
		}
	}

	public int getSoundBarrierSpeed() {
		return soundBarrierSpeed;
	}
	
}
