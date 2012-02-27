package joxy;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicEditorPaneUI;
import javax.swing.tree.DefaultTreeCellEditor.EditorContainer;

public class JoxyEditorPaneUI extends BasicEditorPaneUI {
	
	public static ComponentUI createUI(JComponent c) {
		JoxyEditorPaneUI ui = new JoxyEditorPaneUI();
		return ui;
	}
	
	@Override
	protected void installDefaults() {
		super.installDefaults();
	}
	
	
}
