package elem;

import adt.UpgradeAction;

public class Upgrades {

	private String[] upgradeNames = { "Upgrade cylinders", "Weight reduction bro", "Better fuel", "Bigger turbo",
			"More NOS", "Lighter pistons", "Grippier tyres and gears", "Beefier block" };
	private UpgradePrice[] upgradePrices;
	private UpgradeAction[] upgradeValues;

	public Upgrades() {
		upgradePrices = new UpgradePrice[upgradeNames.length];
		upgradeValues = new UpgradeAction[upgradeNames.length];

		for (int i = 0; i < upgradePrices.length; i++) {
			upgradePrices[i] = new UpgradePrice(1, 50);
		}

		// Cylinders
		upgradeValues[0] = (Car car) -> {
			car.setHp(car.getHp() + 75f);
		};
		// Weight
		upgradeValues[1] = (Car car) -> {
			car.setWeightloss(car.getWeightloss() + (car.getTotalWeight() / 10f));
		};
		// Fuel
		upgradeValues[2] = (Car car) -> {
			car.setHp(car.getHp() + 30f);
		};
		// Turbo
		upgradeValues[3] = (Car car) -> {
			car.setHasTurbo(true);
			car.setHp(car.getHp() + (100f / 1f));
			car.setWeightloss(car.getWeightloss() - 15);
		};
		// NOS
		upgradeValues[4] = (Car car) -> {
			if (!car.isHasNOS()) {
				car.setHasNOS(true);
				car.setNosAmountLeftStandard(1);
			}
			car.setNosStrengthStandard(car.getNosStrengthStandard() + 0.5);
		};
		// Pistons
		upgradeValues[5] = (Car car) -> {
			car.setHp(car.getHp() + 200f);
			car.setWeightloss(car.getWeightloss() - 15);
		};
		// Gears
		upgradeValues[6] = (Car car) -> {
			double topspeedPrev = car.getTopSpeed();
			double topspeedInc = 75.0;

			car.setWeightloss(car.getWeightloss() + 50);
			car.setTopSpeed(topspeedPrev + topspeedInc);
			car.setGearsbalance(car.getGearsbalance() * (1 - (topspeedInc / topspeedPrev)));
			car.setUpgradedGears(true);
		};
		// Block
		upgradeValues[7] = (Car car) -> {
			car.setWeightloss(car.getWeightloss() + 50);
			car.setHp(car.getHp() + 75f);
		};

		//TODO maybe add aero and wind resistance????
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

	public void upgrade(int i, Car car) {
		upgradeValues[i].upgrade(car);
	}

	public String[] getUpgradeNames() {
		return upgradeNames;
	}

	public void setUpgradeNames(String[] upgradeNames) {
		this.upgradeNames = upgradeNames;
	}

}
