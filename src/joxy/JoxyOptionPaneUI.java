package joxy;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicOptionPaneUI;

/**
 * Remark: this is only a stub to make the option pane non-opaque, so the radial
 * background is visible.
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
