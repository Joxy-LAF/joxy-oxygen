/**
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

	/**
	 * The painter for the groove.
	 */
	private DarkEngravingPainter groovePainter = new DarkEngravingPainter();
			
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
		
    	groovePainter.paint(g2, trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }
    
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
    	Graphics2D g2 = (Graphics2D) g;
    	
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
    	HoverIndicatorPainter.paint(g2, thumbBounds.x + 2, thumbBounds.y + 2, thumbBounds.width - 4, thumbBounds.height - 5, hoverAmount);
    	ScrollThumbPainter.paint(g2, thumbBounds.x + 2, thumbBounds.y + 2, thumbBounds.width - 4, thumbBounds.height - 5, 255 - hoverAmount);
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
