package scenes.game.multiplayer.client;

import main.Main;
/**
 * TODO add use for steam
 * 
 * @author Jens Benz
 *
 */
public class ClientController {

	private Client clientUDP;
	private ClientThreadHandler cth;

	public ClientController(String ip, ClientThreadHandler cth) {
		clientUDP = new UDPEchoClient(ip);
		this.cth = cth;
	}

	/**
	 * If you have an important message then it will be sent with tcp
	 */
	public String sendRequest(String request) {

		String response = clientUDP.sendRequest(request);
		
		if (response != null && response.equals(Main.END_ALL_CLIENT_STRING)) {
			System.err.println("ENDING ClientThreadHandler: " + request);
			cth.stopAll();
			cth.end();
		}

		return response;
	}
}
