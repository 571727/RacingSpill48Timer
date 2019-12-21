package elem;

import adt.UpgradeAction;

//Dyrere for de som leder: 1st (+10%), 2nd (5%), 3rd (2%) osv.

public class Upgrades {

	public static String[] UPGRADE_NAMES = { "Clutch", "Weight reduction", "Fuel", "Bigger turbo", "More NOS",
			"Lighter pistons", "Gears", "Beefier block", "Tireboost" };
	private String[][] bonusText;
	private int[][] bonusTextLVLs;
	private UpgradePrice[] upgradePrices;
	private Upgrade[] upgradeValues;

	public Upgrades() {
		upgradePrices = new UpgradePrice[UPGRADE_NAMES.length];
		upgradeValues = new Upgrade[UPGRADE_NAMES.length];
		bonusText = new String[UPGRADE_NAMES.length][4];
		bonusTextLVLs = new int[UPGRADE_NAMES.length][4];

		// Clutch
		int clutchID = 0;
		int[] clutchLVLs = { 3, 5, 7, 14 };
		int clutchMaxLVL = 14;
		float[] clutchRegUpgrades = { 50, -1, -1, -1, -1, 10, -1, 20 };
		boolean[] clutchRegUpgradesPercent = { false, false, false, false, false, true, false, true };
		UpgradeRegularValues clutchRegularUpgradeText = new UpgradeRegularValues(clutchRegUpgrades,
				clutchRegUpgradesPercent);

		// Weight
		int weightID = 1;
		int[] weightLVLs = { 3, 5, 7 };
		int weightMaxLVL = 7;

		float[] weightRegUpgrades = { -1, -2, -1, -1, -1, -1, -1, -1 };
		boolean[] weightRegUpgradesPercent = { false, true, false, false, false, false, false, false };
		UpgradeRegularValues weightRegularUpgradeText = new UpgradeRegularValues(weightRegUpgrades,
				weightRegUpgradesPercent);

		// Fuel
		int fuelID = 2;
		int fuelHP[] = { 2, 28, 206 };
		int[] fuelLVLs = { 1, 2, 3 };
		int fuelMaxLVL = 3;
		float[] fuelRegUpgrades = { 2, -1, -1, -1, -1, -1, -1, -1 };
		boolean[] fuelRegUpgradesPercent = { false, false, false, false, false, false, false, false };
		UpgradeRegularValues fuelRegularUpgradeText = new UpgradeRegularValues(fuelRegUpgrades, fuelRegUpgradesPercent);

		// Turbo
		int turboID = 3;
		int[] turboLVLs = { 3, 7, 9 };
		int turboMaxLVL = 20;
		float[] turboRegUpgrades = { 100, 2, -1, -1, -1, -1, -1, -1 };
		boolean[] turboRegUpgradesPercent = { false, true, false, false, false, false, false, false };
		UpgradeRegularValues turboRegularUpgradeText = new UpgradeRegularValues(turboRegUpgrades,
				turboRegUpgradesPercent);

		// NOS
		int nosID = 4;
		int[] nosLVLs = { 3, 5, 8 };
		int nosMaxLVL = 20;
		float[] nosRegUpgrades = { -1, -1, 0.4f, 1, -1, -1, -1, -1 };
		boolean[] nosRegUpgradesPercent = { false, false, false, false, false, false, false, false };
		UpgradeRegularValues nosRegularUpgradeText = new UpgradeRegularValues(nosRegUpgrades, nosRegUpgradesPercent);

		// Pistons
		int pistonsID = 5;
		int[] pistonsLVLs = { 3, 5 };
		int pistonsMaxLVL = 20;
		float[] pistonsRegUpgrades = { 8, -8, -1, -1, -1, -1, -1, -1 };
		boolean[] pistonsRegUpgradesPercent = { true, true, false, false, false, false, false, false };
		UpgradeRegularValues pistonsRegularUpgradeText = new UpgradeRegularValues(pistonsRegUpgrades,
				pistonsRegUpgradesPercent);

		// Gears
		int gearID = 6;
		int[] gearLVLs = { 3, 5, 10 };
		int gearMaxLVL = 20;
		float[] gearRegUpgrades = { -1, -3, -1, -1, -1, 92, -1, -1 };
		boolean[] gearRegUpgradesPercent = { false, true, false, false, false, false, false, false };
		UpgradeRegularValues gearRegularUpgradeText = new UpgradeRegularValues(gearRegUpgrades, gearRegUpgradesPercent);

		// Block
		int blockID = 7;
		int[] blockLVLs = { 5, 7, 8 };
		int blockMaxLVL = 20;
		float[] blockRegUpgrades = { 170, 14, -1, -1, -1, -1, -1, -1 };
		boolean[] blockRegUpgradesPercent = { false, true, false, false, false, false, false, false };
		UpgradeRegularValues blockRegularUpgradeText = new UpgradeRegularValues(blockRegUpgrades,
				blockRegUpgradesPercent);

		// Tires
		int tbID = 8;
		int[] tbLVLs = { 3, 5, 7 };
		int tbMaxLVL = 20;

		float[] tbRegUpgrades = { -1, -1, -1, -1, -1, -1, 0.6f, -1 };
		boolean[] tbRegUpgradesPercent = { false, false, false, false, false, false, false, false };
		UpgradeRegularValues tbRegularUpgradeText = new UpgradeRegularValues(tbRegUpgrades, tbRegUpgradesPercent);

		String[] clutchTexts = { UPGRADE_NAMES[gearID] + " - 6 % weight instead", "Virtually guaranteed TB",
				"+ 0.2 extra TB", "Super clutch (equal power at any RPM except at too early shift)" };
		
		UpgradeAction[] clutchBonuses = { (CarRep car, boolean notRep) -> {
			// First bonus
			if (notRep)
				gearRegularUpgradeText.setValue(1, -6, true);
			return -1;
		}, (CarRep car, boolean notRep) -> {
			// Second bonus
			car.guarenteeRightShift();
			return -1;
		}, (CarRep car, boolean notRep) -> {
			// Third bonus
			if (notRep)
				tbRegularUpgradeText.setValue(6, tbRegularUpgradeText.getValue(6) + 0.2f, false);
			return -1;
		}, (CarRep car, boolean notRep) -> {
			// Fourth bonus
			car.setClutchSuper(true);
			return -1;
		} };
		String[] weightTexts = { "\"" + Upgrades.UPGRADE_NAMES[turboID] + "\" no longer increases weight",
				"\"" + Upgrades.UPGRADE_NAMES[blockID] + "\" no longer increases weight",
				UPGRADE_NAMES[clutchID] + " switch TB area with - 10 % weight"};
		UpgradeAction[] weightBonuses = { (CarRep car, boolean notRep) -> {
			// First bonus
			if (notRep)
				turboRegularUpgradeText.setValue(1, -1, false);
			return -1;
		}, (CarRep car, boolean notRep) -> {
			// Second bonus
			if (notRep)
				blockRegularUpgradeText.setValue(1, -1, false);
			return -1;
		}, (CarRep car, boolean notRep) -> {
			// Third bonus
			if (notRep) {
				clutchRegularUpgradeText.setValue(1, - 10, true);
				clutchRegularUpgradeText.setValue(7, -1, false);
			}
			return -1;
		} };
		String[] fuelTexts = { "+ " + fuelHP[1] + " HP instead from " + UPGRADE_NAMES[fuelID],
				"+ " + fuelHP[2] + " HP instead from " + UPGRADE_NAMES[fuelID],
				"Upgrading \"" + Upgrades.UPGRADE_NAMES[6] + "\" now increases TS by + 20 % instead" };
		UpgradeAction[] fuelBonuses = { (CarRep car, boolean notRep) -> {
			// First bonus
			if (notRep)
				fuelRegularUpgradeText.setValue(0, fuelHP[1], false);
			return -1;
		}, (CarRep car, boolean notRep) -> {
			if (notRep)
				fuelRegularUpgradeText.setValue(0, fuelHP[2], false);
			return -1;
		}, (CarRep car, boolean notRep) -> {

			gearRegularUpgradeText.setValue(5, 20, true);
			return -1;
		} };
		String[] turboTexts = { "\"" + Upgrades.UPGRADE_NAMES[4] + "\" now contains an additional bottle",
				"Everything gets on sale! 10 % off!", "Turbo no longer spools" };
		UpgradeAction[] turboBonuses = { (CarRep car, boolean notRep) -> {
			// First bonus
			car.setNosBottleAmountStandard(car.getNosBottleAmountStandard() + 1);
			return -1;
		}, (CarRep car, boolean notRep) -> {
			// Second bonus
			if (notRep) {
				for (UpgradePrice up : upgradePrices) {
					up.addSale(0.1, turboID);
				}
			}

			return -1;
		}, (CarRep car, boolean notRep) -> {
			// Third bonus
			car.setDoesSpool(false);
			return -1;
		} };
		String[] nosTexts = { "+ 0.1 TB for " + UPGRADE_NAMES[tbID], "- 50 % $ \"" + Upgrades.UPGRADE_NAMES[8] + "\"", "+ 420 TS" };
		UpgradeAction[] nosBonuses = { (CarRep car, boolean notRep) -> {
			// First bonus
			tbRegularUpgradeText.setValue(6, tbRegularUpgradeText.getValue(6) + 0.1f, false);
			return -1;
		}, (CarRep car, boolean notRep) -> {
			// Second bonus
			upgradePrices[tbID].addSale(0.5, nosID);
			return -1;
		}, (CarRep car, boolean notRep) -> {
			// Third bonus
			car.setSpeedTop(car.getSpeedTop() + 420);
			return -1;
		} };
		String[] pistonsTexts = { "+ 100 % HP from \"" + Upgrades.UPGRADE_NAMES[2] + "\" even if already upgraded",
				"Double " + UPGRADE_NAMES[pistonsID] + " effect" };
		UpgradeAction[] pistonsBonuses = { (CarRep car, boolean notRep) -> {
			// Second bonus
			for (int i = 0; i < fuelHP.length; i++) {
				if (i < car.getUpgradeLVL(fuelID))
					car.setHp(car.getHp() + fuelHP[i]);
				else
					fuelHP[i] = fuelHP[i] * 2;
			}
			return -1;
		}, (CarRep car, boolean notRep) -> {
			// Third bonus
			if (notRep) {
				for (int i = 0; i < 8; i++) {
					float value = pistonsRegularUpgradeText.getValue(i);
					if (value != -1)
						pistonsRegularUpgradeText.setValue(i, value * 2, pistonsRegularUpgradeText.getPercent(i));
				}
			}
			return -1;
		} };
		String[] gearTexts = { UPGRADE_NAMES[pistonsID] + " has + 0.1 NOS boost",
				"Sequential shifting; (use arrows + virtually no clutch)", "Direct drive" };
		UpgradeAction[] gearBonuses = { (CarRep car, boolean notRep) -> {
			if (notRep) {
				pistonsRegularUpgradeText.setValue(2, 0.1f, false);
			}
			return -1;
		}, (CarRep car, boolean notRep) -> {
			car.setSequentialShift(true);
			return -1;
		}, (CarRep car, boolean notRep) -> {
			car.setGearTop(1);
			return -1;
		} };
		String[] blockTexts = { "+ 100 % TB time", "+ 100 % current TB", "- 50 % $ " + UPGRADE_NAMES[gearID] };
		UpgradeAction[] blockBonuses = { (CarRep car, boolean notRep) -> {
			// Second bonus
			car.setTireGripTimeStandard(car.getTireGripTimeStandard() * 2);
			return -1;
		}, (CarRep car, boolean notRep) -> {
			// Third bonus
			car.setTireGripStrengthStandard(car.getTireGripStrengthStandard() * 2);
			return -1;
		}, (CarRep car, boolean notRep) -> {
			// Fourth bonus
			upgradePrices[gearID].addSale(0.5, blockID);
			return -1;
		} };
		String[] tbTexts = { "+ 50 % NOS boost time", "+ 100 % more NOS boost always", "+ 30 % TS", "" };
		UpgradeAction[] tbBonuses = { (CarRep car, boolean notRep) -> {
			// First bonus
			car.setNosTimeStandard((int) (car.getNosTimeStandard() * 1.5));
			return -1;
		}, (CarRep car, boolean notRep) -> {
			// Second bonus
			car.setNosStrengthStandard(car.getNosStrengthStandard() * 2);
			if(notRep)
				nosRegularUpgradeText.setValue(2, nosRegularUpgradeText.getValue(2), false);
			return -1;
		}, (CarRep car, boolean notRep) -> {
			// Third bonus
			car.setSpeedTop(car.getSpeedTop() * 1.30);
			return -1;
		} };

		bonusText[clutchID] = clutchTexts;
		bonusText[weightID] = weightTexts;
		bonusText[fuelID] = fuelTexts;
		bonusText[turboID] = turboTexts;
		bonusText[nosID] = nosTexts;
		bonusText[pistonsID] = pistonsTexts;
		bonusText[gearID] = gearTexts;
		bonusText[blockID] = blockTexts;
		bonusText[tbID] = tbTexts;
		
		bonusTextLVLs[clutchID] = clutchLVLs;
		bonusTextLVLs[weightID] = weightLVLs;
		bonusTextLVLs[fuelID] = fuelLVLs;
		bonusTextLVLs[turboID] = turboLVLs;
		bonusTextLVLs[nosID] = nosLVLs;
		bonusTextLVLs[pistonsID] = pistonsLVLs;
		bonusTextLVLs[gearID] = gearLVLs;
		bonusTextLVLs[blockID] = blockLVLs;
		bonusTextLVLs[tbID] = tbLVLs;
		
		
		try {

			upgradeValues[clutchID] = new Upgrade((CarRep car, boolean notRep) -> {

				clutchRegularUpgradeText.upgrade(car);

				return car.iterateUpgradeLVL(clutchID);
			}, clutchBonuses, clutchID, clutchRegularUpgradeText, clutchTexts, clutchLVLs, clutchMaxLVL);

			upgradeValues[weightID] = new Upgrade((CarRep car, boolean notRep) -> {

				weightRegularUpgradeText.upgrade(car);
				int lvl = car.iterateUpgradeLVL(weightID);
				if (notRep)
					weightRegularUpgradeText.setValue(1, -3 * (lvl + 1), true);
				if (car.getWeight() <= 1) {
					car.setWeight(1);
				}

				return lvl;
			}, weightBonuses, weightID, weightRegularUpgradeText, weightTexts, weightLVLs, weightMaxLVL);

			upgradeValues[fuelID] = new Upgrade((CarRep car, boolean notRep) -> {

				fuelRegularUpgradeText.upgrade(car);
				return car.iterateUpgradeLVL(fuelID);
			}, fuelBonuses, fuelID, fuelRegularUpgradeText, fuelTexts, fuelLVLs, fuelMaxLVL);

			upgradeValues[turboID] = new Upgrade((CarRep car, boolean notRep) -> {

				turboRegularUpgradeText.upgrade(car);
				car.setTurboHP(car.getTurboHP() + turboRegularUpgradeText.getValue(0));

				return car.iterateUpgradeLVL(turboID);
			}, turboBonuses, turboID, turboRegularUpgradeText, turboTexts, turboLVLs, turboMaxLVL);

			upgradeValues[nosID] = new Upgrade((CarRep car, boolean notRep) -> {

				nosRegularUpgradeText.upgrade(car);
				if (notRep && car.getUpgradeLVL(nosID) == 0) {
					nosRegularUpgradeText.setValue(3, nosRegularUpgradeText.getValue(3) - 1, false);
				}

				return car.iterateUpgradeLVL(nosID);
			}, nosBonuses, nosID, nosRegularUpgradeText, nosTexts, nosLVLs, nosMaxLVL);

			upgradeValues[pistonsID] = new Upgrade((CarRep car, boolean notRep) -> {

				pistonsRegularUpgradeText.upgrade(car);
				return car.iterateUpgradeLVL(pistonsID);
			}, pistonsBonuses, pistonsID, pistonsRegularUpgradeText, pistonsTexts, pistonsLVLs, pistonsMaxLVL);

			upgradeValues[gearID] = new Upgrade((CarRep car, boolean notRep) -> {

				gearRegularUpgradeText.upgrade(car);
				return car.iterateUpgradeLVL(gearID);

			}, gearBonuses, gearID, gearRegularUpgradeText, gearTexts, gearLVLs, gearMaxLVL);

			upgradeValues[blockID] = new Upgrade((CarRep car, boolean notRep) -> {

				blockRegularUpgradeText.upgrade(car);
				return car.iterateUpgradeLVL(blockID);
			}, blockBonuses, blockID, blockRegularUpgradeText, blockTexts, blockLVLs, blockMaxLVL);

			upgradeValues[tbID] = new Upgrade((CarRep car, boolean notRep) -> {
				tbRegularUpgradeText.upgrade(car);

				return car.iterateUpgradeLVL(tbID);
			}, tbBonuses, tbID, tbRegularUpgradeText, tbTexts, tbLVLs, tbMaxLVL);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int getCostMoney(int i, CarRep upgradedCarParts) {
		return (int) (upgradePrices[i].getMoney() * 0.75f * (upgradedCarParts.getUpgradeLVL(i) + 1f));
	}

	public int getCostPoints(int i, CarRep upgradedCarParts) {
		return (int) (upgradePrices[i].getPoints() * (upgradedCarParts.getUpgradeLVL(i) + 1f));
	}

	public String getUpgradedStats(int i, Car car, boolean notRep) {
		CarRep newCar = upgradeNewCarRep(i, car, notRep);
		String s = newCar.getStatsNew(car.getUpgradeLVL(i), newCar.getUpgradeLVL(i));
		return "<font size='4'>" + UPGRADE_NAMES[i] + "</font><br/>" + s;
	}

	public CarRep upgradeNewCarRep(int i, Car car, boolean notRep) {
		CarRep newCar = null;
		try {
			newCar = car.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		upgrade(i, newCar, notRep);
		return newCar;
	}

	public boolean upgrade(int i, CarRep car, boolean notRep) {
		return upgradeValues[i].upgrade(car, notRep);
	}

	public String[] getUpgradeNames() {
		return UPGRADE_NAMES;
	}

	public UpgradePrice[] getUpgradePrices() {
		return upgradePrices;
	}

	public void setUpgradePrices(UpgradePrice[] upgradePrices) {
		this.upgradePrices = upgradePrices;
	}

	public String getInformation(int i, Car car) {
		return upgradeValues[i].information(car);
	}

	public int specialPricePoints(int i, CarRep car) {
		int res = -1;
		return res;
	}

	public int specialPriceMoney(int i, CarRep car) {
		int res = -1;
		return res;
	}

	public String getBonuses() {
		String res = "";
		
		for(int i = 0; i < UPGRADE_NAMES.length; i++) {
			res += UPGRADE_NAMES[i] + "<br/>";
			for(int u = 0; u < bonusText[i].length; u++) {
				if(bonusText[i][u] == null || bonusText[i][u].isEmpty())
					break;
				res += "LVL " + bonusTextLVLs[i][u] + ": " + bonusText[i][u] + "<br/>";
			}
			res += "Max LVL: " + getUpgrade(i).getMaxLVL() + "<br/>";
			res += "<br/>";
		}
		
		
		return res;
	}
	
	public Upgrade getUpgrade(int i) {
		return upgradeValues[i];
	}
}
