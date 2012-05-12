package joxy;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

public class JoxyTextPaneUI extends JoxyEditorPaneUI {
	
	public static ComponentUI createUI(JComponent c) {
		//c.setOpaque(false);
		return new JoxyTextPaneUI(c);
	}

    public JoxyTextPaneUI(JComponent c) {
        super(c);
    }
}
