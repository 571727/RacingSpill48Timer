package server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

/**
 * Holds info about who is a part of this game. Also holds info about the cars
 * when racing.
 * 
 * @author jonah
 *
 */

public class ServerInfo {

	private HashMap<String, PlayerInfo> players;
	private int started;
	private int amountFinished;
	private int length;
	private int races;
	private Random r;

	public ServerInfo() {
		players = new HashMap<String, PlayerInfo>();
		r = new Random();
	}

	public int getStarted() {
		return started;
	}

	public void setStarted(int started) {
		this.started = started;
	}

	/**
	 * input 1 = name input 2 = id input 3 = host boolean input 4 = carname
	 */

	public String joinLobby(String[] input) {

		PlayerInfo newPlayer = new PlayerInfo(input[1], input[3], input[4]);

		players.put(input[1] + input[2], newPlayer);

		return updateLobby();
	}

	/**
	 * @return name#ready#car#...
	 */
	public String updateLobby() {
		String result = "";

		for (Entry<String, PlayerInfo> entry : players.entrySet()) {
			result += "#" + entry.getValue().getLobbyInfo() + "#" + started;
		}

		return result;
	}

	/**
	 * input 1 = name input 2 = id input 3 = sitsh
	 * 
	 * @return name#ready#car#...
	 */
	public String updateLobby(String[] input) {

		PlayerInfo player = players.get(input[1] + input[2]);
		if (player == null) {
			return null;
		}
		player.updateLobby(input);

		return updateLobby();
	}

	/**
	 * input[2] -> 1 = race started. 0 = race ready to start
	 */
	public void startRace(String[] input) {
		if (Integer.valueOf(input[1]) == 1) {
			if (Integer.valueOf(input[2]) == 1) {
				races--;
				
				for (Entry<String, PlayerInfo> entry : players.entrySet()) {
					entry.getValue().newRace();
				}
				length = randomizeConfiguration();
			} else {
				amountFinished = 0;
				length = 0;
			}
			started = Integer.valueOf(input[2]);
		}
	}

	/**
	 * Creates a new racetrack somewhere in the world and with some length of some
	 * type.
	 * 
	 * @return length of the track
	 */
	public int randomizeConfiguration() {
		return 500 * (r.nextInt(4) + 1);
	}

	public void leave(String[] input) {
		players.remove(input[1] + input[2]);
	}

	public String getTrackLength() {
		return String.valueOf(length);
	}
	
	/**
	 * UPDATERACE#name#id#finished(0-1)#longtimemillis
	 * 
	 * Første gang får alle 10 andre gang får ingen poeng?
	 */
	public String updateRace(String[] input) {

		PlayerInfo player = players.get(input[1] + input[2]);

		// If racing, finished and is first time telling that it has finished
		if (started == 1 && Integer.valueOf(input[3]) == 1 && player.getFinished() != 1) {
			
			if(Long.valueOf(input[4]) != -1) {
				
				int amountFaster = 0;
				
				for (Entry<String, PlayerInfo> entry : players.entrySet()) {
					if(entry.getValue().getFinished() == 1 && entry.getValue().getTime() < Long.valueOf(input[4])) {
						amountFaster++;
					}
				}
				
				player.addPointsAndMoney(players.size(), amountFaster);
			}
			else
				player.addPointsAndMoney(-1, -1);
			amountFinished++;
		}

		player.updateRaceResults(input);

		return updateRaceLobby();
	}

	/**
	 * @return name#ready#car#...
	 */
	public String updateRaceLobby() {
		String result = "";

		for (Entry<String, PlayerInfo> entry : players.entrySet()) {
			result += "#" + entry.getValue().getRaceInfo();
		}

		return result;
	}
	
	public void setPointsMoney(String[] input) {
		PlayerInfo player = players.get(input[1] + input[2]);
		player.setPoints(Integer.valueOf(input[3]));
		player.setMoney(Integer.valueOf(input[4]));
	}

	public String getPointsMoney(String[] input) {
		PlayerInfo player = players.get(input[1] + input[2]);
		
		return player.getPoints() + "#" + player.getMoney();
	}

	public void newRaces() {
		races = 9;
	}

	public String getRacesLeft() {
		return String.valueOf(races);
	}
	
	public String getPlayerWithMostPoints() {
		PlayerInfo winner = null;
		
		for (Entry<String, PlayerInfo> entry : players.entrySet()) {
			if(winner == null || entry.getValue().getPoints() > winner.getPoints()) {
				winner = entry.getValue();
			}
		}
		
		return winner.getName() + ", " + winner.getCarName();
	}

}
