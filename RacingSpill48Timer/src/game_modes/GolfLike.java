package game_modes;

import java.util.Map.Entry;

import elem.AI;
import server.PlayerInfo;

public class GolfLike extends GameMode {

	private int races;
	private int totalRaces;

	@Override
	public boolean isGameEnded() {
		return races <= 0;
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
		return 120 * (r.nextInt(totalRaces - races + 1) + 1
				+ Integer.parseInt(String.valueOf(totalRaces - races + 1 / 3f).split("\\.")[0]));
	}

	@Override
	public void newEndGoal(int gameLength) {
		totalRaces = gameLength;
		races = totalRaces;

		prepareNextRace();
	}

	@Override
	public String getEndGoalText() {
		return "Races left: " + String.valueOf(races);
	}

	@Override
	public String getName() {
		return "GolfLike";
	}

	@Override
	public void rewardPlayer(int place, int amountOfPlayers, int behindLeaderBy, PlayerInfo player) {
		player.addPointsAndMoney(amountOfPlayers, place, behindLeaderBy, Math.abs(totalRaces - races));
	}

	@Override
	public int getRaceGoal() {
		return length;
	}
}
