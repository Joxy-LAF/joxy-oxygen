package joxy;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneUI;

public class JoxySplitPaneUI extends BasicSplitPaneUI {

    public static ComponentUI createUI(JComponent c) {
        return new JoxySplitPaneUI();
    }
    
    @Override
    protected void installDefaults() {
    	super.installDefaults();
    	
    	splitPane.setOpaque(false);
    }
}
