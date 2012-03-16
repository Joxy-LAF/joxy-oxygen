package joxy;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicListUI;

public class JoxyListUI extends BasicListUI {

	public static ComponentUI createUI(JComponent c) {
		JoxyListUI ui = new JoxyListUI();
		return ui;
	}
	
	@Override
	protected void installDefaults() {
		super.installDefaults();
		
		list.setBorder(null);
		list.setSelectionBackground(Color.BLUE);
	}
}
