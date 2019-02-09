package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class TCPEchoServer {

	private ServerSocket welcomeSocket;
	
	public TCPEchoServer(ServerSocket welcomeSocket) {
		this.welcomeSocket = welcomeSocket;
	}

	public void process() {
		
		
		try {
			System.out.println("SERVER ACCEPTING");
			
			Socket connectionSocket = welcomeSocket.accept();
			
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
		
			String text = inFromClient.readLine();
			
			System.out.println("SERVER RECEIVED: " + text);
			int n = 0;
			do {
				
				String outtext = JOptionPane.showInputDialog("Hva vil du skrive tilbake?");

				System.out.println("SERVER SENDING: " + outtext);

				outToClient.write(outtext.getBytes());
				outToClient.flush();

				n = JOptionPane.showConfirmDialog(null, "Vil du sende en melding til?");
				
			} while( n == 0);
		
			outToClient.close();
			inFromClient.close();

			connectionSocket.close();
			
		} catch (IOException e) {

			System.out.println("TCPServer: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);

		}
	}

}
