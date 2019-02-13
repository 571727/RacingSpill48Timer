package server;

public class PlayerInfo {

	private int ready;
	private int host;
	private String name;
	private long time;
	private int finished;
	private int points;
	private int money;
	private int pointsAdded;
	private int moneyAdded;

	private String carName;

	public PlayerInfo(String name, String host, String carName) {
		this.name = name;
		this.host = Integer.valueOf(host);
		this.carName = carName;
	}

	/**
	 * fra og med input[3]
	 * 
	 * @param input
	 */
	public void updateLobby(String[] input) {
		ready = Integer.parseInt(input[3]);
	}

	/**
	 * @return name#ready#host#points
	 */
	public String getLobbyInfo() {
		return name + "#" + ready + "#" + host + "#" + carName + "#" + points;
	}

	public void newRace() {
		finished = 0;
		time = 0;
	}

	/**
	 * fra og med input[3] input[3] finished input[4] timecurrently
	 * 
	 * @param input
	 * 
	 */
	public void updateRaceResults(String[] input) {
		finished = Integer.valueOf(input[3]);
		time = Long.valueOf(input[4]);
	}

//	public String getRaceResults() {
//		return name + "#" finished + "#" + time + "#" + pointsGained or something.;
//	}

	/**
	 * @return name#ready#car#...
	 */
	public String getRaceInfo() {
		if (finished == 0)
			return name + "#" + finished + "#" + time + "#";
		else
			return name + "#" + finished + "#" + time + "#, +" + pointsAdded + " points, +$" + moneyAdded;
	}

	public void addPointsAndMoney(int amountPlayers, int place) {
		if (!(amountPlayers == -1 || place == -1)) {
			points += (amountPlayers + 1) / (place + 1);
			money += 100f * place;
			pointsAdded = (amountPlayers + 1) / (place + 1);
			moneyAdded = (int) (100f * place);
		}
	}

	public int getFinished() {
		return finished;
	}

	public void setFinished(int finished) {
		this.finished = finished;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCarName() {
		return carName;
	}

	public void setCarName(String carName) {
		this.carName = carName;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getPointsAdded() {
		return pointsAdded;
	}

	public void setPointsAdded(int pointsAdded) {
		this.pointsAdded = pointsAdded;
	}

	public int getMoneyAdded() {
		return moneyAdded;
	}

	public void setMoneyAdded(int moneyAdded) {
		this.moneyAdded = moneyAdded;
	}

}
