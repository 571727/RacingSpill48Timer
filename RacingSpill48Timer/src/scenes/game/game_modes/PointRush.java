package scenes.game.game_modes;

import java.util.Map.Entry;

import elem.AI;
import player_local.PlayerInfo;

public class PointRush extends GameModeLegacy {

	private int currentRace;
	private int pointGoal;

	@Override
	public String getName() {
		return "PointRush";
	}

	@Override
	public boolean isGameEnded() {
		boolean res = false;

		if (allFinished) {
			for (Entry<Byte, PlayerInfo> entry : players.entrySet()) {
				if (entry.getValue().getPoints() >= pointGoal) {
					res = true;
					break;
				}
			}
		}

		return res;
	}

	@Override
	public void startNewRace() {
		currentRace++;

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
	public int getRandomRaceType() {
		return 0;
	}

	@Override
	public String getNextRaceInfo() {
		return currentPlace + "#" + String.valueOf(length);
	}

	@Override
	public int getRandomRaceGoal() {
		return 120 * (r.nextInt(currentRace + 1) + 1);
	}

	@Override
	public void newEndGoal(int gameLength) {
		pointGoal = gameLength;
		currentRace = 0;

		prepareNextRace();
	}

	@Override
	public String getEndGoalText() {
		return "Points needed to win: " + String.valueOf(pointGoal);
	}

	@Override
	public void rewardPlayer(int place, int amountOfPlayers, int behindLeaderBy, PlayerInfo player) {
		player.addPointsAndMoney(amountOfPlayers, place, behindLeaderBy, currentRace);
	}

	@Override
	public int getRaceGoal() {
		return length;
	}


}
