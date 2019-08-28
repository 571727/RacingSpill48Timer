package handlers;

import client.EchoClient;
import scenes.Race;

public class ClientThreadHandler {

	private EchoClient client;
	private int id;

	private Thread clientThread;

	private String lobbyString = "-1#LOADING";
	private String raceLobbyString = "LOADING";
	private int raceLights = -1;

	private boolean running;
	private boolean pingRunning;
	private boolean lobbyRunning;
	private boolean raceLobbyRunning;
	private boolean raceLightsRunning;

	public ClientThreadHandler(EchoClient client, int id) {

		this.client = client;
		this.id = id;

		clientThread = new Thread(() -> {
			long lastTime = System.nanoTime();
			double amountOfTicks = Race.TICK_STD;
			double nsp = 1000000000 / (amountOfTicks / 5.0);
			double deltap = 0;

			while (running) {
				long now = System.nanoTime();
				deltap += (now - lastTime) / nsp;
				lastTime = now;
				// Ping
				while (deltap >= 1) {

					if (this.pingRunning)
						client.sendRequest("#" + this.id);
					if (this.lobbyRunning)
						lobbyString = client.sendRequest("UL#" + this.id);
					if (this.raceLobbyRunning)
						raceLobbyString = client.sendRequest("UR#" + this.id);
					if (this.raceLightsRunning)
						raceLights = Integer.valueOf(client.sendRequest("RL#" + this.id));
					deltap--;
				}

			}
		});

	}

	public void startPing() {
		pingRunning = true;
	}

	public void stopPing() {
		pingRunning = false;
	}

	public void startLobby() {
		lobbyRunning = true;
	}

	public void stopLobby() {
		lobbyRunning = false;
	}

	public void startRaceLobby() {
		raceLobbyRunning = true;
	}

	public void stopRaceLobby() {
		raceLobbyRunning = false;
	}

	public void startRaceLights() {
		raceLightsRunning = true;
	}

	public void stopRaceLights() {
		raceLightsRunning = false;
	}

	public String getLobbyString() {
		return lobbyString;
	}

	public String getRaceLobbyString() {
		return raceLobbyString;
	}

	public int getRaceLights() {
		return raceLights;
	}

	public void setID(int id) {
		this.id = id;
	}

	public void stopAll() {
		pingRunning = false;
		lobbyRunning = false;
		raceLobbyRunning = false;
		raceLightsRunning = false;
	}

	public void end() {
		running = false;
		try {
			clientThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void start() throws Exception {
		if (id != -1) {
			running = true;
			clientThread.start();
		} else {
			throw new Exception();
		}
	}
}
