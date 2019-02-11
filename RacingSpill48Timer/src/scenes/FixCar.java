package scenes;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import adt.Scene;
import elem.Player;
import handlers.SceneHandler;

public class FixCar extends Scene{

	private JButton goBackLobby;
	
	private JLabel currentStats;
	private JLabel cartStats;
	private JLabel cashStats;
	
	private JButton cylinder;
	private JButton weightloss;
	private JButton fuel;
	private JButton turbo;
	private JButton nos;
	private JButton piston;
	private JButton tyres;
	private JButton block;
	private JButton buyMoney;
	private JButton buyPoints;
	private JScrollPane currentStatsPane;
	private JScrollPane cartStatsPane;
	private JScrollPane cashStatsPane;
	private Player player;
	
	public FixCar() {
		
		goBackLobby = new JButton("Go back to the lobby");
		goBackLobby.addActionListener((ActionEvent e) -> SceneHandler.instance.changeScene(1));
		
		cylinder = new JButton("Upgrade cylinders");
		weightloss = new JButton("Weight reduction bro");
		fuel = new JButton("Better fuel");
		turbo = new JButton("Bigger turbo");
		nos = new JButton("More NOS");
		piston = new JButton("Lighter pistons");
		tyres = new JButton("Grippier tyres");
		block = new JButton("Beefier block");
		
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
		
		add(cylinder);
		add(weightloss);
		add(fuel);
		add(turbo);
		add(nos);
		add(piston);
		add(tyres);
		add(block);
		
		add(currentStatsPane);
		add(cartStatsPane);
		add(cashStatsPane);
		
		add(goBackLobby);
		add(buyMoney);
		add(buyPoints);
	}
	
	public void init(Player player) {
		this.player = player;
		currentStats.setText(player.getCar().showStats());
//		cashStats.setText(player.get);
	}


}
