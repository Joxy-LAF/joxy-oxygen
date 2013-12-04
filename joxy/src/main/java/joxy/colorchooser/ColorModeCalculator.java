/*
 * Copyright 2013  Thom Castermans  thom.castermans@gmail.com
 * Copyright 2013  Willem Sonke     willemsonke@planet.nl
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License or (at your option) version 3 or any later version
 * accepted by the membership of KDE e.V. (or its successor approved
 * by the membership of KDE e.V.), which shall act as a proxy 
 * defined in Section 14 of version 3 of the license.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
