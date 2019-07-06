package elem;

import java.util.Random;

import client.EchoClient;
import connection_standard.Config;
import handlers.SceneHandler;
import scenes.Lobby;

/**
 * holds and handles its client. Controls lobby for now.
 * 
 * @author jonah
 *
 */
public class Player{

	private String name;
	private int host;
	private int id;
	private int ready;
	private String ip;
	private EchoClient client;
	private Car car;
	private String carName;
	private Random r;
	private int points;
	private int money;
	private int[] inflation;
	private boolean inTheRace;

	public Player(String name, int host, String car) {
		this(name, host, car, Config.SERVER);
	}

	public Player(String name, int host, String car, String ip) {
		this.name = name;
		this.ip = ip;
		this.host = host;
		ready = 0;
		this.car = new Car(car);
		carName = car;
		r = new Random();
		id = r.nextInt(999);
		inflation = new int[8];
		client = new EchoClient(ip);
		
		// Request stats about lobby and update lobby
		joinServer();
	}

	public String getPointsAndMoney() {
		String result = client.sendRequest("GETPOINTSMONEY#" + name + "#" + id);
		String[] output = result.split("#");
		points = Integer.valueOf(output[0]);
		money = Integer.valueOf(output[1]);
		return "<html>" + name + ": <br/>" + "Points: " + points + ".<br/>" + "Money: " + money + ".";
	}
	
	public void setPointsAndMoney(int newPoints, int newMoney) {
		client.sendRequest("SETPOINTSMONEY#" + name + "#" + id + "#" + newPoints + "#" + newMoney);
	}
	
	/**
	 * JOIN#name+id#host-boolean
	 */
	public String joinServer() {
		return client.sendRequest("JOIN#" + name + "#" + id + "#" + host + "#" + carName);
	}

	/**
	 * UPDATELOBBY#name+id#ready - ready : int (0,1)
	 */
	public String updateLobbyFromServer() {
		return client.sendRequest("UPDATELOBBY#" + name  + "#" +  id + "#" + ready );
	}
	
	public void inTheRace() {
		inTheRace = true;
		client.sendRequest("IN#" + name  + "#" +  id);
	}
	
	public void startRace(){
		client.sendRequest("STARTRACE#" + host + "#" + 1);
	}
	
	public void stopRace(){
		client.sendRequest("STARTRACE#" + host + "#" + 0);
	}
	
	public int getTrackLength() {
		return Integer.valueOf(client.sendRequest("GETLENGTH"));
	}
	
	/**
	 * LEAVE#name+id 
	 */
	public void leaveServer() {
		client.sendRequest("LEAVE#" + name  + "#" +  id);
	}
	
	public int getStatusRaceLights() {
		//FIXME numberformatexception
		return Integer.valueOf(client.sendRequest("RACELIGHTS"));
	}
	
	public void finishRace() {
		// TODO Auto-generated method stub
		client.sendRequest("F#" + name + "#" + id);
	}

	public String updateRaceLobby() {
		return client.sendRequest("UPDATERACE");
	}
	
	public void pingServer() {
		System.out.println("PING");
		client.sendRequest("PING#" + name + "#" + id);
	}
	
	public String getRacesLeft() {
		return client.sendRequest("GETRACESLEFT");
	}
	
	public String getWinner() {
		return client.sendRequest("WINNER");
	}
	
	public void createNewRaces() {
		client.sendRequest("NEWRACES");
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

	public int[] getInflation() {
		return inflation;
	}

	public void setInflation(int[] inflation) {
		this.inflation = inflation;
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



}
