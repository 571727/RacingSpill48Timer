package elem;

import startup.Main;

/**
 * Used to store stats about Car and more easily communicate Cars with the
 * server. Also used to restore a players car after they have lost connection
 * and rejoined.
 * 
 * @author jonah
 *
 */

public class CarRep implements Cloneable {

	private String name;
	private int nosTimeStandard;
	private int nosBottleAmountStandard;
	private double nosStrengthStandard;
	private double hp;
	private double weight;
	private double speedTop;
	private int rpmIdle;
	private int rpmTop;
	private int gearTop;
	private int tireGripTimeStandard;
	private double tireGripStrengthStandard;
	private double tireGripAreaTop;
	private double tireGripAreaBottom;
	private int[] upgradeLVLs;
	private double gearsbalance;
	private double maxValuePitch;
	private int highestSpeedAchived;

	/**
	 * @param name                     ex "Supra"
	 * @param nosTimeStandard          ex 1500
	 * @param nosBottleAmountStandard  ex 1
	 * @param nosStrengthStandard      ex 0.8
	 * @param hp                       ex 220
	 * @param weight                   ex 1400
	 * @param speedTop                 ex 250
	 * @param rpmIdle                  ex 1000
	 * @param rpmTop                   ex 8000
	 * @param gearTop                  ex 6
	 * @param tireGripTimeStandard     ex 1000
	 * @param tireGripStrengthStandard ex 0.5
	 * @param tireGripAreaTop          ex 24
	 * @param tireGripAreaBottom       ex 4
	 * @param upgradeLVLs              ex {1, 2 ...}
	 * @param maxValuePitch
	 */
	public CarRep(String name, int nosTimeStandard, int nosBottleAmountStandard, double nosStrengthStandard, double hp,
			double weight, double speedTop, int rpmIdle, int rpmTop, int gearTop, int tireGripTimeStandard,
			double tireGripStrengthStandard, double tireGripAreaTop, double tireGripAreaBottom, int[] upgradeLVLs,
			double gearsbalance, double maxValuePitch) {

		this.name = name;
		this.nosTimeStandard = nosTimeStandard;
		this.nosBottleAmountStandard = nosBottleAmountStandard;
		this.nosStrengthStandard = nosStrengthStandard;
		this.hp = hp;
		this.weight = weight;
		this.speedTop = speedTop;
		this.rpmIdle = rpmIdle;
		this.rpmTop = rpmTop;
		this.gearTop = gearTop;
		this.tireGripTimeStandard = tireGripTimeStandard;
		this.tireGripStrengthStandard = tireGripStrengthStandard;
		this.tireGripAreaTop = tireGripAreaTop;
		this.tireGripAreaBottom = tireGripAreaBottom;
		this.upgradeLVLs = upgradeLVLs;
		this.gearsbalance = gearsbalance;
		this.maxValuePitch = maxValuePitch;
	}

	public double getMaxValuePitch() {
		return maxValuePitch;
	}

	public void setMaxValuePitch(double maxValuePitch) {
		this.maxValuePitch = maxValuePitch;
	}

	public CarRep(String cloneString, int fromIndex) {
		setClone(cloneString, fromIndex);
	}

	public CarRep(String[] cloneStrings, int fromIndex) {
		setClone(cloneStrings, fromIndex);
	}

	public void setClone(String cloneString, int fromIndex) {
		setClone(cloneString.split("#"), fromIndex);
	}

	public void setClone(String[] values, int fromIndex) {
		name = values[fromIndex + 0];
		nosTimeStandard = Integer.parseInt(values[fromIndex + 1]);
		nosBottleAmountStandard = Integer.parseInt(values[fromIndex + 2]);
		nosStrengthStandard = Double.valueOf(values[fromIndex + 3]);
		hp = Double.valueOf(values[fromIndex + 4]);
		weight = Double.valueOf(values[fromIndex + 5]);
		speedTop = Double.valueOf(values[fromIndex + 6]);
		rpmIdle = Integer.parseInt(values[fromIndex + 7]);
		rpmTop = Integer.parseInt(values[fromIndex + 8]);
		gearTop = Integer.parseInt(values[fromIndex + 9]);
		tireGripTimeStandard = Integer.parseInt(values[fromIndex + 10]);
		tireGripStrengthStandard = Double.valueOf(values[fromIndex + 11]);
		tireGripAreaTop = Double.valueOf(values[fromIndex + 12]);
		tireGripAreaBottom = Double.valueOf(values[fromIndex + 13]);
		upgradeLVLsSetString(values[fromIndex + 14]);
		gearsbalance = Double.valueOf(values[fromIndex + 15]);
		maxValuePitch = Double.valueOf(values[fromIndex + 16]);
		highestSpeedAchived = Integer.parseInt(values[fromIndex + 17]);
	}

	public String getCloneString() {
		return name + "#" + nosTimeStandard + "#" + nosBottleAmountStandard + "#" + nosStrengthStandard + "#" + hp + "#"
				+ weight + "#" + speedTop + "#" + rpmIdle + "#" + rpmTop + "#" + gearTop + "#" + tireGripTimeStandard
				+ "#" + tireGripStrengthStandard + "#" + tireGripAreaTop + "#" + tireGripAreaBottom + "#"
				+ upgradeLVLsGetString() + "#" + gearsbalance + "#" + maxValuePitch + "#" + highestSpeedAchived;
	}

