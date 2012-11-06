package joxy;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicViewportUI;

/**
 * Class overriding the default Viewport (BasicViewportUI) to provide a good
 * integration with the Oxygen KDE style. Part of the Joxy Look and Feel.
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
