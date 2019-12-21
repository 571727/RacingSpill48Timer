package handlers;

import audio.SFX;
import elem.Bank;
import elem.Car;
import elem.CarRep;
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

	public String selectUpgrade(String upgradeName, Car car, Player player) {
		for (int i = 0; i < upgradeNames.length; i++) {
			if (upgradeName.equals(getUpgradeName(i)))
				currentUpgrade = i;
		}

		SFX.playMP3Sound("btn/" + upgradeNames[currentUpgrade]);

		double amount = getCostMoney(currentUpgrade, car.getRepresentation(), player.getPlacePodium());

		String upgradeText = "<html>" + upgrades.getUpgradedStats(currentUpgrade, car, false) + "<br/><br/><font size='4'>$" + amount
				+ " or " + getCostPoints(currentUpgrade, car.getRepresentation()) + " points</font>";

		return upgradeText;
	}

	public void buyWithMoneyClient(Player player) {
		boolean bought = buyWithMoney(player.getBank(), currentUpgrade, player.getCar().getRepresentation(),
				player.getPlacePodium(), -1);

		if (bought) {
			player.setPointsAndMoney(player.getBank().getPoints(), player.getBank().getMoney());
			player.updateCarCloneToServer();
			player.getCar().reset();
			SFX.playMP3Sound("btn/" + "money");
		} else {
			SFX.playMP3Sound("btn/" + "not_enough");
		}
	}

	public void buyWithPointsClient(Player player) {
		boolean bought = buyWithPoints(player.getBank(), currentUpgrade, player.getCar().getRepresentation(), -1);

		if (bought) {
			player.setPointsAndMoney(player.getBank().getPoints(), player.getBank().getMoney());
			player.updateCarCloneToServer();
			player.getCar().reset();

			SFX.playMP3Sound("btn/" + "points");
		} else {
			SFX.playMP3Sound("btn/" + "not_enough");
		}
	}

	public boolean buyWithMoney(Bank bank, int upgrade, CarRep representation, int podiumPosition, double maxSpend) {
		boolean res = false;
		double amount = getCostMoney(upgrade, representation, podiumPosition);
		if ((maxSpend == -1 ? true : amount <= maxSpend) && bank.canAffordMoney((int) amount)
				&& upgrades.upgrade(upgrade, representation, true)) {
			bank.buyWithMoney((int) amount, upgrade);
			res = true;
		}
		System.out.println("Upgrading: " + getUpgradeName(upgrade) + ", " + res);
		return res;
	}

	public boolean buyWithPoints(Bank bank, int upgrade, CarRep representation, int maxSpend) {
		boolean res = false;
		int amount = getCostPoints(upgrade, representation);
		if ((maxSpend == -1 ? true : amount <= maxSpend) && bank.canAffordPoints(amount)
				&& upgrades.upgrade(upgrade, representation, true)) {
			bank.buyWithPoints(amount, upgrade);
			res = true;
		}
		System.out.println("Upgrading: " + getUpgradeName(upgrade) + ", " + res);
		return res;
	}

	public double getCostMoney(int upgrade, CarRep representation, int podiumPosition) {
		int sp = upgrades.specialPriceMoney(upgrade, representation);
		if (sp == -1)
			return Math.round(upgrades.getCostMoney(upgrade, representation) * podiumInflation(podiumPosition));
		else
			return sp;
	}

	public int getCostPoints(int upgrade, CarRep representation) {
		int sp = upgrades.specialPricePoints(upgrade, representation);
		if (sp == -1)
			return upgrades.getCostPoints(upgrade, representation);
		else
			return sp;
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
			res += "<b>Information: </b><br/><br/>";

		for (int i = fromWith; i < toWithout; i++) {
			res += upgrades.getInformation(i, car);
		}
		return res;
	}

	public Upgrades getUpgrades() {
		return upgrades;
	}

	public void setUpgrades(Upgrades upgrades) {
		this.upgrades = upgrades;
	}
}
