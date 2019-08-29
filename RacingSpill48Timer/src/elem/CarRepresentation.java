package elem;

public class CarRepresentation {
	
	private String name;
	private int nosTimeStandard;
	private int nosBottleAmountStandard;
	private double nosStrengthStandard;
	private double hp;
	private double currentWeight;
	private double topSpeed;
	private int totalRPM;
	private int totalGear;
	private boolean upgradedGears;
	private double tireGripStrengthStandard;
	private int[] upgradeLVLs;

	public CarRepresentation(String name, int nosTimeStandard, int nosBottleAmountStandard, double nosStrengthStandard,
			double hp, double currentWeight, double topSpeed, int totalRPM, int totalGear, boolean upgradedGears,
			double tireGripStrengthStandard, int[] upgradeLVLs) {
		this.name = name;
		this.nosTimeStandard = nosTimeStandard;
		this.nosBottleAmountStandard = nosBottleAmountStandard;
		this.nosStrengthStandard = nosStrengthStandard;
		this.hp = hp;
		this.currentWeight = currentWeight;
		this.topSpeed = topSpeed;
		this.totalRPM = totalRPM;
		this.totalGear = totalGear;
		this.upgradedGears = upgradedGears;
		this.tireGripStrengthStandard = tireGripStrengthStandard;
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

	public double getTireGripStrengthStandard() {
		return tireGripStrengthStandard;
	}

	public void setTireGripStrengthStandard(double tireGripStrengthStandard) {
		this.tireGripStrengthStandard = tireGripStrengthStandard;
	}

	public int[] getUpgradeLVLs() {
		return upgradeLVLs;
	}

	public void setUpgradeLVLs(int[] upgradeLVLs) {
		this.upgradeLVLs = upgradeLVLs;
	}

}
