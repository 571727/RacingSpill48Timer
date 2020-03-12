package scenes.game.adt;

public interface GameFeatures {

	public void ready();
	public void buy();
	public void finish();
	public void leave();
	public String updateLobby();
	public String updateRaceLobby();
	public void startRace();
	public void getPrices();
	public boolean isGameOverPossible();
	public boolean isGameOverActually();
	
}
