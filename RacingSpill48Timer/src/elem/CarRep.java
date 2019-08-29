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
	private double currentWeight;
	private double topSpeed;
	private int idleSpeed;
	private int totalRPM;
	private int totalGear;
	private boolean upgradedGears;
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
	 * @param currentWeight            ex 1400
	 * @param topSpeed                 ex 250
	 * @param idleSpeed                ex 1000
	 * @param totalRPM                 ex 8000
	 * @param totalGear                ex 6
	 * @param upgradedGears            ex true
	 * @param tireGripTimeStandard     ex 1000
	 * @param tireGripStrengthStandard ex 0.5
	 * @param tireGripAreaTop          ex 24
	 * @param tireGripAreaBottom       ex 4
	 * @param upgradeLVLs              ex {1, 2 ...}
	 */
	public CarRep(String name, int nosTimeStandard, int nosBottleAmountStandard, double nosStrengthStandard, double hp,
			double currentWeight, double topSpeed, int idleSpeed, int totalRPM, int totalGear, boolean upgradedGears,
			int tireGripTimeStandard, double tireGripStrengthStandard, double tireGripAreaTop,
			double tireGripAreaBottom, int[] upgradeLVLs) {

		this.name = name;
		this.nosTimeStandard = nosTimeStandard;
		this.nosBottleAmountStandard = nosBottleAmountStandard;
		this.nosStrengthStandard = nosStrengthStandard;
		this.hp = hp;
		this.currentWeight = currentWeight;
		this.topSpeed = topSpeed;
		this.idleSpeed = idleSpeed;
		this.totalRPM = totalRPM;
		this.totalGear = totalGear;
		this.upgradedGears = upgradedGears;
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

	public double getCurrentWeight() {
		return currentWeight;
	}

	public void setCurrentWeight(double currentWeight) {
		this.currentWeight = currentWeight;
	}

	public double getTopSpeed() {
		return topSpeed;
	}

	public void setTopSpeed(double topSpeed) {
		this.topSpeed = topSpeed;
	}

	public int getIdleSpeed() {
		return idleSpeed;
	}

	public void setIdleSpeed(int idleSpeed) {
		this.idleSpeed = idleSpeed;
	}

	public int getTotalRPM() {
		return totalRPM;
	}

	public void setTotalRPM(int totalRPM) {
		this.totalRPM = totalRPM;
	}

	public int getTotalGear() {
		return totalGear;
	}

	public void setTotalGear(int totalGear) {
		this.totalGear = totalGear;
	}

	public boolean isUpgradedGears() {
		return upgradedGears;
	}

	public void setUpgradedGears(boolean upgradedGears) {
		this.upgradedGears = upgradedGears;
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
