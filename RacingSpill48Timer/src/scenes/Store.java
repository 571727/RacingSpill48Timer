package scenes;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import adt.Scene;
import audio.SFX;
import elem.Player;
import handlers.SceneHandler;
import handlers.StoreHandler;
import window.Windows;

public class Store extends Scene {

	private JButton goBackLobby;

	private JLabel currentStats;
	private JLabel cartStats;
	private JLabel cashStats;
	private JLabel playerInformation;
	private JLabel information0;
	private JLabel information1;

	private JButton[] upgrades;
	private JButton buyMoney;
	private JButton buyPoints;
	private JScrollPane currentStatsPane;
	private JScrollPane cartStatsPane;
	private JScrollPane cashStatsPane;

	private StoreHandler storeHandler;
	private Player player;

	private String currentPlace;

	private int currentLength;

	public Store() {
		super("store");
		goBackLobby = new JButton("Go back to the lobby");
		storeHandler = new StoreHandler();
		upgrades = new JButton[storeHandler.getUpgradeNames().length];

		for (int i = 0; i < upgrades.length; i++) {
			upgrades[i] = new JButton(storeHandler.getUpgradeName(i));
			upgrades[i].addActionListener((ActionEvent e) -> showUpgrades(e));
			add(upgrades[i]);
		}

		currentStats = new JLabel();
		currentStatsPane = new JScrollPane(currentStats);
		currentStatsPane.setPreferredSize(new Dimension(200, 150));
		cartStats = new JLabel();
		cartStatsPane = new JScrollPane(cartStats);
		cartStatsPane.setPreferredSize(new Dimension(200, 150));
		cashStats = new JLabel();
		cashStatsPane = new JScrollPane(cashStats);
		cashStatsPane.setPreferredSize(new Dimension(200, 150));
		information0 = new JLabel();
		information1 = new JLabel();
		playerInformation = new JLabel();

		buyMoney = new JButton("Purchase with money");
		buyPoints = new JButton("Purchase with points");

		goBackLobby.addActionListener((ActionEvent e) -> {
			SFX.playMP3Sound("btn/close_store");
			SceneHandler.instance.changeScene(1);
		});

		buyMoney.addActionListener((ActionEvent e) -> {
			storeHandler.buyWithMoney(player);
			updateText(currentPlace, currentLength);

		});
		buyPoints.addActionListener((ActionEvent e) -> {
			storeHandler.buyWithPoints(player);
			updateText(currentPlace, currentLength);
		});

		// Layout
		setLayout(null);

		Font font = new Font("TimesRoman", Font.ITALIC, 16);
		int ipLabelSize = 250;
		int bw = 180;
		int bh = bw / 4;
		int margin = (int) (0.4 * bh);
		int marginW = (int) (1.5 * margin);
		int infH2 = Windows.HEIGHT * 3 / 6 + 12;
		int infH1 = infH2 + 18;
		int infWInf = (int) (Windows.WIDTH * 0.4);
		int infW = (int) (Windows.WIDTH * 0.36);
		int infY2 = Windows.HEIGHT - infH2 - 2 * margin;
		int infY1 = infY2;
		int informationW = 2 * infW + margin;
		int midRightX = (Windows.WIDTH - informationW) / 2 + informationW;
		int btnX = midRightX - bw / 2;

		information0.setFont(font);
		information1.setFont(font);
		information0.setBounds(margin, infY1, infWInf, infH1);
		information1.setBounds(infW, infY2, infWInf, infH2);

		double from = 3;
		double buyW = bw * 0.9;
		buyMoney.setBounds((int) (midRightX - (buyW * 0.51) - buyW / 2 - from), (int) ((from / 2) * bh - margin),
				(int) buyW, bh);
		buyPoints.setBounds((int) (midRightX + (buyW * 0.51) - buyW / 2 - from), (int) ((from / 2) * bh - margin),
				(int) buyW, bh);

		for (int i = 0; i < upgrades.length; i++) {
			upgrades[i].setBounds(btnX, (int) ((from + i) * bh + i * margin), bw, bh);
		}
		int gbW = (int) (bw * 1.4);
		goBackLobby.setBounds(midRightX - gbW / 2, (int) ((from + upgrades.length) * bh + upgrades.length * margin),
				gbW, bh);

		int statsPaneW = (informationW - 4 * margin) / 3;
		int statsPaneH = Windows.HEIGHT * 3 / 4 / 2;

		currentStatsPane.setBounds(margin, margin, statsPaneW, statsPaneH);
		cartStatsPane.setBounds(2 * margin + statsPaneW, margin, statsPaneW, statsPaneH);
		cashStatsPane.setBounds(3 * margin + 2 * statsPaneW, margin, statsPaneW, statsPaneH);
		playerInformation.setBounds(margin, margin + statsPaneH + font.getSize(), statsPaneW, font.getSize() * 3 / 2);
		playerInformation.setFont(font);
//		lobbyInfoScrollPane.setBounds(marginW, margin, paneSizeW, paneSizeH);
//		chatScrollPane.setBounds(marginW, Windows.HEIGHT - paneSizeH - 5 * margin, paneSizeW, paneSizeH);
//		chatInput.setBounds(marginW, Windows.HEIGHT - 5 * margin + (chatInputH / 8), paneSizeW, chatInputH);

		add(currentStatsPane);
		add(cartStatsPane);
		add(cashStatsPane);
		add(goBackLobby);
		add(buyMoney);
		add(buyPoints);
		add(information0);
		add(information1);
		add(playerInformation);
	}

	public void init(Player player) {
		this.player = player;
		player.setFixCarHandler(storeHandler);
		player.setPricesAccordingToServer();
	}

	public void updateText(String currentPlace, int currentLength) {
		this.currentPlace = currentPlace;
		this.currentLength = currentLength;
		currentStats.setText(player.getCar().showStats());
		cashStats
				.setText(player.getPointsAndMoney() + "<br/><br/>" + currentPlace + ", " + currentLength + " m</html>");
		cartStats.setText("");
		buyPoints.setEnabled(false);
		buyMoney.setEnabled(false);

		information0.setText(storeHandler.getInformation(player.getCar(), 0, 4));

		information1.setText(storeHandler.getInformation(player.getCar(), 4, 9));

		double di = storeHandler.podiumInflation(player.getPlacePodium());
		playerInformation.setText("<html><font color =" + (di == 1 ? "'white'>+ 0" : "'red'>+ " + (int) (di * 300 / 53))
				+ " % $ podium inflation</font>\"</html>");
	}

	private void showUpgrades(ActionEvent e) {
		JButton source = (JButton) e.getSource();

		buyPoints.setEnabled(true);
		buyMoney.setEnabled(true);

		String upgradedCarText = storeHandler.selectUpgrade(source.getText(), player.getCar(), player);

		cartStats.setText(upgradedCarText);

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(backgroundImage, 0, 0, Windows.WIDTH, Windows.HEIGHT, null);
	}

}
