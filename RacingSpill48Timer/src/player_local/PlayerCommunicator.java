package player_local;

import java.util.Arrays;

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

	public PlayerCommunicator(String ip) {
		this.ip = ip;
		cth = new ClientThreadHandler(-1);
		client = new ClientController(ip, cth);
		cth.setClient(client);
	}

	/**
	 * JOIN#name+id#host-boolean#discID#checksum
	 */
	public void joinServer(PlayerInfo myInfo, RegularSettings settings) {
		String[] ids = client.sendRequest(
				"J#" + myInfo.getNameID() + "#" + myInfo.getHost() + "#" + settings.getDiscID() + "#" + Main.CHECKSUM)
				.split("#");
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

	/**
	 * LEAVE#name+id
	 */
	public void leaveServer(PlayerInfo myInfo) {
		System.out.println("Leaving server...");
		client.sendRequest("L#" + myInfo.getID());
	}

	/**
	 * UPDATELOBBY#name+id#ready - ready : int (0,1)
	 */
	public void stopAllClientHandlerOperations() {
		if (cth != null)
			cth.stopAll();
	}

	public void endClientHandler() {
		if (cth != null) {
			cth.end();
		}
	}

	public void startClientHandler() {
		try {
			cth.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startPing() {
		if (cth != null)
			cth.startPing();
	}

	public void stopPing() {
		if (cth != null)
			cth.stopPing();
	}

	public String updateLobby() {
		if (cth != null)
			return cth.getLobbyString();
		return null;
	}

	public String updateRaceLobby() {
		if (cth != null)
			return cth.getRaceLobbyString();
		return null;
	}

	public void startUpdateLobby() {
		if (cth != null)
			cth.startLobby();
	}

	public void stopUpdateLobby() {
		if (cth != null)
			cth.stopLobby();
	}

	public void stopUpdateRaceLobby() {
		if (cth != null)
			cth.stopRaceLobby();
	}

	public void stopUpdateRaceLights() {
		if (cth != null)
			cth.stopRaceLights();
	}

	public void updateReady(PlayerInfo myInfo) {
		client.sendRequest("RE#" + myInfo.getID() + "#" + myInfo.getReady());
	}

	public int getTrackLength(PlayerInfo myInfo) {
		return Integer.valueOf(client.sendRequest("GL#" + myInfo.getID()));
	}

	public void setPointsAndMoney(PlayerInfo myInfo, int newPoints, int newMoney) {
		client.sendRequest("SPM#" + myInfo.getID() + "#" + newPoints + "#" + newMoney);
	}

	public String getPointsAndMoney(PlayerInfo myInfo) {
		String result = client.sendRequest("GPM#" + myInfo.getID());
		String[] output = result.split("#");
		myInfo.getBank().setPoints(Integer.valueOf(output[0]));
		myInfo.getBank().setMoney(Integer.valueOf(output[1]));
		return "<html>" + myInfo.getName() + ": <br/>" + "Points: " + myInfo.getBank().getPoints() + ".<br/>" + "Money: " + myInfo.getBank().getMoney()
				+ ".";
	}

	public void finishRace(PlayerInfo myInfo, long time) {
		client.sendRequest("F#" + myInfo.getID() + "#" + time);
	}

	public void inTheRace(PlayerInfo myInfo) {
		myInfo.setIn(true);
		client.sendRequest("I#" + myInfo.getID());
	}

	public void createNewRaces(PlayerInfo myInfo, int amount) {
		client.sendRequest("NEW#" + myInfo.getID() + "#" + amount);
	}

	public String getEndGoal(PlayerInfo myInfo) {
		return client.sendRequest("GEG#" + myInfo.getID());
	}
	
	/**
	 * At end of game figure out who won
	 */
	public String getWinner(PlayerInfo myInfo) {
		return client.sendRequest("W#" + myInfo.getID());
	}

	public void addChat(PlayerInfo myInfo, String text) {
		client.sendRequest("ADC#" + myInfo.getNameID() + "#" + text);
	}

	public String getChat(PlayerInfo myInfo) {
		return client.sendRequest("GC#" + myInfo.getID());
	}

	public String getCurrentPlace(PlayerInfo myInfo) {
		return client.sendRequest("GP#" + myInfo.getID());
	}

	public void setPricesAccordingToServer(PlayerInfo myInfo, StoreHandler store) {
		String[] s = client.sendRequest("GPR#" + myInfo.getID()).split("#");
		store.setPrices(Arrays.asList(s).stream().mapToInt(Integer::parseInt).toArray());
	}

	public void updateCarCloneToServer(PlayerInfo myInfo, CarRep rep) {
		client.sendRequest("CAR#" + myInfo.getID() + "#" + rep.getCloneString());
	}

	public boolean isGameOver(PlayerInfo myInfo) {
		return client.sendRequest("GO#" + myInfo.getID()).equals("1");
	}

	public String forceUpdateRaceLobby(PlayerInfo myInfo) {
		return client.sendRequest("FUR#" + myInfo.getID());
	}

}
