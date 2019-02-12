package server;

public class PlayerInfo {

	private int ready;
	private int host;
	private String name;
	private long time;
	private int finished;
	private int points;
	private int money;
	

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
	 * fra og med input[3]
	 * input[3] finished
	 * input[4] timecurrently
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
		return name + "#" + finished + "#" + time;
	}
	
	public void addPointsAndMoney(int amountPlayers, int amountFinished) {
		if(!(amountPlayers == -1 || amountFinished == -1)) {
			points += (amountPlayers + 1) / (amountFinished + 1);
			money += 100f * (0.75f * (amountFinished + 1));
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

	

}
