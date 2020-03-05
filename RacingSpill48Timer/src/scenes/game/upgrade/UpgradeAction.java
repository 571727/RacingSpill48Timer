package scenes.game.upgrade;

import player_local.CarRep;

public interface UpgradeAction {

	/**
	 * 
	 * @param car
	 * @param notRep to avoid car stats permenatly changing when showing result
	 * @return for instance new lvl after upgrade
	 */
	public int upgrade(CarRep car, boolean notRep);
}
