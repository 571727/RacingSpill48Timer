package elem;

import java.util.Arrays;
import java.util.Random;

import client.EchoClient;
import connection_standard.Config;
import handlers.FixCarHandler;

/**
 * holds and handles its client. Controls lobby for now.
 * 
 * @author jonah
 *
 */
public class Player {

	private String name;
	private int host;
	private int id;
	private int ready;
	private String ip;
	private EchoClient client;
	private Car car;
	private FixCarHandler fixCarHandler;
	private String carName;
	private Random r;
	private Bank bank;
	private boolean inTheRace;

	public Player(String name, int host, String car) {
		this(name, host, car, Config.SERVER);
	}

	public Player(String name, int host, String car, String ip) {
		this.name = name;
		this.ip = ip;
		this.host = host;
		ready = 0;
		this.car = new Car(car, true);
		carName = car;
		r = new Random();
		id = r.nextInt(999);
		client = new EchoClient(ip);
		bank = new Bank();

		// Request stats about lobby and update lobby
	}

	public String getPointsAndMoney() {
		String result = client.sendRequest("GETPOINTSMONEY#" + name + "#" + id);
		String[] output = result.split("#");
		bank.setPoints(Integer.valueOf(output[0]));
		bank.setMoney(Integer.valueOf(output[1]));
		return "<html>" + name + ": <br/>" + "Points: " + bank.getPoints() + ".<br/>" + "Money: " + bank.getMoney()
				+ ".";
	}

	public void setPointsAndMoney(int newPoints, int newMoney) {
		client.sendRequest("SETPOINTSMONEY#" + name + "#" + id + "#" + newPoints + "#" + newMoney);
	}

	/**
	 * JOIN#name+id#host-boolean
	 */
	public String joinServer() {
		return client.sendRequest("JOIN#" + name + "#" + id + "#" + host);
	}
	
	public void updateCarCloneToServer() {
		client.sendRequest("UPDATECARCLONE#"+ name + "#" + id + "#" + car.cloneToServerString());
	}

	/**
	 * UPDATELOBBY#name+id#ready - ready : int (0,1)
	 */
	public String updateLobbyFromServer() {
		return client.sendRequest("UPDATELOBBY#" + name + "#" + id + "#" + ready);
	}

	public void inTheRace() {
		inTheRace = true;
		client.sendRequest("IN#" + name + "#" + id);
	}

	public void startRace() {
		client.sendRequest("STARTRACE#" + host + "#" + 1);
	}

	public void stopRace() {
		client.sendRequest("STARTRACE#" + host + "#" + 0);
	}

	public int getTrackLength() {
		return Integer.valueOf(client.sendRequest("GETLENGTH"));
	}

	public String getCurrentPlace() {
		return client.sendRequest("GETPLACE");
	}

	/**
	 * LEAVE#name+id
	 */
	public void leaveServer() {
		System.out.println("Leaving server...");
		client.sendRequest("LEAVE#" + name + "#" + id);
	}

	public int getStatusRaceLights() {
		return Integer.valueOf(client.sendRequest("RACELIGHTS"));
	}

	public void finishRace(long time) {
		client.sendRequest("F#" + name + "#" + id + "#" + time);
	}

	public String updateRaceLobby() {
		return client.sendRequest("UPDATERACE");
	}

	public void pingServer() {
//		System.out.println("PING");
		client.sendRequest("PING#" + name + "#" + id);
	}

	public String getRacesLeft() {
		return client.sendRequest("GETRACESLEFT");
	}

	public String getWinner() {
		return client.sendRequest("WINNER#" + name + "#" + id);
	}

	public void createNewRaces() {
		client.sendRequest("NEWRACES");
	}

	public void addChat(String text) {
		client.sendRequest("ADDCHAT#" + name + "#" + text);
	}

	public String getChat() {
		return client.sendRequest("GETCHAT#" + name + "#" + id);
	}

	public void setPricesAccordingToServer() {
		String[] s = client.sendRequest("GETPRICES").split("#");
		fixCarHandler.setPrices(Arrays.asList(s).stream().mapToInt(Integer::parseInt)
				.toArray());

	}

	public int getHost() {
		return host;
	}

	public void setHost(int host) {
		this.host = host;
	}

	public int getReady() {
		return ready;
	}

	public void setReady(int ready) {
		this.ready = ready;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
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

	public boolean isHost() {
		return host == 1;
	}

	public boolean isInTheRace() {
		return inTheRace;
	}

	public void outOfTheRace() {
		inTheRace = false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public FixCarHandler getFixCarHandler() {
		return fixCarHandler;
	}

	public void setFixCarHandler(FixCarHandler fixCarHandler) {
		this.fixCarHandler = fixCarHandler;
	}

}
