package elem;

import adt.UpgradeAction;

public class Upgrade {

	private UpgradeAction action;
	private int nameID;
	private int bonusLVL;
	private String upgrades;
	private String bonus;

	public Upgrade(UpgradeAction action, int nameID, String upgrades, String bonus, int bonusLVL) {
		this.action = action;
		this.nameID = nameID;
		this.upgrades = upgrades;
		this.bonus = bonus;
		this.bonusLVL = bonusLVL;
	}

	public String information(Car car) {
		return Upgrades.UPGRADE_NAMES[nameID] + ": <br/>" + upgrades + "<br/>" + "<font color="
				+ (hasUpgrade(nameID, car, bonusLVL) ? "'green'" : "'white'") + ">LVL " + bonusLVL + ": " + bonus
				+ "</font><br/><br/>";

	}

	public boolean hasUpgrade(int i, Car car, int comparedLVL) {
		return car.getUpgradeLVL(i) >= comparedLVL;
	}
	
	public boolean upgrade(Car car) {
		return action.upgrade(car);
	}
}
