package joxy.painter;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Painter for a round button slab. A button slab is the shape that makes up a button.
 */
public class RoundButtonSlabPainter {
	
	/**
	 * Draws the button slab.
	 * @param g2 The Graphics2D to paint with.
	 * @param x x-coordinate for the left upper corner.
	 * @param y y-coordinate for the left upper corner.
	 * @param width Width of the shape.
	 * @param height Height of the shape.
	 */
	public static void paint(Graphics2D g2, float x, float y, float width, float height) {
		GradientPaint top = new GradientPaint(0, 2, new Color(241, 240, 239), 0, height, new Color(223, 220, 217));
//		GradientPaint top = new GradientPaint(0, 2, ColorUtils.darken(UIManager.getColor("Button.background"), 0.9f), 0, height, UIManager.getColor("Button.background"));
		g2.setPaint(top);
		g2.fill(new Ellipse2D.Double(x, y, width, height));
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(0.2f));
		g2.draw(new Ellipse2D.Double(x, y, width - 1, height - 1));
	}

}
