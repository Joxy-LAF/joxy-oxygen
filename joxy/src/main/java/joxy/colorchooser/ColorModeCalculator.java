package joxy.colorchooser;

import java.awt.Color;

/**
 * This class can be extended to specify a mode for the colour chooser.
 * 
 * @see DiagramComponent
 */
public abstract class ColorModeCalculator {
	
	/**
	 * Returns the colour represented when the colour square and line have a certain
	 * value.
	 * 
	 * @param xSquare The x coordinate of the position in the colour square.
	 * @param ySquare The y coordinate of the position in the colour square.
	 * @param yLine The y coordinate of the position on the colour line.
	 * @return The colour represented on this position.
	 */
	public abstract Color diagramToColor(int xSquare, int ySquare, int yLine);
	
	/**
	 * Returns the coordinates in the colour square and line that represent the
	 * given colour.
	 * 
	 * @param color The colour to be looked for.
	 * @return An <code>int</code>-array containing three elements: in order the x coordinate
	 * of the position in the colour square, the y coordinate of the position in the colour
	 * square and the y coordinate of the position on the colour line.
	 */
	public abstract int[] colorToDiagram(Color color);
}
