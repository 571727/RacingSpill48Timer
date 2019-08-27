package elem;

import java.util.Arrays;
import java.util.Random;

import client.EchoClient;
import connection_standard.Config;
import handlers.ClientThreadHandler;
import handlers.StoreHandler;
import startup.Main;

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
	private StoreHandler fixCarHandler;
	private Random r;
	private Bank bank;
	private ClientThreadHandler cth;
	private boolean inTheRace;
	private int moneyAchived;
	private int pointsAchived;
	private int podium;

	public Player(String name, int host, String car) {
		this(name, host, car, Config.SERVER);
	}

	public Player(String name, int host, String car, String ip) {
		this.name = name;
		this.ip = ip;
		this.host = host;
		ready = 0;
		this.car = new Car(car, true);
		r = new Random();
		client = new EchoClient(ip);
		bank = new Bank();
		cth = new ClientThreadHandler(client, -1);
		// Request stats about lobby and update lobby
	}

	public void finishRace(long time) {
		client.sendRequest("F#" + id + "#" + time);
	}


	public void inTheRace() {
		inTheRace = true;
		client.sendRequest("I#" + id);
	}

	/**
	 * JOIN#name+id#host-boolean
	 */
	public void joinServer() {
		String[] ids = client
				.sendRequest("J#" + id + "#" + name + "#" + host + "#" + Main.DISCONNECTED_ID)
				.split("#");
		this.id = Byte.valueOf(ids[0]);
		Main.newDisconnectedID(Long.valueOf(ids[1]));
		
		if (Integer.valueOf(ids[2]) == 1) {
			name = ids[3];
			car.updateServerClone(ids, 4);
		}
		
		cth.setID(id);
		
	}

	/**
	 * LEAVE#name+id
	 */
	public void leaveServer() {
		System.out.println("Leaving server...");
		client.sendRequest("L#" + id);
	}

	/**
	 * UPDATELOBBY#name+id#ready - ready : int (0,1)
	 */
	
	public void stopAllClientHandlerOperations() {
		cth.stopAll();
	}

	public void endClientHandler() {
		cth.end();
	}

	public void startClientHandler() {
		try {
			cth.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void startPing() {
		cth.startPing();
	}
	
	public void stopPing() {
		cth.stopPing();
	}
	
	public String updateLobby() {
		return cth.getLobbyString();
	}

	public String updateRaceLobby() {
		return cth.getRaceLobbyString();
	}

	public int updateRaceLights() {
		return cth.getRaceLights();
	}
	
	
	public void startUpdateLobby() {
		cth.startLobby();
	}

	public void startUpdateRaceLobby() {
		cth.startRaceLobby();
	}

	public void startUpdateRaceLights() {
		cth.startRaceLights();
	}
	
	
	public void stopUpdateLobby() {
		cth.stopLobby();
	}

	public void stopUpdateRaceLobby() {
		cth.stopRaceLobby();
	}

	public void stopUpdateRaceLights() {
		cth.stopRaceLights();
	}
	
	
	public void updateReady() {
		client.sendRequest("RE#" + id + "#" + ready);
	}



	public void startRace() {
		client.sendRequest("SR#" + id + "#" + host + 1);
	}

	public void stopRace() {
		client.sendRequest("SR#" + id + "#" + host + 0);
	}

	public int getTrackLength() {
		return Integer.valueOf(client.sendRequest("GL#" + id));
	}

	public void setPointsAndMoney(int newPoints, int newMoney) {
		client.sendRequest("SPM#" + id + "#" + newPoints + "#" + newMoney);
	}

	public String getPointsAndMoney() {
		String result = client.sendRequest("GPM#" + id);
		String[] output = result.split("#");
		bank.setPoints(Integer.valueOf(output[0]));
		bank.setMoney(Integer.valueOf(output[1]));
		return "<html>" + name + ": <br/>" + "Points: " + bank.getPoints() + ".<br/>" + "Money: " + bank.getMoney()
				+ ".";
	}

	public void createNewRaces(int amount) {
		client.sendRequest("NEW#" + id + "#" + amount);
	}

	public String getEndGoal() {
		return client.sendRequest("GEG#" + id);
	}

	public String getWinner() {
		return client.sendRequest("W#" + id);
	}

	public void addChat(String text) {
		client.sendRequest("ADC#" + id + "#" + name + "#" + text);
	}

	public String getChat() {
		return client.sendRequest("GC#" + id);
	}

	public String getCurrentPlace() {
		return client.sendRequest("GP#" + id);
	}

	public void setPricesAccordingToServer() {
		String[] s = client.sendRequest("GPR#" + id).split("#");
		fixCarHandler.setPrices(Arrays.asList(s).stream().mapToInt(Integer::parseInt).toArray());
	}

	public void updateCarCloneToServer() {
		client.sendRequest("CAR#" + id + "#" + car.cloneToServerString());
	}

	public boolean isGameOver() {
		return client.sendRequest("GO#" + id).equals("1");
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

	public StoreHandler getFixCarHandler() {
		return fixCarHandler;
	}

	public void setFixCarHandler(StoreHandler fixCarHandler) {
		this.fixCarHandler = fixCarHandler;
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

	public void setPlacePodium(int podium) {
		this.podium = podium;
	}

	public int getPlacePodium() {
		return podium;
	}

}
