package server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import handlers.SceneHandler;
import scenes.FixCar;
import scenes.Lobby;
import scenes.Options;
import startup.Main;

/**
 * Holds info about who is a part of this game. Also holds info about the cars
 * when racing.
 * 
 * @author jonah
 *
 */

public class ServerInfo implements Runnable {

	private HashMap<String, PlayerInfo> players;
	private HashMap<String, Long> ping;
	private int started;
	private int amountFinished;
	private int length;
	private int races;
	private int raceLights;
	private long raceStartedTime;
	private final long waitTime = 1000;
	private long regulatingWaitTime = -1;
	private boolean running = true;
	private boolean greenLights;
	private boolean allFinished;
	private Random r;
	private int amountInTheRace;

	public ServerInfo() {
		players = new HashMap<String, PlayerInfo>();
		ping = new HashMap<String, Long>();
		r = new Random();
		races = -1;
		setRunning(true);
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

		PlayerInfo newPlayer = new PlayerInfo(input[1], input[2], input[3], input[4]);

		players.put(input[1] + input[2], newPlayer);
		ping.put(input[1] + input[2], System.currentTimeMillis());

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

	public void finishPlayer(String[] input) {
		PlayerInfo player = players.get(input[1] + input[2]);
		player.setFinished(1);
		amountFinished++;

		if (greenLights) {
			player.setTime(System.currentTimeMillis() - raceStartedTime);
		} else {
			player.setTime(-1);
			if (player.isIn() == false)
				inTheRace(input);
		}

		this.allFinished = amountFinished == players.size();
	}

	private void updateRaceStatus() {

		if (started != 1)
			return;

		greenLights = updateRaceLights();
		// Update time per player

	}

	private boolean updateRaceLights() {
		// Racelights green
		if (raceLights == 4)
			return true;

		if (amountInTheRace == players.size()) {
			// Wait for 3 secounds before the race starts && wait for each racelight
			if (raceStartedTime + regulatingWaitTime < System.currentTimeMillis()) {
				regulatingWaitTime = waitTime - 300 + r.nextInt(1200);
				raceStartedTime = System.currentTimeMillis();
				raceLights++;
			}
		} else {
			raceStartedTime = System.currentTimeMillis();
		}

		// Racelights red
		return false;

	}

	public String getRaceLightsStatus() {
		return String.valueOf(raceLights);
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
				raceStartedTime = System.currentTimeMillis();
				regulatingWaitTime = waitTime * 3;
			} else {
				amountInTheRace = 0;
				amountFinished = 0;
				length = 0;
			}
			started = Integer.valueOf(input[2]);
			raceLights = 0;
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
		leave(input[1] + input[2]);
	}

	private void leave(String nameid) {
		players.remove(nameid);
	}

	public String getTrackLength() {
		return String.valueOf(length);
	}

	public void inTheRace(String[] input) {
		players.get(input[1] + input[2]).setIn(true);
		amountInTheRace++;
	}

	/**
	 * UPDATERACE#name#id#finished(0-1)#longtimemillis
	 * 
	 * F�rste gang f�r alle 10 andre gang f�r ingen poeng?
	 */
	public String updateRace() {

		// If racing, finished and is first time telling that it has finished
		if (allFinished) {

			for (Entry<String, PlayerInfo> entry : players.entrySet()) {
				PlayerInfo player = entry.getValue();
				int place = 0;
				long thisTime = player.getTime();

				if (thisTime == -1) {

					player.addPointsAndMoney(-1, -1);

				} else {

					for (Entry<String, PlayerInfo> otherEntry : players.entrySet()) {

						if (otherEntry.getKey() != entry.getKey()) {

							long otherTime = otherEntry.getValue().getTime();
							if (thisTime > otherTime) {
								place++;
							}
						}
					}

					player.addPointsAndMoney(players.size(), place);
				}
			}

			allFinished = false;
		}

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
		String res = null;
		try {
			res = player.getPoints() + "#" + player.getMoney();
		} catch (NullPointerException e) {
			System.err.println("Player " + input[1] + input[2] + " timed out");
			e.printStackTrace();
		}
		return res;
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
			if (winner == null || entry.getValue().getPoints() > winner.getPoints()) {
				winner = entry.getValue();
			}
		}

		return winner.getName() + ", " + winner.getCarName();
	}

	public void ping(String[] input) {
		ping.put(input[1] + input[2], System.currentTimeMillis());
	}

	public boolean validPing(long ping) {
		return ping > System.currentTimeMillis() - 5000;
	}

	public void checkPings() {
		for (Entry<String, Long> entry : ping.entrySet()) {

			if (!validPing(entry.getValue()))
				leave(entry.getKey());
		}
	}

	@Override
	public void run() {

		long lastTime = System.nanoTime();
		double amountOfTicks = 5.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;

		while (races != 0 && isRunning()) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				if (!Main.DEBUG)
					checkPings();
				updateRaceStatus();
				delta--;
			}

		}
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

}
