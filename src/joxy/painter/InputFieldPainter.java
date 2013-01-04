package joxy.painter;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import javax.swing.border.BevelBorder;

import joxy.border.JoxyBevelBorder;

/**
 * Painter for an input field such as a text field.
 * 
 * This painter doesn't need any data (at the moment).
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class InputFieldPainter extends Painter {

	private static final int ARC = 4;
	
	/**
	 * Draws the hover indicator. Note that a hover indicator is  a 'border' around the
	 * rectangle given here.
	 * @param g2 The Graphics2D to paint with.
	 * @param x x-coordinate for the left upper corner.
	 * @param y y-coordinate for the left upper corner.
	 * @param width Width of the shape.
	 * @param height Height of the shape.
	 */
	@Override
	protected void paintObject(Graphics2D g2, float width, float height) {
		// background
		g2.setColor(Color.WHITE);
		g2.fill(new RoundRectangle2D.Float(2, 2, width - 4, height - 5, ARC, ARC));

		JoxyBevelBorder.paintActualBorder(g2, 0, 0, width, height, BevelBorder.LOWERED);
	}
}
