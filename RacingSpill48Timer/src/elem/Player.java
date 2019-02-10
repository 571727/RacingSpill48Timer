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
		Random r = new Random();
		id = r.nextInt(200);

		client = new EchoClient(ip);

		// Request stats about lobby and update lobby
		joinServer();
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
		return client.sendRequest("UPDATELOBBY#" + name  + "#" +  id + "#" + ready);
	}
	/**
	 * LEAVE#name+id 
	 */
	public void leaveServer() {
		client.sendRequest("LEAVE#" + name  + "#" +  id);
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

}
