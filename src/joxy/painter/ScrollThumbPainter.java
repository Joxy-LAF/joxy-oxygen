package joxy.painter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.UIManager;

/**
 * Painter for the thumb of a scroll bar.
 */
public class ScrollThumbPainter {

	private static final int ARC = 4;
	
	/**
	 * Draws the thumb.
	 * @param g2 The Graphics2D to paint with.
	 * @param x x-coordinate for the left upper corner.
	 * @param y y-coordinate for the left upper corner.
	 * @param width Width of the shape.
	 * @param height Height of the shape.
	 * @param shadow How much shadow there should be (0-255).
	 */
	public static void paint(Graphics2D g2, float x, float y, float width, float height, int shadow) {
		GradientPaint top = new GradientPaint(0, 2, UIManager.getDefaults().getColor("Window.background"), 0, height, UIManager.getDefaults().getColor("Window.background"));
		g2.setPaint(top);
		g2.fill(new RoundRectangle2D.Double(x + 1, y + 1, width - 2, height - 2, ARC, ARC));
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(0.2f));
		g2.draw(new RoundRectangle2D.Double(x + 1, y + 1, width - 3, height - 3, ARC, ARC));
		g2.setColor(new Color(0, 0, 0, 80 * shadow / 255));
		g2.setStroke(new BasicStroke(1.5f));
		g2.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, ARC, ARC));
	}

}
