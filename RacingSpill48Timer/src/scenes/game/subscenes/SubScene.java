package scenes.game.subscenes;

import org.lwjgl.nuklear.NkContext;

import elem.objects.Camera;
import scenes.adt.GlobalFeatures;
import scenes.adt.Scene;
import scenes.game.gamefeat.GameFeatures;

public abstract class SubScene extends Scene{

	protected GameFeatures gameFeat;

	public SubScene(GlobalFeatures features, Camera camera, String sceneName, NkContext ctx, long window, int x, int y,
			int width, int height) {
		super(features, camera, sceneName, ctx, window, x, y, width, height);
	}
	
	public void setGameFeatures(GameFeatures gameFeat) {
		this.gameFeat = gameFeat;
	}
}
