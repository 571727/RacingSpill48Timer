package scenes.upgrade;

import player_local.Car;
import player_local.CarRep;

public class Upgrade {

	private UpgradeAction regular;
	private UpgradeAction[] bonuses;
	private int nameID;
	private int bonusLVL;
	private int[] bonusLVLs;
	private int maxLVL;
	private UpgradeRegularValues upgrades;
	private String[] bonusesText;

	public Upgrade(UpgradeAction regular, UpgradeAction[] bonuses, int nameID, UpgradeRegularValues upgrades,
			String[] bonusesText, int[] bonusLVLs, int maxLVL) throws Exception {
		this.regular = regular;
		this.nameID = nameID;
		this.upgrades = upgrades;
		this.bonusesText = bonusesText;
		bonusLVL = 0;
		this.bonusLVLs = bonusLVLs;
		this.bonuses = bonuses;
		this.maxLVL = maxLVL;

		if (bonuses != null && bonusLVLs.length != bonuses.length && bonuses.length != 4) {
			throw new Exception("Bonuses not same length as lvl ints or wrong length");
		}

	}

	public int getMaxLVL() {
		return maxLVL;
	}

	public void setMaxLVL(int maxLVL) {
		this.maxLVL = maxLVL;
	}

	public String information(Car car) {

		String bonusColor = null;
		String res = null;

		switch (bonusLVL) {
		case 0:
			bonusColor = "'rgb(255,255,255)'";
			break;
		case 1:
			bonusColor = "'rgb(0,255,0)'";
			break;
		case 2:
			bonusColor = "'rgb(0, 128, 255)'";
			break;
		case 3:
			bonusColor = "'rgb(255, 0, 191)'";
			break;
		}

		if (car.getRep().getUpgradeLVL(nameID) < maxLVL) {
			res = "<b>" + Upgrades.UPGRADE_NAMES[nameID] + "</b>: <br/>" + upgrades.getUpgradeRep() + "<br/>"
					+ "<font color=" + bonusColor + ">";
			if (bonuses != null && bonusLVL < bonusLVLs.length) {
				res += "LVL " + bonusLVLs[bonusLVL] + ": " + bonusesText[bonusLVL];
			}
			res += "</font><br/><br/>";

		} else {
			res = "<b><font color='rgb(0,255,0)'>" + Upgrades.UPGRADE_NAMES[nameID] + "</font></b>: <br/>Fully upgraded<br/>"
					+ "<br/><br/>";
		}

		return res;

	}

	public boolean upgrade(CarRep car, boolean notRep) {
		if (car.getUpgradeLVL(nameID) < maxLVL) {
			int newLVL = regular.upgrade(car, notRep);

			if (bonuses != null && notRep && bonusLVL < bonusLVLs.length && newLVL == bonusLVLs[bonusLVL]) {
				bonuses[bonusLVL].upgrade(car, notRep);
				bonusLVL++;
			}
			return true;
		}
		return false;
	}
}
