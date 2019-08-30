package elem;

/**
 * Used to store stats about Car and more easily communicate Cars with the
 * server. Also used to restore a players car after they have lost connection
 * and rejoined.
 * 
 * @author jonah
 *
 */

public class CarRep {

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
	 */
	public CarRep(String name, int nosTimeStandard, int nosBottleAmountStandard, double nosStrengthStandard, double hp,
			double weight, double speedTop, int rpmIdle, int rpmTop, int gearTop, int tireGripTimeStandard,
			double tireGripStrengthStandard, double tireGripAreaTop, double tireGripAreaBottom, int[] upgradeLVLs) {

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
		this.speedTop = speedTop;
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

}
