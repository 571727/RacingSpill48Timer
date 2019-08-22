package server;

import elem.Bank;
import elem.Car;

public class PlayerInfo {

	protected String name;
	protected byte ready;
	protected long timeLapsedInRace;
	protected int pointsAdded;
	protected int moneyAdded;
	private byte host;
	private byte id;
	private byte finished;
	private Bank bank;

	protected Car car;
	private boolean inTheRace;
	private Long discID;

	public PlayerInfo(String name, byte id, String host) {
		this.name = name;
		this.id = id;
		this.host = Byte.valueOf(host);
		this.car = null;
		bank = new Bank();
	}

	/**
	 * fra og med input[3]
	 * 
	 * @param input
	 */
	public void updateLobby(byte ready) {
		setReady(ready);
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
			res = car.getCarName().toUpperCase() + ", " + car.getHp() + " HP, " + car.getCurrentWeight() + " kg, TS: "
					+ car.getTopSpeed() + " km/h, NOS: " + car.isHasNOS() + ", TG: " + car.getGearBoostSTD();
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
	public void updateRaceResults(byte finished, long time) {
		setFinished(finished);
		timeLapsedInRace = time;
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
			if (place > 0)
				moneyAdded = (int) (100f * place * inflation);
			else
				moneyAdded = (int) (25f * inflation);
		} else {
			moneyAdded = (int) (50f * inflation);
		}

		bank.addPoints(pointsAdded);
		bank.addMoney(moneyAdded);
	}

	public int getFinished() {
		return finished;
	}

	public void setFinished(int i) {
		this.finished = (byte) i;
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

	public String getNameID() {
		return name + id;
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

	public byte getID() {
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

	public byte getReady() {
		return ready;
	}

	public void setReady(byte ready) {
		this.ready = ready;
	}

	public Long getDisconnectID() {
		return discID;
	}

	public void setDisconnectID(long id2) {
		discID = id2;		
	}

}
