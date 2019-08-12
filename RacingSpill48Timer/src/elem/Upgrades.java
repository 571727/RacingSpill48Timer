package elem;

import adt.UpgradeAction;

//Dyrere for de som leder: 1st (+10%), 2nd (5%), 3rd (2%) osv.

public class Upgrades {

	public static String[] UPGRADE_NAMES = { "Cylinders", "Weight reduction", "Fuel", "Bigger turbo",
			"More NOS", "Lighter pistons", "Gears", "Beefier block", "Tires" };
	private UpgradePrice[] upgradePrices;
	private UpgradeAction[] upgradeValues;

	public Upgrades() {
		upgradePrices = new UpgradePrice[UPGRADE_NAMES.length];
		upgradeValues = new UpgradeAction[UPGRADE_NAMES.length];
		
		// Cylinders
		upgradeValues[0] = (Car car) -> {
			car.setHp(car.getHp() + 35f);
			car.upgradeRightShift(1.20);
			car.iterateUpgradeLVL(0);
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
				car.iterateUpgradeLVL(1);
				return true;
			} else {
				return false;
			}
		};
		// Fuel
		upgradeValues[2] = (Car car) -> {
			boolean res = false;
			if(car.getUpgradeLVL(2) < 3) {
				switch (car.getUpgradeLVL(2)) {
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
				car.iterateUpgradeLVL(2);
			}
			return res;
		};
		// Turbo
		upgradeValues[3] = (Car car) -> {
			car.setCurrentWeight();
			car.setHasTurbo(true);
			car.setHp(car.getHp() + (100f));
			car.setWeightloss(car.getWeightloss() - (car.getCurrentWeight() * 0.02f));
			car.iterateUpgradeLVL(3);
			return true;
		};
		// NOS
		upgradeValues[4] = (Car car) -> {
			if (!car.isHasNOS()) {
				car.setHasNOS(true);
				car.setNosAmountLeftStandard(1);
			}
			car.setNosStrengthStandard(car.getNosStrengthStandard() + 0.5);
			car.iterateUpgradeLVL(4);
			return true;
		};
		// Pistons
		upgradeValues[5] = (Car car) -> {
			car.setCurrentWeight();
			car.setWeightloss(car.getWeightloss() + (car.getCurrentWeight() * 0.05f));
			car.setHp(car.getHp() + 60f);
			car.iterateUpgradeLVL(5);
			return true;
		};
		// Gears
		upgradeValues[6] = (Car car) -> {
			double topspeedPrev = car.getTopSpeed();
			double topspeedInc = 72.0 * (car.getUpgradeLVL(2) + 1);
			car.setCurrentWeight();
			car.setWeightloss(car.getWeightloss() + (car.getCurrentWeight() * 0.03f));
			car.setTopSpeed(topspeedPrev + topspeedInc);
			car.setGearsbalance(car.getGearsbalance() * (1 - (topspeedInc / topspeedPrev)));
			car.setUpgradedGears(true);
			car.iterateUpgradeLVL(6);
			return true;
		};
		// Block
		upgradeValues[7] = (Car car) -> {
			car.setCurrentWeight();
			car.setHp(car.getHp() + 170f);
			car.setWeightloss(car.getWeightloss() - (car.getCurrentWeight() * 0.14f));
			car.iterateUpgradeLVL(7);
			return true;
		};
		// Tires
		upgradeValues[8] = (Car car) -> {
			car.setGearBoostSTD(car.getGearBoostSTD() + 0.4);
			car.iterateUpgradeLVL(8);
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

	public UpgradeAction[] getUpgradeValues() {
		return upgradeValues;
	}

	public void setUpgradeValues(UpgradeAction[] upgradeValues) {
		this.upgradeValues = upgradeValues;
	}
	
}
