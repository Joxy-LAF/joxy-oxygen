package joxy;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicEditorPaneUI;

public class JoxyEditorPaneUI extends BasicEditorPaneUI {
	
	public static ComponentUI createUI(JComponent c) {
		JoxyEditorPaneUI ui = new JoxyEditorPaneUI();
		return ui;
	}
	
	
}
