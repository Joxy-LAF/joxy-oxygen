package joxy.painter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.UIManager;

/**
 * Painter for an input field such as a text field.
 */
public class InputFieldPainter {

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
		// background
		g2.setColor(new Color(225, 225, 225));
		g2.fill(new RoundRectangle2D.Double(x, y + 1, width, height / 2, ARC, ARC));
		g2.setColor(Color.WHITE);
		g2.fill(new RoundRectangle2D.Double(x, y + 2, width, height - 2, ARC, ARC));

		// border
		g2.setStroke(new BasicStroke(0.5f));
		g2.setColor(new Color(140, 140, 140));
		g2.draw(new RoundRectangle2D.Double(x, y, width, height, ARC, ARC));
	}
}
