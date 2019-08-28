package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TODO legg til sjekk av hvem som sender inn requests. Om en ikke har sendt inn
 * en request p� lenge s� betyr det at den clienten har forsvunnet.
 * 
 * @author jonah
 *
 */

public class TCPEchoServer {

	private ServerSocket welcomeSocket;
	private ServerInfo info;
	private int startRace;

	public int getStartRace() {
		return startRace;
	}

	public void setStartRace(int startRace) {
		this.startRace = startRace;
	}

	public TCPEchoServer(ServerSocket welcomeSocket, ServerInfo info) {
		this.welcomeSocket = welcomeSocket;
		this.info = info;
	}

	public void process() {

		try {
//			System.out.println("SERVER ACCEPTING");
			// sitter her og venter.
			Socket connectionSocket = welcomeSocket.accept();

			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			String text = inFromClient.readLine();
//			System.out.println("SERVER RECEIVED: " + text);
			String outtext = understandRequest(text);

			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

			int n = 0;
			do {

//				System.out.println("SERVER SENDING: " + outtext);
				if (outtext == null) {
					outtext = "";
				}
				outToClient.write(outtext.getBytes());
				outToClient.flush();

				n = 1;
			} while (n == 0);

			outToClient.close();
			inFromClient.close();

			connectionSocket.close();

		} catch (IOException e) {
			// Dette skjer om man lukker connection til serveren
			System.out.println("TCPServer: " + e.getMessage());

		}
	}

	/**
	 * take the first word and run the rest to its responsible function. Like SQL.
	 * 
	 * JOIN#name+id#host-boolean#carname LEAVE#name+id CLOSE
	 * UPDATELOBBY#name+id#ready UPDATERACE#name+id#mysitsh
	 * 
	 * @param text input from client
	 * @return answer based upon request
	 */
	public String understandRequest(String request) {
		String[] input = request.split("#");

		String res = null;
		System.out.println(request);

		switch (input[0]) {
		case "F":
			info.finishPlayer(input);
			break;
		case "I":
			info.inTheRace(input);
			break;
		case "J":
			res = join(input);
			break;
		case "L":
			res = leave(input);
			break;
		case "C":
			res = join(input);
			break;
		case "UL":
			res = updateLobby(input);
			break;
		case "UR":
			res = info.updateRaceLobbyString();
			break;
		case "RL":
			res = info.getRaceLightsStatus();
			break;
		case "SR":
			info.startStopRace(input);
			break;
		case "GL":
			res = info.getTrackLength();
			break;
		case "SPM":
			info.setPointsMoney(input);
			break;
		case "GPM":
			res = info.getPointsMoney(input);
			break;
		case "NEW":
			info.newRaces(input);
			break;
		case "GEG":
			res = info.getEndGoal();
			break;
		case "W":
			res = info.getPlayerWithMostPoints(input);
			break;
		case "ADC":
			info.addChat(input);
			break;
		case "GC":
			res = info.getChat(input);
			break;
		case "GP":
			res = info.getCurrentPlace();
			break;
		case "GPR":
			res = info.getPrices();
			break;
		case "CAR":
			info.updateCarForPlayer(input);
			break;
		case "RE":
			info.updateReady(input);
			break;
		case "GO":
			res = info.isGameOver();
			break;
		}

		info.ping(input);

		return res;
	}

	private String join(String[] input) {
		return info.joinLobby(input);
	}

	private String leave(String[] input) {

		info.leave(input);
		return "";
	}

	private String updateLobby(String[] input) {
		return info.updateLobby(input);
	}

	/*
	 * Getters and setters
	 */

	public ServerSocket getWelcomeSocket() {
		return welcomeSocket;
	}

	public void setWelcomeSocket(ServerSocket welcomeSocket) {
		this.welcomeSocket = welcomeSocket;
	}

}
