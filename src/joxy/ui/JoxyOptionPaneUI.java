package joxy.ui;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicOptionPaneUI;

/**
 * Joxy's UI delegate for the JOptionPane.
 * 
 * <p>This is only a stub that makes the JOptionPane non-opaque, so that
 * the radial background becomes visible.</p>
 * 
 * <p>The various icons (error, warning, etc.) are defined in {@link joxy.utils.Utils#getKDEIconsMap()}.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyOptionPaneUI extends BasicOptionPaneUI {

	public static ComponentUI createUI(JComponent c) {
		return new JoxyOptionPaneUI();
	}

	@Override
	protected void installDefaults() {
		LookAndFeel.installColorsAndFont(optionPane, "OptionPane.background",
				"OptionPane.foreground", "OptionPane.font");
		LookAndFeel.installBorder(optionPane, "OptionPane.border");
		minimumSize = UIManager.getDimension("OptionPane.minimumSize");
		LookAndFeel.installProperty(optionPane, "opaque", Boolean.FALSE);
	}
}
