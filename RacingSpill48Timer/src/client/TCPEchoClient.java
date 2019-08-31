package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import connection_standard.Config;
import handlers.ClientThreadHandler;
import handlers.SceneHandler;
import startup.Main;

public class TCPEchoClient {
	
	private String ip;
	private int port;
	
	public TCPEchoClient() {
		this.ip = Config.SERVER;
		this.port = Config.SERVERPORT;
	}
	
	public TCPEchoClient(String ip) {
		this.ip = ip;
		this.port = Config.SERVERPORT;
	}
	
	public String sendRequest (String text) {
		
		if(text == null)
			return null;
		
		String outtext = null;
		
		try {
			
			Socket clientSocket = new Socket(ip, port);
			
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
			text = text + "\n"; // implementation is simpler with readLine
			
			outToServer.write(text.getBytes());
			
			outtext = inFromServer.readLine();
			
			outToServer.close();
			inFromServer.close();
			
			clientSocket.close();
			
			
		} catch (IOException ex) {
			System.err.println("ERROR IN ECHOCLIENT:\nSENT " + text);
			System.err.println("IN " + outtext);
			System.err.println("TCP client: " + ex.getMessage());
			
			SceneHandler.instance.changeScene(0);
			
			outtext = Main.END_ALL_CLIENT_STRING;
		}

		
		return outtext;
		
	}
	
}
