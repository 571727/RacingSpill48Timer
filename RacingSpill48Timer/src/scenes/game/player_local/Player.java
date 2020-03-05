package scenes.game.player_local;

import java.util.Arrays;

import scenes.game.multiplayer.server.connection_standard.Config;
import file_manipulation.RegularSettings;
import scenes.game.player_local.Car;
import scenes.game.upgrade.StoreHandler;

/**
 * holds and handles its client. Controls lobby for now.
 * 
 * FIXME SEPERATE THE SERVER COMMUNICATION FROM PLAYER INTO ITS OWN SIMILAR CLASS	
 * 
 * @author jonah
 *
 */
public class Player {

	private PlayerCommunicator com;
	private PlayerInfo myInfo;
	
	public Player() {
		// TODO Auto-generated constructor stub
	}

	public Player(String name, byte host, String car) {
		this(name, host, car, Config.SERVER);
	}

	public Player(String name, byte host, String car, String ip) {
		com = new PlayerCommunicator(ip);
		myInfo = new PlayerInfo(name, host);
	}

	public void lockSetupReadyInit(String car) {
		myInfo.newCar(car);
	}
	
	
	public PlayerInfo getMyInfo() {
		return myInfo;
	}
	
	public void setMyInfo(PlayerInfo myInfo) {
		this.myInfo = myInfo;
	}
	
	public Bank getBank() {
		return myInfo.getBank();
	}

	public Car getCar() {
		return myInfo.getCar();
	}
	
	
	/*
	 * SERVER COMMUNICATION
	 */
	

	public void joinServer(RegularSettings settings) {
		com.joinServer(myInfo, settings);
	}

	public void leaveServer() {
		com.leaveServer(myInfo);
	}

	public void stopAllClientHandlerOperations() {
		com.stopAllClientHandlerOperations();
	}

	public void endClientHandler() {
		com.endClientHandler();
	}

	public void startClientHandler() {
		com.startClientHandler();
	}

	public void startPing() {
		com.startPing();
	}

	public void stopPing() {
		com.stopPing();
	}

	public String updateLobby() {
		return com.updateLobby();
	}

	public String updateRaceLobby() {
		return com.updateRaceLobby();
	}

	public void startUpdateLobby() {
		com.startUpdateLobby();
	}

	public void stopUpdateLobby() {
		com.stopUpdateLobby();
	}

	public void stopUpdateRaceLobby() {
		com.stopUpdateRaceLobby();
	}

	public void stopUpdateRaceLights() {
		com.stopUpdateRaceLights();
	}

	public void updateReady() {
		com.updateReady(myInfo);
	}

	public int getTrackLength() {
		return com.getTrackLength(myInfo);
	}

	public void setPointsAndMoney(int newPoints, int newMoney) {
		com.setPointsAndMoney(myInfo, newPoints, newMoney);
	}
	

	public void finishRace(long time) {
		com.finishRace(myInfo, time);
	}

	public void inTheRace() {
		com.inTheRace(myInfo);
	}
	
	public void createNewRaces(int amount) {
		com.createNewRaces(myInfo, amount);
	}

	public String getEndGoal() {
		return com.getEndGoal(myInfo);
	}

	public String getWinner() {
		return com.getWinner(myInfo);
	}

	public void addChat(String text) {
		com.addChat(myInfo, text);
	}

	public String getChat() {
		return com.getChat(myInfo);
	}

	public String getCurrentPlace() {
		return com.getCurrentPlace(myInfo);
	}

	public void setPricesAccordingToServer(StoreHandler store) {
		com.setPricesAccordingToServer(myInfo, store);
	}

	public void updateCarCloneToServer() {
		com.updateCarCloneToServer(myInfo, myInfo.getCar().getRep());
	}

	public boolean isGameOver() {
		return com.isGameOver(myInfo);
	}
	
	public String forceUpdateRaceLobby() {
		return com.forceUpdateRaceLobby(myInfo);
	}
	
	public String getPointsAndMoney() {
		return com.getPointsAndMoney(myInfo);
	}

}
