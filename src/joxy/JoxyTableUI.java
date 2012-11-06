package joxy;

import java.awt.*;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableUI;

import joxy.painter.InputFieldPainter;
import joxy.painter.TextFieldFocusIndicatorPainter;
import joxy.painter.TextFieldHoverIndicatorPainter;

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
		table.getColumnModel().setColumnMargin(0);
		table.setRowMargin(0);
		table.setFillsViewportHeight(true);
	}
}
