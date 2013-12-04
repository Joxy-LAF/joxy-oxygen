/*
 * Copyright 2013  Thom Castermans  thom.castermans@gmail.com
 * Copyright 2013  Willem Sonke     willemsonke@planet.nl
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License or (at your option) version 3 or any later version
 * accepted by the membership of KDE e.V. (or its successor approved
 * by the membership of KDE e.V.), which shall act as a proxy 
 * defined in Section 14 of version 3 of the license.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package joxy.ui;

import java.awt.Component;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolBarUI;

/**
 * Joxy's UI delegate for the JToolBar.
 * 
 * <p>This doesn't do very much, but it applies {@link AbstractButton#setContentAreaFilled(boolean)}
 * on buttons placed inside the JToolBar. {@link JoxyButtonUI} then renders the button
 * like a KDE toolbar button.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyToolBarUI extends BasicToolBarUI {

	public static ComponentUI createUI(JComponent b) {
        return new JoxyToolBarUI(b);
    }

    public JoxyToolBarUI(JComponent b) {
		super();
	}
    
	@Override
	protected void installDefaults() {
		// TODO Auto-generated method stub
		super.installDefaults();
		
		toolBar.setBorder(null);
		toolBar.setOpaque(false);
	}
	
	@Override
	protected void setBorderToNonRollover(Component c) {
		if (c instanceof AbstractButton) {
			c.setFont(UIManager.getFont("Button.toolbarFont"));
			// This interacts with the JoxyButtonUI code to remove the background
			// and apply other effects. See JoxyButtonUI.
			((AbstractButton) c).setContentAreaFilled(false);
		}
	}
	
	// [ws] TODO implement the other methods of this kind
}
