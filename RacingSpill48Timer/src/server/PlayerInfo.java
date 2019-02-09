package server;

public class PlayerInfo {

	int ready;
	int host;
	String name;
	String time;
	int points;

	public PlayerInfo(String name, String host) {
		this.name = name;
		this.host = Integer.valueOf(host);
	}

	/**
	 * fra og med input[2]
	 * 
	 * @param input
	 */
	public void updateLobby(String[] input) {
		ready = Integer.parseInt(input[2]);
	}

	/**
	 * @return name#ready#host#points
	 */
	public String getLobbyInfo() {
		return name + "#" + ready + "#" + host + "#" + points;
	}

	/**
	 * @return name#ready#car#...
	 */
	public String getRaceInfo() {
		return name;
	}

}
