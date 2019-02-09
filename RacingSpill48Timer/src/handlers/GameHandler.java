package handlers;

public class GameHandler {

	private boolean running;

	public GameHandler(int numScenes) {

		new SceneHandler(numScenes);

		SceneHandler.instance.changeScene(0);

		running = false;
		// Loop som kjører viss kode basert på scene, hvis i det hele tatt-
		
	}

	public void loop() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				SceneHandler.instance.getCurrentScene().tick();
				delta--;
			}
			if (running)
				SceneHandler.instance.getCurrentScene().render();
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
	}

}
