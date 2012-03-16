package joxy;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

import joxy.painter.ButtonSlabPainter;
import joxy.painter.DarkEngravingPainter;

public class JoxyScrollBarUI extends BasicScrollBarUI {
	
    public static ComponentUI createUI(JComponent c) {
        return new JoxyScrollBarUI();
    }
    
    @Override
    protected void installDefaults() {
    	super.installDefaults();
    	
    	scrollbar.setOpaque(false);
    }
    
    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
    	Graphics2D g2 = (Graphics2D) g;
    	
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
    	DarkEngravingPainter.paint(g2, trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }
    
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
    	Graphics2D g2 = (Graphics2D) g;
    	
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
    	ButtonSlabPainter.paint(g2, thumbBounds.x + 2, thumbBounds.y + 2, thumbBounds.width - 4, thumbBounds.height - 4);
    }
    
    @Override
    protected JButton createDecreaseButton(int orientation) {
		JButton button = new JoxyArrowButton(orientation);
		return button;
    }
    
    @Override
    protected JButton createIncreaseButton(int orientation) {
		JButton button = new JoxyArrowButton(orientation);
		return button;
    }
}
