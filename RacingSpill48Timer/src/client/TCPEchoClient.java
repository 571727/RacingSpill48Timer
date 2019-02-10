package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;

import handlers.SceneHandler;

public class TCPEchoClient {
	
	private String ip;
	private int port;
	
	public TCPEchoClient(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	public String convert (String text) {
		
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
			System.err.println("TCP client: " + ex.getMessage());
			SceneHandler.instance.changeScene(0);
			
		}
		
		return outtext;
		
	}
	
}
