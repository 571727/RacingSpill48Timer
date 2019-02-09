package client;

import javax.swing.JOptionPane;

import connection_standard.Config;

public class EchoClient {

	public EchoClient() {
		this(Config.SERVER);
	}
	
	public EchoClient(String ip) {
		
		int serverport = Config.SERVERPORT;
		
		TCPEchoClient tcpclient = new TCPEchoClient(ip, serverport);
		
		System.out.println("TCP client started: " + ip + " #");
		//Connect først og si at du er med.
		
		//Kjør ut og inn her.
		String text = JOptionPane.showInputDialog(null, "Message to transform");

		if (text != null) {
			
			text = tcpclient.convert(text);
			JOptionPane.showMessageDialog(null, text);
		}
	}
	
	
}
