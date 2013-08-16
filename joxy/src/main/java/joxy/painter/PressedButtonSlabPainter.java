package joxy.painter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Painter for a pressed button slab. A pressed button slab is the shape that makes
 * up a pressed button.
 */
public class PressedButtonSlabPainter {

	private static final int ARC = 8;
	
	/**
	 * Draws the pressed button slab.
	 * @param g2 The Graphics2D to paint with.
	 * @param x x-coordinate for the left upper corner.
	 * @param y y-coordinate for the left upper corner.
	 * @param width Width of the shape.
	 * @param height Height of the shape.
	 */
	public static void paint(Graphics2D g2, float x, float y, float width, float height) {
		g2.setPaint(Color.WHITE);
 		g2.fill(new RoundRectangle2D.Double(x, y + 1, width, height - 0.6f, ARC, ARC));
        GradientPaint fill = new GradientPaint(0, 0, new Color(203, 200, 197), 0, 6, new Color(223, 220, 217));
 		g2.setPaint(fill);
 		g2.fill(new RoundRectangle2D.Double(x, y, width, height, ARC, ARC));
 		g2.setColor(new Color(169, 161, 158));
 		g2.setStroke(new BasicStroke(0.2f));
 		g2.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, ARC, ARC));
	}

}
