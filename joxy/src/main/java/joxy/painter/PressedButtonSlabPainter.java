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
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.border.BevelBorder;

import joxy.border.JoxyBevelBorder;
import joxy.color.ColorUtils;

/**
 * Painter for a pressed button slab. A pressed button slab is the shape that makes
 * up a pressed button.
 */
public class PressedButtonSlabPainter extends Painter {

	private static final int ARC = 8;
	
	/**
	 * The slab's background colour.
	 */
	private Color color = Color.WHITE;
	
	private Color light = Color.WHITE;
	
	/**
	 * Sets the slab's background colour for the next calls to
	 * {@link #paint(Graphics2D, int, int, int, int)}.
	 * 
	 * <p>Note: you may want to use {@link #setColor(JComponent)} instead.</p>
	 * 
	 * @param c The new background colour.
	 */
	public void setColor(Color c) {
		if (this.color.equals(c)) {
			return;
		}
		
		this.color = c;
		this.light = ColorUtils.mix(ColorUtils.calcLightColor(c), c, 0.3f);
		
		clearCache();
	}
	
	/**
	 * Utility method that sets the slab's background colour to the appropriate
	 * colour for the component's position in the window.
	 * 
	 * @param c The component to set the background colour for. The component needs to have
	 * a {@link JRootPane} anchestor.
	 * @see ColorUtils#backgroundColor(Color, javax.swing.JComponent, double, double)
	 */
	public void setColor(JComponent c) {
		Color color = ColorUtils.backgroundColor(c.getBackground(), c, c.getWidth() / 2, c.getHeight() / 2);
		
		setColor(color);
	}
	
	@Override
	protected void paintObject(Graphics2D g2, float width, float height) {
		GradientPaint gradient = new GradientPaint(0, 2, light, 0, height, color);
		g2.setPaint(gradient);
		g2.fill(new RoundRectangle2D.Double(0, 0, width, height, ARC, ARC));
 		//g2.setColor(new Color(169, 161, 158));
 		//g2.setStroke(new BasicStroke(0.2f));
 		//g2.draw(new RoundRectangle2D.Double(0, 0, width - 1, height - 1, ARC, ARC));

		JoxyBevelBorder.paintActualBorder(g2, -1, -1, width + 2, height + 3, BevelBorder.LOWERED);
	}
}
