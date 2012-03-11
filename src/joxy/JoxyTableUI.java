package joxy;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableUI;

public class JoxyTableUI extends BasicTableUI {
	
	public static ComponentUI createUI(JComponent c) {
		JoxyTableUI ui = new JoxyTableUI();
		return ui;
	}
	
	@Override
	protected void installDefaults() {
		super.installDefaults();
		
		table.setBorder(null);
		table.setShowGrid(false);
	}
}
