package server;

import java.io.IOException;
import java.net.ServerSocket;

import server.connection_standard.Config;
/**
 * Passer på lokal server. Kan bare brukes med en client.
 * @author jonah
 *
 */
public class ServerSocketHandler implements Runnable {

	private int serverport;
	private boolean running;
	private ServerSocket serverSocket;
	private ServerInfo info;
	
	public ServerSocketHandler(ServerInfo info) {
		serverport = Config.SERVERPORT;
		this.info = info;
		System.out.println("TCP server starting at port " + serverport);
		
	}

	@Override
	public void run() {
		running = true;
		
		try  {
			serverSocket = new ServerSocket(serverport);

			while (running) {
				new TCPEchoServer(serverSocket.accept(), info).start();
			}
		} catch (IOException e) {
			System.err.println("TCP server: " + e.getMessage());
		}

		try {
			serverSocket.close();
		} catch (IOException e1) {
			e1.printStackTrace();
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
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
