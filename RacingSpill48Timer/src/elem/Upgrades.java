package elem;

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
		upgradeValues[0] = new Upgrade((CarRep car) -> {

			car.setHp(car.getHp() + 60f);

			double topspeedPrev = car.getSpeedTop();
			double topspeedInc = car.getSpeedTop() * 0.05;
			car.setSpeedTop(topspeedPrev + topspeedInc);
			car.setGearsbalance(car.getGearsbalance() * (1 - (topspeedInc / topspeedPrev)));

			car.iterateUpgradeLVL(0);
			if (car.getUpgradeLVL(0) < 5)
				car.upgradeRightShift(1.20);
			else if (car.getUpgradeLVL(0) == 5)
				car.guarenteeRightShift();

			return true;
		}, 0, "+ 60 HP, + 20 % TG area, + 5 % TS", "Guaranteed TG shift at high RPM", 5);
		// Weight
		upgradeValues[1] = new Upgrade((CarRep car) -> {
			if (car.getWeight() > 2) {
				car.setWeight(car.getWeight() - (car.getWeight() * 0.09f));
				if (car.getWeight() <= 1) {
					car.setWeight(1);
				}

				car.iterateUpgradeLVL(1);
				return true;
			} else {
				return false;
			}
		}, 1, "- 9 % weight", "\"" + Upgrades.UPGRADE_NAMES[7] + "\" no longer increases weight", 5);
		// Fuel
		upgradeValues[2] = new Upgrade((CarRep car) -> {
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
		}, 2, "1st: + 2 HP, 2nd: + 28 HP, 3rd: + 206 HP",
				"For every upgrade; upgrading \"" + Upgrades.UPGRADE_NAMES[3] + "\" increases HP by 5 % more", 3);
		// Turbo
		upgradeValues[3] = new Upgrade((CarRep car) -> {
			car.setHp(car.getHp() + (100f * (1.05 * car.getUpgradeLVL(2) + car.getUpgradeLVL(2) == 0 ? 1 : 0)));
			car.setWeight(car.getWeight() + (car.getWeight() * 0.02f));
			car.iterateUpgradeLVL(3);
			if (car.getUpgradeLVL(3) == 5) {
				upgradePrices[0].addSale(0.90, 3);
				upgradePrices[7].addSale(0.90, 3);
			}

			if (car.getUpgradeLVL(6) == 5) {
				car.setNosBottleAmountStandard(2);
			}

			return true;
		}, 3, "+ 100 HP, + 2 % weight", "\"" + Upgrades.UPGRADE_NAMES[4] + "\" now contains two bottles and "
				+ "- 10 % $ \"" + Upgrades.UPGRADE_NAMES[0] + "\" & \"" + Upgrades.UPGRADE_NAMES[7] + "\"", 5);
		// NOS
		upgradeValues[4] = new Upgrade((CarRep car) -> {
			if (car.getNosBottleAmountStandard() < 1) {
				car.setNosBottleAmountStandard(1);
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
		}, 4, "+ 0.4 NOS strength", "- 50 % $ \"" + Upgrades.UPGRADE_NAMES[8] + "\"", 5);
		// Pistons
		upgradeValues[5] = new Upgrade((CarRep car) -> {

			double weightloss = 0.04;

			if (car.getUpgradeLVL(7) >= 5)
				weightloss = weightloss * 2;

			car.setWeight(car.getWeight() - (car.getWeight() * weightloss));
			car.setHp(car.getHp() + 35f);
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
		}, 5, "+ 35 HP, - 4 % weight", "+ 100 % HP from \"" + Upgrades.UPGRADE_NAMES[2] + "\" even if already upgraded",
				5);
		// Gears
		upgradeValues[6] = new Upgrade((CarRep car) -> {
			double topspeedPrev = car.getSpeedTop();
			double topspeedInc = 72.0;
			car.setWeight(car.getWeight() - (car.getWeight() * 0.03f));
			car.setSpeedTop(topspeedPrev + topspeedInc);
			car.setGearsbalance(car.getGearsbalance() * (1 - (topspeedInc / topspeedPrev)));

			car.iterateUpgradeLVL(6);

			return true;
		}, 6, "+ 72 (TS * \"" + Upgrades.UPGRADE_NAMES[2] + "\"), - 3 % weight", "Sequential shifting", 5);
		// Block
		upgradeValues[7] = new Upgrade((CarRep car) -> {
			car.iterateUpgradeLVL(7);
			double inc = 170;
			car.setHp(car.getHp() + inc);
			if (car.getUpgradeLVL(1) < 5)
				car.setWeight(car.getWeight() + (car.getWeight() * 0.14f));
			return true;
		}, 7, "+ 170 HP, + 14 % weight", "Upgrading \"" + Upgrades.UPGRADE_NAMES[5] + "\" reduces + 100 % more weight",
				5);
		// Tires
		upgradeValues[8] = new Upgrade((CarRep car) -> {
			car.setTireGripStrengthStandard(car.getTireGripStrengthStandard() + 0.6);
			car.iterateUpgradeLVL(8);
			if (car.getUpgradeLVL(8) == 5)
				car.setNosStrengthStandard(car.getNosStrengthStandard() * 2);
			return true;
		}, 8, "+ 0.6 TG", "+ 100 % more NOS strength", 5);

	}

	public int getCostMoney(int i, CarRep upgradedCarParts) {
		return (int) (upgradePrices[i].getMoney() * 0.75f * (upgradedCarParts.getUpgradeLVL(i) + 1f));
	}

	public int getCostPoints(int i, CarRep upgradedCarParts) {
		return (int) (upgradePrices[i].getPoints() * (upgradedCarParts.getUpgradeLVL(i) + 1f));
	}

	public String getUpgradedStats(int i, Car car) {
		CarRep newCar = null;
		try {
			newCar = car.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		upgrade(i, newCar);
		String s = newCar.getStatsNew(car.getUpgradeLVL(i), newCar.getUpgradeLVL(i));
		return s;
	}

	public boolean upgrade(int i, CarRep car) {
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
