package scenes.game.gamefeat;

import elem.Action;
import scenes.Scenes;
import scenes.adt.SceneChangeAction;
import scenes.game.game_modes.GameMode;

public class SingleFeatures implements GameFeatures{

	private Action destroyAction, subSceneUpdate;
	private SceneChangeAction sceneChange, subSceneChange;
	private GameMode gameMode;
	
	public SingleFeatures(Action destroyAction, SceneChangeAction sceneChange, SceneChangeAction subSceneChange, Action subSceneUpdate) {
		this.destroyAction = destroyAction;
		this.sceneChange = sceneChange;
		this.subSceneChange = subSceneChange;
		this.subSceneUpdate = subSceneUpdate;
	}

	@Override
	public void ready() {
		//Init settings and runnn
		subSceneChange.run(Scenes.RACE, true);
	}

	@Override
	public void buy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leave(boolean runGameDestroy) {
		if(runGameDestroy)
			destroyAction.run();
		
		sceneChange.run(Scenes.MAIN_MENU, true);
	}

	@Override
	public String updateLobby() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateRaceLobby() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startRace() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getPrices() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isGameOverPossible() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isGameOverActually() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public GameMode settings(GameMode gameMode) {
		return this.gameMode = gameMode;
	}

}
