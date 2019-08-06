package client;

import connection_standard.Config;

public class EchoClient {

	private TCPEchoClient tcpclient;
	
	public EchoClient() {
		this(Config.SERVER);
	}
	
	public EchoClient(String ip) {
		
		int serverport = Config.SERVERPORT;
		tcpclient = new TCPEchoClient(ip, serverport);
		
	}
	
	public String sendRequest(String text) {
		
		
//		System.out.println("TCP client started: " + ip + " #");
		//Connect f�rst og si at du er med.
		
		//Kj�r ut og inn her.

		if (text != null) {
			
			text = tcpclient.convert(text);
			return text;
		}
		return null;
	}
	
	
}
