package scenes.game.player_local;

import main.Main;
import scenes.game.upgrade.Upgrades;

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
	private int[] upgradeLVLs;

	private double[] stats;
	private boolean[] changed;

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
		this.upgradeLVLs = upgradeLVLs;

		stats = new double[25];
		changed = new boolean[stats.length];

		stats[0] = nosTimeStandard;
		stats[1] = nosBottleAmountStandard;
		stats[2] = nosStrengthStandard;
		stats[3] = hp;
		stats[4] = weight;
		stats[5] = speedTop;
		stats[6] = rpmIdle;
		stats[7] = rpmTop;
		stats[8] = gearTop;
		stats[9] = tireGripTimeStandard;
		stats[10] = tireGripStrengthStandard;
		stats[11] = tireGripAreaTop;
		stats[12] = tireGripAreaBottom;
		stats[13] = gearsbalance;
		stats[14] = maxValuePitch;
		stats[15] = 0; // highestSpeedAchived
		stats[16] = 0; // super clutch
		stats[17] = 0; // turboHP
		stats[18] = 0; // doesSpool
		stats[19] = 0; // sequentialShift
		stats[20] = 0; // Point paradise
		stats[21] = 0; // Money mails
		stats[22] = 0; // GripStart
		stats[23] = 0; // StrutseAle strength std
		stats[24] = 0; // StrutseAle amount left

	}

	public CarRep(String cloneString, int fromIndex) {
		setClone(cloneString, fromIndex);
	}

	public CarRep(String[] cloneStrings, int fromIndex) {
		setClone(cloneStrings, fromIndex);
	}

	public CarRep() {
		// do notnin
	}

	public void setClone(String cloneString, int fromIndex) {
		setClone(cloneString.split("#"), fromIndex);
	}

	//TODO
	public void setClone(String[] values, int fromIndex) {

	}

	public String getCloneString() {
		String res = name + "#" + upgradeLVLsGetString();
		for (int i = 0; i < stats.length; i++) {
			res += "#";
			if (changed[i]) {
				res += stats[i];
				changed[i] = false;
			}
		}
		return res;
	}

	public String getCloneStringAll() {
		String res = name + "#" + upgradeLVLsGetString();
		for (double v : stats) {
			res += "#" + v;
		}
		changed = new boolean[changed.length];
		return res;
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

	public int[] getUpgradeLVLs() {
		return upgradeLVLs;
	}

	public void setUpgradeLVLs(int[] upgradeLVLs) {
		this.upgradeLVLs = upgradeLVLs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNosTimeStandard() {
		return (int) stats[0];
	}

	public void setNosTimeStandard(int nosTimeStandard) {
		stats[0] = nosTimeStandard;
		changed[0] = true;
	}

	public int getNosBottleAmountStandard() {
		return (int) stats[1];
	}

	public void setNosBottleAmountStandard(int nosBottleAmountStandard) {
		stats[1] = nosBottleAmountStandard;
		changed[1] = true;
	}

	public double getNosStrengthStandard() {
		return stats[2];
	}

	public void setNosStrengthStandard(double nosStrengthStandard) {
		stats[2] = nosStrengthStandard;
		changed[2] = true;
	}

	public double getHp() {
		return stats[3];
	}

	public void setHp(double hp) {
		stats[3] = hp;
		changed[3] = true;
	}

	public double getWeight() {
		return stats[4];
	}

	public void setWeight(double weight) {
		stats[4] = weight;
		changed[4] = true;
	}

	public double getSpeedTop() {
		return stats[5];
	}

	public void setSpeedTop(double speedTop) {
		stats[5] = Math.round(speedTop);
		changed[5] = true;
	}

	public int getRpmIdle() {
		return (int) stats[6];
	}

	public void setRpmIdle(int rpmIdle) {
		stats[6] = rpmIdle;
		changed[6] = true;
	}

	public int getRpmTop() {
		return (int) stats[7];
	}

	public void setRpmTop(int rpmTop) {
		stats[7] = rpmTop;
		changed[7] = true;
	}

	public int getGearTop() {
		return (int) stats[8];
	}

	public void setGearTop(int gearTop) {
		stats[8] = gearTop;
		changed[8] = true;
	}

	public int getTireboostTimeStandard() {
		return (int) stats[9];
	}

	public void setTireboostTimeStandard(int tireGripTimeStandard) {
		stats[9] = tireGripTimeStandard;
		changed[9] = true;
	}

	public double getTireboostStrengthStandard() {
		return stats[10];
	}

	public void setTireboostStrengthStandard(double tireGripStrengthStandard) {
		stats[10] = tireGripStrengthStandard;
		changed[10] = true;
	}

	public double getTireboostAreaTop() {
		return stats[11];
	}

	public void setTireboostAreaTop(double tireGripAreaTop) {
		stats[11] = tireGripAreaTop;
		changed[11] = true;
	}

	public double getTireboostAreaBottom() {
		return stats[12];
	}

	public void setTireGripboostBottom(double tireGripAreaBottom) {
		stats[12] = tireGripAreaBottom;
		changed[12] = true;
	}

//FIXME infos in carrep to show stats
	public String getInfo() {
//		return name + ", " + String.format("%.1f", hp) + " HP, " + String.format("%.1f", weight) + " kg, TS: "
//				+ (int) speedTop + " km/h, NOS: " + String.format("%.1f", nosStrengthStandard) + ", TB: "
//				+ String.format("%.1f", tireGripStrengthStandard);
		return "fixme";
	}

	public String getStatsNew(int prevLVL, int nextLVL) {
		return "<font size='4'>From LVL " + prevLVL + " to LVL " + nextLVL + ":</font><br/>" + stats();
	}

	public String getStatsCurrent() {
		return "<html><font size='4'>" + name + ":</font><br/>" + stats();
	}

	private String stats() {
//		return "HP: " + String.format("%.1f", hp) + "<br/>" + "Weight: " + String.format("%.1f", weight) + " kg<br/>"
//				+ "Topspeed: " + (int) speedTop + " km/h<br/>" + "Amount of gears: " + (int) gearTop + "<br/>"
//				+ "NOS boost: " + String.format("%.1f", nosStrengthStandard) + "<br/>" + "NOS bottles: "
//				+ (int) nosBottleAmountStandard + "<br/>Tireboost: " + String.format("%.1f", tireGripStrengthStandard);
		return "fixme";
	}

	public int iterateUpgradeLVL(int LVL) {
		upgradeLVLs[LVL]++;
		return upgradeLVLs[LVL];
	}

	public void upgradeRightShift(double change) {
		stats[11] = stats[11] * change;
		stats[12] = stats[12] * (1 - Math.abs(1 - change));
		changed[11] = true;
		changed[12] = true;
	}

	public void guarenteeRightShift() {
		stats[11] = -1;
		changed[11] = true;
	}

	public double getGearsbalance() {
		return stats[13];
	}

	public void setGearsbalance(double gearsbalance) {
		stats[13] = gearsbalance;
		changed[13] = true;
	}

	public double getMaxValuePitch() {
		return stats[14];
	}

	public void setMaxValuePitch(double maxValuePitch) {
		stats[14] = maxValuePitch;
		changed[14] = true;
	}

	public int getHighestSpeedAchived() {
		return (int) stats[15];
	}

	public void setHighestSpeedAchived(int highestSpeedAchived) {
		stats[15] = highestSpeedAchived;
		changed[15] = true;
	}

	public boolean isClutchSuper() {
		return stats[16] == 1;
	}

	public void setClutchSuper(boolean clutchSuper) {
		stats[16] = clutchSuper ? 1 : 0;
		changed[16] = true;
	}

	public double getTurboHP() {
		return stats[17];
	}

	public void setTurboHP(double d) {
		stats[17] = d;
		changed[17] = true;
	}

	public void setDoesSpool(boolean b) {
		stats[18] = b ? 1 : 0;
		changed[18] = true;
	}

	public boolean doesSpool() {
		return stats[18] == 1;
	}

	public boolean isSequentialShift() {
		return stats[19] == 1;
	}

	public void setSequentialShift(boolean sequentialShift) {
		this.stats[19] = sequentialShift ? 1 : 0;
		changed[19] = true;
	}

	public int getPointParadise() {
		return (int) stats[20];
	}

	public void setPointParadise(int pp) {
		stats[20] = pp;
		changed[20] = true;
	}

	public int getMoneyMails() {
		return (int) stats[21];
	}

	public void setMoneyMails(int mm) {
		stats[21] = mm;
		changed[21] = true;
	}

	public double getGripStartStandard() {
		return stats[22];
	}

	public void setGripStartStandard(double d) {
		stats[22] = d;
		changed[22] = true;
	}

	public void setStrutseAleStrengthStandard(int i) {
		stats[23] = i;
		changed[23] = true;
	}

	public double getStrutseAleStrengthStandard() {
		return stats[23];
	}

	public int getStrutseAleAmountLeft() {
		return (int) stats[24];
	}

	public void setStrutseAleAmountLeft(int i) {
		stats[24] = i;
		changed[24] = true;
	}

}
