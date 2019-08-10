package handlers;

import server.EchoServer;
import server.ServerInfo;

/**
 * Handles which server is running at this computer. Only run one at a time.
 * Also controls what is sent in and out.
 * 
 * @author jonah
 *
 */
public class ServerHandler {

	private Thread thread;
	private EchoServer currentServer;
	private ServerInfo info;

	private Thread infoThread;
	public ServerHandler() {

	}

	public ServerHandler(EchoServer currentServer) {
		createNew(currentServer);
	}

	public void createNew(int amountOfAI, int diff) {
		info = new ServerInfo(amountOfAI, diff);
		createNew(new EchoServer(info));
		infoThread = new Thread(info);
		infoThread.start();
	}

	public void createNew(EchoServer newServer) {
		if(currentServer != null && currentServer.isRunning())
			close();

		currentServer = newServer;
		
		thread = new Thread(currentServer);
		thread.start();
	}

	
	/**
	 * close tha program yo
	 */
	public void close() {
		try {
			if (thread.isAlive()) {
				
				info.setRaceOver();
				infoThread.join();
				currentServer.setRunning(false);
				thread.join();
				info = null;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Joined");
	}

}
