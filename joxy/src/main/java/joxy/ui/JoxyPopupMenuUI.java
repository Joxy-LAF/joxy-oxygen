/**
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import javax.swing.plaf.basic.DefaultMenuLayout;

/**
 * Joxy's UI delegate for the JPopupMenu.
 * 
 * <p>Note that this delegate is also responsible for drawing the backgrounds
 * of popup menus that appear in a "normal" menu bar.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyPopupMenuUI extends BasicPopupMenuUI {
	
	private static final int ARC = 6;
	
    public static ComponentUI createUI(JComponent c) {
        return new JoxyPopupMenuUI();
    }
    
    @Override
	public void installDefaults() {
        if (popupMenu.getLayout() == null ||
            popupMenu.getLayout() instanceof UIResource)
            popupMenu.setLayout(new DefaultMenuLayout(popupMenu, BoxLayout.Y_AXIS));

        //LookAndFeel.installProperty(popupMenu, "opaque", Boolean.TRUE);
        popupMenu.setBorder(BorderFactory.createEmptyBorder(7, 8, 10, 8));
        //LookAndFeel.installColorsAndFont(popupMenu, "PopupMenu.background", "PopupMenu.foreground", "PopupMenu.font");
    }
    
    @Override
    public void paint(Graphics g, JComponent c) {
    	Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		// Shadow
		g2.setColor(new Color(0, 0, 0, 5));
		for (int i = 0; i < 3; i++) {
	    	g2.fill(new RoundRectangle2D.Float(i, 0.9f * i, c.getWidth() - 2*i, c.getHeight() - 2*i, ARC - i + 6, ARC - i + 6));
		}
		
		g2.setColor(new Color(0, 0, 0, 15));
		for (int i = 3; i < 6; i++) {
	    	g2.fill(new RoundRectangle2D.Float(i, 0.9f * i, c.getWidth() - 2*i, c.getHeight() - 2*i, ARC - i + 6, ARC - i + 6));
		}
		
		// Inner side
		Color color = UIManager.getColor("Window.background");
		// [ws] TODO search real colors
	    Color backgroundTopColor = JoxyRootPaneUI.getBackgroundTopColor(color);
        Color backgroundBottomColor = JoxyRootPaneUI.getBackgroundBottomColor(color);
        
    	g2.setPaint(new GradientPaint(0, 0, backgroundTopColor,
    			                      0, 100, backgroundBottomColor));
    	g2.fill(new RoundRectangle2D.Float(6, 4, c.getWidth() - 12, c.getHeight() - 12, ARC, ARC));
    	
    	// Border
    	g2.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255, 100),
                0, c.getHeight(), new Color(255, 255, 255, 0)));
		g2.setStroke(new BasicStroke(1f));
		g2.draw(new RoundRectangle2D.Float(6, 4, c.getWidth() - 13, c.getHeight() - 13, ARC, ARC));
    }
}
