package elem;

public class Bank {
	private int points;
	private int money;
	private int moneyAchived;
	private int pointsAchived;

	public boolean buyWithMoney(int amount, int i) {
		boolean res = false;

		if (amount <= money) {
			money -= amount;
			res = true;
		}

		return res;
	}

	public void addMoney(int amount) {
		if (amount > 0)
			money += amount;
		moneyAchived += amount;
	}

	public boolean buyWithPoints(int amount, int i) {
		boolean res = false;

		if (amount <= points) {
			points -= amount;
			res = true;
		}

		return res;
	}

	public boolean canAffordMoney(int cost) {
		return cost <= money;
	}

	public boolean canAffordPoints(int cost) {
		return cost <= points;
	}

	public void addPoints(int amount) {
		if (amount > 0)
			pointsAchived += amount;
		points += amount;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		if (points - this.points > 0)
			pointsAchived += points - this.points;
		this.points = points;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		if (money - this.money > 0)
			moneyAchived += money - this.money;
		this.money = money +100000;
	}

	public int getMoneyAchived() {
		return moneyAchived;
	}

	public void setMoneyAchived(int moneyAchived) {
		this.moneyAchived = moneyAchived;
	}

	public int getPointsAchived() {
		return pointsAchived;
	}

	public void setPointsAchived(int pointsAchived) {
		this.pointsAchived = pointsAchived;
	}

}
