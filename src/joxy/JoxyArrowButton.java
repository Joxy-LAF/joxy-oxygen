package joxy;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicArrowButton;

import joxy.utils.ColorUtils;

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
			g2.setColor(ColorUtils.mix(Color.BLACK, UIManager.getColor("Button.hover"), ((JoxyButtonUI) getUI()).hoverAmount / 255f));
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
