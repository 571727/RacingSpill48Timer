package server;

public class PlayerInfo {

	private int ready;
	private int host;
	private String name;
	private String time;
	private int finished;
	private int points;
	private String carName;

	public PlayerInfo(String name, String host, String carName) {
		this.name = name;
		this.host = Integer.valueOf(host);
		this.carName = carName;
	}

	/**
	 * fra og med input[3]
	 * 
	 * @param input
	 */
	public void updateLobby(String[] input) {
		ready = Integer.parseInt(input[3]);
	}

	/**
	 * @return name#ready#host#points
	 */
	public String getLobbyInfo() {
		return name + "#" + ready + "#" + host + "#" + carName + "#" + points;
	}
	
	public void newRace() {
		finished = 0;
	}
	
	/**
	 * fra og med input[3]
	 * 
	 * @param input
	 */
	public void updateRaceResults(String[] input) {
		
	}
	
	public String getRaceResults() {
		return name + "#" + time;
	}

	/**
	 * @return name#ready#car#...
	 */
	public String getRaceInfo() {
		return name;
	}

}
