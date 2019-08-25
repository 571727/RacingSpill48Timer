package handlers;

public class ClientThreadHandler {

	private Thread pingThread;
	private Thread lobbyThread;
	private Thread raceLobbyThread;
	private Thread raceLightsThread;
	private String lobbyString;
	private String raceLobbyString;
	private int raceLights;

	public ClientThreadHandler() {
		// TODO make threads to run when started / end when stopped - so that requesting
		// stuff from server isnt from the same thread that is also running the race
	}

}
