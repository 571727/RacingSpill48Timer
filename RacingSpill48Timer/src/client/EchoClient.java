package client;

import javax.swing.JOptionPane;

import connection_standard.Config;
import no.hvl.dat110.tcpexample.system.Configuration;

public class EchoClient {

	public EchoClient() {
		this(Config.SERVER);
	}
	
	public EchoClient(String ip) {
		
		int serverport = Config.SERVERPORT;
		
		TCPEchoClient tcpclient = new TCPEchoClient(ip, serverport);
		
		System.out.println("TCP client started: " + ip + " #");
		
		
		//Kj�r ut og inn her.
		String text = JOptionPane.showInputDialog(null, "Message to transform");

		if (text != null) {
			
			text = tcpclient.convert(text);
			JOptionPane.showMessageDialog(null, text);
		}
	}
	
	
}
