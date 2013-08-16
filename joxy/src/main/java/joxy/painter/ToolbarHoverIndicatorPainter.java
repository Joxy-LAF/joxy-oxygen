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

	private static Color hover = UIManager.getColor("Button.focus");
	private static final int ARC = 8;
	
	/**
	 * Draws the hover indicator. Note that a hover indicator is  a 'border' around the
	 * rectangle given here.
	 * @param g2 The Graphics2D to paint with.
	 * @param x x-coordinate for the left upper corner.
	 * @param y y-coordinate for the left upper corner.
	 * @param width Width of the shape.
	 * @param height Height of the shape.
	 * @param opacity Opacity of the hover indicator, with 0 completely transparent, and 255 completely opaque.
	 */
	public static void paint(Graphics2D g2, float x, float y, float width, float height, int opacity) {
		g2.setColor(new Color(hover.getRed(), hover.getGreen(), hover.getBlue(), opacity / 2));
		g2.setStroke(new BasicStroke(1.1f));
		g2.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, ARC, ARC));
	}

}
