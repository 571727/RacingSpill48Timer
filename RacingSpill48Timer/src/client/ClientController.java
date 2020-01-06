package client;

import handlers.ClientThreadHandler;
import main.Main;

public class ClientController {

	private TCPEchoClient client;
	private ClientThreadHandler cth;
	
	public ClientController (TCPEchoClient client, ClientThreadHandler cth) {
		this.client = client;
		this.cth = cth;
	}
	
	public String sendRequest(String request) {
		String response = client.sendRequest(request);
		
		if(response != null && response.equals(Main.END_ALL_CLIENT_STRING)) {
			System.err.println("ENDING ClientThreadHandler: " +request);
			cth.stopAll();
			cth.end();
		}
		
		return response;
	}
}
