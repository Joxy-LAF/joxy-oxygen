package joxy;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

/**
 * Class overriding the default Panel (BasicPanelUI) to provide a good
 * integration with the Oxygen KDE style. Part of the Joxy Look and Feel.
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