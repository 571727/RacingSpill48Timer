package player_local;

public class CarStats {
	private boolean throttle;
	private boolean brake;
	private boolean clutch;
	private boolean hasTurbo;
	private boolean hasNOS;
	private boolean gearTooHigh;
	private boolean NOSON;
	private boolean TireboostON;
	private boolean sequentialShift;
	private boolean failedShift;
	private boolean soundBarrierBroken;

	private int nosBottleAmountLeft;
	private long nosTimeLeft;
	private double speed;
	private double distance;
	private double resistance;
	private int gear;
	private double rpm;
	private long tireboostTimeLeft;
	private double drag;
	private double spool;
	
	public void reset(CarRep rep) {
		brake = false;
		failedShift = false;
		clutch = false;
		throttle = false;
		NOSON = false;
		soundBarrierBroken = false;
		nosTimeLeft = 0;
		nosBottleAmountLeft = rep.getNosBottleAmountStandard();
		speed = 0;
		distance = 0;
		gear = 0;
		rpm = 0;
		tireboostTimeLeft = 0;
		resistance = 1.0;
		drag = 1;
		if (rep.getUpgradeLVL(3) >= 1) {
				// Turbo
				hasTurbo = true;
		}
		sequentialShift = rep.isSequentialShift();
	}

	public boolean isThrottle() {
		return throttle;
	}

	public void setThrottle(boolean gas) {
		this.throttle = gas;
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

	public boolean isTireboostON() {
		return TireboostON;
	}

	public void setTireboostON(boolean tireboostON) {
		TireboostON = tireboostON;
	}

	public boolean isSequentialShift() {
		return sequentialShift;
	}

	public void setSequentialShift(boolean sequentialShift) {
		this.sequentialShift = sequentialShift;
	}

	public boolean isFailedShift() {
		return failedShift;
	}

	public void setFailedShift(boolean failedShift) {
		this.failedShift = failedShift;
	}

	public boolean isSoundBarrierBroken() {
		return soundBarrierBroken;
	}

	public void setSoundBarrierBroken(boolean soundBarrierBroken) {
		this.soundBarrierBroken = soundBarrierBroken;
	}

	public int getNosBottleAmountLeft() {
		return nosBottleAmountLeft;
	}

	public void setNosBottleAmountLeft(int nosBottleAmountLeft) {
		this.nosBottleAmountLeft = nosBottleAmountLeft;
	}
	
	public void decreaseNosBottleAmountLeft() {
		nosBottleAmountLeft--;
	}

	public long getNosTimeLeft() {
		return nosTimeLeft;
	}

	public void setNosTimeLeft(long nosTimeLeft) {
		this.nosTimeLeft = nosTimeLeft;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public void addSpeed(double speed) {
		this.speed += speed;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public void addDistance(double distance) {
		this.distance += distance;
	}

	public double getResistance() {
		return resistance;
	}

	public void setResistance(double resistance) {
		this.resistance = resistance;
	}

	public int getGear() {
		return gear;
	}

	public void setGear(int gear) {
		this.gear = gear;
	}

	public double getRpm() {
		return rpm;
	}

	public void setRpm(double rpm) {
		this.rpm = rpm;
	}
	
	public void addRpm(double rpm) {
		this.rpm += rpm;
	}

	public long getTireboostTimeLeft() {
		return tireboostTimeLeft;
	}

	public void setTireboostTimeLeft(long tireboostTimeLeft) {
		this.tireboostTimeLeft = tireboostTimeLeft;
	}

	public double getDrag() {
		return drag;
	}

	public void setDrag(double drag) {
		this.drag = drag;
	}

	public double getSpool() {
		return spool;
	}

	public void setSpool(double spool) {
		this.spool = spool;
	}
	
	public void addSpool(double spool) {
		this.spool += spool;
	}
	
}
