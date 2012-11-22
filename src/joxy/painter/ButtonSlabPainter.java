package joxy.painter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Painter for a button slab. A button slab is the shape that makes up a button.
 */
public class ButtonSlabPainter extends Painter {

	private static final int ARC = 8;
	
	@Override
	protected void paintObject(Graphics2D g2, float width, float height) {
		GradientPaint top = new GradientPaint(0, 2, new Color(241, 240, 239), 0, height, new Color(223, 220, 217));
//		GradientPaint top = new GradientPaint(0, 2, ColorUtils.darken(UIManager.getColor("Button.background"), 0.9f), 0, height, UIManager.getColor("Button.background"));
		g2.setPaint(top);
		g2.fill(new RoundRectangle2D.Double(0, 0, width, height, ARC, ARC));
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(0.2f));
		g2.draw(new RoundRectangle2D.Double(0, 0, width - 1, height - 1, ARC, ARC));
	}
}
