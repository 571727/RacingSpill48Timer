package elem;

import java.util.Random;

import scenes.Race;
import server.PlayerInfo;
import startup.Main;

public class AI extends PlayerInfo {

	private static Random r = new Random();
	private static Car car;
	private int diff;

	/**
	 * @param id
	 * @param diff
	 */

	public AI(int id, int diff) {
		this(randomOfStringArray(Main.AI_NAMES), String.valueOf(id), "0", randomCar());
		this.diff = diff;
		car.setAudioActivated(false);
		ready = 1;
	}

	private AI(String name, String id, String host, String carName) {
		super(name, id, host, carName);

	}

	private static String randomCar() {
		car = new Car(randomOfStringArray(Main.CAR_TYPES), false);

		return car.getCarStyle();
	}

	private static String randomOfStringArray(String[] arr) {
		return arr[r.nextInt(arr.length)];
	}

	public String randomName(String[] names) {
		r = new Random();

		return names[r.nextInt(names.length)];
	}

	public void upgradeCar() {
		// TODO implement upgrading via Car class and make AI upgrade in a smart / dumb
		// way.
	}

	public long calculateRace(int length) {
		// TODO balance
		long time = -1;

		// DNF?
		if (shouldIEngageGear(40 - (2 * diff))) {
			return time;
		} else {
			time++;
			shiftUp(false);
		}

		// Race
		while (car.getDistance() < length) {

			if (car.isGearCorrect() || car.isTopGear()) {
				car.accelerateCar();
			} else {
				car.decelerateCar();
				shiftUp(true);
			}

			car.calculateActualSpeed();
			car.calculateDistance();
			time++;
		}

		// convert time to right timetype

		long tickTime = 1000 / Race.TICK_STD;
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

		car.shiftUp();

		if (countTime)
			time += tryEngageGear();

		return time;
	}

	private long tryEngageGear() {
		long time = 0;
		while (!shouldIEngageGear(0)) {
			time++;
		}
		return time;
	}

	private boolean shouldIEngageGear(int finetune) {
		int ranval = r.nextInt(100);
		return ranval + diff - finetune >= 50;
	}

	/**
	 * @return name#ready#car#...
	 */
	@Override
	public String getRaceInfo(boolean allFinished) {
		return name + "#" + 2 + "#" + timeLapsedInRace + "#, +" + pointsAdded + " points, +$" + moneyAdded + "#"
				+ carName.toLowerCase();
	}

}
