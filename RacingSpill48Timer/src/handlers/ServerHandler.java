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
	
	public ServerHandler() {

	}

	public ServerHandler(EchoServer currentServer) {
		createNew(currentServer);
	}

	public void createNew() {
		info = new ServerInfo();
		createNew(new EchoServer(info));
	}

	public void createNew(EchoServer newServer) {
		if(currentServer != null && currentServer.isRunning())
			join();

		currentServer = newServer;
		
		thread = new Thread(currentServer);
		thread.start();
	}

	
	/**
	 * close tha program yo
	 */
	public void join() {
		try {
			if (thread.isAlive()) {
				
				currentServer.setRunning(false);
				thread.join();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Joined");
	}

}
