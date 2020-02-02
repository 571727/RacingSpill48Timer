package scenes.upgrade;

import player_local.CarRep;

/**
 * { "HP", "weight", "NOS boost", "NOS bottle", "gears", "TS", "TB", "TB area",
 * "Grip", "RPM", "Aero", "Victory Money", "Victory Point"};
 * 
 * @author jhoffis
 *
 */
public class UpgradeRegularValues {

	private double[] values;
	private String[] tags = { "HP", "weight", "NOS boost", "NOS bottle", "gears", "TS", "TB", "TB area", "Grip", "RPM",
			"Aero", "Victory Money", "Victory Point" };
	private boolean changed;

	public UpgradeRegularValues(double[] values) {
		this.values = values;
	}

	public String getUpgradeRep() {
		String res = "";
		for (int i = 0; i < values.length; i++) {
			if (values[i] != -1 && values[i] != 0) {

				if (res.length() > 0)
					res += ", ";
				else if (changed)
					res += "<font color='rgb(0, 255, 255)'>";


				if (isPercent(values[i])) {
					
					if (values[i] < 1.0)
						res += "- ";
					else
						res += "+ ";
					
					res += Math.abs((values[i] - 1) * 100.0);
					res += " %";
					
				} else {
					
					if (values[i] < 0)
						res += "- ";
					else
						res += "+ ";

					if (i == 2 || i == 6)
						res += Math.abs(values[i] / 10.0); // Is NOS or Tireboost
					else
						res += Math.abs(values[i]);

				}

				res += " " + tags[i];

			}
		}

		if (changed && res.length() > 0)
			res += "</font>";

		return res;
	}

	private boolean isPercent(double d) {
		return (d % 1) == 0;
	}

	public double getValue(int i) {
		return values[i];
	}

	public void setValue(int i, double d) {
		values[i] = d;
	}

	public void upgrade(CarRep car) {
		if (values[0] != -1) {
			if (isPercent(values[0]))
				car.setHp(car.getHp() * values[0]);
			else
				car.setHp(car.getHp() + values[0]);
		}
		if (values[1] != -1) {
			if (isPercent(values[1]))
				car.setWeight(car.getWeight() * values[1]);
			else
				car.setWeight(car.getWeight() + values[1]);
		}
		if (values[2] != -1) {
			if (isPercent(values[2]))
				car.setNosStrengthStandard(car.getNosStrengthStandard() * values[2]);
			else
				car.setNosStrengthStandard(car.getNosStrengthStandard() + values[2] / 10.0);
		}
		if (values[3] != -1) {
			car.setNosBottleAmountStandard((int) (car.getNosBottleAmountStandard() + values[3]));
		}
		if (values[4] != -1) {
			car.setGearTop((int) (car.getGearTop() + values[4]));
		}
		if (values[5] != -1) {
			double speedTopPrev = car.getSpeedTop();
			if (isPercent(values[5]))
				car.setSpeedTop(car.getSpeedTop() * values[5]);
			else
				car.setSpeedTop(car.getSpeedTop() + values[5]);
			car.setGearsbalance(car.getGearsbalance() * (1 - ((car.getSpeedTop() - speedTopPrev) / speedTopPrev)));
		}
		if (values[6] != -1) {
			if (isPercent(values[6]))
				car.setTireboostStrengthStandard(car.getTireboostStrengthStandard() * values[6]);
			else
				car.setTireboostStrengthStandard(car.getTireboostStrengthStandard() + values[6] / 10.0);
		}
		if (values[7] != -1) {
			car.upgradeRightShift(values[7]);
		}

		if (values[8] != -1) {
			if (isPercent(values[8]))
				car.setGripStartStandard(car.getGripStartStandard() * values[8]);
			else
				car.setGripStartStandard(car.getGripStartStandard() + values[8]);
		}

		if (values[9] != -1) {
			if (isPercent(values[9]))
				car.setRpmTop((int) (car.getRpmTop() * values[9]));
			else
				car.setRpmTop((int) (car.getRpmTop() + values[9]));
		}

		if (values[10] != -1) {
			if (isPercent(values[10]))
				car.setStrutseAleStrengthStandard((int) (car.getStrutseAleStrengthStandard() * values[10]));
			else
				car.setStrutseAleStrengthStandard((int) (car.getStrutseAleStrengthStandard() + values[10]));

			if (car.getStrutseAleAmountLeft() == 0)
				car.setStrutseAleAmountLeft(1);
		}
	}
}
