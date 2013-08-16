package joxy.ui;

import java.awt.Graphics;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicViewportUI;

/**
 * Joxy's UI delegate for the JViewport.
 * 
 * <p>This is just a stub.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyViewportUI extends BasicViewportUI {
	
	public static ComponentUI createUI(JComponent c) {
		JoxyViewportUI viewportUI = new JoxyViewportUI();
		return viewportUI;
	}
	
	@Override
	public void paint(Graphics g, JComponent c) {
		
		// TODO handle Joxy's custom border painting
		
		super.paint(g, c);
	}

}
