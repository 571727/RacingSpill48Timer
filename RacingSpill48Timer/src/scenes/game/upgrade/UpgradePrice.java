package scenes.game.upgrade;

import java.util.HashMap;
import java.util.Map.Entry;

public class UpgradePrice {

	private int points;
	private int money;
	private HashMap<Integer, Double> sale;

	public UpgradePrice(int points, int money) {
		this.points = points;
		this.money = money;
		sale = new HashMap<Integer, Double>();
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getMoney() {
		double money = this.money;
		
		for (Entry<Integer, Double> sale : sale.entrySet()) {
			money = money * (1 - sale.getValue());
		}
		
		return (int) money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public void addSale(double value, int key) {
		sale.put(key, value);
	}
}
