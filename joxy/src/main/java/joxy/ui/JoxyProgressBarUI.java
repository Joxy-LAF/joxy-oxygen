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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;

import joxy.painter.ProgressBarIndicatorPainter;
import joxy.painter.DarkEngravingPainter;

/**
 * Joxy's UI delegate for the JProgressBar.
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyProgressBarUI extends BasicProgressBarUI {
	
	/**
	 * The painter for the groove.
	 */
	private DarkEngravingPainter groovePainter = new DarkEngravingPainter();
	/**
	 * The painter for the progress indicator.
	 */
	private ProgressBarIndicatorPainter indicatorPainter = new ProgressBarIndicatorPainter();
	
	public static ComponentUI createUI(JComponent c) {
		JoxyProgressBarUI progressUI = new JoxyProgressBarUI();
		return progressUI;
	}
	
	@Override
	protected void installDefaults() {
		progressBar.setStringPainted(true);
	}
	
	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		c.setFont(UIManager.getFont("Button.font"));
	}
	
	@Override
	public void paint(Graphics g, JComponent c) {

		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

		paintGroove(g, c);
		
		if (progressBar.isIndeterminate()) {
			paintIndeterminateBox(g, c);
		} else {
	        int amount = getAmountFull(c.getInsets(), c.getWidth(), c.getHeight());
			
			paintProgress(g, c, amount);

	        if (progressBar.isStringPainted()) {
	        	g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

	            paintString(g, 0, 0, c.getWidth(), c.getHeight(), amount, c.getInsets());
	        }
        }
	}
	
	/**
	 * Paints the groove of the progress bar.
	 * @param g The Graphics object to paint with.
	 * @param c The progress bar component.
	 */
	protected void paintGroove(Graphics g, JComponent c) {
		groovePainter.paint((Graphics2D) g, 0, 0, c.getWidth(), c.getHeight());
	}
	
	/**
	 * Paints the progress indicator for indeterminate progress bars.
	 * @param g The Graphics object to paint with.
	 * @param c The progress bar component.
	 */
	protected void paintIndeterminateBox(Graphics g, JComponent c) {
        boxRect = getBox(boxRect);
        
		indicatorPainter.paint((Graphics2D) g, boxRect.x + 2, boxRect.y + 2, boxRect.width - 4, boxRect.height - 5);
	}

	/**
	 * Paints the progress indicator for determinate progress bars.
	 * @param g The Graphics object to paint with.
	 * @param c The progress bar component.
	 * @param amount The amount of progress, indicated as the number of pixels.
	 */
	protected void paintProgress(Graphics g, JComponent c, int amount) {
		indicatorPainter.paint((Graphics2D) g, 2, 2, amount - 4, c.getHeight() - 5);
	}
	
	@Override
	public Dimension getPreferredSize(JComponent c) {
		Dimension flup = super.getPreferredSize(c);
		flup.height += 7;
		return flup;
	}
}
