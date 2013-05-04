package joxy;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicColorChooserUI;

/**
 * Joxy's UI delegate for the JColorChooser.
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyColorChooserUI extends BasicColorChooserUI {

    public static ComponentUI createUI(JComponent c) {
        return new JoxyColorChooserUI();
    }
    
	@Override
	protected void installDefaults() {
		super.installDefaults();
		chooser.setOpaque(false);
	}
}
