package adt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import elem.AI;
import server.PlayerInfo;

public interface GameMode {

	public void init (HashMap<Byte, PlayerInfo> players, ArrayList<AI> ai, String[] places, Random r);
	
	/**
	 * Name to identify which gamemode to host and init
	 */
	public String getName();
	
	/**
	 * Based on the gamemode rules - where does the asker stand?
	 */
	public String getPodiumPosition(PlayerInfo asker);

	/**
	 * Alerts game that a player has finished. Is the game over? Has everyone
	 * finished their single race?
	 */
	public boolean controlGameAfterFinishedPlayer(PlayerInfo player);

	public void prepareNextRace();
	
	/**
	 * @return has the server run "endGame()"?
	 */
	public boolean isGameEnded();

	/**
	 * Resets and closes down most - if not all - values
	 */
	public void endGame();

	/**
	 * Ends a single race and resets values used to determine status of ended race
	 */
	public void stopRace();

	/**
	 * Sets everything up as if race has started
	 */
	public void startNewRace();

	/**
	 * Creates a new racetrack somewhere in the world and with a race type of
	 * choice. For instance regular 1000 m or first to 200 km/h
	 * 
	 * @return type of race
	 */
	public int getRandomRaceType();

	public String getNextRaceInfo();
	
	/**
	 * Checks type of race and determines the length. For instance if it's 1000 m or
	 * 2000 m.
	 * 
	 * @return length of current type of race
	 */
	public int getRandomRaceGoal();

	/**
	 * Is it first to 20 points or one with most points after 18 races?
	 */
	public void newEndGoal(int gameLength);

	/**
	 * @return A text that shows the players what the goal of the game is
	 */
	public String getEndGoalText();

	/**
	 * This is run at the end of the game. It looks at points and such to determine
	 * who won based on the rules of the gamemode. This is only run once to not lose
	 * information about players who leave.
	 */
	public void determineWinner();

	/**
	 * @param asker - client
	 * @return Winnerstring to show in "WinnerVisual" based on who is asking
	 */
	public String getDeterminedWinnerText(PlayerInfo asker);

	/**
	 * You are the winner
	 */
	public String youWinnerText(PlayerInfo asker);

	/**
	 * One other player won, how are the stats of that player compared to you?
	 */
	public String otherSingleWinnerText(PlayerInfo asker);

	/**
	 * Multiple other players have won. How are their stats compared to yours?
	 */
	public String otherMultiWinnerText(PlayerInfo asker);

	public int getStarted();

	public void setStarted(int started);

	public boolean getAllFinished();

	public boolean everyoneInRace();

	public boolean waitTimeRaceLights();

	public void resetWaitTimeRaceLights();

	public void playerInTheRace();

	public void rewardPlayer(int place, int amountOfPlayers, PlayerInfo player);

	public String getRaceGoal();

	public String getCurrentPlace();


}
