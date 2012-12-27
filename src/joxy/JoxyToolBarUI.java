package joxy;

import java.awt.Component;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolBarUI;

/**
 * Joxy's UI delegate for the JToolBar.
 * 
 * <p>This doesn't do very much, but it applies {@link AbstractButton#setContentAreaFilled(boolean)}
 * on buttons placed inside the JToolBar. {@link JoxyButtonUI} then renders the button
 * like a KDE toolbar button.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
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
		if (c instanceof AbstractButton) {
			// This interacts with the JoxyButtonUI code to remove the background
			// and apply other effects. See JoxyButtonUI.
			((AbstractButton) c).setContentAreaFilled(false);
		}
	}
	
	// [ws] TODO implement the other methods of this kind
}
