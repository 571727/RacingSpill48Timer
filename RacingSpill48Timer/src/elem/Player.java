package elem;

import client.EchoClient;
import connection_standard.Config;

/**
 * holds and handles its client. Controls lobby for now.
 * @author jonah
 *
 */
public class Player {

	private String name;
	private String ip;
	private EchoClient client;
	
	public Player(String name) {
		this(name, Config.SERVER);
	}
	
	public Player(String name, String ip) {
		this.name = name;
		this.ip = ip;
		
		client = new EchoClient(ip);
		
		//Request stats about lobby and update lobby
	}
	
}
