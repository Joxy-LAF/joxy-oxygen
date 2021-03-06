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
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.JRootPane;

import joxy.color.ColorUtils;

/**
 * Painter for a button slab. A button slab is the shape that makes up a button.
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class ButtonSlabPainter extends Painter {

	private static final int ARC = 8;
	
	/**
	 * The slab's background colour.
	 */
	private Color color = Color.WHITE;
	
	/**
	 * A lighter variant of the background colour, used to paint the gradient.
	 */
	private Color light = Color.WHITE;
	
	/**
	 * Whether this painter paints rounded rectangles (<code>false</code>)
	 * or ellipses (<code>true</code>).
	 */
	private boolean round = false;
	
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
	 * a {@link JRootPane} ancestor.
	 * @see ColorUtils#backgroundColor(Color, javax.swing.JComponent, double, double)
	 */
	public void setColor(JComponent c) {
		Color color = ColorUtils.backgroundColor(c.getBackground(), c, c.getWidth() / 2, c.getHeight() / 2);
		
		setColor(color);
	}
	
	/**
	 * Sets whether this painter paints rounded rectangles (<code>false</code>)
	 * or ellipses (<code>true</code>) for the next calls to
	 * {@link #paint(Graphics2D, int, int, int, int)}.
	 * 
	 * @param round The new roundness.
	 */
	public void setRound(boolean round) {
		if (this.round == round) {
			return;
		}
		
		this.round = round;
		
		clearCache();
	}
	
	@Override
	protected void paintObject(Graphics2D g2, float width, float height) {
//		GradientPaint top = new GradientPaint(0, 2, new Color(241, 240, 239), 0, height, new Color(223, 220, 217));
//		GradientPaint top = new GradientPaint(0, 2, ColorUtils.darken(UIManager.getColor("Button.background"), 0.9f), 0, height, UIManager.getColor("Button.background"));
		GradientPaint gradient = new GradientPaint(0, 2, light, 0, height, color);
		g2.setPaint(gradient);
		if (round) {
			g2.fill(new Ellipse2D.Double(0, 0, width, height));
		} else {
			g2.fill(new RoundRectangle2D.Double(0, 0, width, height, ARC, ARC));
		}
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(0.2f));
		if (round) {
			g2.draw(new Ellipse2D.Double(0, 0, width - 1, height - 1));
		} else {
			g2.draw(new RoundRectangle2D.Double(0, 0, width - 1, height - 1, ARC, ARC));
		}
	}
}
