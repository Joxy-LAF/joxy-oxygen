package joxy.painter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.geom.RoundRectangle2D;

/**
 * Painter for a dark engraving. A dark engraving is e.g. the track of a slider
 * or the background of a pressed button.
 */
public class DarkEngravingPainter {

	private static final int ARC = 8;
	
	/**
	 * Draws the dark engraving.
	 * @param g2 The Graphics2D to paint with.
	 * @param x x-coordinate for the left upper corner.
	 * @param y y-coordinate for the left upper corner.
	 * @param width Width of the shape.
	 * @param height Height of the shape.
	 */
	public static void paint(Graphics2D g2, float x, float y, float width, float height) {
		g2.setPaint(Color.WHITE);
 		g2.fill(new RoundRectangle2D.Double(x, y, width, height + 0.4f, ARC, ARC));
        GradientPaint fill = new GradientPaint(0, 0, new Color(159, 152, 149), 0, 6, new Color(182, 174, 170));
 		g2.setPaint(fill);
 		g2.fill(new RoundRectangle2D.Double(x, y, width, height, ARC, ARC));
 		LinearGradientPaint gr = new LinearGradientPaint(0, y, 0, y + height, new float[]{0, 1}, new Color[]{new Color(80, 77, 74), new Color(159, 152, 149)});
 		g2.setPaint(gr);
 		g2.setStroke(new BasicStroke(0.2f));
 		g2.draw(new RoundRectangle2D.Double(x, y, width - 1, height-1, ARC, ARC));
	}

}
