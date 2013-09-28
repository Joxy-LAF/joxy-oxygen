package joxy.painter;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.UIManager;

import joxy.utils.ColorUtils;

/**
 * Painter for a button slab. A button slab is the shape that makes up a button.
 * 
 * This painter doesn't need any data (at the moment).
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class ButtonSlabPainter extends Painter {

	private static final int ARC = 8;
	
	/**
	 * The slab's background colour.
	 */
	private Color color = Color.WHITE;
	
	private Color light = Color.WHITE;
	
	/**
	 * Sets the slab's background colour for the next calls to
	 * {@link #paint(Graphics2D, int, int, int, int)}.
	 * 
	 * <p>Note: you may want to use {@link #setColor(JComponent)} instead.</p>
	 * 
	 * @param c The new background colour.
	 */
	public void setColor(Color c) {
		if (this.color.equals(c)) {
			return;
		}
		
		this.color = c;
		this.light = ColorUtils.mix(ColorUtils.calcLightColor(c), c, 0.3f);
		
		clearCache();
	}
	
	/**
	 * Utility method that sets the slab's background colour to the appropriate
	 * colour for the component's position in the window.
	 * 
	 * @param c The component to set the background colour for. The component needs to have
	 * a {@link JRootPane} anchestor.
	 * @see ColorUtils#backgroundColor(Color, javax.swing.JComponent, double, double)
	 */
	public void setColor(JComponent c) {
		Color color = ColorUtils.backgroundColor(c.getBackground(), c, c.getWidth() / 2, c.getHeight() / 2);
		
		setColor(color);
	}
	
	@Override
	protected void paintObject(Graphics2D g2, float width, float height) {
//		GradientPaint top = new GradientPaint(0, 2, new Color(241, 240, 239), 0, height, new Color(223, 220, 217));
//		GradientPaint top = new GradientPaint(0, 2, ColorUtils.darken(UIManager.getColor("Button.background"), 0.9f), 0, height, UIManager.getColor("Button.background"));
		GradientPaint gradient = new GradientPaint(0, 2, light, 0, height, color);
		g2.setPaint(gradient);
		g2.fill(new RoundRectangle2D.Double(0, 0, width, height, ARC, ARC));
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(0.2f));
		g2.draw(new RoundRectangle2D.Double(0, 0, width - 1, height - 1, ARC, ARC));
	}
}
