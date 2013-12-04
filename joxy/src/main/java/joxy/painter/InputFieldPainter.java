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

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.border.BevelBorder;

import joxy.border.JoxyBevelBorder;
import joxy.utils.ColorUtils;

/**
 * Painter for an input field such as a text field.
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class InputFieldPainter extends Painter {

	private static final int ARC = 4;
	
	private Color color = Color.WHITE;
	
	/**
	 * Sets the field's background colour for the next calls to
	 * {@link #paint(Graphics2D, int, int, int, int)}.
	 * 
	 * @param c The new background colour.
	 */
	public void setColor(Color c) {
		if (this.color.equals(c)) {
			return;
		}
		
		this.color = c;
		
		clearCache();
	}
	
	@Override
	protected void paintObject(Graphics2D g2, float width, float height) {
		// background
		g2.setColor(color);
		g2.fill(new RoundRectangle2D.Float(2, 2, width - 4, height - 5, ARC, ARC));

		JoxyBevelBorder.paintActualBorder(g2, 0, 0, width, height, BevelBorder.LOWERED);
	}
}
