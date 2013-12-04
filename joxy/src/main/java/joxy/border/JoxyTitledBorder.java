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

import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import joxy.ui.JoxyPanelUI;
import joxy.utils.JoxyGraphics;
import joxy.utils.Output;

/**
 * This class implements a more KDE-like variant of the {@link TitledBorder}.
 * It looks more like a KDE GroupBox.
 * 
 * <p>We try to substitute Joxy-specific borders for the default Java Swing
 * borders in the {@link JoxyPanelUI} class.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyTitledBorder extends TitledBorder {

	private static final int ARC = 6;
	
	/**
	 * Creates a new Joxy titled border.
	 * @param title The title for the border.
	 */
	public JoxyTitledBorder(String title) {
		super(title);
    }
	
	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
		try {
			// shadow
			g2.setStroke(new BasicStroke(0.5f));
			LinearGradientPaint shadowGradient = new LinearGradientPaint(0, y, 0, height - 2,
					new float[]{0, 3f / height, 1 - 40f / height, 1},
					new Color[]{new Color(0, 0, 0, 0), new Color(0, 0, 0, 80), new Color(0, 0, 0, 80), new Color(0, 0, 0, 0)});
			g2.setPaint(shadowGradient);
			g2.drawRoundRect(x, y, width - 1, height - 2, ARC + 2, ARC + 2);
			LinearGradientPaint secondShadowGradient = new LinearGradientPaint(0, y, 0, height - 2,
					new float[]{1 - 40f / height, 1},
					new Color[]{new Color(0, 0, 0, 40), new Color(0, 0, 0, 0)});
			g2.setPaint(secondShadowGradient);
			g2.drawRoundRect(x, y, width - 1, height - 1, ARC + 2, ARC + 2);
			
			// white border
			LinearGradientPaint borderGradient = new LinearGradientPaint(0, y + 1, 0, height - 4,
					new float[]{1 - 38f / height, 1},
					new Color[]{new Color(255, 255, 255, 255), new Color(255, 255, 255, 0)});
			g2.setPaint(borderGradient);
			g2.drawRoundRect(x + 1, y + 1, width - 3, height - 4, ARC, ARC);
			
			// fill
			LinearGradientPaint fillGradient = new LinearGradientPaint(0, y + 1, 0, height - 4,
					new float[]{1 - 38f / height, 1},
					new Color[]{new Color(255, 255, 255, 30), new Color(255, 255, 255, 0)});
			g2.setPaint(fillGradient);
			g2.fillRoundRect(x + 1, y + 1, width - 3, height - 4, ARC, ARC);
		} catch (IllegalArgumentException e) {
			Output.debug("IllegalArgumentException from the titled border gradients: " + e.getMessage());
		}
		
		// text
		if (title != null) {
			g2.setColor(UIManager.getColor("TitledBorder.foreground"));
			g2.setFont(UIManager.getFont("TitledBorder.font"));
			FontMetrics f = g2.getFontMetrics();
			int w = f.stringWidth(title);
			int h = f.getHeight();
			JoxyGraphics.drawString(g2, title, x + (width - w) / 2, y + h + 2);
		}
	}
	
    /**
     * Reinitialize the insets parameter with this Border's current Insets.
     * @param c the component for which this border insets value applies
     * @param insets the object to be reinitialized
     */
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.bottom = 5;
        insets.left = 5;
        insets.right = 5;
        
        String title = getTitle();
        
        if (title == null || title.isEmpty()) {
        	insets.top = 0;
        	return insets;
        }
        
        FontMetrics f = c.getFontMetrics(UIManager.getFont("TitledBorder.font"));
        insets.top = f.getHeight() + 8;
        return insets;
    }
}
