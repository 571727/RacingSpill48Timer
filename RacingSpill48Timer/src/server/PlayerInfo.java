package server;

import elem.Bank;
import elem.Car;

public class PlayerInfo {

	protected String name;
	protected int ready;
	protected long timeLapsedInRace;
	protected int pointsAdded;
	protected int moneyAdded;
	private int host;
	private String id;
	private int finished;
	private Bank bank;

	protected Car car;
	private boolean inTheRace;

	public PlayerInfo(String name, String id, String host) {
		this.name = name;
		this.id = id;
		this.host = Integer.valueOf(host);
		this.car = null;
		bank = new Bank();
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
		if (car != null)
			return name + "#" + ready + "#" + host + "#" + bank.getPoints();
		else
			return "Joining...";
	}

	public String getCarInfo() {
		String res = "";
		if (car != null)
			res = car.getCarName().toUpperCase() + ", HP: " + car.getHp() + ", KG: " + car.getCurrentWeight() + ", TS: "
					+ car.getTopSpeed() + ", NOS: " + car.isHasNOS();
		return res;
	}

	public void newRace() {
		finished = 0;
		timeLapsedInRace = 0;
		inTheRace = false;
	}

	/**
	 * fra og med input[3] input[3] finished input[4] timecurrently
	 * 
	 * @param input
	 * 
	 */
	public void updateRaceResults(String[] input) {
		finished = Integer.valueOf(input[3]);
		timeLapsedInRace = Long.valueOf(input[4]);
	}

//	public String getRaceResults() {
//		return name + "#" finished + "#" + time + "#" + pointsGained or something.;
//	}

	/**
	 * @return name#ready#car#...
	 */
	public String getRaceInfo(boolean allFinished) {
		if (allFinished == false)
			return name + "#" + finished + "#" + timeLapsedInRace + "#0#" + car.getCarName();
		else
			return name + "#" + finished + "#" + timeLapsedInRace + "#, +" + pointsAdded + " points, +$" + moneyAdded
					+ "#" + car.getCarName();
	}

	public void addPointsAndMoney(int amountPlayers, int place, float races, float totalRaces) {

		float inflation = (Math.abs(totalRaces - races) + 1) / 2;
		int winnerExtraPoint = (place == 0 ? 1 : 0);

		pointsAdded = 0;
		
		if (!(amountPlayers == -1 || place == -1)) {
			pointsAdded = (amountPlayers - (place + 1)) + winnerExtraPoint;
			moneyAdded = (int) (100f * place * inflation);
		} else {
			moneyAdded = (int) (50f * inflation);
		}

		bank.addPoints(pointsAdded);
		bank.addMoney(moneyAdded);
	}

	public int getFinished() {
		return finished;
	}

	public void setFinished(int finished) {
		this.finished = finished;
	}

	public int getPoints() {
		return bank.getPoints();
	}

	public void setPoints(int points) {
		bank.setPoints(points);
	}

	public int getMoney() {
		return bank.getMoney();
	}

	public void setMoney(int money) {
		bank.setMoney(money);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCarName() {
		return car.getCarName();
	}

	public long getTime() {
		return timeLapsedInRace;
	}

	public void setTime(long time) {
		this.timeLapsedInRace = time;
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

	public String getID() {
		return id;
	}

	public void setIn(boolean in) {
		inTheRace = in;
	}

	public boolean isIn() {
		return inTheRace;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

}
