package elem;

import adt.UpgradeAction;

//Dyrere for de som leder: 1st (+10%), 2nd (5%), 3rd (2%) osv.

public class Upgrades {

	public static String[] upgradeNames = { "Upgrade cylinders", "Weight reduction bro", "Better fuel", "Bigger turbo",
			"More NOS", "Lighter pistons", "Transmission", "Beefier block", "Tires" };
	private UpgradePrice[] upgradePrices;
	private UpgradeAction[] upgradeValues;
	private int fuelUpgrades;

	public Upgrades() {
		upgradePrices = new UpgradePrice[upgradeNames.length];
		upgradeValues = new UpgradeAction[upgradeNames.length];
		fuelUpgrades = 0;
		// Cylinders
		upgradeValues[0] = (Car car) -> {
			car.setHp(car.getHp() + 35f);
			car.upgradeRightShift(1.20);
			return true;
		};
		// Weight
		upgradeValues[1] = (Car car) -> {
			car.setCurrentWeight();
			if (car.getCurrentWeight() > 2) {
				car.setWeightloss(car.getWeightloss() + (car.getCurrentWeight() * 0.09f));
				if (car.getCurrentWeight() <= 1) {
					car.setWeightloss(0);
					car.setTotalWeight(1);
					car.setCurrentWeight();
				}
				return true;
			} else {
				return false;
			}
		};
		// Fuel
		upgradeValues[2] = (Car car) -> {
			boolean res = false;
			if(fuelUpgrades < 3) {
				switch (fuelUpgrades) {
				case 0:
					car.setHp(car.getHp() + 2f);
					break;
				case 1:
					car.setHp(car.getHp() + 28f);
					break;
				case 2:
					car.setHp(car.getHp() + 206f);
					break;
				}
				res = true;
				fuelUpgrades++;
			}
			return res;
		};
		// Turbo
		upgradeValues[3] = (Car car) -> {
			car.setCurrentWeight();
			car.setHasTurbo(true);
			car.setHp(car.getHp() + (100f));
			car.setWeightloss(car.getWeightloss() - (car.getCurrentWeight() * 0.02f));
			return true;
		};
		// NOS
		upgradeValues[4] = (Car car) -> {
			if (!car.isHasNOS()) {
				car.setHasNOS(true);
				car.setNosAmountLeftStandard(1);
			}
			car.setNosStrengthStandard(car.getNosStrengthStandard() + 0.5);
			return true;
		};
		// Pistons
		upgradeValues[5] = (Car car) -> {
			car.setCurrentWeight();
			car.setWeightloss(car.getWeightloss() + (car.getCurrentWeight() * 0.05f));
			car.setHp(car.getHp() + 60f);
			return true;
		};
		// Gears
		upgradeValues[6] = (Car car) -> {
			double topspeedPrev = car.getTopSpeed();
			double topspeedInc = 72.0 * (fuelUpgrades + 1);
			car.setCurrentWeight();
			car.setWeightloss(car.getWeightloss() + (car.getCurrentWeight() * 0.03f));
			car.setTopSpeed(topspeedPrev + topspeedInc);
			car.setGearsbalance(car.getGearsbalance() * (1 - (topspeedInc / topspeedPrev)));
			car.setUpgradedGears(true);
			return true;
		};
		// Block
		upgradeValues[7] = (Car car) -> {
			car.setCurrentWeight();
			car.setHp(car.getHp() + 170f);
			car.setWeightloss(car.getWeightloss() - (car.getCurrentWeight() * 0.14f));
			return true;
		};
		// Tires
		upgradeValues[8] = (Car car) -> {
			car.setGearBoostSTD(car.getGearBoostSTD() + 0.4);
			return true;
		};

		// TODO maybe add aero and wind resistance????
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
		return newCar.showStats();
	}

	public boolean upgrade(int i, Car car) {
		return upgradeValues[i].upgrade(car);
	}

	public static String[] getUpgradeNames() {
		return upgradeNames;
	}

	public void setUpgradeNames(String[] upgradeNames) {
		this.upgradeNames = upgradeNames;
	}

	public UpgradePrice[] getUpgradePrices() {
		return upgradePrices;
	}

	public void setUpgradePrices(UpgradePrice[] upgradePrices) {
		this.upgradePrices = upgradePrices;
	}

	public UpgradeAction[] getUpgradeValues() {
		return upgradeValues;
	}

	public void setUpgradeValues(UpgradeAction[] upgradeValues) {
		this.upgradeValues = upgradeValues;
	}

}
