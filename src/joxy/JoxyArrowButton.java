package joxy;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.plaf.basic.BasicArrowButton;

public class JoxyArrowButton extends BasicArrowButton {

	public JoxyArrowButton(int direction) {
		super(direction);
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
		Point2D center = new Point2D.Double(getWidth() / 2f, getHeight() / 2f);
		
		double width = 8;
		double height = 4;   // note: should be width / 2
		
		Rectangle2D paintRect = new Rectangle2D.Double(center.getX() - width / 2, center.getY() - height / 2, width, height);
		
		g2.setStroke(new BasicStroke(1.5f));
		
		switch (direction) {
		case SOUTH: default:
			g2.draw(new Line2D.Double(paintRect.getMinX(), paintRect.getMinY(), paintRect.getCenterX(), paintRect.getMaxY()));
			g2.draw(new Line2D.Double(paintRect.getMaxX(), paintRect.getMinY(), paintRect.getCenterX(), paintRect.getMaxY()));
		}
	}
}
