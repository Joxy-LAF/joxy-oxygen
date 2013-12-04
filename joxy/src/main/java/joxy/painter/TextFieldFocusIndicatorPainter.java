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

package joxy.painter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.UIManager;

/**
 * Painter for a round focus indicator. A focus indicator is drawn around a button if it has
 * the focus.
 */
public class TextFieldFocusIndicatorPainter {

	private static Color focus = UIManager.getColor("Button.focus");
	private static final int ARC = 6;
	
	/**
	 * Draws the focus indicator. Note that a focus indicator is  a 'border' around the
	 * rectangle given here.
	 * @param g2 The Graphics2D to paint with.
	 * @param x x-coordinate for the left upper corner.
	 * @param y y-coordinate for the left upper corner.
	 * @param width Width of the shape.
	 * @param height Height of the shape.
	 * @param opacity Opacity of the focus indicator, with 0 completely transparent, and 255 completely opaque.
	 */
	public static void paint(Graphics2D g2, float x, float y, float width, float height, int opacity) {
		g2.setColor(new Color(focus.getRed(), focus.getGreen(), focus.getBlue(), opacity));
		g2.setStroke(new BasicStroke(1f));
		g2.draw(new RoundRectangle2D.Float(x + 1, y + 1, width - 3, height - 4, ARC, ARC));
		g2.setColor(new Color(focus.getRed(), focus.getGreen(), focus.getBlue(), opacity / 2));
		g2.setStroke(new BasicStroke(1f));
		g2.draw(new RoundRectangle2D.Float(x + 2, y + 2, width - 5, height - 6, ARC - 2, ARC - 2));
		g2.setColor(new Color(focus.getRed(), focus.getGreen(), focus.getBlue(), opacity / 4));
		g2.setStroke(new BasicStroke(1f));
		g2.draw(new RoundRectangle2D.Float(x + 3, y + 3, width - 7, height - 8, ARC - 4, ARC - 4));
	}

}
