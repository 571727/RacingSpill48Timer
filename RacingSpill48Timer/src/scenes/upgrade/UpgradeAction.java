package scenes.upgrade;

import player_local.car.CarRep;

public interface UpgradeAction {

	/**
	 * 
	 * @param car
	 * @param notRep to avoid car stats permenatly changing when showing result
	 * @return for instance new lvl after upgrade
	 */
	public int upgrade(CarRep car, boolean notRep);
}
