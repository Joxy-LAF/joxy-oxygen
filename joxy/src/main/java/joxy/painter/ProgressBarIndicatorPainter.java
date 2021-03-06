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

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.UIManager;

import joxy.color.ColorUtils;

/**
 * Painter for a progress bar indicator.
 */
public class ProgressBarIndicatorPainter extends Painter {

	// In actual code: 2.5f. But it seems Java arcs are different... sigh.
	private static final float ARC = 6;
	
	@Override
	protected void paintObject(Graphics2D g2, float width, float height) {
		
		// this is to prevent a 0-length bar to be drawn => exception
		if (width > 0) {
			/*  De echte kleurtjescode:
	            const QColor highlight( pal.color( QPalette::Highlight ) );
	            const QColor lhighlight( calcLightColor( highlight ) );
	            const QColor color( pal.color( QPalette::Active, QPalette::Window ) );
	            const QColor light( calcLightColor( color ) );
	            const QColor dark( calcDarkColor( color ) );
	            const QColor shadow( calcShadowColor( color ) );
			 */
			
			// TODO replace with correct values
			Color color = UIManager.getColor("Button.hover");
			Color highlight = UIManager.getColor("TextField.selectionBackground"); // Raar maar waar: dit is highlight
			Color lhighlight = ColorUtils.calcLightColor(highlight);
			Color light = ColorUtils.calcLightColor(color);
			Color dark = ColorUtils.calcDarkColor(color);
			// Color shadow = ColorUtils.calcShadowColor(color);
			
			// draw shadow
			// TODO draw shadow
			
			// draw fill
			g2.setColor(ColorUtils.mix(highlight, dark, 0.2f));
			g2.fill(new RoundRectangle2D.Float(0, 0, width, height, ARC, ARC));
			
			// fake radial gradient
			// [ws] note that we here copy the original code straightforward by first
			// creating an image; probably it would be more efficient if we find a way
			// to paint directly on g2
			BufferedImage gradient = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D grg2 = (Graphics2D) gradient.getGraphics();
			grg2.setColor(new Color(0, 0, 0, 0));
			grg2.fill(new Rectangle2D.Float(0, 0, gradient.getWidth(), gradient.getHeight()));
	
			Color transparent = new Color(0, 0, 0, 0);
			
			{
				Color mixed = ColorUtils.mix(lhighlight, light, 0.3f);
				Color mixedTransparent = new Color(mixed.getRed(), mixed.getGreen(), mixed.getBlue(), 0);
				
				LinearGradientPaint mask = new LinearGradientPaint(new Point2D.Float(0, 0), new Point2D.Float(width, 0), new float[]{0f, 0.4f, 0.6f, 1f}, new Color[]{transparent, Color.BLACK, Color.BLACK, transparent});
				LinearGradientPaint radial = new LinearGradientPaint(new Point2D.Float(0, 0), new Point2D.Float(0, height), new float[]{0f, 0.5f, 0.6f, 1f}, new Color[]{mixed, mixedTransparent, mixedTransparent, mixed});
	
				grg2.setPaint(mask);
				grg2.fill(new Rectangle2D.Float(0, 0, width, height));
				grg2.setComposite(AlphaComposite.SrcIn);
				grg2.setPaint(radial);
				grg2.fill(new Rectangle2D.Float(0, 0, width, height));
			}
			
			g2.drawImage(gradient, 0, 0, null);
			
			// draw bevel
			LinearGradientPaint bevel = new LinearGradientPaint(new Point2D.Float(0, 0.5f), new Point2D.Float(0, height - 0.5f), new float[]{0f, 0.5f, 1f}, new Color[]{lhighlight, highlight, ColorUtils.calcDarkColor(highlight)});
			g2.setPaint(bevel);
			g2.setStroke(new BasicStroke(1));
			g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, width, height, ARC, ARC));
			
			// draw bright top edge
			Color mixed = ColorUtils.mix(highlight, light, 0.8f);
			Color mixedTransparent = new Color(mixed.getRed(), mixed.getGreen(), mixed.getBlue(), 0);
			LinearGradientPaint lightHl = new LinearGradientPaint(new Point2D.Float(0, 0), new Point2D.Float(width, 0), new float[]{0f, 0.5f, 1f}, new Color[]{mixedTransparent, mixed, mixedTransparent});
			g2.setPaint(lightHl);
			g2.setStroke(new BasicStroke(1));
			g2.draw(new Line2D.Float(0.5f, 0.5f, width + 0.5f, 0.5f));
			
			/*Color fill = UIManager.getColor("Button.hover"); // TODO een andere key!
			g2.setPaint(new GradientPaint(x, y, fill.darker(), x + width / 2, y, fill, true));
			g2.fill(new RoundRectangle2D.Float(x, y, width, height, ARC, ARC));
			g2.setPaint(new GradientPaint(x, y + 1, new Color(fill.darker().getRed(), fill.darker().getGreen(), fill.darker().getBlue(), 0), x, y + height / 2, fill.darker(), true));
			g2.fill(new RoundRectangle2D.Float(x, y, width, height, ARC, ARC));
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(0.1f));
			g2.draw(new RoundRectangle2D.Float(x, y, width, height, ARC, ARC));*/
			
			// [ws] FIXME hack:
			g2.setColor(Color.WHITE);
		}
	}
}
