package joxy;

import java.awt.Component;

import javax.swing.DebugGraphics;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

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
