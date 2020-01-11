package server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import elem.AI;
import game_modes.GameMode;
import main.Main;
import player_local.PlayerInfo;
import player_local.car.Car;
import scenes.upgrade.Upgrades;

/**
 * Holds info about who is a part of this game. Also holds info about the cars
 * when racing.
 * 
 * TODO Things to do from here: - Shifting delay - Minigame while ready - Tier
 * upgrades - High Gear bonus: sequential shifting - LWJGL drawing of game -
 * Finish gamemodes
 * 
 * 
 * @author jonah
 *
 */

public class ServerInfo implements Runnable {
	private HashMap<Byte, PlayerInfo> players;
	private ArrayList<AI> ai;

	private HashMap<Long, PlayerInfo> lostPlayers;
	private ConcurrentHashMap<Byte, Long> ping;
	private HashMap<PlayerInfo, Queue<String>> chat;
	private int raceLights;
	private boolean running;
	private boolean greenLights;
	private String raceLobbyString;
	private String[] places;
	private int[] upgradePrices;
	private Random r;
	private GameMode gm;
	private byte nextID;
	private boolean raceLobbyStringFinalized;
	private List<PlayerInfo> sortedPlayers;

	public ServerInfo(int amountOfAI, int diff, String gamemode) {
		players = new HashMap<Byte, PlayerInfo>();
		ping = new ConcurrentHashMap<Byte, Long>();
		chat = new HashMap<PlayerInfo, Queue<String>>();
		lostPlayers = new HashMap<Long, PlayerInfo>();
		ai = new ArrayList<AI>();
		running = true;

		places = new String[4];
		places[0] = "Japan";
		places[1] = "America";
		places[2] = "Britain";
		places[3] = "Germany";

		r = new Random();
		setRunning(true);

		upgradePrices = new int[Upgrades.UPGRADE_NAMES.length];

		for (int i = 0; i < upgradePrices.length; i++) {
			upgradePrices[i] = 30 + r.nextInt(60);
		}

		ArrayList<String> names = new ArrayList<String>();
		for (String name : Main.AI_NAMES) {
			names.add(name);
		}

		for (int i = 0; i < amountOfAI; i++) {
			int nameIndex = r.nextInt(names.size());
			AI ai = new AI(names.get(nameIndex), (byte) i, diff, upgradePrices);
			names.remove(nameIndex);
			this.ai.add(ai);
			players.put(generateID(), ai);
		}

		for (int i = 0; i < Main.GAME_MODES.length; i++) {
			if (Main.GAME_MODES[i].getName().equals(gamemode))
				try {
					gm = Main.GAME_MODES[i].getClass().newInstance();
					break;
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
		}

		gm.init(players, ai, places, r);

	}

	public void endGame() {
		gm.endGame();
	}

	private byte generateID() {
		return nextID++;
	}

	private long generateDisconnectID(PlayerInfo player) {
		long id = Math.abs(r.nextLong());
		player.setDiscID(id);
		return id;
	}

	public int getStarted() {
		return gm.getStarted();
	}

	public void setStarted(int started) {
		gm.setStarted(started);
	}

	/**
	 * input 1 = name input 2 = host boolean input 3 = disconnectedid input 4 = gameversion
	 * 
	 */

	public String joinLobby(String[] input) {

		PlayerInfo newPlayer = null;
		boolean jump = false;
		int copyCar = 0;
		long discID = Long.valueOf(input[3]);
		
		//On a different version
		if(input.length < 5 || !input[4].equals(Main.GAME_VERSION)) {
			return Main.END_ALL_CLIENT_STRING;
		}

		// Have key?
		if (lostPlayers.containsKey(discID)) {
			newPlayer = lostPlayers.remove(discID);

			newPlayer.setName(input[1]);
			
			players.put(newPlayer.getID(), newPlayer);
			ping.put(newPlayer.getID(), System.currentTimeMillis());
			chat.put(newPlayer, new ConcurrentLinkedQueue<String>());
			copyCar = 1;
			jump = true;
		}

		// Joined, but is still in? Perhaps lost connection and connected before server
		// noticed?
		if (!jump) {
			for (Entry<Byte, PlayerInfo> entry : players.entrySet()) {
				if (entry.getValue().getDisconnectID() != null && entry.getValue().getDisconnectID() == discID) {
					newPlayer = entry.getValue();
					copyCar = 1;
					jump = true;
				}
			}
		}

		// Add new player to club
		if (!jump) {
			newPlayer = new PlayerInfo(input[1], generateID(), Byte.valueOf(input[2]));

			players.put(newPlayer.getID(), newPlayer);
			ping.put(newPlayer.getID(), System.currentTimeMillis());
			chat.put(newPlayer, new ConcurrentLinkedQueue<String>());
		}

		addChat(newPlayer.getName() + " joined the game.");

		//Respond with new id, discID, old car if lost.
		String res = newPlayer.getID() + "#" + generateDisconnectID(newPlayer) + "#" + copyCar
				+ ((copyCar == 1)
						? "#" + newPlayer.getCar().getRep().getCloneString()
						: "");
		
		updateSortedPlayers();
		return res;
	}

	public void updateCarForPlayer(String[] input) {
		PlayerInfo player = getPlayer(input);
		int from = 2;
		if (player.getCar() != null) {
			player.getCar().getRep().setClone(input, from);
		} else {
			player.setCar(new Car(input, from));
		}
	}

	/**
	 * @return name#ready#car#...
	 */
	public String updateLobby(PlayerInfo player) {
		String result = getPlacePodiumString(player);

		for (int i = 0; i < sortedPlayers.size(); i++) {
			PlayerInfo entry = sortedPlayers.get(i);
			result += "#" + entry.getLobbyInfo() + "#"
					+ (ping.containsKey(entry.getID()) ? getPing(entry) : ":-) ") + "#" + gm.getStarted()
					+ "#" + entry.getCarInfo();
		}

		return result;
	}

	private String getPlacePodiumString(PlayerInfo player) {
		return String.valueOf(getPlacePodium(player));
	}

	private int getPlacePodium(PlayerInfo player) {
		int place = 0;
		for (Entry<Byte, PlayerInfo> otherEntry : players.entrySet()) {

			if (otherEntry.getValue() != player) {

				int otherPoints = otherEntry.getValue().getPoints();
				if (player.getPoints() < otherPoints) {
					place++;
				}
			}
		}
		player.setPodium(place);
		return place;
	}

	private int getPointsAhead(PlayerInfo player) {
		int pointDifference = -10000;
		for (Entry<Byte, PlayerInfo> otherEntry : players.entrySet()) {

			if (otherEntry.getValue() != player) {

				int otherPoints = otherEntry.getValue().getPoints();
				int tempDifference = player.getPoints() - otherPoints;
				if (tempDifference > pointDifference) {
					pointDifference = tempDifference;
				}
			}
		}
		return pointDifference;
	}

	/**
	 * input 1 = name input 2 = id input 3 = sitsh
	 * 
	 * @return name#ready#car#...
	 */
	public String updateLobby(String[] input) {

		PlayerInfo player = getPlayer(input);
		if (player == null) {
			return null;
		}

		return updateLobby(player);
	}

	public void updateReady(String[] input) {
		PlayerInfo player = getPlayer(input);
		if (player == null) {
			return;
		}
		player.setReady(Byte.valueOf(input[2]));
	}

	private PlayerInfo getPlayer(String[] input) {
		return getPlayer(Byte.valueOf(input[1]));
	}

	private PlayerInfo getPlayer(byte id) {
		return players.get(id);
	}

	public String getPrices() {
		String res = String.valueOf(upgradePrices[0]);

		for (int i = 0; i < upgradePrices.length; i++) {
			res += "#" + upgradePrices[i];
		}

		return res;
	}

	public void finishPlayer(String[] input) {
		PlayerInfo player = getPlayer(input);
		if (player == null) {
			return;
		}
		player.setFinished(1);

//		System.out.println(greenLights);

		if (greenLights) {
			player.setTime(Long.valueOf(input[2]));
		} else {
			player.setTime(-1);
			if (player.isIn() == false)
				inTheRace(input);
		}

		gm.anotherPlayerFinished();
		finishControl();
		updateSortedPlayers();
	}
	
	private void updateSortedPlayers() {
		sortedPlayers = new LinkedList<PlayerInfo>();

		// Sorter alle spillere etter alle har fullført racet
		sortedPlayers.addAll(players.values());
		Collections.sort(sortedPlayers, new Comparator<PlayerInfo>() {
			@Override
			public int compare(PlayerInfo o1, PlayerInfo o2) {

				int result = 0;
				if (o1.getBank().getPoints() < o2.getBank().getPoints()) {
					result = 1;

				} else if (o1.getBank().getPoints() > o2.getBank().getPoints()) {
					result = -1;
				}

				return result;
			}
		});
	}

	private void updateRaceStatus() {

//		if (started != 1)
//			return;

		greenLights = updateRaceLights();
		// Update time per player

	}

	private boolean updateRaceLights() {
		// Racelights green
		if (raceLights == 4)
			return true;

		// Everyone in the race
		if (gm.everyoneInRace()) {
			gm.setStarted(0);
			// Wait for 3 secounds before the race starts && wait for each racelight
			if (gm.waitTimeRaceLights()) {
				raceLights++;
			}
		} else {
			gm.resetWaitTimeRaceLights();
		}

		// Racelights red
		return false;

	}

	public String getRaceLightsStatus() {
		return String.valueOf(raceLights);
	}

	public void startStopRace(String[] input) {
		int values = Integer.parseInt(input[2]);

		// host? first number
		if (values >= 10) {
			// second number -> 1 = race started. 0 = race ready to start
			values = values % 10;
			raceLights = 0;
			if (values == 1) {
				gm.startNewRace();
				raceLobbyStringFinalized = false;
				System.err.println("RACE STARTED");
			} else {
				gm.stopRace();
				System.err.println("RACE STOPPED");
			}

			gm.setStarted(values);
			if (!gm.isRacing())
				gm.setRacing(values == 1);
		}
	}

	public void leave(String[] input) {
		leave(getPlayer(input));
	}

	private void leave(byte nameID) {
		leave(getPlayer(nameID));
	}

	/**
	 * 
	 * Removes player from the game, but FIXME saves their state in case they want
	 * to rejoin the game
	 * 
	 */
	@SuppressWarnings("unused")
	private void leave(PlayerInfo player) {
		String output = "Removing player: " + player.getName();

		if (player != null) {
			boolean s = gm.isRacing();

			if (s) {
				gm.rewardPlayer(-1, -1, -1, player);
				gm.disconnectedFinish();
				finishControl();
			}

			chat.remove(player);
			players.remove(player.getID());
			ping.remove(player.getID());
			addChat(player.getName() + " left the game.");
			lostPlayers.put(player.getDisconnectID(), player);
			output += " SUCCESS";
		} else {
			output += " FAILED";
		}
		System.out.println(output);
	}

	public String getTrackLength() {
		return String.valueOf(gm.getRaceGoal());
	}

	public void inTheRace(String[] input) {
		getPlayer(input).setIn(true);
		gm.playerInTheRace();
	}

	private void finishControl() {
		if (gm.getAllFinished()) {
			gm.setStarted(0);
			gm.setRacing(false);
			determinePositioningFinishedRace();
		}

		if (gm.controlGameAfterFinishedPlayer()) {
			raceLobbyString = updateRaceLobby(true, true);
			raceLobbyStringFinalized = true;
			endGame();
		} else {
			raceLobbyString = updateRaceLobby(true, false);
			raceLobbyStringFinalized = true;
			gm.noneFinished();
		}

	}

	/**
	 * UPDATERACE#name#id#finished(0-1)#longtimemillis
	 * 
	 * F�rste gang f�r alle 10 andre gang f�r ingen poeng?
	 */
	public String updateRaceLobbyString(boolean full) {

		// If racing, finished and is first time telling that it has finished
		if (!raceLobbyStringFinalized) {
			raceLobbyString = updateRaceLobby(false, full);
		}

		return raceLobbyString;
	}

	public void determinePositioningFinishedRace() {
		for (Entry<Byte, PlayerInfo> entry : players.entrySet()) {
			PlayerInfo player = entry.getValue();
			int place = 0;
			long thisTime = player.getTime();
			int thisPoints = player.getPoints();
			System.out.println(thisTime);
			if (thisTime == -1) {

				gm.rewardPlayer(-1, -1, -1, player);

			} else {

				int behindLeaderBy = 0;
				
				for (Entry<Byte, PlayerInfo> otherEntry : players.entrySet()) {

					if (otherEntry.getKey() != entry.getKey()) {

						long otherTime = otherEntry.getValue().getTime();
						if (thisTime > otherTime && otherTime != -1) {
							place++;
						}
						int otherPoints = otherEntry.getValue().getPoints();
						if(thisPoints < otherPoints)
							behindLeaderBy = otherPoints - thisPoints;
					}
				}

				gm.rewardPlayer(place, players.size(), behindLeaderBy, player);
				System.out.println(behindLeaderBy);
			}
		}

		for (AI ai : this.ai) {
			ai.upgradeCar(getPointsAhead(ai));
		}
	}

	/**
	 * @return name#ready#car#...
	 */
	public String updateRaceLobby(boolean allFinished, boolean full) {
		String result = "";

		if (!allFinished) {
			// Hent spillere i hvilken som helst rekkefølge og sett de inn i returnstrengen

			result += 3;

			for (Entry<Byte, PlayerInfo> entry : players.entrySet()) {
				result += "#" + entry.getValue().getRaceInfo(allFinished, full);
			}
		} else {

			result += 6;

			LinkedList<PlayerInfo> sortedByTime = new LinkedList<PlayerInfo>();

			// Sorter alle spillere etter alle har fullført racet
			sortedByTime.addAll(players.values());
			Collections.sort(sortedByTime, new Comparator<PlayerInfo>() {
				@Override
				public int compare(PlayerInfo o1, PlayerInfo o2) {

					int result = 0;
					if (!full) {
						if (o1.getTime() < o2.getTime()) {
							if (o1.getTime() != -1)
								result = -1;
							else
								result = 1;

						} else if (o1.getTime() > o2.getTime()) {
							if (o2.getTime() != -1)
								result = 1;
							else
								result = -1;
						}
					} else {
						if (o1.getBank().getPoints() < o2.getBank().getPoints()) {
							result = 1;

						} else if (o1.getBank().getPoints() > o2.getBank().getPoints()) {
							result = -1;
						}
					}
					return result;
				}
			});

			// Legg de inn i strengen
			for (int i = 0; i < sortedByTime.size(); i++) {

				String str = null;
				if (sortedByTime.get(i).getClass().equals(AI.class)) {
					AI p = (AI) sortedByTime.get(i);
					str = p.getRaceInfo(allFinished, full);
				} else {
					str = sortedByTime.get(i).getRaceInfo(allFinished, full);
				}

				result += "#" + (i + 1) + ": " + str;
			}
		}
		System.out.println(result);
		return result;
	}

	public void setPointsMoney(String[] input) {
		PlayerInfo player = getPlayer(input);
		;
		if (player == null) {
			return;
		}
		player.setPoints(Integer.valueOf(input[2]));
		player.setMoney(Integer.valueOf(input[3]));
		updateSortedPlayers();
	}

	public String getPointsMoney(String[] input) {
		PlayerInfo player = getPlayer(input);

		String res = null;
		try {
			res = player.getPoints() + "#" + player.getMoney();
		} catch (NullPointerException e) {
			System.err.println("Player " + input[1] + " timed out");
			checkPings();
		}
		return res;
	}

	/**
	 * Creates a new racetrack somewhere in the world and with some length of some
	 * type.
	 * 
	 * @return length of the track
	 */
	public int randomizeLengthOfTrack() {
		return gm.getRandomRaceGoal();
	}

	public void newRaces(String[] input) {
		gm.newEndGoal(Integer.parseInt(input[2]));
	}

	public String getEndGoal() {
		return gm.getEndGoalText();
	}

	public String getPlayerWithMostPoints(String[] input) {
		return gm.getDeterminedWinnerText(getPlayer(input));
	}

	public void ping(String[] input) {
		ping.put(Byte.valueOf(input[1]), System.currentTimeMillis());
	}

	public boolean validPing(long ping) {
		return ping > System.currentTimeMillis() - 10000;
	}

	public void checkPings() {
		for (Entry<Byte, Long> entry : ping.entrySet()) {
			PlayerInfo player = getPlayer(entry.getKey());

			if (!ai.contains(player) && !validPing(entry.getValue())) {
				System.out.println("LEAVING BY PING");
				if (player == null) {
					ping.remove(entry.getKey());
					return;
				}

				addChat(player.getName() + " has too high ping!");
				leave(entry.getKey());
			}
		}
	}

	public long getPing(PlayerInfo player) {
		return System.currentTimeMillis() - ping.get(player.getID());
	}

	/**
	 * Updates pings and trafficlights
	 */
	@Override
	public void run() {

		long lastTime = System.nanoTime();
		double amountOfTicks = 5.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;

		while (!gm.isGameExplicitlyEnded()) {
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
		System.err.println("ENDING SERVER THREAD");
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	private void addChat(String str) {
		for (Queue<String> q : chat.values()) {
			q.offer(str);
		}
	}

	public void addChat(String[] input) {
		if (input.length <= 3)
			return;

		String str = input[2] + ": ";
		for (int i = 3; i < input.length; i++) {
			str += input[i];
			if (i + 1 < input.length)
				str += "#";
		}

		for (Queue<String> q : chat.values()) {
			q.offer(str);
		}

	}

	public String getChat(String[] input) {
		PlayerInfo player = getPlayer(input);
		String chatText = null;

		if (player != null) {
			chatText = chat.get(getPlayer(input)).poll();
		}
		return chatText;
	}

	public String getCurrentPlace() {
		return gm.getCurrentPlace();
	}

	public String isGameOver() {
		return gm.isGameExplicitlyEnded() ? "1" : "0";
	}

	public boolean isIdValid(String str) {

		try {
			byte id = Byte.parseByte(str);
			return players.get(id) != null;

		} catch (Exception e) {
			return false;
		}
	}

}
