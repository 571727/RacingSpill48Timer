package server;

import java.io.IOException;
import java.net.ServerSocket;

import connection_standard.Config;

public class EchoServer {

	public EchoServer() {
		int serverport = Config.SERVERPORT;
		
		System.out.println("TCP server starting at port " + serverport);
		
		try (ServerSocket welcomeSocket = new ServerSocket(serverport)){
			
			TCPEchoServer server = new TCPEchoServer(welcomeSocket);
			
			while(true) {
				
				server.process();
				
			}
		} catch(IOException e) {
			System.out.println("TCP server: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
}
