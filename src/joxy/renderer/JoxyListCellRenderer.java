package joxy.renderer;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import javax.swing.*;

/**
 * A custom {@link ListCellRenderer} for Joxy.
 * 
 * <p>This renderer is not used in JLists at the moment instead of the {@link DefaultListCellRenderer},
 * because for some reason setting the <i>List.cellRenderer</i> property doesn't work.</p>
 */
public class JoxyListCellRenderer extends DefaultListCellRenderer {
	
	/**
     * Constructs a default renderer object for an item in a list.
     */
    public JoxyListCellRenderer() {
        super();
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        setName("List.cellRenderer");
    }
    
    @Override
    public void paint(Graphics g) {
    	
    	super.paint(g);
    }
	
	@Override
	public Component getListCellRendererComponent(JList<? extends Object> list,
			Object value, int index, boolean isSelected, boolean cellHasFocus) {
		return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	}
}
