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
import elem.Car;
import elem.Player;
import handlers.StoreHandler;
import window.Windows;
import handlers.SceneHandler;

public class Store extends Scene {

	private JButton goBackLobby;

	private JLabel currentStats;
	private JLabel cartStats;
	private JLabel cashStats;
	private JLabel information0;
	private JLabel information1;

	private JButton[] upgrades;
	private JButton buyMoney;
	private JButton buyPoints;
	private JScrollPane currentStatsPane;
	private JScrollPane cartStatsPane;
	private JScrollPane cashStatsPane;

	private StoreHandler fixCarHandler;
	private Player player;

	private String currentPlace;

	private int currentLength;

	public Store() {
		super("store");
		goBackLobby = new JButton("Go back to the lobby");
		fixCarHandler = new StoreHandler();
		upgrades = new JButton[fixCarHandler.getUpgradeNames().length];

		for (int i = 0; i < upgrades.length; i++) {
			upgrades[i] = new JButton(fixCarHandler.getUpgradeName(i));
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

		buyMoney = new JButton("Purchase with money");
		buyPoints = new JButton("Purchase with points");

		goBackLobby.addActionListener((ActionEvent e) -> {
			SFX.playMP3Sound("close_store");
			SceneHandler.instance.changeScene(1);
		});

		buyMoney.addActionListener((ActionEvent e) -> {
			fixCarHandler.buyWithMoney(player);
			updateText(currentPlace, currentLength);

		});
		buyPoints.addActionListener((ActionEvent e) -> {
			fixCarHandler.buyWithPoints(player);
			updateText(currentPlace, currentLength);
		});

		// Layout
		setLayout(null);

		int ipLabelSize = 250;
		int bw = 180;
		int bh = bw / 4;
		int margin = (int) (0.4 * bh);
		int marginW = (int) (1.5 * margin);
		int infH = Windows.HEIGHT * 3 / 5;
		int paneSizeW = (int) (Windows.WIDTH * 0.36);
		int paneSizeH = Windows.HEIGHT - infH;
		int informationW = 2 * paneSizeW + margin;
		int btnX = (Windows.WIDTH - informationW) / 2 - bw / 2 + informationW;
		Font font = new Font("TimesRoman", Font.ITALIC, 16);

		information0.setFont(font);
		information1.setFont(font);
		information0.setBounds(margin, paneSizeH, paneSizeW, infH);
		information1.setBounds(paneSizeW, paneSizeH, paneSizeW, infH);

		for (int i = 0; i < upgrades.length; i++) {
			upgrades[i].setBounds(btnX, (3 + i) * bh + i * margin, bw, bh);
		}

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
	}

	public void init(Player player) {
		this.player = player;
		player.setFixCarHandler(fixCarHandler);
		player.setPricesAccordingToServer();
	}

	public void updateText(String currentPlace, int currentLength) {
		this.currentPlace = currentPlace;
		this.currentLength = currentLength;
		currentStats.setText(player.getCar().showStats());
		cashStats.setText(player.getPointsAndMoney() + "<br/><br/>" + currentPlace + ", " + currentLength + "m</html>");
		cartStats.setText("");
		buyPoints.setEnabled(false);
		buyMoney.setEnabled(false);

		information0.setText("<html><font color='white'>Information: <br/><br/>" + " Cylinders: <br/>"
				+ "+35 HP, +20 % TG area <br/>" + "<font color="
				+ (fixCarHandler.hasUpgrade(0, player.getCar(), 5) ? "'green'" : "'white'")
				+ ">LVL 5: Guaranteed TG shift at high RPM</font><br/><br/>" + " Weight reduction: <br/>"
				+ "-9 % weight <br/>" + "<font color="
				+ (fixCarHandler.hasUpgrade(0, player.getCar(), 5) ? "'green'" : "'white'")
				+ ">LVL 5: Upgrading \"Beefier Block\" no longer increases weight</font><br/><br/>" + " Fuel: <br/>"
				+ "1st: +2 HP, 2nd: +28 HP, 3rd: +206 HP<br/>" + "<font color="
				+ (fixCarHandler.hasUpgrade(0, player.getCar(), 3) ? "'green'" : "'white'")
				+ ">For every upgrade; upgrading \"Gears\" increases TS more</font><br/><br/>" + " Bigger turbo: <br/>"
				+ "+100 HP, +2 % weight<br/>" + "<font color="
				+ (fixCarHandler.hasUpgrade(0, player.getCar(), 5) ? "'green'" : "'white'")
				+ ">LVL 5: -15 % \"Beefier Block\" and \"Lighter Piston\" cost, <br/>"
				+ "upgrading \"Gears\" increases TS -33 %</font></font></html>");

		information1.setText("<html><font color ='white'>" + " More NOS: <br/>" + "+0.5 NOS strength<br/>"
				+ "<font color=" + (fixCarHandler.hasUpgrade(0, player.getCar(), 5) ? "'green'" : "'white'")
				+ ">LVL 5: \"Tires\" -50 % cost<br/><br/>" + "Lighter Pistons: <br/>" + "+60 HP, -5 % weight <br/>"
				+ "<font color=" + (fixCarHandler.hasUpgrade(0, player.getCar(), 5) ? "'green'" : "'white'")
				+ ">LVL 5: +100 % HP from \"Fuel\" even if fuel is upgraded</font><br/><br/>" + " Gears: <br/>"
				+ "+72 (TS * Fuel), -3 % weight <br/>" + "<font color="
				+ (fixCarHandler.hasUpgrade(0, player.getCar(), 5) ? "'green'" : "'white'")
				+ ">LVL 5: \"More NOS\" has longer boost-time</font><br/><br/>" + " Bigger Block: <br/>"
				+ "+170 HP, +14 % weight<br/>" + "<font color="
				+ (fixCarHandler.hasUpgrade(0, player.getCar(), 5) ? "'green'" : "'white'")
				+ ">LVL 5: Upgrading \"Beefier Block\" gives +75 % more HP</font><br/><br/>" + " Tires: <br/>"
				+ "+0.4 TG <br/>" + "<font color="
				+ (fixCarHandler.hasUpgrade(0, player.getCar(), 5) ? "'green'" : "'white'")
				+ ">LVL 5: Doubles current NOS strength</font></font></html>");

	}

	private void showUpgrades(ActionEvent e) {
		JButton source = (JButton) e.getSource();

		buyPoints.setEnabled(true);
		buyMoney.setEnabled(true);

		String upgradedCarText = fixCarHandler.selectUpgrade(source.getText(), player.getCar(), player.getBank());

		cartStats.setText(upgradedCarText);

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(backgroundImage, 0, 0, Windows.WIDTH, Windows.HEIGHT, null);
	}

}
