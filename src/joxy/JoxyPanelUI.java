package joxy;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

/**
 * Joxy's UI delegate for the JPanel.
 * 
 * <p>It changes nothing, except for making every panel non-opaque.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyPanelUI extends BasicPanelUI {
	
	public static ComponentUI createUI(JComponent c) {
		c.setOpaque(false);
		JoxyPanelUI panelUI = new JoxyPanelUI();
		return panelUI;
	}

}