package server;

/**
 * Handles which server is running at this computer. Only run one at a time.
 * Also controls what is sent in and out.
 * 
 * @author jonah
 *
 */
public class ServerHandler {

	private Thread thread;
	private ServerSocketHandler currentServer;
	private ServerInfo info;

	private Thread infoThread;
	public ServerHandler() {

	}

	public ServerHandler(ServerSocketHandler currentServer) {
		createNew(currentServer);
	}

	public void createNew(int amountOfAI, int diff, String gamemode) {
		info = new ServerInfo(amountOfAI, diff, gamemode);
		createNew(new ServerSocketHandler(info));
		infoThread = new Thread(info);
		infoThread.start();
	}

	public void createNew(ServerSocketHandler newServer) {
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
				
				info.endGame();
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
