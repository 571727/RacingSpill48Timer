package handlers;

import audio.SFX;
import elem.Bank;
import elem.Car;
import elem.Player;
import elem.UpgradePrice;
import elem.Upgrades;

/**
 * 
 * @author jhoffis
 * 
 *         Returns plain text and whatever that the actual scene just shows. The
 *         FixCar scene should not deal with figuring out text, just printing.
 */

public class StoreHandler {
	private Upgrades upgrades;
	private String[] upgradeNames;
	private int currentUpgrade;

	public StoreHandler() {
		upgrades = new Upgrades();
		upgradeNames = upgrades.getUpgradeNames();
	}

	public String getUpgradeName(int i) {
		return upgradeNames[i];
	}

	public String selectUpgrade(int i) {
		currentUpgrade = i;
		return null;
	}

	public String selectUpgrade(String upgradeName, Car car, Bank bank) {
		for (int i = 0; i < upgradeNames.length; i++) {
			if (upgradeName.equals(getUpgradeName(i)))
				currentUpgrade = i;
		}

		SFX.playMP3Sound("btn/" + upgradeNames[currentUpgrade]);

		String upgradeText = "<html>" + upgrades.getUpgradedStats(currentUpgrade, car) + "<br/><br/>$"
				+ upgrades.getCostMoney(currentUpgrade, bank) + " or " + upgrades.getCostPoints(currentUpgrade, bank)
				+ " points </html>";

		return upgradeText;
	}

	public void buyWithMoney(Player player) {
		double amount = upgrades.getCostMoney(currentUpgrade, player.getBank())
				* podiumInflation(player.getPlacePodium());
		if (player.getBank().canAffordMoney((int) amount) && upgrades.upgrade(currentUpgrade, player.getCar())) {

			SFX.playMP3Sound("btn/" + "money");

			player.getBank().buyWithMoney((int) amount, currentUpgrade);
			player.setPointsAndMoney(player.getBank().getPoints(), player.getBank().getMoney());
			player.updateCarCloneToServer();
			player.getCar().reset();
		} else {
			SFX.playMP3Sound("btn/" + "not_enough");
		}
	}

	public void buyWithPoints(Player player) {
		int amount = upgrades.getCostPoints(currentUpgrade, player.getBank());
		if (player.getBank().canAffordPoints(amount) && upgrades.upgrade(currentUpgrade, player.getCar())) {

			SFX.playMP3Sound("btn/" + "points");

			player.getBank().buyWithPoints(amount, currentUpgrade);
			player.setPointsAndMoney(player.getBank().getPoints(), player.getBank().getMoney());
			player.updateCarCloneToServer();
			player.getCar().reset();
		} else {
			SFX.playMP3Sound("btn/" + "not_enough");
		}
	}

	public String[] getUpgradeNames() {
		return upgradeNames;
	}

	public void setUpgradeNames(String[] upgradeNames) {
		this.upgradeNames = upgradeNames;
	}

	public void setPrices(int[] prices) {
		UpgradePrice[] upgradePrices = upgrades.getUpgradePrices();
		for (int i = 0; i < upgradePrices.length; i++) {
			upgradePrices[i] = new UpgradePrice(1, prices[i]);
		}
	}

	public double podiumInflation(int podium) {
		if (podium > 3)
			return 1;
		return 0.02 * (3 - podium) + 1;
	}

	public String getInformation(Car car, int fromWith, int toWithout) {
		String res = "<html><font color='white'>";

		if (fromWith == 0)
			res += "Information: <br/><br/>";
		
		for (int i = fromWith; i < toWithout; i++) {
			res += upgrades.getInformation(i, car);
		}
		return res;
	}

}
