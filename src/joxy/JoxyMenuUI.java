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
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicMenuUI;
import javax.swing.text.View;

import joxy.utils.JoxyGraphics;

public class JoxyMenuUI extends BasicMenuUI {

	/** The width and height of the arcs that form up
	 *  the corners of the rounded rectangles. */
	public static final int ARC = 8;
	/** The Rectangle to paint the icon in. */
    private Rectangle paintIconR = new Rectangle();
	/** The Rectangle to paint the text in. */
    private Rectangle paintTextR = new Rectangle();
    
	public static ComponentUI createUI(JComponent c) {
		JoxyMenuUI menuUI = new JoxyMenuUI();
		return menuUI;
	}
	
	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		c.setOpaque(false);
		((JMenu) c).setRolloverEnabled(true);
		c.setFont(UIManager.getFont("Button.font"));
	}
	
	@Override
	public void paint(Graphics g, JComponent c) {
		// TODO Auto-generated method stub
		//super.paint(g, c);
		
		JMenu m = (JMenu) c;
		
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
		// If mouse is over the component, fill it blue
		if (m.getModel().isRollover() || m.getModel().isSelected()) { // [ws] en hier is het weer selected!
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
		if (!m.getModel().isEnabled()) {
			
		}
		
		
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		// Layout the menu, i.e. determine the place for icon and text
		FontMetrics f = m.getFontMetrics(m.getFont());
		String clippedText = layout(m, f, c.getWidth(), c.getHeight());
		
		// Draw icon
		if (m.getIcon() != null) {
			m.getIcon().paintIcon(m, g2, paintIconR.x, paintIconR.y);
		}
		
		// Draw text
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g2.setColor(Color.BLACK);
		g2.setFont(m.getFont());
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
	private String layout(JMenu m, FontMetrics fm, int width, int height) {
		Insets insets = m.getInsets(null);
		String text = m.getText();
		Icon icon = (m.isEnabled()) ? m.getIcon() : m.getDisabledIcon();
		Rectangle paintViewR = new Rectangle();
		paintViewR.x = insets.left;
		paintViewR.y = insets.top;
		paintViewR.width = width - (insets.left + insets.right);
		paintViewR.height = height - (insets.top + insets.bottom);
		paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
		paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;
		return layoutCL(m, fm, text, icon, paintViewR, paintIconR,
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
        JMenu m,
        FontMetrics fontMetrics,
        String text,
        Icon icon,
        Rectangle viewR,
        Rectangle iconR,
        Rectangle textR)
    {
        return SwingUtilities.layoutCompoundLabel(
            (JComponent) m,
            fontMetrics,
            text,
            icon,
            m.getVerticalAlignment(),
            m.getHorizontalAlignment(),
            m.getVerticalTextPosition(),
            m.getHorizontalTextPosition(),
            viewR,
            iconR,
            textR,
            m.getIconTextGap());
    }
}
