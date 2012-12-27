package joxy;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;

/**
 * Joxy's UI delegate for the JScrollPane.
 * 
 * <p>This is only a stub. Note that styling of the scroll bars themselves is
 * managed by {@link JoxyScrollBarUI}.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyScrollPaneUI extends BasicScrollPaneUI {
	
    public static ComponentUI createUI(JComponent c) {
        return new JoxyScrollPaneUI();
    }
    
    @Override
    protected void installDefaults(JScrollPane scrollpane) {
    	super.installDefaults(scrollpane);
    	scrollpane.setBorder(null);
    	scrollpane.setBackground(null);
    	scrollpane.setOpaque(false);
    	scrollpane.getViewport().setOpaque(false);
    	// [ws] TODO We moeten hier eens goed naar kijken.
    	// We kunnen in plaats van dit soort ingecodeerde dingen beter
    	// de defaults gebruiken zoals die in Java zitten.
    }
}
