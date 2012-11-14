package joxy;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicToolTipUI;
import javax.swing.text.View;

import joxy.utils.JoxyGraphics;

/**
 * Joxy's UI delegate for the JToolTip.
 * 
 * <p>This class only distributes one shared instance to all JToolTips, to prevent many
 * instances of this class being generated.</p>
 * 
 * <p>Joxy draws JToolTips customly, such that they can be semi-transparent.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyToolTipUI extends BasicToolTipUI {
	
	/**
	 * Instance of this class that will be given to all tooltips.
	 */
	static BasicToolTipUI sharedInstance = new JoxyToolTipUI();
	
	private static final int ARC = 4;

    public static ComponentUI createUI(JComponent c) {
        return sharedInstance;
    }
    
	@Override
	protected void installDefaults(JComponent c) {
		super.installDefaults(c);
		
		c.setOpaque(false);
		c.setBorder(BorderFactory.createEmptyBorder(8, 6, 8, 6));
	}
	
	@Override
	public void paint(Graphics g, JComponent c) {
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
		paintBackground(g2, c);
		paintForeground(g2, c);
	}

	private void paintBackground(Graphics2D g2, JComponent c) {
		// shadow
		g2.setColor(new Color(0, 0, 0, 10));
		for (int i = 0; i < 3; i++) {
	    	g2.fill(new RoundRectangle2D.Float(i, 0.9f * i, c.getWidth() - 2*i, c.getHeight() - 2*i, ARC - i + 6, ARC - i + 6));
		}
		
		g2.setColor(new Color(0, 0, 0, 25));
		for (int i = 3; i < 6; i++) {
	    	g2.fill(new RoundRectangle2D.Float(i, 0.9f * i, c.getWidth() - 2*i, c.getHeight() - 2*i, ARC - i + 6, ARC - i + 6));
		}
		
		// fill
		Color bg = c.getBackground();
		g2.setColor(new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), 180));
		g2.fill(new RoundRectangle2D.Float(5, 5, c.getWidth() - 10, c.getHeight() - 10, ARC, ARC));
	}
	
	private void paintForeground(Graphics2D g2, JComponent c) {
		Font font = c.getFont();
        FontMetrics metrics = c.getFontMetrics(font);
        Dimension size = c.getSize();

        g2.setColor(c.getForeground());

        String tipText = ((JToolTip)c).getTipText();
        if (tipText == null) {
            tipText = "";
        }

        Insets insets = c.getInsets();
        Rectangle paintTextR = new Rectangle(
            insets.left + 3,
            insets.top,
            size.width - (insets.left + insets.right) - 6,
            size.height - (insets.top + insets.bottom));
        
        View v = (View) c.getClientProperty(BasicHTML.propertyKey);
        
        if (v != null) {
        	// HTML
            v.paint(g2, paintTextR);
        } else {
        	// No HTML
            g2.setFont(font);
            JoxyGraphics.drawString(g2, tipText, paintTextR.x, paintTextR.y + metrics.getAscent());
        }
	}

}
