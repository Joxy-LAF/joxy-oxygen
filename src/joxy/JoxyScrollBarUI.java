package joxy;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.Timer;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

import joxy.painter.DarkEngravingPainter;
import joxy.painter.HoverIndicatorPainter;
import joxy.painter.ScrollThumbPainter;

/**
 * Joxy's UI delegate for the JScrollBar.
 * 
 * <p>The JoxyScrollBarUI supports a hover animation for the thumb, see
 * {@link JoxyButtonUI} for more explanation on this.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyScrollBarUI extends BasicScrollBarUI {
	
	/** Amount of hover, from 0 to 255 */
	private int hoverAmount = 0;
	
	/** Timer for the animation */
	private Timer hoverTimer;

	/** Listener for the animation */
	private MouseMotionListener hoverListener;
	
	/** Listener for enabling/disabling the arrow buttons */
	private AdjustmentListener adjustmentListener;
			
    public static ComponentUI createUI(JComponent c) {
        return new JoxyScrollBarUI();
    }
    
    @Override
    protected void installDefaults() {
    	super.installDefaults();
    	
    	scrollbar.setOpaque(false);
    }
    @Override
	protected void installListeners() {
		super.installListeners();
		
		hoverListener = new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				hoverTimer.start();
			}

			@Override
			public void mouseDragged(MouseEvent e) {}
		};
		scrollbar.addMouseMotionListener(hoverListener);
		
		adjustmentListener = new AdjustmentListener() {
			
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				decrButton.setEnabled(e.getValue() > scrollbar.getMinimum());
				incrButton.setEnabled(e.getValue() < scrollbar.getMaximum()-scrollbar.getVisibleAmount());
			}
		};
		scrollbar.addAdjustmentListener(adjustmentListener);

		createTimers();
    }
    
    @Override
	protected void uninstallListeners() {
		super.uninstallListeners();
		
		scrollbar.removeMouseMotionListener(hoverListener);
		scrollbar.removeAdjustmentListener(adjustmentListener);
	}
    
    private void createTimers() {
		hoverTimer = new Timer(40, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JoxyScrollBarUI.super.isThumbRollover()) {
					hoverAmount += 60;
				} else {
					hoverAmount -= 60;
				}
				if (hoverAmount > 255) {
					hoverAmount = 255;
					hoverTimer.stop();
				}
				if (hoverAmount < 0) {
					hoverAmount = 0;
					hoverTimer.stop();
				}
				scrollbar.repaint();
			}
		});
    }
    
    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
    	Graphics2D g2 = (Graphics2D) g;
    	
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
    	DarkEngravingPainter.paint(g2, trackBounds.x + 1, trackBounds.y + 1, trackBounds.width - 2, trackBounds.height - 2);
    }
    
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
    	Graphics2D g2 = (Graphics2D) g;
    	
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
    	HoverIndicatorPainter.paint(g2, thumbBounds.x + 2, thumbBounds.y + 2, thumbBounds.width - 4, thumbBounds.height - 4, hoverAmount);
    	ScrollThumbPainter.paint(g2, thumbBounds.x + 2, thumbBounds.y + 2, thumbBounds.width - 4, thumbBounds.height - 4, 255 - hoverAmount);
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