	public CarRep getCloneObject() throws CloneNotSupportedException {
		CarRep newCar = (CarRep) super.clone();
		int[] upgradeLVLs = new int[this.upgradeLVLs.length];
		for (int i = 0; i < this.upgradeLVLs.length; i++) {
			upgradeLVLs[i] = getUpgradeLVL(i);
		}
		newCar.setUpgradeLVLs(upgradeLVLs);
		return newCar;
	}

	@Override
	public CarRep clone() throws CloneNotSupportedException {
		return getCloneObject();
	}

	public int getUpgradeLVL(int LVL) {
		return upgradeLVLs[LVL];
	}

	private String upgradeLVLsGetString() {
		String r = "";

		for (int lvl : upgradeLVLs) {
			r += lvl + Main.UPGRADELVL_REGEX;
		}

		return r;
	}

	private void upgradeLVLsSetString(String string) {

		String[] input = string.split(Main.UPGRADELVL_REGEX);
		upgradeLVLs = new int[Upgrades.UPGRADE_NAMES.length];

		for (int i = 0; i < upgradeLVLs.length; i++) {
			upgradeLVLs[i] = Integer.parseInt(input[i]);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNosTimeStandard() {
		return nosTimeStandard;
	}

	public void setNosTimeStandard(int nosTimeStandard) {
		this.nosTimeStandard = nosTimeStandard;
	}

	public int getNosBottleAmountStandard() {
		return nosBottleAmountStandard;
	}

	public void setNosBottleAmountStandard(int nosBottleAmountStandard) {
		this.nosBottleAmountStandard = nosBottleAmountStandard;
	}

	public double getNosStrengthStandard() {
		return nosStrengthStandard;
	}

	public void setNosStrengthStandard(double nosStrengthStandard) {
		this.nosStrengthStandard = nosStrengthStandard;
	}

	public double getHp() {
		return hp;
	}

	public void setHp(double hp) {
		this.hp = hp;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getSpeedTop() {
		return speedTop;
	}

	public void setSpeedTop(double speedTop) {
		this.speedTop = Math.round(speedTop);
	}

	public int getRpmIdle() {
		return rpmIdle;
	}

	public void setRpmIdle(int rpmIdle) {
		this.rpmIdle = rpmIdle;
	}

	public int getRpmTop() {
		return rpmTop;
	}

	public void setRpmTop(int rpmTop) {
		this.rpmTop = rpmTop;
	}

	public int getGearTop() {
		return gearTop;
	}

	public void setGearTop(int gearTop) {
		this.gearTop = gearTop;
	}

	public int getTireGripTimeStandard() {
		return tireGripTimeStandard;
	}

	public void setTireGripTimeStandard(int tireGripTimeStandard) {
		this.tireGripTimeStandard = tireGripTimeStandard;
	}

	public double getTireGripStrengthStandard() {
		return tireGripStrengthStandard;
	}

	public void setTireGripStrengthStandard(double tireGripStrengthStandard) {
		this.tireGripStrengthStandard = tireGripStrengthStandard;
	}

	public double getTireGripAreaTop() {
		return tireGripAreaTop;
	}

	public void setTireGripAreaTop(double tireGripAreaTop) {
		this.tireGripAreaTop = tireGripAreaTop;
	}

	public double getTireGripAreaBottom() {
		return tireGripAreaBottom;
	}

	public void setTireGripAreaBottom(double tireGripAreaBottom) {
		this.tireGripAreaBottom = tireGripAreaBottom;
	}

	public int[] getUpgradeLVLs() {
		return upgradeLVLs;
	}

	public void setUpgradeLVLs(int[] upgradeLVLs) {
		this.upgradeLVLs = upgradeLVLs;
	}

	public String getInfo() {
		return name + ", " + String.format("%.1f", hp) + " HP, " + String.format("%.1f", weight) + " kg, TS: "
				+ (int) speedTop + " km/h, NOS: " + String.format("%.1f", nosStrengthStandard) + ", TB: "
				+ String.format("%.1f", tireGripStrengthStandard);
	}

	public String getStatsNew(int prevLVL, int nextLVL) {
		return "From LVL " + prevLVL + " to LVL " + nextLVL + ": <br/>" + stats();
	}

	public String getStatsCurrent() {
		return "<html>" + name + ": <br/>" + stats();
	}

	private String stats() {
		return "HP: " + String.format("%.1f", hp) + "<br/>" + "Weight: " + String.format("%.1f", weight) + " kg<br/>"
				+ "Topspeed: " + (int) speedTop + " km/h<br/>" + "Amount of gears: " + (int) gearTop + "<br/>"
				+ "NOS boost: " + String.format("%.1f", nosStrengthStandard) + "<br/>" + "NOS bottles: "
				+ (int) nosBottleAmountStandard + "<br/>Tireboost: " + String.format("%.1f", tireGripStrengthStandard);
	}

	public void iterateUpgradeLVL(int LVL) {
		upgradeLVLs[LVL]++;
	}

	public void upgradeRightShift(double change) {
		tireGripAreaTop = tireGripAreaTop * change;
		tireGripAreaBottom = tireGripAreaBottom * (1 - Math.abs(1 - change));
	}

	public void guarenteeRightShift() {
		tireGripAreaTop = -1;
	}

	public double getGearsbalance() {
		return gearsbalance;
	}

	public void setGearsbalance(double gearsbalance) {
		this.gearsbalance = gearsbalance;
	}

	public int getHighestSpeedAchived() {
		return highestSpeedAchived;
	}

	public void setHighestSpeedAchived(int highestSpeedAchived) {
		this.highestSpeedAchived = highestSpeedAchived;
	}

}
