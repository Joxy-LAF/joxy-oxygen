package joxy.ui;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/**
 * Joxy's UI delegate for the JTextPane.
 * 
 * <p>This is just a subclass of {@link JoxyEditorPaneUI}.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyTextPaneUI extends JoxyEditorPaneUI {
	
	public static ComponentUI createUI(JComponent c) {
		return new JoxyTextPaneUI(c);
	}

    public JoxyTextPaneUI(JComponent c) {
        super(c);
    }
}
