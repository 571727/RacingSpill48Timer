package player_local;

import client.ClientController;
import client.ClientThreadHandler;
import client.TCPEchoClient;
import file_manipulation.RegularSettings;
import main.GameHandler;
import main.Main;
import scenes.upgrade.StoreHandler;

public class PlayerCommunicator {

	private String ip;
	private ClientController client;
	private ClientThreadHandler cth;
	private TCPEchoClient echoClient;

	public PlayerCommunicator(String ip) {
		this.ip = ip;
		echoClient = new TCPEchoClient(ip);
		cth = new ClientThreadHandler(-1);
		client = new ClientController(echoClient, cth);
		cth.setClient(client);
	}

	public void joinServer(PlayerInfo myInfo, RegularSettings settings) {
		String[] ids = client.sendRequest("J#" + myInfo.getNameID() + "#" + myInfo.getHost() + "#"
				+ settings.getDiscID() + "#" + Main.GAME_VERSION).split("#");
		byte id = Byte.valueOf(ids[0]);
		myInfo.setID(id);
		cth.setID(id);

		long discID = Long.valueOf(ids[1]);
		settings.setDiscID(discID);
		myInfo.setDiscID(discID);

		// Rejoined server
		if (Integer.valueOf(ids[2]) == 1) {
			myInfo.setName(ids[3]);
			myInfo.getCar().getRep().setClone(ids, 4);
		}
		myInfo.getCar().reset();

	}

	public void leaveServer(PlayerInfo myInfo) {
		// TODO Auto-generated method stub

	}

	public void stopAllClientHandlerOperations() {
		// TODO Auto-generated method stub

	}

	public void endClientHandler() {
		// TODO Auto-generated method stub

	}

	public void startClientHandler() {
		// TODO Auto-generated method stub

	}

	public void startPing() {
		// TODO Auto-generated method stub

	}

	public void stopPing() {
		// TODO Auto-generated method stub

	}

	public String updateLobby() {
		// TODO Auto-generated method stub
		return null;
	}

	public String updateRaceLobby() {
		// TODO Auto-generated method stub
		return null;
	}

	public int updateRaceLights() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void startUpdateLobby() {
		// TODO Auto-generated method stub

	}

	public void startUpdateRaceLobby() {
		// TODO Auto-generated method stub

	}

	public void startUpdateRaceLights() {
		// TODO Auto-generated method stub

	}

	public void stopUpdateLobby() {
		// TODO Auto-generated method stub

	}

	public void stopUpdateRaceLobby() {
		// TODO Auto-generated method stub

	}

	public void stopUpdateRaceLights() {
		// TODO Auto-generated method stub

	}

	public void updateReady(PlayerInfo myInfo) {
		// TODO Auto-generated method stub

	}

	public void startRace(PlayerInfo myInfo) {
		// TODO Auto-generated method stub

	}

	public void stopRace(PlayerInfo myInfo) {
		// TODO Auto-generated method stub

	}

	public int getTrackLength(PlayerInfo myInfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setPointsAndMoney(PlayerInfo myInfo, int newPoints, int newMoney) {
		// TODO Auto-generated method stub

	}

	public void finishRace(PlayerInfo myInfo, long time) {
		// TODO Auto-generated method stub

	}

	public void inTheRace(PlayerInfo myInfo) {
		// TODO Auto-generated method stub

	}

	public void createNewRaces(PlayerInfo myInfo, int amount) {
		// TODO Auto-generated method stub

	}

	public String getEndGoal(PlayerInfo myInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getWinner(PlayerInfo myInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addChat(PlayerInfo myInfo, String text) {
		// TODO Auto-generated method stub

	}

	public String getChat(PlayerInfo myInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCurrentPlace(PlayerInfo myInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPricesAccordingToServer(PlayerInfo myInfo, StoreHandler store) {
		// TODO Auto-generated method stub

	}

	public void updateCarCloneToServer(PlayerInfo myInfo, CarRep rep) {
		// TODO Auto-generated method stub

	}

	public boolean isGameOver(PlayerInfo myInfo) {
		// TODO Auto-generated method stub
		return false;
	}

	public String forceUpdateRaceLobby(PlayerInfo myInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPointsAndMoney(PlayerInfo myInfo) {
		// TODO Auto-generated method stub
		return null;
	}

}
