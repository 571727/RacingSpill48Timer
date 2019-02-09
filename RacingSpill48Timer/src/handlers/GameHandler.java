package handlers;

public class GameHandler {

	public static GameHandler instance;

	public GameHandler(int numScenes) {

		new SceneHandler(numScenes);

		SceneHandler.instance.changeScene(0);

		// Loop som kjører viss kode basert på scene, hvis i det hele tatt-

	}

}
