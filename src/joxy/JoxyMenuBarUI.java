package joxy;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicMenuBarUI;
import javax.swing.plaf.basic.DefaultMenuLayout;

/**
 * Joxy's UI delegate for the JMenuBar.
 * 
 * <p>This delegate only makes the menu bar non-opaque, but that is all that is needed.</p>
 */
public class JoxyMenuBarUI extends BasicMenuBarUI {

	public static ComponentUI createUI(JComponent c) {
		c.setOpaque(false);
		JoxyMenuBarUI menuBarUI = new JoxyMenuBarUI();
		return menuBarUI;
	}
	
    @Override
	protected void installDefaults() {
        if (menuBar.getLayout() == null ||
            menuBar.getLayout() instanceof UIResource) {
            menuBar.setLayout(new DefaultMenuLayout(menuBar,BoxLayout.LINE_AXIS));
        }

        //LookAndFeel.installBorder(menuBar,"MenuBar.border"); // VERY ugly
        LookAndFeel.installColorsAndFont(menuBar,
                                              "MenuBar.background",
                                              "MenuBar.foreground",
                                              "MenuBar.font");
    }
}