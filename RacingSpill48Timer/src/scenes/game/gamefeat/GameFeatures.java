package scenes.game.gamefeat;

import scenes.game.game_modes.GameMode;

public interface GameFeatures {

	public void ready();

	public void buy();

	public void finish();

	public void leave(boolean runGameDestroy);

	public String updateLobby();

	public String updateRaceLobby();

	public void startRace();

	public void getPrices();

	public boolean isGameOverPossible();

	public boolean isGameOverActually();

	/**
	 * @param gameMode set as null if you just want to get
	 */
	public GameMode settings(GameMode gameMode);
	
}
