package client;

import engine.utils.Timer;

public class ClientThreadHandler {

	private ClientController client;
	private int id;

	private Thread clientThread;

	private String lobbyString = "-1#LOADING";
	private String raceLobbyString = "LOADING";
	private int raceLights = 0;

	private boolean running;
	private boolean pingRunning;
	private boolean lobbyRunning;
	private boolean raceLobbyRunning;
	private boolean raceLightsRunning;

	public ClientThreadHandler(int id) {

		this.id = id;

		clientThread = new Thread(() -> {
			long lastTime = System.nanoTime();
			double amountOfTicks = Timer.TARGET_TPS;
			double nsp = 1000000000 / (amountOfTicks / 5.0);
			double deltap = 0;

			while (running) {
				long now = System.nanoTime();
				deltap += (now - lastTime) / nsp;
				lastTime = now;
				// Ping
				while (deltap >= 1) {

//					System.out.println(
//							pingRunning + "," + lobbyRunning + "," + raceLobbyRunning + "," + raceLightsRunning);
					String response;
					if (this.pingRunning) {
						response = client.sendRequest("#" + this.id);
						
					}
					if (this.lobbyRunning) {
						response = client.sendRequest("UL#" + this.id);
						lobbyString = response;
					}
					if (this.raceLobbyRunning) {
						response = client.sendRequest("UR#" + this.id);
						raceLobbyString = response;
					}
					if (this.raceLightsRunning) {
						response = client.sendRequest("RL#" + this.id);
						raceLights = Integer.valueOf(response);
					}
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
		raceLights = 0;
	}

	public void stopRaceLights() {
		raceLightsRunning = false;
		raceLights = 0;
	}

	public String getLobbyString() {
		return lobbyString;
	}

	public String getRaceLobbyString() {
		String res = raceLobbyString;
		raceLobbyString = null;
		return res;
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

	public void setClient(ClientController client) {
		this.client = client;
	}
}
