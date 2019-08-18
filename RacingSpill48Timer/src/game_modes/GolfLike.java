package game_modes;

import java.util.ArrayList;
import java.util.Map.Entry;

import adt.GameMode;
import elem.AI;
import server.PlayerInfo;

public class GolfLike implements GameMode{

	@Override
	public String getPodiumPosition(PlayerInfo asker) {
		int place = 0;
		for (Entry<String, PlayerInfo> otherEntry : players.entrySet()) {

			if (otherEntry.getValue() != player) {

				int otherPoints = otherEntry.getValue().getPoints();
				if (player.getPoints() < otherPoints) {
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

		for (Entry<String, PlayerInfo> entry : players.entrySet()) {
			entry.getValue().newRace();
		}

		raceStartedTime = System.currentTimeMillis();
		regulatingWaitTime = waitTime * 3;
		raceLobbyStringFinalized = false;

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
		return 500 * (r.nextInt(totalRaces - races + 1) + 1);
	}

	@Override
	public void newEndGoal(int gameLength) {
		totalRaces = Integer.parseInt(input[1]);
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

		for (Entry<String, PlayerInfo> entry : players.entrySet()) {
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
			winnerText = youWinningText(asker);
		else if (winners.size() == 1)
			winnerText = otherWinningText(asker);
		else
			winnerText = othersWinningText(asker);

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

		if(isGameEnded()) {
			determineWinner();
		} else if (allFinished) {
			prepareNextRace();
		}
	
	}


	@Override
	public void prepareNextRace() {
		length = randomizeLengthOfTrack();
		currentPlace = places[r.nextInt(places.length)];		
	}



	

}
