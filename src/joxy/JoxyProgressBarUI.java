package joxy;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;

import joxy.painter.ProgressBarIndicator;
import joxy.painter.DarkEngravingPainter;

public class JoxyProgressBarUI extends BasicProgressBarUI {

	public static ComponentUI createUI(JComponent c) {
		JoxyProgressBarUI progressUI = new JoxyProgressBarUI();
		return progressUI;
	}
	
	@Override
	protected void installDefaults() {
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
	
	protected void paintGroove(Graphics g, JComponent c) {
		DarkEngravingPainter.paint((Graphics2D) g, 0, 0, c.getWidth(), c.getHeight() - 1);
	}

	protected void paintIndeterminateBox(Graphics g, JComponent c) {
        boxRect = getBox(boxRect);
        
		ProgressBarIndicator.paint((Graphics2D) g, boxRect.x + 1, boxRect.y + 1, boxRect.width - 2, boxRect.height - 3);
	}

	protected void paintProgress(Graphics g, JComponent c, int amount) {
		ProgressBarIndicator.paint((Graphics2D) g, 1, 1, amount - 2, c.getHeight() - 3);
	}
	
	@Override
	public Dimension getPreferredSize(JComponent c) {
		Dimension flup = super.getPreferredSize(c);
		flup.height += 7;
		return flup;
	}
}
