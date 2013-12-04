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
