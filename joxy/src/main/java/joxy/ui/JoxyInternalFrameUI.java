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

package joxy.ui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 * Joxy's UI delegate for the JInternalFrame.
 * 
 * <p>This is only a stub.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyInternalFrameUI extends BasicInternalFrameUI {

	public static ComponentUI createUI(JComponent c) {
		JoxyInternalFrameUI ui = new JoxyInternalFrameUI((JInternalFrame) c);
		return ui;
	}
	
	public JoxyInternalFrameUI(JInternalFrame b) {
		super(b);
	}
	
	@Override
	protected void installDefaults() {
		super.installDefaults();
		frame.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	}
	
	@Override
	public void paint(Graphics g, JComponent c) {
		super.paint(g, c);
		
		frame.getRootPane().getUI().paint(g, c);
	}
}
