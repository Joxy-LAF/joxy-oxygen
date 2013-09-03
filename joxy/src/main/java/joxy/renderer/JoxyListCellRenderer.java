/**
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
