package joxy;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicColorChooserUI;

import joxy.colorchooser.JoxyColorChooserPanel;

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
	
	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		
		// Remove the preview panel
		chooser.setPreviewPanel(new JPanel());
	}
	
	@Override
	protected AbstractColorChooserPanel[] createDefaultChoosers() {
		return new AbstractColorChooserPanel[]{new JoxyColorChooserPanel()};
	}
}
