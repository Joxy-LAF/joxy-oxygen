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
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.geom.RoundRectangle2D;

import javax.swing.border.BevelBorder;

import joxy.border.JoxyBevelBorder;

/**
 * Painter for a dark engraving. A dark engraving is e.g. the track of a slider
 * or the background of a pressed button.
 * 
 * This painter doesn't need any data (at the moment).
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class DarkEngravingPainter extends Painter {

	private static final int ARC = 6;

	@Override
	protected void paintObject(Graphics2D g2, float width, float height) {
		
		// small white line below the engraving
		g2.setColor(new Color(255, 255, 255, 100));
		g2.draw(new RoundRectangle2D.Float(1, 1, width - 3, height - 3, ARC, ARC));
		
		// background
		g2.setColor(new Color(182, 174, 170)); // TODO find correct color
		g2.fill(new RoundRectangle2D.Float(2, 2, width - 4, height - 4, ARC, ARC));

		JoxyBevelBorder.paintActualBorder(g2, 0, 0, width, height, BevelBorder.LOWERED);
	}

}
