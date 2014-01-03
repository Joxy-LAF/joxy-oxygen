/*
 * Copyright 2013  Thom Castermans  thom.castermans@gmail.com
 * Copyright 2013  Willem Sonke     willemsonke@planet.nl
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License or (at your option) version 3 or any later version
 * accepted by the membership of KDE e.V. (or its successor approved
 * by the membership of KDE e.V.), which shall act as a proxy 
 * defined in Section 14 of version 3 of the license.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package joxy.painter;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import javax.swing.UIManager;

import joxy.color.ColorUtils;
import joxy.color.ColorUtils.ShadeRoles;
import joxy.utils.Utils;

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
