package joxy.painter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.UIManager;

/**
 * Painter for a toolbar hover indicator.
 */
public class ToolbarHoverIndicatorPainter {

	private static final int ARC = 8;
	
	/**
	 * Draws the hover indicator. Note that a hover indicator is  a 'border' around the
	 * rectangle given here.
	 * @param g2 The Graphics2D to paint with.
	 * @param x x-coordinate for the left upper corner.
	 * @param y y-coordinate for the left upper corner.
	 * @param width Width of the shape.
	 * @param height Height of the shape.
	 */
	public static void paint(Graphics2D g2, float x, float y, float width, float height) {
		Color focus = UIManager.getColor("Button.hover"); // [ws] TODO moet dit naar de initialisatie?
		g2.setColor(new Color(focus.getRed(), focus.getGreen(), focus.getBlue(), 128));
		g2.setStroke(new BasicStroke(1.5f));
		g2.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, ARC, ARC));
	}

}
