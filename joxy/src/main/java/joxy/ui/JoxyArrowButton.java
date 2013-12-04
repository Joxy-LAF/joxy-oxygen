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

package joxy.ui;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicArrowButton;

import joxy.utils.ColorUtils;

/**
 * A {@link JButton} subclass that implements an arrow button.
 * 
 * <p>It overrides the paint method to draw an arrow instead of a button. It
 * has support for the hover animation by using the {@link JoxyButtonUI}
 * variables that keep track of the hover amount.</p>
 * 
 * <p>Note: JoxyArrowButton is not a UI class, but it still resides in the
 * <code>joxy.ui</code> package because we need to access a package-private
 * field.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyArrowButton extends BasicArrowButton {

	public JoxyArrowButton(int direction) {
		super(direction);
		
		setOpaque(false);
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		
		Point2D center = new Point2D.Double(getWidth() / 2f, getHeight() / 2f + 1);
		
		double width, height;
		
		if (direction == EAST || direction == WEST) {
			width = 3.5;
			height = 7;
		} else {
			width = 7;
			height = 3.5;
		}
		
		// white shadow
		Rectangle2D paintRect = new Rectangle2D.Double(center.getX() - width / 2, center.getY() - height / 2 + 0.8f, width, height);
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(1.25f));
		paintArrow(g2, paintRect);
		
		// black arrow
		paintRect = new Rectangle2D.Double(center.getX() - width / 2, center.getY() - height / 2, width, height);
		if (isEnabled()) {
			if (getName() != null && getName().equals("ComboBox.arrowButton")) {
				g2.setColor(Color.BLACK);
			} else {
				g2.setColor(ColorUtils.mix(Color.BLACK, UIManager.getColor("Button.hover"), ((JoxyButtonUI) getUI()).hoverAmount / 255f));
			}
		} else {
			g2.setColor(Color.GRAY);
		}
		g2.setStroke(new BasicStroke(1.25f));
		paintArrow(g2, paintRect);
	}
	
	private void paintArrow(Graphics2D g2, Rectangle2D paintRect) {
		switch (direction) {
		
		case NORTH:
			g2.draw(new Line2D.Double(paintRect.getMinX(), paintRect.getMaxY(), paintRect.getCenterX(), paintRect.getMinY()));
			g2.draw(new Line2D.Double(paintRect.getMaxX(), paintRect.getMaxY(), paintRect.getCenterX(), paintRect.getMinY()));
			break;
			
		case SOUTH:
			g2.draw(new Line2D.Double(paintRect.getMinX(), paintRect.getMinY(), paintRect.getCenterX(), paintRect.getMaxY()));
			g2.draw(new Line2D.Double(paintRect.getMaxX(), paintRect.getMinY(), paintRect.getCenterX(), paintRect.getMaxY()));
			break;
			
		case EAST:
			g2.draw(new Line2D.Double(paintRect.getMinX(), paintRect.getMinY(), paintRect.getMaxX(), paintRect.getCenterY()));
			g2.draw(new Line2D.Double(paintRect.getMinX(), paintRect.getMaxY(), paintRect.getMaxX(), paintRect.getCenterY()));
			break;
			
		case WEST:
			g2.draw(new Line2D.Double(paintRect.getMaxX(), paintRect.getMinY(), paintRect.getMinX(), paintRect.getCenterY()));
			g2.draw(new Line2D.Double(paintRect.getMaxX(), paintRect.getMaxY(), paintRect.getMinX(), paintRect.getCenterY()));
		}
	}
}
