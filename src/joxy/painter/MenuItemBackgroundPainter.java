package joxy.painter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.UIManager;

import joxy.utils.ColorUtils;
import joxy.utils.HCYColor;
import joxy.utils.Output;
import joxy.utils.Utils;
import joxy.utils.ColorUtils.ShadeRoles;

/**
 * Painter for the hover background of a menu item.
 */
public class MenuItemBackgroundPainter {

	private static final int ARC = 4;
	
	/**
	 * Draws the item background.
	 * @param g2 The Graphics2D to paint with.
	 * @param x x-coordinate for the left upper corner.
	 * @param y y-coordinate for the left upper corner.
	 * @param width Width of the shape.
	 * @param height Height of the shape.
	 * @param opacity Opacity of the hover indicator, with 0 completely transparent, and 255 completely opaque.
	 */
	public static void paint(Graphics2D g2, float x, float y, float width, float height, int opacity) {
		
		Color focus = UIManager.getColor("MenuItem.hover");
		
		if (UIManager.getString("MenuItem.highlight").equals(Utils.MENU_HIGHLIGHT_STRONG)) {
			focus = focus.darker(); // TODO should be: something with tint
		} else if (UIManager.getString("MenuItem.highlight").equals(Utils.MENU_HIGHLIGHT_DARK)) {
			// TODO: [tc] I think the color is linked to the gradient in the menu... suppose we should take that into account.
			focus = ColorUtils.shadeScheme(UIManager.getColor("Window.background"), ShadeRoles.MidShade, UIManager.getInt("General.contrast"));
		}
		
		focus = new Color(focus.getRed(), focus.getGreen(), focus.getBlue(), opacity);
		g2.setColor(focus);
		g2.fill(new RoundRectangle2D.Double(x + 1, y + 1, width - 2, height - 2, ARC, ARC));
		
		// TODO no idea how these colors are originally implemented, but this works pretty well
		Color focusTop = new Color(Math.max(focus.getRed() - 20, 0),
								   Math.max(focus.getGreen() - 20, 0),
								   Math.max(focus.getBlue() - 20, 0), opacity);
		Color focusBottom = new Color(Math.min(focus.getRed() + 20, 255),
									  Math.min(focus.getGreen() + 20, 255),
									  Math.min(focus.getBlue() + 20, 255), opacity);
		
		GradientPaint borderPaint = new GradientPaint(0, y, focusTop, 0, y + height, focusBottom);
		g2.setPaint(borderPaint);
		g2.setStroke(new BasicStroke(1));

		g2.draw(new RoundRectangle2D.Double(x + 1, y + 1, width - 2, height - 2, ARC, ARC));
	}

}
