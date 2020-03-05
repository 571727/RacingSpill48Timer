package scenes.game.multiplayer.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import scenes.game.multiplayer.server.connection_standard.Config;

public class UDPEchoClient implements Client {

	private DatagramSocket socket;
	private InetAddress address;

	private byte[] buf;

	public UDPEchoClient(String ip) {
		try {
			socket = new DatagramSocket();
			address = InetAddress.getByName(ip);
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String sendRequest(String req) {
		buf = req.getBytes();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, Config.SERVERPORT);
		try {
			socket.send(packet);
			packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String received = new String(packet.getData(), 0, packet.getLength());
		return received;

	}

	public void close() {
		socket.close();
	}
}
