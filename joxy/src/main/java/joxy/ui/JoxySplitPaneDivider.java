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
import java.awt.event.*;

import javax.swing.Timer;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
 * A component that is being used as the divider in the JoxySplitPane.
 * 
 * <p>This component supports hover animations. See the {@link JoxyButtonUI}
 * for details on this mechanism.</p>
 */
public class JoxySplitPaneDivider extends BasicSplitPaneDivider {

	/** Amount of hover and focus, from 0 to 255 */
	private int hoverAmount = 0;
	
	/** Timers for the animation */
	private Timer hoverTimer;
	
	/** Listeners for the animation */
	private MouseListener hoverListener;
	
	private boolean hovered = false;
	
	/**
	 * Creates a new {@link JoxySplitPaneDivider}.
	 * @param ui The UI delegate the divider is used for.
	 */
	public JoxySplitPaneDivider(BasicSplitPaneUI ui) {
		super(ui);
		createListeners();
		createTimers();
	}
	
	private void createListeners() {
		
		hoverListener = new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				hovered = true;
				hoverTimer.start();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				hovered = false;
				hoverTimer.start();
			}
		};
		addMouseListener(hoverListener);
	}
	
	private void createTimers() {
		hoverTimer = new Timer(40, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (hovered) {
					hoverAmount += 25;
				} else {
					hoverAmount -= 25;
				}
				if (hoverAmount > 100) {
					hoverAmount = 100;
					hoverTimer.stop();
				}
				if (hoverAmount < 0) {
					hoverAmount = 0;
					hoverTimer.stop();
				}
				JoxySplitPaneDivider.this.repaint();
			}
		});
	}
	
	
	@Override
	public void paint(Graphics g) {
		g.setColor(new Color(255, 255, 255, hoverAmount));
		g.fillRect(1, 1, getWidth() - 2, getHeight() - 2);
	}

}
