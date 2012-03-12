package joxy;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.text.View;

import joxy.utils.JoxyGraphics;


/**
 * Class overriding the default LabelUI (BasicLabelUI) to provide a good
 * integration with the Oxygen KDE style. Part of the Joxy Look and Feel.
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyLabelUI extends BasicLabelUI {
	/**
	 * The Rectangle to paint the icon in.
	 */
    private Rectangle paintIconR = new Rectangle();
    
	/**
	 * The Rectangle to paint the text in.
	 */
    private Rectangle paintTextR = new Rectangle();
	
	public static ComponentUI createUI(JComponent c) {
		c.setOpaque(false);
		JoxyLabelUI ui = new JoxyLabelUI();
		return ui;
	}
	
	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		
		// Pick the correct font from the defaults
		Font f = UIManager.getFont("Button.font");
		c.setFont(f);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Well, this in fact is a copy of the super implementation, but with more comments.</p>
	 */
	@Override
	public void paint(Graphics g, JComponent c) {
		// First switch on anti-aliasing
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		
		// Get the label's text and icon
		JLabel label = (JLabel) c;
		String text = label.getText();
		Icon icon = (label.isEnabled()) ? label.getIcon() : label.getDisabledIcon();

		// Is there something to paint?
		if ((icon == null) && (text == null)) {
			return;
		}

		// Measure the width and height of the font
		FontMetrics fm = label.getFontMetrics(label.getFont());
		
		// Layout the label, i.e. determine the place for icon and text
		String clippedText = layout(label, fm, c.getWidth(), c.getHeight());

		// Paint the icon
		if (icon != null) {
			icon.paintIcon(c, g, paintIconR.x, paintIconR.y);
		}

		// Paint the text
		if (text != null) {
			
			// Get the view for the text; a view is a "component" that renders a javax.swing.text element
			View v = (View) c.getClientProperty(BasicHTML.propertyKey);
			
			if (v != null) {
				// We have HTML text, so delegate painting to the view
				v.paint(g, paintTextR);
			} else {
				// No HTML, so paint the text ourselves
				int textX = paintTextR.x;
				int textY = paintTextR.y + fm.getAscent();

				if (label.isEnabled()) {
					// This method is of the superclass
					paintEnabledText(label, g, clippedText, textX, textY);
				} else {
					// This method is of the superclass
					paintDisabledText(label, g, clippedText, textX, textY);
				}
			}
		}
	}
	
	@Override
	protected void paintEnabledText(JLabel l, Graphics g, String s, int textX, int textY) {
		g.setColor(l.getForeground());
		JoxyGraphics.drawString((Graphics2D) g, s, textX, textY);
	}

	/**
	 * No idea what this does... simply copied from superclass.
	 */
	private String layout(JLabel label, FontMetrics fm, int width, int height) {
		Insets insets = label.getInsets(null);
		String text = label.getText();
		Icon icon = (label.isEnabled()) ? label.getIcon() : label
				.getDisabledIcon();
		Rectangle paintViewR = new Rectangle();
		paintViewR.x = insets.left;
		paintViewR.y = insets.top;
		paintViewR.width = width - (insets.left + insets.right);
		paintViewR.height = height - (insets.top + insets.bottom);
		paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
		paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;
		return layoutCL(label, fm, text, icon, paintViewR, paintIconR,
				paintTextR);
	}
}
