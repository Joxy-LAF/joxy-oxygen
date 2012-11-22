package joxy;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

import joxy.border.JoxyBevelBorder;
import joxy.border.JoxyTitledBorder;

/**
 * Joxy's UI delegate for the JPanel.
 * 
 * <p>It changes nothing, except for making every panel non-opaque.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyPanelUI extends BasicPanelUI {
	
	/**
	 * The old border (from before the new Joxy border was applied).
	 * If this is <code>null</code>, the border has not been swapped.
	 */
	private Border oldBorder;
	
	public static ComponentUI createUI(JComponent c) {
		JoxyPanelUI panelUI = new JoxyPanelUI();
		return panelUI;
	}
	
	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		
		c.setOpaque(false);
	}
	
	@Override
	public void uninstallUI(JComponent c) {
		super.uninstallUI(c);
		
		// revert the Joxy border to the default border
		// TODO for some reason, this doesn't work yet...
		if (oldBorder != null) {
			c.setBorder(oldBorder);
		}
	}
	
	@Override
	public void update(Graphics g, JComponent c) {
		
		// in this method, we try to swap the default Swing borders for nice KDE-like borders
		Border b = c.getBorder();
		
		// note that setBorder repaints the component itself; if we also would do that we
		// get a StackOverflowError
		
		if (b instanceof BevelBorder) {
			oldBorder = b;
			c.setBorder(new JoxyBevelBorder(((BevelBorder) b).getBevelType()));
		}
		if (b instanceof EtchedBorder) {
			oldBorder = b;
			// TODO this is not the correct Joxy border type, but we don't have a real etched border in KDE
			c.setBorder(new JoxyBevelBorder(BevelBorder.RAISED));
		}
		if (b instanceof TitledBorder) {
			oldBorder = b;
			c.setBorder(new JoxyTitledBorder(((TitledBorder) b).getTitle()));
		}
		
		super.update(g, c);
	}

}