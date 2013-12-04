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

package joxy.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;

import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSeparatorUI;

/**
 * Joxy's UI delegate for the JSeparator.
 * 
 * <p>Only horizontal separators are rendered properly, vertical ones
 * are still rendered in the Basic look-and-feel style.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxySeparatorUI extends BasicSeparatorUI {

    public static ComponentUI createUI(JComponent c) {
        return new JoxySeparatorUI();
    }

	@Override
	public void paint(Graphics g, JComponent c) {
    	Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

		// Construct colors
		Color light = c.getForeground();
		Color dark = c.getBackground();
		Color opaqueLight = new Color(light.getRed(), light.getGreen(), light.getBlue(), 160);
		Color opaqueDark = new Color(dark.getRed(), dark.getGreen(), dark.getBlue(), 160);
		Color transparentLight = new Color(light.getRed(), light.getGreen(), light.getBlue(), 0);
		Color transparentDark = new Color(dark.getRed(), dark.getGreen(), dark.getBlue(), 0);
		GradientPaint gradient1 = new GradientPaint(0, 0, transparentDark, c.getWidth()/2, 0, opaqueDark, true);
		GradientPaint gradient2 = new GradientPaint(0, 0, transparentLight, c.getWidth()/2, 0, opaqueLight, true);

		Dimension s = c.getSize();

		if (((JSeparator) c).getOrientation() == JSeparator.VERTICAL) {
			g.setColor(c.getForeground());
			g.drawLine(0, 0, 0, s.height);

			g.setColor(c.getBackground());
			g.drawLine(1, 0, 1, s.height);
		} else {
			g2.setPaint(gradient2);
			g2.draw(new Line2D.Float(0, 0, s.width, 0));
			g2.setPaint(gradient1);
			g2.draw(new Line2D.Float(0, 1, s.width, 1));
		}
	}
}
