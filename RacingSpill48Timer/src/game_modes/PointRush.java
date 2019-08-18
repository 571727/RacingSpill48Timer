package game_modes;

import adt.GameMode;
import server.PlayerInfo;

public class PointRush implements GameMode{

	@Override
	public String getPodiumPosition(PlayerInfo asker) {
		return null;
	}

	@Override
	public void controlGameAfterFinishedPlayer(PlayerInfo player) {
		
	}

	@Override
	public boolean isGameEnded() {
		return false;
	}

	@Override
	public void endGame() {
		
	}

	@Override
	public void stopRace() {
		
	}

	@Override
	public void startNewRace() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getRandomRaceType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRandomRaceGoal() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void newEndGoal(int gameLength) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getEndGoalText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void determineWinner() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDeterminedWinnerText(PlayerInfo asker) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String youWinnerText(PlayerInfo asker) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String otherSingleWinnerText(PlayerInfo asker) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String otherMultiWinnerText(PlayerInfo asker) {
		// TODO Auto-generated method stub
		return null;
	}

}
