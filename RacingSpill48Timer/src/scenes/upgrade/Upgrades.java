package scenes.upgrade;

import player_local.Car;
import player_local.CarRep;

//Dyrere for de som leder: 1st (+10%), 2nd (5%), 3rd (2%) osv.

public class Upgrades {

	public static String[] UPGRADE_NAMES = { "Clutch", "Weight reduction", "Fuel", "Bigger turbo", "More NOS",
			"Lighter pistons", "Gears", "Beefier block", "Tireboost", "Titjuice", "Grippy Bobbady", "Lift that cam",
			"Money mails", "Point paradise", };
	private String[][] bonusText;
	private int[][] bonusTextLVLs;
	private UpgradePrice[] upgradePrices;
	private Upgrade[] upgradeValues;

	public Upgrades() {
		upgradePrices = new UpgradePrice[UPGRADE_NAMES.length];
		upgradeValues = new Upgrade[UPGRADE_NAMES.length];
		bonusText = new String[UPGRADE_NAMES.length][4];
		bonusTextLVLs = new int[UPGRADE_NAMES.length][4];

		/*
		 * When a upgrade value is not an integer it is persumed to be a percentage. Otherwise it is a addition.
		 */
		
		// Clutch
		int clutchID = 0;
		int[] clutchLVLs = { 3, 5, 8 };
		int clutchMaxLVL = 8;
		double[] clutchRegUpgrades = { 40, -1, -1, -1, -1, 1.10, -1, 1.20, -1, -1, -1, -1, -1 };
		UpgradeRegularValues clutchRegularUpgradeText = new UpgradeRegularValues(clutchRegUpgrades);

		// Weight
		int weightID = 1;
		int[] weightLVLs = { 2, 5, 7 };
		int weightMaxLVL = 7;
		double[] weightRegUpgrades = { -1, 0.98, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
		UpgradeRegularValues weightRegularUpgradeText = new UpgradeRegularValues(weightRegUpgrades);

		// Fuel
		int fuelID = 2;
		int fuelHP[] = { 2, 28, 206 };
		int[] fuelLVLs = { 1, 2, 3 };
		int fuelMaxLVL = 3;
		double[] fuelRegUpgrades = { 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
		UpgradeRegularValues fuelRegularUpgradeText = new UpgradeRegularValues(fuelRegUpgrades);

		// Turbo
		int turboID = 3;
		int[] turboLVLs = { 3, 5, 6 };
		int turboMaxLVL = 20;
		double[] turboRegUpgrades = { 100, 0.98, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
		UpgradeRegularValues turboRegularUpgradeText = new UpgradeRegularValues(turboRegUpgrades);

		// NOS
		int nosID = 4;
		int[] nosLVLs = { 3, 5, 8 };
		int nosMaxLVL = 8;
		double[] nosRegUpgrades = { -1, -1, 4, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
		UpgradeRegularValues nosRegularUpgradeText = new UpgradeRegularValues(nosRegUpgrades);

		// Pistons
		int pistonsID = 5;
		int[] pistonsLVLs = { 3, 5 };
		int pistonsMaxLVL = 10;
		double[] pistonsRegUpgrades = { 1.08, 0.92, -1, -1, -1, -1, -1, -1, -1, 200, -1, -1, -1 };
		UpgradeRegularValues pistonsRegularUpgradeText = new UpgradeRegularValues(pistonsRegUpgrades);

		// Gears
		int gearID = 6;
		int[] gearLVLs = { 3, 5, 10 };
		int gearMaxLVL = 22;
		double[] gearRegUpgrades = { -1, 0.97, -1, -1, -1, 92, -1, -1, -1, 1.02, -1, -1, -1 };
		UpgradeRegularValues gearRegularUpgradeText = new UpgradeRegularValues(gearRegUpgrades);

		// Block
		int blockID = 7;
		int[] blockLVLs = { 5, 7, 8 };
		int blockMaxLVL = 12;
		double[] blockRegUpgrades = { 170, 1.14, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
		UpgradeRegularValues blockRegularUpgradeText = new UpgradeRegularValues(blockRegUpgrades);

		// Tires
		int tbID = 8;
		int[] tbLVLs = { 3, 5, 7 };
		int tbMaxLVL = 12;
		double[] tbRegUpgrades = { -1, -1, -1, -1, -1, -1, 6, -1, -1, -1, -1, -1, -1 };
		UpgradeRegularValues tbRegularUpgradeText = new UpgradeRegularValues(tbRegUpgrades);

		// Grippy Bombady
		int gbID = 9;
		int gbMaxLVL = 4;
		double[] gbRegUpgrades = { -1, -1, -1, -1, -1, -1, -1, -1, 4, -1, -1, -1, -1 };
		UpgradeRegularValues gbRegularUpgradeText = new UpgradeRegularValues(gbRegUpgrades);

		// Lift that cam
		int liftCamID = 10;
		int[] liftCamLVLs = { 3 };
		int liftCamMaxLVL = 3;
		double[] liftCamRegUpgrades = { 1.03, -1, -1, -1, -1, -1, -1, -1, -1, 1200, -1, -1, -1 };
		UpgradeRegularValues liftCamRegularUpgradeText = new UpgradeRegularValues(liftCamRegUpgrades);

		// Money mails
		int moneyID = 11;
		int moneyMaxLVL = 3;
		double[] moneyRegUpgrades = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 100, -1 };
		UpgradeRegularValues moneyRegularUpgradeText = new UpgradeRegularValues(moneyRegUpgrades);

		// Point paradise
		int paradiseID = 12;
		int paradiseMaxLVL = 1;
		double[] paradiseRegUpgrades = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1 };
		UpgradeRegularValues paradiseRegularUpgradeText = new UpgradeRegularValues(paradiseRegUpgrades);

		/*
		 * BONUSES
		 */

		String[] clutchTexts = { UPGRADE_NAMES[gearID] + " - 6 % weight instead", "Virtually guaranteed TB",
				"Super clutch (equal power at any RPM except at too early shift)" };

		UpgradeAction[] clutchBonuses = { (CarRep car, boolean notRep) -> {
			// First bonus
			if (notRep)
				gearRegularUpgradeText.setValue(1, -0.06);
			return -1;
		}, (CarRep car, boolean notRep) -> {
			// Second bonus
			car.guarenteeRightShift();
			return -1;
		}, (CarRep car, boolean notRep) -> {
			// Third bonus
			car.setClutchSuper(true);
			return -1;
		} };
		String[] weightTexts = { "\"" + Upgrades.UPGRADE_NAMES[turboID] + "\" no longer increases weight",
				"\"" + Upgrades.UPGRADE_NAMES[blockID] + "\" no longer increases weight",
				UPGRADE_NAMES[clutchID] + " switch TB area with - 10 % weight" };
		UpgradeAction[] weightBonuses = { (CarRep car, boolean notRep) -> {
			// First bonus
			if (notRep)
				turboRegularUpgradeText.setValue(1, -1);
			return -1;
		}, (CarRep car, boolean notRep) -> {
			// Second bonus
			if (notRep)
				blockRegularUpgradeText.setValue(1, -1);
			return -1;
		}, (CarRep car, boolean notRep) -> {
			// Third bonus
			if (notRep) {
				clutchRegularUpgradeText.setValue(1, -0.10);
				clutchRegularUpgradeText.setValue(7, -1);
			}
			return -1;
		} };
		String[] fuelTexts = { "+ " + fuelHP[1] + " HP instead from " + UPGRADE_NAMES[fuelID],
				"+ " + fuelHP[2] + " HP instead from " + UPGRADE_NAMES[fuelID],
				"Upgrading \"" + Upgrades.UPGRADE_NAMES[6] + "\" now increases TS by + 20 % instead" };
		UpgradeAction[] fuelBonuses = { (CarRep car, boolean notRep) -> {
			// First bonus
			if (notRep)
				fuelRegularUpgradeText.setValue(0, fuelHP[1]);
			return -1;
		}, (CarRep car, boolean notRep) -> {
			if (notRep)
				fuelRegularUpgradeText.setValue(0, fuelHP[2]);
			return -1;
		}, (CarRep car, boolean notRep) -> {

			gearRegularUpgradeText.setValue(5, 0.20);
			return -1;
		} };
		String[] turboTexts = { "\"" + Upgrades.UPGRADE_NAMES[nosID] + "\" now contains an additional bottle",
				"Everything gets on sale! 10 % off!",
				"\"Sexy Turbo Blow\"" };
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
			turboRegularUpgradeText.setValue(5, 0.15);
			return -1;
		} };
		String[] nosTexts = { "+ 1 TB for " + UPGRADE_NAMES[tbID], "- 50 % $ \"" + Upgrades.UPGRADE_NAMES[8] + "\"",
				"+ (69 * \"" + UPGRADE_NAMES[clutchID] + "\") TS" };
		UpgradeAction[] nosBonuses = { (CarRep car, boolean notRep) -> {
			// First bonus
			tbRegularUpgradeText.setValue(6, tbRegularUpgradeText.getValue(6) + 1);
			return -1;
		}, (CarRep car, boolean notRep) -> {
			// Second bonus
			upgradePrices[tbID].addSale(0.5, nosID);
			return -1;
		}, (CarRep car, boolean notRep) -> {
			// Third bonus
			car.setSpeedTop(car.getSpeedTop() + (69 * car.getUpgradeLVL(clutchID)));
			return -1;
		} };
		String[] pistonsTexts = { "+ 100 % HP from \"" + Upgrades.UPGRADE_NAMES[2] + "\" even if already upgraded",
				"+ 100 % \"" + UPGRADE_NAMES[pistonsID] + "\" effects" };
		UpgradeAction[] pistonsBonuses = { (CarRep car, boolean notRep) -> {
			// First bonus
			for (int i = 0; i < fuelHP.length; i++) {
				if (i < car.getUpgradeLVL(fuelID))
					car.setHp(car.getHp() + fuelHP[i]);
				else
					fuelHP[i] = fuelHP[i] * 2;
			}
			return -1;
		}, (CarRep car, boolean notRep) -> {
			// sec bonus
			if (notRep) {
				for (int i = 0; i < 8; i++) {
					double value = pistonsRegularUpgradeText.getValue(i);
					if (value != -1)
						pistonsRegularUpgradeText.setValue(i, value * 2);
				}
			}
			return -1;
		} };
		String[] gearTexts = { UPGRADE_NAMES[pistonsID] + " has + 1 NOS boost",
				"Sequential shifting; (use arrows + virtually no clutch)", "Direct drive (Only one gear)" };
		UpgradeAction[] gearBonuses = { (CarRep car, boolean notRep) -> {
			if (notRep) {
				pistonsRegularUpgradeText.setValue(2, 1);
			}
			return -1;
		}, (CarRep car, boolean notRep) -> {
			car.setSequentialShift(true);
			return -1;
		}, (CarRep car, boolean notRep) -> {
			car.setGearTop(1);
			return -1;
		} };
		String[] blockTexts = { "+ 25 % TB time", "+ 50 % current TB", "- 50 % $ " + UPGRADE_NAMES[gearID] };
		UpgradeAction[] blockBonuses = { (CarRep car, boolean notRep) -> {
			// Second bonus
			car.setTireboostTimeStandard((int) (car.getTireboostTimeStandard() * 1.25));
			return -1;
		}, (CarRep car, boolean notRep) -> {
			// Third bonus
			car.setTireboostStrengthStandard(car.getTireboostStrengthStandard() * 1.5);
			return -1;
		}, (CarRep car, boolean notRep) -> {
			// Fourth bonus
			upgradePrices[gearID].addSale(0.5, blockID);
			return -1;
		} };
		String[] tbTexts = { "+ 50 % NOS boost time", "+ 100 % current NOS boost", "+ 30 % TS" };
		UpgradeAction[] tbBonuses = { (CarRep car, boolean notRep) -> {
			// First bonus
			car.setNosTimeStandard((int) (car.getNosTimeStandard() * 1.5));
			return -1;
		}, (CarRep car, boolean notRep) -> {
			// Second bonus
			car.setNosStrengthStandard(car.getNosStrengthStandard() * 2);
			if (notRep)
				nosRegularUpgradeText.setValue(2, nosRegularUpgradeText.getValue(2));
			return -1;
		}, (CarRep car, boolean notRep) -> {
			// Third bonus
			car.setSpeedTop(car.getSpeedTop() * 1.30);
			return -1;
		} };

		String[] liftCamTexts = { "+ 200 HP from \"" + UPGRADE_NAMES[blockID] + "\" instead" };
		UpgradeAction[] liftCamBonuses = { (CarRep car, boolean notRep) -> {
			// First bonus
			car.setNosTimeStandard((int) (car.getNosTimeStandard() * 1.5));
			return -1;
		}, (CarRep car, boolean notRep) -> {
			// Second bonus
			car.setNosStrengthStandard(car.getNosStrengthStandard() * 2);
			if (notRep)
				nosRegularUpgradeText.setValue(2, nosRegularUpgradeText.getValue(2));
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
		bonusText[gbID] = null;
		bonusText[liftCamID] = liftCamTexts;
		bonusText[moneyID] = null;
		bonusText[paradiseID] = null;

		bonusTextLVLs[clutchID] = clutchLVLs;
		bonusTextLVLs[weightID] = weightLVLs;
		bonusTextLVLs[fuelID] = fuelLVLs;
		bonusTextLVLs[turboID] = turboLVLs;
		bonusTextLVLs[nosID] = nosLVLs;
		bonusTextLVLs[pistonsID] = pistonsLVLs;
		bonusTextLVLs[gearID] = gearLVLs;
		bonusTextLVLs[blockID] = blockLVLs;
		bonusTextLVLs[tbID] = tbLVLs;
		bonusTextLVLs[gbID] = null;
		bonusTextLVLs[liftCamID] = liftCamLVLs;
		bonusTextLVLs[moneyID] = null;
		bonusTextLVLs[paradiseID] = null;

		try {

			upgradeValues[clutchID] = new Upgrade((CarRep car, boolean notRep) -> {

				clutchRegularUpgradeText.upgrade(car);

				return car.iterateUpgradeLVL(clutchID);
			}, clutchBonuses, clutchID, clutchRegularUpgradeText, clutchTexts, clutchLVLs, clutchMaxLVL);

			upgradeValues[weightID] = new Upgrade((CarRep car, boolean notRep) -> {

				weightRegularUpgradeText.upgrade(car);
				int lvl = car.iterateUpgradeLVL(weightID);
				if (notRep)
					weightRegularUpgradeText.setValue(1, -0.02 * (lvl + 1));
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
					nosRegularUpgradeText.setValue(3, nosRegularUpgradeText.getValue(3) - 1);
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
			
			upgradeValues[gbID] = new Upgrade((CarRep car, boolean notRep) -> {
				gbRegularUpgradeText.upgrade(car);
				
				return car.iterateUpgradeLVL(gbID);
			}, null, gbID, gbRegularUpgradeText, null, null, gbMaxLVL);
			
			upgradeValues[liftCamID] = new Upgrade((CarRep car, boolean notRep) -> {
				liftCamRegularUpgradeText.upgrade(car);
				
				return car.iterateUpgradeLVL(liftCamID);
			}, liftCamBonuses, liftCamID, liftCamRegularUpgradeText, liftCamTexts, liftCamLVLs, liftCamMaxLVL);
			
			upgradeValues[moneyID] = new Upgrade((CarRep car, boolean notRep) -> {
				moneyRegularUpgradeText.upgrade(car);
				
				return car.iterateUpgradeLVL(moneyID);
			}, null, moneyID, moneyRegularUpgradeText, null, null, moneyMaxLVL);
			
			upgradeValues[paradiseID] = new Upgrade((CarRep car, boolean notRep) -> {
				paradiseRegularUpgradeText.upgrade(car);

				return car.iterateUpgradeLVL(paradiseID);
			}, null, paradiseID, paradiseRegularUpgradeText, null, null, paradiseMaxLVL);
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int getCostMoney(int i, CarRep rep) {
		return (int) (upgradePrices[i].getMoney() * 0.75f * (rep.getUpgradeLVL(i) + 1f));
	}

	public int getCostPoints(int i, CarRep rep) {
		return (int) (upgradePrices[i].getPoints() * (rep.getUpgradeLVL(i) + 1f));
	}

	public String getUpgradedStats(int i, Car car, boolean notRep) {
		CarRep newCar = upgradeNewCarRep(i, car, notRep);
		String s = newCar.getStatsNew(car.getRep().getUpgradeLVL(i), newCar.getUpgradeLVL(i));
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

		for (int i = 0; i < UPGRADE_NAMES.length; i++) {
			res += UPGRADE_NAMES[i] + "<br/>";
			for (int u = 0; u < bonusText[i].length; u++) {
				if (bonusText[i][u] == null || bonusText[i][u].isEmpty())
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
