package game_modes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

import adt.GameMode;
import elem.AI;
import server.PlayerInfo;

public class GolfLike implements GameMode {

	private HashMap<Byte, PlayerInfo> players;
	private ArrayList<PlayerInfo> winners;
	private String currentPlace;
	private String[] places;

	private ArrayList<AI> ai;
	private Thread finishAI_thread;

	private Random r;
	private boolean allFinished;
	private int races;
	private int totalRaces;
	private int started;
	private int amountFinished;
	private int amountInTheRace;
	private int length;
	private long raceStartedTime;
	private final long waitTime = 1000;
	private long regulatingWaitTime = -1;

	@Override
	public String getPodiumPosition(PlayerInfo asker) {
		int place = 0;
		for (Entry<Byte, PlayerInfo> otherEntry : players.entrySet()) {

			if (otherEntry.getValue() != asker) {

				int otherPoints = otherEntry.getValue().getPoints();
				if (asker.getPoints() < otherPoints) {
					place++;
				}
			}
		}
		return String.valueOf(place);
	}

	@Override
	public boolean isGameEnded() {
		return allFinished && races <= 0;
	}

	@Override
	public void endGame() {
		allFinished = true;
		races = -1;
	}

	@Override
	public void stopRace() {
		amountInTheRace = 0;
		amountFinished = 0;
		started = 0;
	}

	@Override
	public void startNewRace() {
		races--;

		for (Entry<Byte, PlayerInfo> entry : players.entrySet()) {
			entry.getValue().newRace();
		}

		raceStartedTime = System.currentTimeMillis();
		regulatingWaitTime = waitTime * 3;
		

		amountInTheRace += ai.size();

		finishAI_thread = new Thread(() -> {
			for (AI ai : this.ai) {
				finishAI(ai, ai.calculateRace(length));
			}
		});
		finishAI_thread.start();
	}

	private void finishAI(AI player, long time) {
		player.setFinished(1);
		amountFinished++;
		player.setTime(time);
	}

	@Override
	public String getNextRaceInfo() {
		return currentPlace + "#" + String.valueOf(length);
	}

	@Override
	public int getRandomRaceType() {
		return 0;
	}

	@Override
	public int getRandomRaceGoal() {
		return 500 * (r.nextInt(totalRaces - races + 1) + 1);
	}

	@Override
	public void newEndGoal(int gameLength) {
		totalRaces = gameLength;
		races = totalRaces;

		prepareNextRace();
	}

	@Override
	public String getEndGoalText() {
		return String.valueOf(races);
	}

	@Override
	public void determineWinner() {
		winners = new ArrayList<PlayerInfo>();

		for (Entry<Byte, PlayerInfo> entry : players.entrySet()) {
			PlayerInfo other = entry.getValue();
			if (winners.size() == 0 || other.getPoints() == winners.get(0).getPoints()) {
				winners.add(other);
			} else if (other.getPoints() > winners.get(0).getPoints()) {
				winners.clear();
				winners.add(other);
			}
		}

	}

	@Override
	public String getDeterminedWinnerText(PlayerInfo asker) {
		String winnerText = null;

		if (asker.getPoints() == winners.get(0).getPoints())
			winnerText = youWinnerText(asker);
		else if (winners.size() == 1)
			winnerText = otherSingleWinnerText(asker);
		else
			winnerText = otherMultiWinnerText(asker);

		winnerText += "#Highest speed you achived was " + asker.getCar().getHighestSpeedAchived() + "km/h!";
		winnerText += "#You made $" + asker.getBank().getMoneyAchived() + " and " + asker.getBank().getPointsAchived()
				+ " points in total!";

		return winnerText;
	}

	@Override
	public String youWinnerText(PlayerInfo asker) {
		String winnerText = "";
		winnerText += "You won";

		// Are you the only winner?
		if (winners.size() > 1) {
			winnerText += " along with: ";
			for (PlayerInfo player : winners) {
				winnerText += "#" + player.getName() + " who drove a " + player.getCarName();
			}
		} else {
			winnerText += "!!!";
		}
		winnerText += "#You have " + asker.getPoints() + " points!";

		return winnerText;
	}

	@Override
	public String otherSingleWinnerText(PlayerInfo asker) {
		String winnerText = "";
		winnerText = winners.get(0).getName() + " won!!!##" + "He drove a " + winners.get(0).getCarName() + "!#"
				+ winners.get(0).getName() + " has " + winners.get(0).getPoints() + " points!#";

		winnerText += "You drove a " + asker.getCarName() + " and you only have " + asker.getPoints() + " points!";
		return winnerText;
	}

	@Override
	public String otherMultiWinnerText(PlayerInfo asker) {
		String winnerText = "";
		winnerText = "The winners are: ";

		for (PlayerInfo player : winners) {
			winnerText += "#" + player.getName() + " who drove a " + player.getCarName();
		}

		winnerText += "!#" + "They won with " + winners.get(0).getPoints() + " points!";
		return winnerText;
	}

	@Override
	public boolean controlGameAfterFinishedPlayer(PlayerInfo player) {

		boolean res = false;

		amountFinished++;
		this.allFinished = amountFinished == players.size();

		if (isGameEnded()) {
			determineWinner();
			res = true;
		} else if (allFinished) {
			prepareNextRace();
		}

		return res;
	}

	@Override
	public void prepareNextRace() {
		length = getRandomRaceGoal();
		currentPlace = places[r.nextInt(places.length)];
	}

	@Override
	public String getName() {
		return "GolfLike";
	}

	@Override
	public void init(HashMap<Byte, PlayerInfo> players, ArrayList<AI> ai, String[] places, Random r) {
		this.r = r;
		this.players = players;
		this.ai = ai;
		this.places = places;
	}

	@Override
	public int getStarted() {
		return started;
	}

	@Override
	public void setStarted(int started) {
		this.started = started;
	}

	@Override
	public boolean getAllFinished() {
		return allFinished;
	}

	@Override
	public boolean everyoneInRace() {
		return amountInTheRace == players.size();
	}

	@Override
	public boolean waitTimeRaceLights() {
		boolean res = false;

		if (raceStartedTime + regulatingWaitTime < System.currentTimeMillis()) {
			regulatingWaitTime = waitTime - 300 + r.nextInt(1200);
			raceStartedTime = System.currentTimeMillis();
			res = true;
		}
		return res;
	}

	@Override
	public void resetWaitTimeRaceLights() {
		raceStartedTime = System.currentTimeMillis();
	}

	@Override
	public void playerInTheRace() {
		amountInTheRace++;
	}

	@Override
	public void rewardPlayer(int place, int amountOfPlayers, PlayerInfo player) {
		allFinished = false;
	}

}
