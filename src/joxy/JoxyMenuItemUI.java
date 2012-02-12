package joxy;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicMenuItemUI;
import javax.swing.text.View;

import joxy.utils.JoxyGraphics;

public class JoxyMenuItemUI extends BasicMenuItemUI {
	
	/** The width and height of the arcs that form up
	 *  the corners of the rounded rectangles. */
	public static final int ARC = 8;
	/** The Rectangle to paint the icon in. */
    private Rectangle paintIconR = new Rectangle();
	/** The Rectangle to paint the text in. */
    private Rectangle paintTextR = new Rectangle();
    
	public JoxyMenuItemUI(int i) {
	}
	
	public static ComponentUI createUI(JComponent c) {
		c.setOpaque(false);
		int i = 0;
		if (c instanceof JCheckBoxMenuItem) {
            i = 1;
        } else if (c instanceof JRadioButtonMenuItem) {
            i = 2;
        }
		JoxyMenuItemUI menuItemUI = new JoxyMenuItemUI(i);
		return menuItemUI;
	}

	
	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		((JMenuItem) c).setRolloverEnabled(true);
		c.setFont(UIManager.getFont("Button.font"));
	}
	
	@Override
	public void paint(Graphics g, JComponent c) {
		
		JMenuItem mi = (JMenuItem) c;
		
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
		// If mouse is over the component, fill it blue (TODO get the color from the defaults)
		if (mi.getModel().isArmed()) { // [ws] dit is achterlijk: hier is het dan weer wél armed i.p.v. rollover
			// Rounded rectangle with dark blue border
			Color focus = UIManager.getColor("Button.focus"); // [ws] TODO moet dit naar de initialisatie?
			g2.setColor(focus.darker()); // TODO dit zal met ColorSchemes moeten, tijd om naar de originele code te kijken
			g2.fill(new RoundRectangle2D.Double(2, 1, c.getWidth() - 5, c.getHeight() - 5, ARC, ARC));
			// Rounded rectangle with very light blue border
			g2.setColor(focus.brighter());
			g2.fill(new RoundRectangle2D.Double(2, 3, c.getWidth() - 5, c.getHeight() - 5, ARC, ARC));
			// Rounded rectangle with light blue border
			g2.setColor(focus);
			g2.fill(new RoundRectangle2D.Double(2, 2, c.getWidth() - 5, c.getHeight() - 5, ARC, ARC));
		}
		
		// TODO Draw disabled buttons differently
		if (!mi.getModel().isEnabled()) {
			
		}
		
		
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

		// Layout the menu item, i.e. determine the place for icon and text
		FontMetrics f = mi.getFontMetrics(mi.getFont());
		String clippedText = layout(mi, f, c.getWidth(), c.getHeight());
		
		// Draw icon
		if (mi.getIcon() != null) {
			mi.getIcon().paintIcon(mi, g2, paintIconR.x, paintIconR.y);
		}
		
		// Draw text
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g2.setColor(Color.BLACK);
		g2.setFont(mi.getFont());
		View v = (View) c.getClientProperty(BasicHTML.propertyKey);
		if (v != null) { // Text contains HTML
			v.paint(g2, paintTextR);
		} else { // No HTML, draw ourselves
			int w = f.stringWidth(clippedText);
			int h = f.getHeight();
			JoxyGraphics.drawString(g2, clippedText, paintTextR.x + (paintTextR.width - w) / 2, paintTextR.y + (paintTextR.height + h) / 2 - 3);
		}
	}
	
	/**
	 * This method is copied from the BasicLabelUI class.
	 * What it does exactly (especially in combination with the "layoutCL" method
	 * that is also copied from BasicLabelUI) we don't quite understand, but hey,
	 * it works...
	 */
	private String layout(JMenuItem mi, FontMetrics fm, int width, int height) {
		Insets insets = mi.getInsets(null);
		String text = mi.getText();
		Icon icon = (mi.isEnabled()) ? mi.getIcon() : mi.getDisabledIcon();
		Rectangle paintViewR = new Rectangle();
		paintViewR.x = insets.left;
		paintViewR.y = insets.top;
		paintViewR.width = width - (insets.left + insets.right);
		paintViewR.height = height - (insets.top + insets.bottom);
		paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
		paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;
		return layoutCL(mi, fm, text, icon, paintViewR, paintIconR,
				paintTextR);
	}
	
    /**
     * Forwards the call to SwingUtilities.layoutCompoundLabel().
     * This method is here so that a subclass could do Label specific
     * layout and to shorten the method name a little.
     *
     * @see SwingUtilities#layoutCompoundLabel
     */
    protected String layoutCL(
        JMenuItem mi,
        FontMetrics fontMetrics,
        String text,
        Icon icon,
        Rectangle viewR,
        Rectangle iconR,
        Rectangle textR)
    {
        return SwingUtilities.layoutCompoundLabel(
            (JComponent) mi,
            fontMetrics,
            text,
            icon,
            mi.getVerticalAlignment(),
            mi.getHorizontalAlignment(),
            mi.getVerticalTextPosition(),
            mi.getHorizontalTextPosition(),
            viewR,
            iconR,
            textR,
            mi.getIconTextGap());
    }
}
