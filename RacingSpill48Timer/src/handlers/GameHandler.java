package handlers;

public class GameHandler {

	public GameHandler(int numScenes) {

		new SceneHandler(numScenes);

		SceneHandler.instance.changeScene(0);
	}


}
