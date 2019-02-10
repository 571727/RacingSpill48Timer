package scenes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import adt.Scene;
import elem.Player;
import handlers.SceneHandler;
import handlers.ServerHandler;

public class Lobby extends Scene implements Runnable {

	private static final long serialVersionUID = -5861049279182231248L;
	private JLabel label;
	private JButton ready;
	private JButton fixCar;
	private JButton start;
	private JButton goBack;
	private Player player;
	private boolean running;
	private ServerHandler server;

	public Lobby() {
		label = new JLabel("HELLLLLLLLLLLLLLLLOOOOO?!", SwingConstants.CENTER);
		label.setPreferredSize(new Dimension(400, 50));
		goBack = new JButton("Go back to main menu");

		goBack.addActionListener((ActionEvent e) -> {
			if (server != null) {
				server.join();
				server = null;
			}
			player.leaveServer();
			SceneHandler.instance.changeScene(0);
		});
		add(label);
		add(goBack, BorderLayout.SOUTH);

	}

	/**
	 * FIXME properly show users
	 * 
	 * @param string - outtext from server
	 */
	public void update(String string) {

		String[] outputs = string.split("#");

		String result = "<html>Players: <br/>";

		for (int i = 1; i < outputs.length; i++) {
			result += outputs[i];
			if ((i - 1) % 4 == 0)
				result += "<br/>";
		}

		result += "</html>";

		label.setText(result);

	}

	/**
	 * @param name - username
	 * @param host - int value (0,1) represents boolean
	 */
	public void createNewLobby(String name, int host, String car, ServerHandler server) {
		player = new Player(name, host, car);
		update(player.joinServer());
		initButtons();
		this.server = server;
	}

	/**
	 * @param name - username
	 * @param host - int value (0,1) represents boolean
	 * @param ip
	 */
	public void joinNewLobby(String name, int host, String car, String ip) {
		player = new Player(name, host, car, ip);
		update(player.joinServer());
		initButtons();
	}

	/**
	 * GUI for player
	 */
	private void initButtons() {

		if (ready == null) {
			ready = new JButton("Ready?");
			fixCar = new JButton("Upgrade or fix my car");
			start = new JButton("Start race");

			ready.addActionListener((ActionEvent e) -> player.setReady((player.getReady() + 1) % 2));

			add(ready, BorderLayout.SOUTH);
			add(fixCar, BorderLayout.SOUTH);
			add(start, BorderLayout.SOUTH);
		}
		if (player.getHost() != 1)
			start.setEnabled(false);
		else
			start.setEnabled(true);
	}

	/**
	 * Merely to update lobby once per second
	 */
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 1.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while (SceneHandler.instance.getCurrentScene().getClass().equals(Lobby.class)) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update(player.updateLobbyFromServer());
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

	/*
	 * Getters and setters
	 */
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}
