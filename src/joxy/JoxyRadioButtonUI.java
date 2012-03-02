package joxy;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRadioButtonUI;

import joxy.utils.JoxyGraphics;


/**
 * Class overriding the default RadioButton (BasicRadioButtonUI) to provide a good
 * integration with the Oxygen KDE style. Part of the Joxy Look and Feel.
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyRadioButtonUI extends BasicRadioButtonUI {
	
	public static ComponentUI createUI(JComponent c) {
		c.setOpaque(false);
		((AbstractButton) c).setRolloverEnabled(true);
		JoxyRadioButtonUI ui = new JoxyRadioButtonUI();
		return ui;
	}
	
	@Override
	public synchronized void paint(Graphics g, JComponent c) {
		//super.paint(g, c);
		
		AbstractButton b = (AbstractButton) c;
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		//int ARC = 8; // width and height for the arc used to round off the rectangle
		g2.translate((c.getHeight() - 16) / 2, (c.getHeight() - 16) / 2);
		
		// If mouse is over the component, draw light blue border
		if (b.getModel().isRollover()) {
			// Rounded rectangle with light blue border
			//g2.setColor(new Color(110, 214, 255));
			Color hover = UIManager.getColor("Button.hover"); // [ws] TODO moet dit naar de initialisatie?
			g2.setColor(hover);
			g2.setStroke(new BasicStroke(2f));
			g2.draw(new Ellipse2D.Double(0, 0, 16, 16));
			g2.setColor(new Color(hover.getRed(), hover.getGreen(), hover.getBlue(), 128));
			g2.setStroke(new BasicStroke(5f));
			g2.draw(new Ellipse2D.Double(0, 0, 16, 16));
		} else {
			// If it has the focus, draw dark blue border
			if (b.isFocusOwner()) {
				// Rounded rectangle with dark blue border
				//g2.setColor(new Color(58, 167, 221));
				Color focus = UIManager.getColor("Button.focus"); // [ws] TODO moet dit naar de initialisatie?
				g2.setColor(focus);
				g2.setStroke(new BasicStroke(2f));
				g2.draw(new Ellipse2D.Double(0, 0, 16, 16));
				g2.setColor(new Color(focus.getRed(), focus.getGreen(), focus.getBlue(), 128));
				g2.setStroke(new BasicStroke(5f));
				g2.draw(new Ellipse2D.Double(0, 0, 16, 16));
			} else {
				// No blue borders necessary, so draw shadow
				g2.setColor(new Color(0, 0, 0, 10));
				g2.fill(new Ellipse2D.Double(-1, -1, 20, 20));
				g2.setColor(new Color(0, 0, 0, 40));
				g2.fill(new Ellipse2D.Double(-0, 0, 18, 18));
				g2.fill(new Ellipse2D.Double(1, 1, 16, 16));
			}
		}

		// TODO Draw disabled checkboxes differently
		
		// The inside part
		GradientPaint top = new GradientPaint(0, 0, new Color(231, 229, 226), 0, 15, new Color(221, 219, 215));
		// [ws] Kleuren door met GIMP te meten; dit waren jouw kleuren:
		// GradientPaint top = new GradientPaint(0, 0, new Color(244, 243, 243), 0, 0.7f * 16, new Color(237, 236, 236));
		// Als de kleuren er bij jou echt zo uitzien, moeten we ze maar uit kdeglobals halen.
		g2.setPaint(top);
		g2.fill(new Ellipse2D.Double(0, 0, 17, 17));
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(0.2f));
		g2.draw(new Ellipse2D.Double(0, 0, 16, 16));
		
		// Draw small circle if needed
		if (b.getModel().isSelected()) {
			g2.setColor(Color.BLACK);
			g2.fill(new Ellipse2D.Double(6, 6, 5.5, 5.5));
		}
		
		// Draw text
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g2.setColor(Color.BLACK);
		g2.setFont(b.getFont());
		JoxyGraphics.drawString(g2, b.getText(), 22, 13);
		// Done :)
		g2.translate(-(c.getHeight() - 16) / 2, -(c.getHeight() - 16) / 2);
	}
	
	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		
		//c.setFont(new Font("Ubuntu", Font.PLAIN, 12));
		
		Font f = UIManager.getFont("Button.font");
		c.setFont(f);
	}
	
}
