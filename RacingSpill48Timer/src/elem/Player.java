package elem;

import java.util.Random;

import client.EchoClient;
import connection_standard.Config;
import scenes.Lobby;

/**
 * holds and handles its client. Controls lobby for now.
 * @author jonah
 *
 */
public class Player {

	private String name;
	private int host;
	private int id;
	private String ip;
	private EchoClient client;
	
	public Player(String name, int host) {
		this(name, host, Config.SERVER);
	}
	
	public Player(String name, int host, String ip) {
		this.name = name;
		this.ip = ip;
		Random r = new Random();
		id = r.nextInt(20);
		client = new EchoClient(ip);
		this.host = host;
		
		//Request stats about lobby and update lobby
		joinServer();
	}
	
	public void joinServer() {
		Lobby.update(client.sendRequest("JOIN#" + name + "#" + host));
	}
	
	public void updateLobbyFromServer() {
		
	}
	
}
