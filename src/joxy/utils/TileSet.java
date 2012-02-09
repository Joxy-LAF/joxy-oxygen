package joxy.utils;

public class TileSet {

	//-- CONSTANTS ------------------------------------------------------------
	public static final int RING = 1;
	
	//-- VARIABLES ------------------------------------------------------------
	private int mode;
	
	public TileSet(int mode) {
		this.setMode(mode);
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

}
