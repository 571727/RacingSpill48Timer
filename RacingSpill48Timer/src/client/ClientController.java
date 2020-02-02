package client;

import main.Main;

public class ClientController {

	private Client clientTCP, clientUDP;
	private ClientThreadHandler cth;

	public ClientController(String ip, ClientThreadHandler cth) {
		clientTCP = new TCPEchoClient(ip);
		clientUDP = new UDPEchoClient(ip);
		this.cth = cth;
	}

	/**
	 * If you have an important message then it will be sent with tcp
	 */
	public String sendRequest(String request) {

		String response = clientTCP.sendRequest(request);
		
//		@SuppressWarnings("preview")
//		String response = switch (request.charAt(0)) {
//		case '!' -> this.clientTCP.sendRequest(request);
//		default -> this.clientUDP.sendRequest(request);
//		};

		if (response != null && response.equals(Main.END_ALL_CLIENT_STRING)) {
			System.err.println("ENDING ClientThreadHandler: " + request);
			cth.stopAll();
			cth.end();
		}

		return response;
	}
}
