package adt;


import javax.swing.JPanel;

public abstract class GraphicalScene extends Scene{

	/**
	 * Generated value
	 */
	private static final long serialVersionUID = 1768390057592575498L;
	
	public abstract void tick();
	
	public abstract void render();

}
