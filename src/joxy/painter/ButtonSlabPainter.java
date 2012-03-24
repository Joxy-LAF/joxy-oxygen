package joxy.painter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.UIManager;

import joxy.JoxyRootPaneUI;
import joxy.utils.ColorUtils;

/**
 * Painter for a button slab. A button slab is the shape that makes up a button.
 */
public class ButtonSlabPainter {

	private static final int ARC = 8;
	
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
		g2.fill(new RoundRectangle2D.Double(x, y, width, height, ARC, ARC));
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(0.2f));
		g2.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, ARC, ARC));
	}

}
