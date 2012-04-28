package joxy;

import java.beans.PropertyChangeEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextPaneUI;

public class JoxyTextPaneUI extends JoxyEditorPaneUI {
	
	public static ComponentUI createUI(JComponent c) {
		//c.setOpaque(false);
		return new JoxyTextPaneUI(c);
	}

    public JoxyTextPaneUI(JComponent c) {
        super(c);
    }
}
