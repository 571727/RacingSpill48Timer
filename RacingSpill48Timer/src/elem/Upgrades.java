package elem;

import adt.UpgradeAction;

//Dyrere for de som leder: 1st (+10%), 2nd (5%), 3rd (2%) osv.

public class Upgrades {

	public static String[] UPGRADE_NAMES = { "Clutch", "Weight reduction", "Fuel", "Bigger turbo", "More NOS",
			"Lighter pistons", "Gears", "Beefier block", "Tiregrip" };
	private UpgradePrice[] upgradePrices;
	private Upgrade[] upgradeValues;
	private int fuelHP1, fuelHP2, fuelHP3;

	public Upgrades() {
		upgradePrices = new UpgradePrice[UPGRADE_NAMES.length];
		upgradeValues = new Upgrade[UPGRADE_NAMES.length];

		fuelHP1 = 2;
		fuelHP2 = 28;
		fuelHP3 = 206;

		// Clutch
		upgradeValues[0] = new Upgrade((Car car) -> {

			car.setHp(car.getHp() + 35f);

			double topspeedPrev = car.getTopSpeed();
			double topspeedInc = car.getTopSpeed() * 0.05;
			car.setTopSpeed(topspeedPrev + topspeedInc);
			car.setGearsbalance(car.getGearsbalance() * (1 - (topspeedInc / topspeedPrev)));

			car.iterateUpgradeLVL(0);
			if (car.getUpgradeLVL(0) < 5)
				car.upgradeRightShift(1.20);
			else if (car.getUpgradeLVL(0) == 5)
				car.guarenteeRightShift();

			return true;
		}, 0, "+ 35 HP, + 20 % TG area, + 5 % TS", "Guaranteed TG shift at high RPM", 5);
		// Weight
		upgradeValues[1] = new Upgrade((Car car) -> {
			car.setCurrentWeight();
			if (car.getCurrentWeight() > 2) {
				car.setWeightloss(car.getWeightloss() + (car.getCurrentWeight() * 0.09f));
				if (car.getCurrentWeight() <= 1) {
					car.setWeightloss(0);
					car.setTotalWeight(1);
					car.setCurrentWeight();
				}

				car.iterateUpgradeLVL(1);
				return true;
			} else {
				return false;
			}
		}, 1, "- 9 % weight", "\"" + Upgrades.UPGRADE_NAMES[7] + "\" no longer increases weight", 5);
		// Fuel
		upgradeValues[2] = new Upgrade((Car car) -> {
			boolean res = false;
			if (car.getUpgradeLVL(2) < 3) {
				switch (car.getUpgradeLVL(2)) {
				case 0:
					car.setHp(car.getHp() + fuelHP1);
					break;
				case 1:
					car.setHp(car.getHp() + fuelHP2);
					break;
				case 2:
					car.setHp(car.getHp() + fuelHP3);
					break;
				}
				res = true;
				car.iterateUpgradeLVL(2);
			}
			return res;
		}, 2, "1st: + 2 HP, 2nd: + 28 HP, 3rd: + 206 HP", "For every upgrade; upgrading \"" + Upgrades.UPGRADE_NAMES[6]
				+ "\" increases TS more", 3);
		// Turbo
		upgradeValues[3] = new Upgrade((Car car) -> {
			car.setCurrentWeight();
			car.setHasTurbo(true);
			car.setHp(car.getHp() + (100f));
			car.setWeightloss(car.getWeightloss() - (car.getCurrentWeight() * 0.02f));
			car.iterateUpgradeLVL(3);
			if (car.getUpgradeLVL(3) == 5) {
				upgradePrices[5].addSale(0.85, 3);
				upgradePrices[7].addSale(0.85, 3);
			}
			return true;
		}, 3, "+ 100 HP, + 2 % weight", "- 15 % $ \""
				+ Upgrades.UPGRADE_NAMES[7] + "\" and \"" + Upgrades.UPGRADE_NAMES[5] + "\"", 5);
		// NOS
		upgradeValues[4] = new Upgrade((Car car) -> {
			if (!car.isHasNOS()) {
				car.setHasNOS(true);
				car.setNosAmountLeftStandard(1);
			}
			double inc = 0.4;
			if (car.getUpgradeLVL(8) >= 5) {
				inc = inc * 2;
			}
			car.setNosStrengthStandard(car.getNosStrengthStandard() + inc);
			car.iterateUpgradeLVL(4);
			if (car.getUpgradeLVL(4) == 5) {
				upgradePrices[8].addSale(0.5, 4);
			}

			return true;
		}, 4, "+ 0.4 NOS strength", "- 50 % $ \""
				+ Upgrades.UPGRADE_NAMES[8] + "\"", 5);
		// Pistons
		upgradeValues[5] = new Upgrade((Car car) -> {
			car.setCurrentWeight();
			car.setWeightloss(car.getWeightloss() + (car.getCurrentWeight() * 0.05f));
			car.setHp(car.getHp() + 60f);
			car.iterateUpgradeLVL(5);

			if (car.getUpgradeLVL(5) == 5) {
				if (car.getUpgradeLVL(2) >= 1) {
					car.setHp(car.getHp() + fuelHP1);
					if (car.getUpgradeLVL(2) >= 2) {
						car.setHp(car.getHp() + fuelHP2);
						if (car.getUpgradeLVL(2) >= 3) {
							car.setHp(car.getHp() + fuelHP3);
						}
					}
				}

				fuelHP1 = fuelHP1 * 2;
				fuelHP2 = fuelHP2 * 2;
				fuelHP3 = fuelHP3 * 2;

			}

			return true;
		}, 5, "+ 60 HP, - 5 % weight", "+ 100 % HP from \"" + Upgrades.UPGRADE_NAMES[2]
				+ "\" even if already upgraded", 5);
		// Gears
		upgradeValues[6] = new Upgrade((Car car) -> {
			double topspeedPrev = car.getTopSpeed();
			double topspeedInc = 72.0 * (car.getUpgradeLVL(2) + 1) * (car.getUpgradeLVL(3) < 5 ? 1 : 0.66);
			car.setCurrentWeight();
			car.setWeightloss(car.getWeightloss() + (car.getCurrentWeight() * 0.03f));
			car.setTopSpeed(topspeedPrev + topspeedInc);
			car.setGearsbalance(car.getGearsbalance() * (1 - (topspeedInc / topspeedPrev)));
			car.setUpgradedGears(true);
			car.iterateUpgradeLVL(6);
			if (car.getUpgradeLVL(6) == 5) {
				car.setNosAmountLeftStandard(2);
			}
			return true;
		}, 6, "+ 72 (TS * \"" + Upgrades.UPGRADE_NAMES[2] + "\"), - 3 % weight", "\""
				+ Upgrades.UPGRADE_NAMES[4] + "\" now contains two bottles", 5);
		// Block
		upgradeValues[7] = new Upgrade((Car car) -> {
			car.setCurrentWeight();
			car.iterateUpgradeLVL(7);
			double inc = 170;
			if (car.getUpgradeLVL(7) >= 5)
				inc = inc * 1.75;
			car.setHp(car.getHp() + inc);
			if (car.getUpgradeLVL(1) < 5)
				car.setWeightloss(car.getWeightloss() - (car.getCurrentWeight() * 0.14f));
			return true;
		}, 7, "+ 170 HP, + 14 % weight", "Upgrading \""
				+ Upgrades.UPGRADE_NAMES[7] + "\" gives + 75 % more HP", 5);
		// Tires
		upgradeValues[8] = new Upgrade((Car car) -> {
			car.setGearBoostSTD(car.getGearBoostSTD() + 0.6);
			car.iterateUpgradeLVL(8);
			if (car.getUpgradeLVL(8) == 5)
				car.setNosStrengthStandard(car.getNosStrengthStandard() * 2);
			return true;
		}, 8, "+ 0.6 TG", "+ 100 % more NOS strength", 5);

	}

	public int getCostMoney(int i, Bank bank) {
		return (int) (upgradePrices[i].getMoney() * 0.75f * (bank.getInflation(i) + 1f));
	}

	public int getCostPoints(int i, Bank bank) {
		return (int) (upgradePrices[i].getPoints() * (bank.getInflation(i) + 1f));
	}

	public String getUpgradedStats(int i, Car car) {
		Car newCar = null;
		try {
			newCar = (Car) car.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		upgrade(i, newCar);
		String s = newCar.showStats(car.getUpgradeLVL(i), newCar.getUpgradeLVL(i));
		return s;
	}

	public boolean upgrade(int i, Car car) {
		return upgradeValues[i].upgrade(car);
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
}
