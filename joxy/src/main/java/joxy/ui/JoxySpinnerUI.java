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

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.*;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSpinnerUI;

import joxy.painter.InputFieldPainter;

/**
 * Joxy's UI delegate for the JSpinner.
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxySpinnerUI extends BasicSpinnerUI {
	
	/**
	 * The painter for the input field.
	 */
	private InputFieldPainter fieldPainter = new InputFieldPainter();
	
    public static ComponentUI createUI(JComponent b) {
        return new JoxySpinnerUI();
    }
	
	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
	}
	
	@Override
	protected void installDefaults() {
		super.installDefaults();
		
		spinner.setBorder(null);
		spinner.setOpaque(false);
	}
	
	@Override
	protected void installListeners() {
		super.installListeners();
		
		// A scroll listener to enable scrolling through the elements of the combo box
		MouseWheelListener scrollListener = new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
					
					try {
						if (e.getWheelRotation() == -1) {
							spinner.setValue(spinner.getNextValue());
						}
	
						if (e.getWheelRotation() == 1) {
							spinner.setValue(spinner.getPreviousValue());
						}
					} catch (IllegalArgumentException ex) {
						// in this case, the value was not allowed, so ignore this scroll event
					}
				}
			}
		};
		
		spinner.addMouseWheelListener(scrollListener);
	}
	
	@Override
	public void paint(Graphics g, JComponent c) {

		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
		fieldPainter.paint(g2, 0, 0, spinner.getWidth(), spinner.getHeight());
		
		super.paint(g, c);
	}
	
	@Override
	protected Component createNextButton() {
		JoxyArrowButton b = new JoxyArrowButton(SwingConstants.NORTH);
		installNextButtonListeners(b);
		return b;
	}
	
	@Override
	protected Component createPreviousButton() {
		JoxyArrowButton b = new JoxyArrowButton(SwingConstants.SOUTH);
		installPreviousButtonListeners(b);
		return b;
	}
	
	@Override
	protected JComponent createEditor() {
		JComponent e = super.createEditor();
		
		if (e instanceof DefaultEditor) {
			((DefaultEditor) e).getTextField().putClientProperty("joxy.isEditor", Boolean.TRUE);
		}
		
		return e;
	}
}
