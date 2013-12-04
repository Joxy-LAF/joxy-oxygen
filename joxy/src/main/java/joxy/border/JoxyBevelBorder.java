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

package joxy.border;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import javax.swing.border.BevelBorder;

import joxy.ui.JoxyPanelUI;
import joxy.utils.Output;

/**
 * This class implements a more KDE-like variant of the {@link BevelBorder}.
 * 
 * <p>We try to substitute Joxy-specific borders for the default Java Swing
 * borders in the {@link JoxyPanelUI} class.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyBevelBorder extends BevelBorder {

	private static final int ARC = 6;
	
	/**
	 * Creates a new Joxy bevel border of the specified type. Contrary to the
	 * {@link BevelBorder}, it is not possible to set the colours manually.
	 * @param type The type of the border, either BevelBorder.LOWERED
	 * (produces a "lowered" border) or BevelBorder.RAISED (a "raised" border).
	 */
	public JoxyBevelBorder(int type) {
		super(type);
    }
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>For JoxyBevelBorder, c may be <code>null</code>.</p>
	 */
	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
		paintActualBorder(g2, x, y, width, height, bevelType);
	}

	/**
	 * Paints the border.
	 */
	public static void paintActualBorder(Graphics2D g2, float x, float y, float width, float height, int bevelType) {
		
		if (bevelType == BevelBorder.RAISED) {
			
			// shadow
			g2.setStroke(new BasicStroke(0.5f));
			GradientPaint shadowGradient = new GradientPaint(0, 0, new Color(0, 0, 0, 0), 0, 3, new Color(0, 0, 0, 80));
			g2.setPaint(shadowGradient);
			g2.draw(new RoundRectangle2D.Float(x, y, width - 1, height - 2, ARC + 2, ARC + 2));
			g2.setColor(new Color(0, 0, 0, 40));
			g2.draw(new RoundRectangle2D.Float(x, y, width - 1, height - 1, ARC + 2, ARC + 2));
			
			// white border
			g2.setColor(Color.WHITE);
			g2.draw(new RoundRectangle2D.Float(x + 1, y + 1, width - 3, height - 4, ARC, ARC));
			
		} else {
			try {
				// outer border
				g2.setStroke(new BasicStroke(0.7f));
				LinearGradientPaint outerGradient = new LinearGradientPaint(0, y + 1, 0, height - 4,
						new float[]{0, 3f / height, 1 - 2f / height, 1},
						new Color[]{new Color(0, 0, 0, 150), new Color(0, 0, 0, 90),
						             new Color(0, 0, 0, 90), new Color(0, 0, 0, 30)});
				g2.setPaint(outerGradient);
				g2.draw(new RoundRectangle2D.Float(x + 1, y + 1, width - 3, height - 4, ARC, ARC));
				
				// inner border
				LinearGradientPaint innerGradient = new LinearGradientPaint(0, y + 2, 0, height - 6,
						new float[]{0, 3f / height, 1 - 2f / height, 1},
						new Color[]{new Color(0, 0, 0, 60), new Color(0, 0, 0, 15),
						             new Color(0, 0, 0, 15), new Color(0, 0, 0, 0)});
				g2.setPaint(innerGradient);
				g2.draw(new RoundRectangle2D.Float(x + 2, y + 2, width - 5, height - 6, ARC - 2, ARC - 2));
			} catch (IllegalArgumentException e) {
				Output.debug("IllegalArgumentException from the bevel border gradients: " + e.getMessage());
			}
		}
	}
	
	@Override
	public boolean isBorderOpaque() {
		return false;
	}
}
