package scenes.game.game_modes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

import scenes.game.player_local.PlayerInfo;

public abstract class GameMode {

	protected HashMap<Byte, PlayerInfo> players;
	protected ArrayList<PlayerInfo> winners;
	protected String currentPlace;
	protected String[] places;

	protected ArrayList<AI> ai;
	protected Thread finishAI_thread;

	protected Random r;
	protected boolean allFinished;
	protected boolean racing;
	protected int started;
	protected int amountFinished;
	protected int amountInTheRace;
	protected int length;
	protected long raceStartedTime;
	protected final long waitTime = 1000;
	protected long regulatingWaitTime = -1;
	protected boolean endGame;

	public void init(HashMap<Byte, PlayerInfo> players, ArrayList<AI> ai, String[] places, Random r) {
		this.r = r;
		this.players = players;
		this.ai = ai;
		this.places = places;
		this.endGame = false;
	}

	/**
	 * Based on the gamemode rules - where does the asker stand?
	 */
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

	public abstract boolean isGameEnded();

	/**
	 * @return has the server run "endGame()"?
	 */
	public boolean isGameExplicitlyEnded() {
		return endGame;
	}

	/**
	 * Resets and closes down most - if not all - values
	 */
	public void endGame() {
		endGame = true;
	}

	/**
	 * Ends a single race and resets values used to determine status of ended race
	 */
	public void stopRace() {
		amountInTheRace = 0;
		amountFinished = 0;
		started = 0;
		racing = false;
	}

	/**
	 * Sets everything up as if race has started
	 */
	public abstract void startNewRace();

	protected void finishAI(AI player, long time) {
		player.setFinished(1);
		amountFinished++;
		player.setTime(time);
	}

	public abstract String getNextRaceInfo();

	/**
	 * Creates a new racetrack somewhere in the world and with a race type of
	 * choice. For instance regular 1000 m or first to 200 km/h
	 * 
	 * @return type of race
	 */
	public abstract int getRandomRaceType();

	/**
	 * Checks type of race and determines the length. For instance if it's 1000 m or
	 * 2000 m.
	 * 
	 * @return length of current type of race
	 */
	public abstract int getRandomRaceGoal();

	/**
	 * Is it first to 20 points or one with most points after 18 races?
	 */
	public abstract void newEndGoal(int gameLength);

	/**
	 * @return A text that shows the players what the goal of the game is
	 */
	public abstract String getEndGoalText();

	/**
	 * This is run at the end of the game. It looks at points and such to determine
	 * who won based on the rules of the gamemode. This is only run once to not lose
	 * information about players who leave.
	 */
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

	/**
	 * @param asker - client
	 * @return Winnerstring to show in "WinnerVisual" based on who is asking
	 */
	public String getDeterminedWinnerText(PlayerInfo asker) {
		String winnerText = null;

		if (asker.getPoints() == winners.get(0).getPoints())
			winnerText = youWinnerText(asker);
		else if (winners.size() == 1)
			winnerText = otherSingleWinnerText(asker);
		else
			winnerText = otherMultiWinnerText(asker);

		winnerText += "#You made $" + asker.getBank().getMoneyAchived() + " and " + asker.getBank().getPointsAchived()
				+ " points in total!";

		return winnerText;
	}

	/**
	 * You are the winner
	 */

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

	/**
	 * One other player won, how are the stats of that player compared to you?
	 */

	public String otherSingleWinnerText(PlayerInfo asker) {
		String winnerText = "";
		winnerText = winners.get(0).getName() + " won!!!##" + "He drove a " + winners.get(0).getCarName() + "!#"
				+ winners.get(0).getName() + " has " + winners.get(0).getPoints() + " points!#";

		winnerText += "You drove a " + asker.getCarName() + " and you only have " + asker.getPoints() + " points!";
		return winnerText;
	}

	/**
	 * Multiple other players have won. How are their stats compared to yours?
	 */
	public String otherMultiWinnerText(PlayerInfo asker) {
		String winnerText = "";
		winnerText = "The winners are: ";

		for (PlayerInfo player : winners) {
			winnerText += "#" + player.getName() + " who drove a " + player.getCarName();
		}

		winnerText += "!#" + "They won with " + winners.get(0).getPoints() + " points!";
		return winnerText;
	}

	/**
	 * Alerts game that a player has finished. Is the game over? Has everyone
	 * finished their single race?
	 */
	public boolean controlGameAfterFinishedPlayer() {

		boolean res = false;

		if (isGameEnded()) {
			determineWinner();
			res = true;
		} else if (allFinished) {
			prepareNextRace();
		}

		return res;
	}

	public void anotherPlayerFinished() {
		amountFinished++;
		this.allFinished = amountFinished == players.size();
	}
	
	public void disconnectedFinish() {
		this.allFinished = amountFinished == players.size();
	}
	
	public void prepareNextRace() {
		length = getRandomRaceGoal();
		currentPlace = places[r.nextInt(places.length)];
	}

	/**
	 * Name to identify which gamemode to host and init
	 */
	public abstract String getName();

	public int getStarted() {
		return started;
	}

	public void setStarted(int started) {
		this.started = started;
	}

	public boolean getAllFinished() {
		return allFinished;
	}

	public boolean everyoneInRace() {
		return amountInTheRace == players.size();
	}

	public boolean waitTimeRaceLights() {
		boolean res = false;

		if (raceStartedTime + regulatingWaitTime < System.currentTimeMillis()) {
			regulatingWaitTime = waitTime - 300 + r.nextInt(1200);
			raceStartedTime = System.currentTimeMillis();
			res = true;
		}
		return res;
	}

	public void resetWaitTimeRaceLights() {
		raceStartedTime = System.currentTimeMillis();
	}

	public void playerInTheRace() {
		amountInTheRace++;
	}

	/**
	 * Rewards money and points based on position in just finished race
	 * 
	 * @param place
	 * @param amountOfPlayers
	 * @param player
	 */
	public abstract void rewardPlayer(int place, int amountOfPlayers, int behindLeaderBy, PlayerInfo player);

	public abstract int getRaceGoal();

	public String getCurrentPlace() {
		return currentPlace;
	}

	public void noneFinished() {
		allFinished = false;
	}

	public boolean isRacing() {
		return racing;
	}

	public void setRacing(boolean racing) {
		this.racing = racing;
	}
}
