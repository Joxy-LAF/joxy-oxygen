package joxy;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.plaf.basic.BasicArrowButton;

public class JoxyArrowButton extends BasicArrowButton {

	public JoxyArrowButton(int direction) {
		super(direction);
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		Rectangle2D paintRect = new Rectangle2D.Float(6, 6, getWidth() - 12, getHeight() - 12);
		
		switch (direction) {
		case SOUTH: default:
			g2.draw(new Line2D.Double(paintRect.getMinX(), paintRect.getMinY(), paintRect.getCenterX(), paintRect.getMaxY()));
			g2.draw(new Line2D.Double(paintRect.getMaxX(), paintRect.getMinY(), paintRect.getCenterX(), paintRect.getMaxY()));
		}
	}
}
