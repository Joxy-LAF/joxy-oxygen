package joxy;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
 * Joxy's UI delegate for the JSplitPane.
 * 
 * <p>This is only a stub that makes the panel non-opaque and sets a so-called
 * continuous layout (meaning that the content is immediately updated during
 * scrolling).</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxySplitPaneUI extends BasicSplitPaneUI {

    public static ComponentUI createUI(JComponent c) {
        return new JoxySplitPaneUI();
    }
    
    @Override
    protected void installDefaults() {
    	splitPane.setContinuousLayout(true);
    	
    	super.installDefaults();
    	
    	splitPane.setOpaque(false);
    	splitPane.setBorder(null);
    }
    
    @Override
    public BasicSplitPaneDivider createDefaultDivider() {
    	return new JoxySplitPaneDivider(this);
    }
}
