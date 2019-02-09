package handlers;

import server.EchoServer;

/**
 * Handles which server is running at this computer. Only run one at a time. Also controls what is sent in and out.
 * @author jonah
 *
 */

public class ServerHandler {

	EchoServer currentServer;
	
	public ServerHandler() {
		
	}
	
	public ServerHandler(EchoServer currentServer) {
		this.currentServer = currentServer;
	}
	
}
