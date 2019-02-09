package elem;

import java.util.Random;

import client.EchoClient;
import connection_standard.Config;
import handlers.SceneHandler;
import scenes.Lobby;

/**
 * holds and handles its client. Controls lobby for now.
 * 
 * @author jonah
 *
 */
public class Player implements Runnable {

	private String name;
	private int host;
	private int id;
	private String ip;
	private EchoClient client;
	private boolean running;

	public Player(String name, int host) {
		this(name, host, Config.SERVER);
	}

	public Player(String name, int host, String ip) {
		this.name = name;
		this.ip = ip;
		Random r = new Random();
		id = r.nextInt(20);
		client = new EchoClient(ip);
		this.host = host;
		running = true;
		// Request stats about lobby and update lobby
		joinServer();
	}

	public void joinServer() {
		Lobby.update(client.sendRequest("JOIN#" + name + "#" + host));
	}

	public void updateLobbyFromServer() {

	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 10.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				SceneHandler.instance.getCurrentScene().tick();
				delta--;
				frames++;
			}
//			if (running)
//				SceneHandler.instance.getCurrentScene().render();

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

}
