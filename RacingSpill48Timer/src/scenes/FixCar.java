package scenes;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import adt.Scene;
import audio.SFX;
import elem.Car;
import elem.Player;
import handlers.FixCarHandler;
import handlers.SceneHandler;

public class FixCar extends Scene {

	private JButton goBackLobby;

	private JLabel currentStats;
	private JLabel cartStats;
	private JLabel cashStats;

	private JButton[] upgrades;
	private JButton buyMoney;
	private JButton buyPoints;
	private JScrollPane currentStatsPane;
	private JScrollPane cartStatsPane;
	private JScrollPane cashStatsPane;

	private FixCarHandler fixCarHandler;
	private Player player;

	public FixCar() {

		goBackLobby = new JButton("Go back to the lobby");
		fixCarHandler = new FixCarHandler();
		upgrades = new JButton[fixCarHandler.getUpgradeNames().length];

		for (int i = 0; i < upgrades.length; i++) {
			upgrades[i] = new JButton(fixCarHandler.getUpgradeName(i));
			upgrades[i].addActionListener((ActionEvent e) -> showUpgrades(e));
			add(upgrades[i]);
		}

		currentStats = new JLabel();
		currentStatsPane = new JScrollPane(currentStats);
		currentStatsPane.setPreferredSize(new Dimension(150, 150));
		cartStats = new JLabel();
		cartStatsPane = new JScrollPane(cartStats);
		cartStatsPane.setPreferredSize(new Dimension(150, 150));
		cashStats = new JLabel();
		cashStatsPane = new JScrollPane(cashStats);
		cashStatsPane.setPreferredSize(new Dimension(150, 150));

		buyMoney = new JButton("Purchase with money");
		buyPoints = new JButton("Purchase with points");

		goBackLobby.addActionListener((ActionEvent e) -> {
			SFX.playMP3Sound("close_store");
			SceneHandler.instance.changeScene(1);
		});

		buyMoney.addActionListener((ActionEvent e) -> {
			fixCarHandler.buyWithMoney(player);

			player.getCar().reset();
			updateText();

		});
		buyPoints.addActionListener((

				ActionEvent e) -> {
			fixCarHandler.buyWithPoints(player);

			player.getCar().reset();
			updateText();
		});

		add(currentStatsPane);
		add(cartStatsPane);
		add(cashStatsPane);
		add(goBackLobby);
		add(buyMoney);
		add(buyPoints);
	}

	public void init(Player player) {
		this.player = player;
		player.setFixCarHandler(fixCarHandler);
		player.setPricesAccordingToServer();
		updateText();
	}

	private void updateText() {
		currentStats.setText(player.getCar().showStats());
		cashStats.setText(player.getPointsAndMoney());
		cartStats.setText("");
		buyPoints.setEnabled(false);
		buyMoney.setEnabled(false);

	}

	private void showUpgrades(ActionEvent e) {
		JButton source = (JButton) e.getSource();

		buyPoints.setEnabled(true);
		buyMoney.setEnabled(true);

		String upgradedCarText = fixCarHandler.selectUpgrade(source.getText(), player.getCar(), player.getBank());

		cartStats.setText(upgradedCarText);

	}

}
