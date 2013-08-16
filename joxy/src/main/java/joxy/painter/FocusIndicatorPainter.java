package joxy.painter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.UIManager;

/**
 * Painter for a round focus indicator. A focus indicator is drawn around a button if it has
 * the focus.
 */
public class FocusIndicatorPainter {

	private static Color focus = UIManager.getColor("Button.focus");
	private static final int ARC = 8;
	
	/**
	 * Draws the focus indicator. Note that a focus indicator is  a 'border' around the
	 * rectangle given here.
	 * @param g2 The Graphics2D to paint with.
	 * @param x x-coordinate for the left upper corner.
	 * @param y y-coordinate for the left upper corner.
	 * @param width Width of the shape.
	 * @param height Height of the shape.
	 * @param opacity Opacity of the focus indicator, with 0 completely transparent, and 255 completely opaque.
	 */
	public static void paint(Graphics2D g2, float x, float y, float width, float height, int opacity) {
		// Rounded rectangle with dark blue border
		//g2.setColor(new Color(58, 167, 221));
		g2.setColor(new Color(focus.getRed(), focus.getGreen(), focus.getBlue(), opacity));
		g2.setStroke(new BasicStroke(2f));
		g2.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, ARC, ARC));
		g2.setColor(new Color(focus.getRed(), focus.getGreen(), focus.getBlue(), opacity / 2));
		g2.setStroke(new BasicStroke(5f));
		g2.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, ARC, ARC));
	}

}
