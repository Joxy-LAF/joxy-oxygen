package joxy;

import java.awt.Component;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolBarUI;

public class JoxyToolBarUI extends BasicToolBarUI {

	public static ComponentUI createUI(JComponent b) {
        return new JoxyToolBarUI(b);
    }

    public JoxyToolBarUI(JComponent b) {
		super();
	}
    
	@Override
	protected void installDefaults() {
		// TODO Auto-generated method stub
		super.installDefaults();
		
		toolBar.setBorder(null);
		toolBar.setOpaque(false);
	}
	
	@Override
	protected void setBorderToNonRollover(Component c) {
		if (c instanceof JComponent) {
			// This interacts with the JoxyButtonUI code to remove the background
			// and apply other effects. See JoxyButtonUI.
			((JComponent) c).putClientProperty("isToolbarButton", Boolean.TRUE);
		}
	}
	
	// [ws] TODO implement the other methods of this kind
}
