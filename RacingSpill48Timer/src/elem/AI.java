package elem;

import java.util.Random;

import server.PlayerInfo;
import startup.Main;

public class AI extends PlayerInfo{

	private static Random r;
	private static Car car;
	
	
	public AI(String id) {
		this("AI", id, "0", randomCar());
		
	}
	
	private AI(String name, String id, String host, String carName) {
		super(name, id, host, carName);
		
	}

	private static String randomCar() {
		r = new Random();
		
		car = new Car(Main.CARTYPES[r.nextInt(Main.CARTYPES.length)]);
		
		return car.getCarStyle();
	}

	public String randomName(String[] names) {
		r = new Random();
		
		return names[r.nextInt(names.length)];
	}

	public void upgradeCar() {
		
	}
	
	public long calculateRace() {
		return 0;
	}

	
}
