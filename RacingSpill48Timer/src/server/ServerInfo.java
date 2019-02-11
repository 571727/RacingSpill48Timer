package server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Holds info about who is a part of this game. Also holds info about the cars when racing.
 * @author jonah
 *
 */

public class ServerInfo {

	private HashMap<String, PlayerInfo> players;
	private int started;
	
	public ServerInfo() {
		players = new HashMap<String, PlayerInfo>();
	}
	
	public int getStarted() {
		return started;
	}

	public void setStarted(int started) {
		this.started = started;
	}

	/**
	 * input 1 = name 
	 * input 2 = id
	 * input 3 = host boolean
	 * input 4 = carname
	 */
	
	public String joinLobby(String[] input) {
		
		PlayerInfo newPlayer = new PlayerInfo(input[1], input[3], input[4]);
		
		players.put(input[1] + input[2], newPlayer);
		
		return updateLobby(newPlayer);
	}
	
	
	/**
	 * @return name#ready#car#... 
	 */
	public String updateLobby(PlayerInfo player) {
		String result = "";
		
		for (Entry<String, PlayerInfo> entry : players.entrySet()) 
		{ 
			result += "#" + entry.getValue().getLobbyInfo() + "#" + started;
		}
		
		return result;
	}
	
	/**
	 * input 1 = name
	 * input 2 =  id
	 * input 3 = sitsh
	 * 
	 * @return name#ready#car#... 
	 */
	public String updateLobby(String[] input) {
		
		PlayerInfo player = players.get(input[1] + input[2]);
		player.updateLobby(input);
		
		return updateLobby(player);
	}
	
	public void startRace(String[] input) {
		if(Integer.valueOf(input[1]) == 1) {
			started = Integer.valueOf(input[2]);
		}
	}
	
	public void leave(String[] input) {
		players.remove(input[1] + input[2]);
	}

	public String updateRace(String[] input) {
		return null;
	}
	
}
