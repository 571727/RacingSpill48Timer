package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.JOptionPane;

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

		switch (input[0]) {
		case "F":
			info.finishPlayer(input);
			break;
		case "PING":
			info.ping(input);
			break;
		case "JOIN":
			return join(input);
		case "LEAVE":
			return leave(input);
		case "CLOSE":
			return join(input);
		case "UPDATELOBBY":
			return updateLobby(input);
		case "UPDATERACE":
			return info.updateRace();
		case "RACELIGHTS":
			return info.getRaceLightsStatus();
		case "STARTRACE":
			info.startRace(input);
			break;
		case "GETLENGTH":
			return info.getTrackLength();
		case "SETPOINTSMONEY":
			info.setPointsMoney(input);
			break;
		case "GETPOINTSMONEY":
			return info.getPointsMoney(input);
		case "NEWRACES":
			info.newRaces();
			break;
		case "GETRACESLEFT":
			return info.getRacesLeft();
		case "WINNER":
			return info.getPlayerWithMostPoints();
		}

		return null;
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
