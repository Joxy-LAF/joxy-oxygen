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
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSliderUI;

/**
 * Joxy's UI delegate for the JSlider.
 * 
 * <p>The JoxySliderUI supports animations for the focus state (not yet the hovered state).
 * See {@link JoxyButtonUI} for more details.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxySliderUI extends BasicSliderUI {
	
	public static final int ARC = 8;
	
    public static ComponentUI createUI(JComponent b) {
        return new JoxySliderUI((JSlider) b);
    }
    
	public JoxySliderUI(JSlider b) {
		super(b);
	}
	
	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
	}
	
	@Override
	protected void installDefaults(JSlider slider) {
		super.installDefaults(slider);
		slider.setOpaque(false);
	}
	
	@Override
	public void paintFocus(Graphics g) {
		// do nothing
	}
	
	@Override
	public void paintTrack(Graphics g) {
        Rectangle trackBounds = trackRect;
        
        Graphics2D g2 = (Graphics2D) g;
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        
        if (slider.getOrientation() == JSlider.HORIZONTAL) {
            g.translate(trackBounds.x, trackBounds.y + trackBounds.height / 2);
     		g2.setPaint(Color.WHITE);
     		g2.fill(new RoundRectangle2D.Double(-4, -3, trackBounds.width + 8, 6.4f, ARC, ARC));
            GradientPaint fill = new GradientPaint(0, -3, new Color(159, 152, 149), 0, 3, new Color(182, 174, 170));
     		g2.setPaint(fill);
     		g2.fill(new RoundRectangle2D.Double(-4, -3, trackBounds.width + 8, 6, ARC, ARC));
     		g2.setColor(new Color(169, 161, 158));
     		g2.setStroke(new BasicStroke(0.2f));
     		g2.draw(new RoundRectangle2D.Double(-4, -3, trackBounds.width + 7, 5, ARC, ARC));
            g.translate(-trackBounds.x, -trackBounds.y - trackBounds.height / 2);
        } else {
            g.translate(trackBounds.x + trackBounds.width / 2, trackBounds.y);
            g2.setPaint(Color.WHITE);
            g2.fill(new RoundRectangle2D.Double(-3, -4, 6, trackBounds.height + 8.4f, ARC, ARC));
            GradientPaint fill = new GradientPaint(-3, 0, new Color(159, 152, 149), 3, 0, new Color(182, 174, 170));
     		g2.setPaint(fill);
     		g2.fill(new RoundRectangle2D.Double(-3, -4, 6, trackBounds.height + 8, ARC, ARC));
     		g2.setColor(new Color(169, 161, 158));
     		g2.setStroke(new BasicStroke(0.2f));
     		g2.draw(new RoundRectangle2D.Double(-3, -4, 5, trackBounds.height + 7, ARC, ARC));
            g.translate(-trackBounds.x - trackBounds.width / 2, -trackBounds.y);
        }
	}
	
	@Override
	protected Dimension getThumbSize() {
		return new Dimension(18, 18);
	}
	
	@Override
	public void paintThumb(Graphics g) {
        Rectangle knobBounds = thumbRect;
        g.translate(knobBounds.x + 2, knobBounds.y + 2);
		
        Graphics2D g2 = (Graphics2D) g;
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        
        // If mouse is over the component, draw light blue border
//        if (slider.isRollover() false) {
//        	// Rounded rectangle with light blue border
//        	//g2.setColor(new Color(110, 214, 255));
//        	Color hover = UIManager.getColor("Button.hover"); // [ws] TODO moet dit naar de initialisatie?
//        	g2.setColor(hover);
//        	g2.setStroke(new BasicStroke(2f));
//        	g2.draw(new Ellipse2D.Double(0, 0, 13, 13));
//        	g2.setColor(new Color(hover.getRed(), hover.getGreen(), hover.getBlue(), 128));
//        	g2.setStroke(new BasicStroke(5f));
//        	g2.draw(new Ellipse2D.Double(0, 0, 13, 13));
//        } else {
        	// If it has the focus, draw dark blue border
        	if (slider.isFocusOwner()) {
        		// Rounded rectangle with dark blue border
        		//g2.setColor(new Color(58, 167, 221));
        		Color focus = UIManager.getColor("Button.focus"); // [ws] TODO moet dit naar de initialisatie?
        		g2.setColor(focus);
        		g2.setStroke(new BasicStroke(2f));
        		g2.draw(new Ellipse2D.Double(0, 0, 13, 13));
        		g2.setColor(new Color(focus.getRed(), focus.getGreen(), focus.getBlue(), 128));
        		g2.setStroke(new BasicStroke(5f));
        		g2.draw(new Ellipse2D.Double(0, 0, 13, 13));
        	} else {
        		// No blue borders necessary, so draw shadow
        		g2.setColor(new Color(0, 0, 0, 10));
        		g2.fill(new Ellipse2D.Double(-1, -1, 17, 17));
        		g2.setColor(new Color(0, 0, 0, 40));
        		g2.fill(new Ellipse2D.Double(0, 0, 15, 15));
        		g2.fill(new Ellipse2D.Double(1, 1, 13, 13));
        	}
//        }
        
        // The inside part
        GradientPaint top = new GradientPaint(0, 0, new Color(233, 232, 228), 0, 13, new Color(191, 189, 183));
 		g2.setPaint(top);
 		g2.fill(new Ellipse2D.Double(0, 0, 14, 14));
 		g2.setColor(Color.WHITE);
 		g2.setStroke(new BasicStroke(0.2f));
 		g2.draw(new Ellipse2D.Double(0, 0, 13, 13));
 		
 		// If it is pressed, draw this
 		// [ws] TODO only for KDE 4.6 and higher and so
 		if (isDragging()) {
 	        GradientPaint top2 = new GradientPaint(0, 10, new Color(233, 232, 228), 0, 2, new Color(191, 189, 183));
 	 		g2.setPaint(top2);
 	 		g2.fill(new Ellipse2D.Double(2, 2, 10, 10));
 		}
     		
	}
}
