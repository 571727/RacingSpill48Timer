package scenes;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import adt.Scene;
import elem.Car;
import elem.Player;
import handlers.SceneHandler;

public class FixCar extends Scene {

	private JButton goBackLobby;

	private JLabel currentStats;
	private JLabel cartStats;
	private JLabel cashStats;

	private JButton[] upgrades;
	private String[] upgradeTexts;
	private JButton buyMoney;
	private JButton buyPoints;
	private JScrollPane currentStatsPane;
	private JScrollPane cartStatsPane;
	private JScrollPane cashStatsPane;

	private Player player;
	private Car upgradedCar;
	private int currentCostMoney;
	private int currentCostPoints;
	private int currentTypeCart;

	public FixCar() {

		goBackLobby = new JButton("Go back to the lobby");
		goBackLobby.addActionListener((ActionEvent e) -> SceneHandler.instance.changeScene(1));

		upgradeTexts = initButtonTexts();
		upgrades = new JButton[upgradeTexts.length];

		for (int i = 0; i < upgrades.length; i++) {
			upgrades[i] = new JButton(upgradeTexts[i]);
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
		buyMoney.addActionListener((ActionEvent e) -> {
			if (currentCostMoney <= player.getMoney()) {
				player.setPointsAndMoney(player.getPoints(), player.getMoney() - currentCostMoney);
				player.setCar(upgradedCar);

				int[] temp = player.getInflation();
				temp[currentTypeCart]++;
				player.setInflation(temp);

				init(player);
				player.getCar().reset();
			}
		});
		buyPoints.addActionListener((ActionEvent e) -> {
			if (currentCostPoints <= player.getPoints()) {
				player.setPointsAndMoney(player.getPoints() - currentCostPoints, player.getMoney());
				player.setCar(upgradedCar);

				int[] temp = player.getInflation();
				temp[currentTypeCart]++;
				player.setInflation(temp);

				init(player);
				player.getCar().reset();
			}
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
		currentCostMoney = 0;
		currentCostPoints = 0;
		currentStats.setText(player.getCar().showStats());
		cashStats.setText(player.getPointsAndMoney());
		cartStats.setText("");
		upgradedCar = null;
		
		buyPoints.setEnabled(false);
		buyMoney.setEnabled(false);
	}

	private String[] initButtonTexts() {
		String[] temp = { "Upgrade cylinders", "Weight reduction bro", "Better fuel", "Bigger turbo", "More NOS",
				"Lighter pistons", "Grippier tyres and gears", "Beefier block" };
		return temp;
	}

	private void showUpgrades(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		
		buyPoints.setEnabled(true);
		buyMoney.setEnabled(true);
		
		//TODO flytt dette inn i Upgrades og styr via metoder i Player.
		
		try {
			upgradedCar = (Car) player.getCar().clone();
		} catch (CloneNotSupportedException e1) {
			e1.printStackTrace();
		}

		switch (source.getText()) {
		case "Upgrade cylinders":

			currentTypeCart = 0;
			cost(50, 1);

			upgradedCar.setHp(upgradedCar.getHp() + 75f);
			break;
		case "Weight reduction bro":

			currentTypeCart = 1;
			cost(80, 1);

			upgradedCar.setWeightloss(upgradedCar.getWeightloss() + (upgradedCar.getTotalWeight() / 10f));
			break;
		case "Better fuel":

			currentTypeCart = 2;
			cost(40, 1);

			upgradedCar.setHp(upgradedCar.getHp() + (100f / (player.getInflation()[currentTypeCart] + 1f)));
			break;
		case "Bigger turbo":

			currentTypeCart = 3;
			cost(160, 1);

			upgradedCar.setHasTurbo(true);
			upgradedCar.setHp(upgradedCar.getHp() + (400f / (player.getInflation()[currentTypeCart] + 1f)));
			upgradedCar.setWeightloss(upgradedCar.getWeightloss() - 15);
			break;
		case "More NOS":

			currentTypeCart = 4;
			cost(40, 1);
			if (!upgradedCar.isHasNOS()) {
				upgradedCar.setHasNOS(true);
				upgradedCar.setNosAmountLeftStandard(1);
			}
			upgradedCar.setNosStrengthStandard(upgradedCar.getNosStrengthStandard() + 0.5);
			break;
		case "Lighter pistons":

			currentTypeCart = 5;
			cost(100, 1);
			
			upgradedCar.setWeightloss(upgradedCar.getWeightloss() + 50);
			upgradedCar.setHp(upgradedCar.getHp() + 75f);
			break;
		case "Grippier tyres and gears":

			currentTypeCart = 6;
			cost(100, 1);

			upgradedCar.setWeightloss(upgradedCar.getWeightloss() + 50);
			upgradedCar.setTopSpeed(upgradedCar.getTopSpeed() + 75);
			break;
		case "Beefier block":

			currentTypeCart = 7;
			cost(120, 1);

			upgradedCar.setHp(upgradedCar.getHp() + (200f / (player.getInflation()[currentTypeCart] + 1f)));
			upgradedCar.setWeightloss(upgradedCar.getWeightloss() - 15);
			break;
		}

		showCost();
	}

	private void cost(int stdMoney, int stdPoints) {
		currentCostMoney = (int) (stdMoney * 0.75f * (player.getInflation()[currentTypeCart] + 1f));
		currentCostPoints = (int) (stdPoints * (player.getInflation()[currentTypeCart] + 1f));
	}

	private void showCost() {
		cartStats.setText("<html>UPGRADED " + upgradedCar.showStats() + "<br/><br/>$" + currentCostMoney + " or "
				+ currentCostPoints + " points </html>");
	}

}
