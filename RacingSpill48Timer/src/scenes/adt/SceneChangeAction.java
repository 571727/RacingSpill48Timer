package scenes.adt;

public interface SceneChangeAction {
	/**
	 * @return Scene it changes into
	 */
	public Scene run(int scenenr, boolean update);
}
