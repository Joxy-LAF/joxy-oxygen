package joxy.renderer;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

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
    	
    	// TODO paint selection
    	
    	super.paint(g);
    }
	
	@Override
	public Component getListCellRendererComponent(JList<? extends Object> list,
			Object value, int index, boolean isSelected, boolean cellHasFocus) {
		return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	}
}
