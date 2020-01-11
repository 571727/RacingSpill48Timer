package elem;

import java.util.Random;

import handlers.StoreHandler;
import main.Main;
import player_local.Car;
import player_local.CarRep;
import player_local.PlayerInfo;
import scenes.Race;
import scenes.upgrade.Upgrades;

public class AI extends PlayerInfo {

	private static Random r = new Random();
	private int diff;
	private StoreHandler upgrades;
	private boolean firstUpgrade;

	/**
	 * @param id
	 * @param diff
	 */

	public AI(String name, byte id, int diff, int[] prices) {
		this(name, id, "0");
		randomCar();
		this.diff = diff;
		car.setAudioActivated(false);
		ready = 1;
		upgrades = new StoreHandler();
		upgrades.setPrices(prices);
	}

	private AI(String name, byte id, String host) {
		super(name, id, host);

	}

	private void randomCar() {
		car = new Car(randomOfStringArray(Main.CAR_TYPES), false);
	}

	private String randomOfStringArray(String[] arr) {
		return arr[r.nextInt(arr.length)];
	}

	public String randomName(String[] names) {
		r = new Random();

		return names[r.nextInt(names.length)];
	}

	public void upgradeCar(int leadingPoints) {
		// TODO implement upgrading via Car class and make AI upgrade in a smart / dumb
		// way. if first turn or leading by 4 points? then use points. If has money buy
		// shit
		boolean boughtSomething = false;

		switch (diff) {
		case (0):
			// Easy
			for (int i = 0; i < upgrades.getUpgradeNames().length; i++) {
				buyMoney(super.getBank().getMoney(), i);
			}
			break;
		case (1):
			// Normal
			if (!firstUpgrade) {
				firstUpgrade = true;
				buyBest(false, getBank().getPoints());
				// Use all points
			}

			do {
				boughtSomething = false;
				for (int i = 0; i < upgrades.getUpgradeNames().length; i++) {
					if (buyMoney(super.getBank().getMoney(), i))
						boughtSomething = true;
				}
			} while (boughtSomething);

			break;
		case (2):
		case (3):
			// Hard or godlike
			if (!firstUpgrade) {
				firstUpgrade = true;
				buyBest(false, getBank().getPoints());
				// Use all points
			}


//			when power is hp / w * speed < 800
			buyBest(true, getBank().getMoney());
			
			break;
		}
	}

	private void buyBest(boolean money, int spend) {
		int buythis = 0;
		boolean boughtSomething = false;
		// Needs ts?
		while (car.getHp() / car.getWeight() / car.getSpeedTop() / ((car.getNosStrengthStandard() + 1) / 2)
				/ (car.getTireGripStrengthStandard() / 2) > 0.001 && boughtSomething) {
			boughtSomething = false;
			buythis = findBestUpgrade(1, money, spend);

			if (buythis != -1) {
				if (money)
					boughtSomething = buyMoney(spend, buythis);
				else
					boughtSomething = buyPoints(spend, buythis);
			}
		}

		while (buythis != -1) {
			int boostorpower = r.nextInt(100) > 60 ? 2 : 0;
			buythis = findBestUpgrade(boostorpower, money, spend);

			if (buythis != -1) {
				if (money)
					boughtSomething = buyMoney(spend, buythis);
				else
					boughtSomething = buyPoints(spend, buythis);
			}
		}

	}

