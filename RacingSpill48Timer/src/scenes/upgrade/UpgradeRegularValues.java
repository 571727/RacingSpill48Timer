package scenes.upgrade;

import player_local.car.CarRep;
/**
 * { "HP", "weight", "NOS boost", "NOS bottle", "gears", "TS", "TB", "TB area", "Grip",
			"RPM", "Aero", "Victory Money", "Victory Point"};
 * @author jhoffis
 *
 */
public class UpgradeRegularValues {

	private double[] values;
	private boolean[] percentOrNah;
	private String[] tags = { "HP", "weight", "NOS boost", "NOS bottle", "gears", "TS", "TB", "TB area", "Grip",
			"RPM", "Aero", "Victory Money", "Victory Point"};
	private boolean changed;

	public UpgradeRegularValues(double[] values, boolean[] percentOrNah) {
		this.values = values;
		this.percentOrNah = percentOrNah;
	}

	public String getUpgradeRep() {
		String res = "";
		for (int i = 0; i < values.length; i++) {
			if (values[i] != -1 && values[i] != 0) {

				if (res.length() > 0)
					res += ", ";
				else if (changed)
					res += "<font color='rgb(0, 255, 255)'>";

				if (values[i] < 0)
					res += "- ";
				else
					res += "+ ";

				res += Math.abs(values[i]);

				if (percentOrNah[i]) {
					res += " %";
				}

				res += " " + tags[i];

			}
		}

		if (changed && res.length() > 0)
			res += "</font>";

		return res;
	}

	public double getValue(int i) {
		return values[i];
	}

	public void setValue(int i, double d, boolean percent) {
		values[i] = d;
		setPercent(i, percent);
	}

	public boolean getPercent(int i) {
		return percentOrNah[i];
	}

	public void setPercent(int i, boolean val) {
		percentOrNah[i] = val;
		changed = true;
	}

	public void upgrade(CarRep car) {
		if (values[0] != -1) {
			if (percentOrNah[0])
				car.setHp(car.getHp() * (values[0] / 100.0 + 1));
			else
				car.setHp(car.getHp() + values[0]);
		}
		if (values[1] != -1) {
			if (percentOrNah[1])
				car.setWeight(car.getWeight() * (values[1] / 100.0 + 1));
			else
				car.setWeight(car.getWeight() + values[1]);
		}
		if (values[2] != -1) {
			if (percentOrNah[2])
				car.setNosStrengthStandard(car.getNosStrengthStandard() * (values[2] / 100.0 + 1));
			else
				car.setNosStrengthStandard(car.getNosStrengthStandard() + values[2]);
		}
		if (values[3] != -1) {
			car.setNosBottleAmountStandard((int) (car.getNosBottleAmountStandard() + values[3]));
		}
		if (values[4] != -1) {
			car.setGearTop((int) (car.getGearTop() + values[4]));
		}
		if (values[5] != -1) {
			double speedTopPrev = car.getSpeedTop();
			if (percentOrNah[5])
				car.setSpeedTop(car.getSpeedTop() * (values[5] / 100.0 + 1));
			else
				car.setSpeedTop(car.getSpeedTop() + values[5]);
			car.setGearsbalance(car.getGearsbalance() * (1 - ((car.getSpeedTop() - speedTopPrev) / speedTopPrev)));
		}
		if (values[6] != -1) {
			if (percentOrNah[6])
				car.setTireboostStrengthStandard(car.getTireboostStrengthStandard() * (values[6] / 100.0 + 1));
			else
				car.setTireboostStrengthStandard(car.getTireboostStrengthStandard() + values[6]);
		}
		if (values[7] != -1) {
			car.upgradeRightShift(values[7] / 100.0 + 1);
		}

		if (values[8] != -1) {
			if (percentOrNah[8])
				car.setGripStartStandard(car.getGripStartStandard() * (values[8] / 100.0 + 1));
			else
				car.setGripStartStandard(car.getGripStartStandard() + values[8]);
		}
		
		if (values[9] != -1) {
			if (percentOrNah[8])
				car.setRpmTop((int) (car.getRpmTop() * (values[9] / 100.0 + 1)));
			else
				car.setRpmTop((int) (car.getRpmTop() + values[9]));
		}
		
		if (values[10] != -1) {
			if (percentOrNah[10])
				car.setTitjuiceStrengthStandard((int) (car.getTitjuiceStrengthStandard() * (values[10] / 100.0 + 1)));
			else
				car.setTitjuiceStrengthStandard((int) (car.getTitjuiceStrengthStandard() + values[10]));
			
			if(car.getTitjuiceAmountLeft() == 0)
				car.setTitjuiceAmountLeft(1);
		}
	}
}
