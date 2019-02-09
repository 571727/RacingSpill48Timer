package server;

import java.io.IOException;
import java.net.ServerSocket;

import connection_standard.Config;
/**
 * Passer på lokal server. Kan bare brukes med en client.
 * @author jonah
 *
 */
public class EchoServer implements Runnable {

	private int serverport;
	private boolean running;
	private ServerSocket welcomeSocket;
	private ServerInfo info;
	
	public EchoServer(ServerInfo info) {
		serverport = Config.SERVERPORT;
		this.info = info;
		System.out.println("TCP server starting at port " + serverport);
		
	}

	@Override
	public void run() {
		running = true;
		
		try  {
			welcomeSocket = new ServerSocket(serverport);
			TCPEchoServer server = new TCPEchoServer(welcomeSocket, info);

			while (running) {

				server.process();

			}
		} catch (IOException e) {
			System.out.println("TCP server: " + e.getMessage());
			e.printStackTrace();
		}
	}

	
	
	/*
	 * Getters and setters
	 */
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		
		this.running = running;
		
		if(running == false) {
			try {
				welcomeSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
