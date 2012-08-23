package joxy.painter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.UIManager;

/**
 * Painter for a hover indicator. A hover indicator is drawn around a button if the
 * mouse is hovered over it.
 */
public class TextFieldHoverIndicatorPainter {

	private static Color hover = UIManager.getColor("Button.hover");
	private static final int ARC = 6;
	
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
		g2.setColor(new Color(hover.getRed(), hover.getGreen(), hover.getBlue(), opacity));
		g2.setStroke(new BasicStroke(1f));
		g2.draw(new RoundRectangle2D.Float(x + 1, y + 1, width - 3, height - 4, ARC, ARC));
		g2.setColor(new Color(hover.getRed(), hover.getGreen(), hover.getBlue(), opacity / 2));
		g2.setStroke(new BasicStroke(1f));
		g2.draw(new RoundRectangle2D.Float(x + 2, y + 2, width - 5, height - 6, ARC - 2, ARC - 2));
		g2.setColor(new Color(hover.getRed(), hover.getGreen(), hover.getBlue(), opacity / 4));
		g2.setStroke(new BasicStroke(1f));
		g2.draw(new RoundRectangle2D.Float(x + 3, y + 3, width - 7, height - 8, ARC - 4, ARC - 4));
	}

}
