package joxy;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicListUI;

/**
 * Only a stub.
 */
public class JoxyListUI extends BasicListUI {

	public static ComponentUI createUI(JComponent c) {
		JoxyListUI ui = new JoxyListUI();
		return ui;
	}
	
	@Override
	protected void installDefaults() {
		super.installDefaults();
	}
}
