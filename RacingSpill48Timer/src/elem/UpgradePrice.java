package elem;

public class UpgradePrice {

	private int points;
	private int money;

	public UpgradePrice(int points, int money) {
		this.points = points;
		this.money = money;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
}