	/**
	 * type 0 = power, 1 = ts, 2 = boost
	 * 
	 * @return index to upgrade
	 */
	private int findBestUpgrade(int type, boolean money, int spend) {
		Upgrades u = upgrades.getUpgrades();
		int bestValueIndex = -1;
		switch (type) {
		case 0:
			// POWAAAHHH
			double hpw = -1;
			for (int i = 0; i < upgrades.getUpgradeNames().length; i++) {
				CarRep temp = u.upgradeNewCarRep(i, car, true);
				double price;
				boolean next = false;
				if (money) {
					price = upgrades.getCostMoney(i, car.getRepresentation(), getPodium());
					if (price > spend)
						next = true;
				} else {
					price = upgrades.getCostPoints(i, car.getRepresentation());
					if (price > spend)
						next = true;

				}
				double compared = (temp.getHp() / temp.getWeight()) / price;
				if (!next && compared > hpw) {
					bestValueIndex = i;
					hpw = compared;
				}
			}
			break;
		case 1:
			// TS
			double ts = -1;
			for (int i = 0; i < upgrades.getUpgradeNames().length; i++) {
				double price;
				boolean next = false;
				if (money) {
					price = upgrades.getCostMoney(i, car.getRepresentation(), getPodium());
					if (price > getBank().getMoney())
						next = true;
				} else {
					price = upgrades.getCostPoints(i, car.getRepresentation());
					if (price > getBank().getPoints())
						next = true;

				}
				double compared = (u.upgradeNewCarRep(i, car, true).getSpeedTop() - car.getSpeedTop()) / price;
				if (!next && compared > ts) {
					bestValueIndex = i;
					ts = compared;
				}
			}

			break;
		case 2:
			// Boost : First NOS then Tireboost then whatever
			boolean affordNOS = (money
					? upgrades.getCostMoney(4, car.getRepresentation(), getPodium()) <= getBank().getMoney()
					: upgrades.getCostPoints(4, car.getRepresentation()) <= getBank().getPoints());

			boolean affordTG = (money
					? upgrades.getCostMoney(8, car.getRepresentation(), getPodium()) <= getBank().getMoney()
					: upgrades.getCostPoints(8, car.getRepresentation()) <= getBank().getPoints());

			if (affordNOS && car.getRepresentation().getUpgradeLVL(4) < 5)
				bestValueIndex = 4;
			else if (affordTG && car.getRepresentation().getUpgradeLVL(8) < 5)
				bestValueIndex = 8;
			else {
				if (affordNOS)
					bestValueIndex = 4;
				else if (affordTG)
					bestValueIndex = 8;
			}
			break;

		}
		return bestValueIndex;
	}

	private boolean buyPoints(int pointsToSpend, int i) {
		boolean res = upgrades.buyWithPoints(getBank(), i, car.getRepresentation(), pointsToSpend);
		car.reset();
		return res;
	}

	private boolean buyMoney(int moneyToSpend, int i) {
		boolean res = upgrades.buyWithMoney(getBank(), i, car.getRepresentation(), getPodium(), moneyToSpend);
		car.reset();
		return res;
	}

	public long calculateRace(int length) {
		long time = -1;

		double dnfChance = 0.1 / (diff + 1);
		double tbChance = 0.2 * (diff + 1) / (car.getHp() / 150) * (car.getUpgradeLVL(0) + 1);
		if (diff == 3 || car.getUpgradeLVL(0) >= 5)
			tbChance = 1;

		// DNF?
		if (chance(dnfChance)) {
			return time;
		} else {
			time++;
			shiftUp(false);
			car.setEngineOnActual(true);
			car.setRpm(car.getRepresentation().getRpmIdle());
			car.setGas(true);

			// Tiregrip? TODO change value
			if (chance(tbChance))
				car.gearBoost(time, 1000 / RaceScene.TICK_STD);

		}

		// Race
		while (car.getDistance() < length) {

			if (car.getNosBottleAmountLeft() > 0 && !car.isNOSON()) {
				car.nos(time, 1000 / RaceScene.TICK_STD);
				System.out.println("NOS!!");
			}

			car.updateSpeedInc();
			double speedChange = 0.0;
			if (car.isGearCorrect() || car.isTopGear()) {
				speedChange = car.accelerateCar(time);
			} else {
				// if sequential or normal
				if (car.isSequentialShift() || diff == 3) {
					shiftUp(false);
				} else {
					speedChange = car.decelerateCar();
					shiftUp(true);
				}
			}
			car.setSpeedLinear(car.getSpeedLinear() + speedChange);
			car.updateRPM(1);

			car.calculateActualSpeed();
			car.calculateDrag();
			car.calculateDistance(1);

			time++;
		}

		// convert time to right timetype

		long tickTime = 1000 / RaceScene.TICK_STD;
		long fineTime = (long) (tickTime / (car.getDistance() - length + 1));
		time = tickTime * time;
		time += fineTime;

		car.reset();

		return time;
	}

	private long shiftUp(boolean countTime) {

		// time is 3 because it takes time with clutch.
		long time = 3;

		if (countTime)
			time += tryEngageGear();

		car.clutchOn();
		car.shiftUp();
		car.clutchOff();

		if (countTime)
			time += tryEngageGear();

		return time;
	}

	private long tryEngageGear() {
		long time = 0;
		double missChance = 0.2 / (diff + 1);
		while (chance(missChance)) {
			time++;
		}
		return time;
	}

	private boolean chance(double chance) {
		double ranval = r.nextInt(100) / 100.0;
		return ranval <= chance;
	}

	/**
	 * @return name#ready#car#...
	 */
	@Override
	public String getRaceInfo(boolean allFinished, boolean full) {
		return name + "#" + 2 + "#" + timeLapsedInRace + "#, " + (full ? getBank().getPoints() : "+ " + pointsAdded) + " points, +$" + moneyAdded + "#"
				+ car.getCarName();
	}

}
