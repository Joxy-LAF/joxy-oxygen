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

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicCheckBoxUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;

import joxy.painter.ButtonSlabPainter;
import joxy.painter.FocusIndicatorPainter;
import joxy.painter.HoverIndicatorPainter;
import joxy.utils.JoxyGraphics;

/**
 * Joxy's UI delegate for the JCheckBox and the JRadioButton.
 * 
 * <p>This class is largely copied from {@link JoxyButtonUI}.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyCheckRadioUI extends BasicCheckBoxUI {

	/** The width and height of the arcs that form up
	 *  the corners of the rounded rectangles. */
	public static final int ARC = 8;
	
	private static final int WIDTH = 16;
	private static final int HEIGHT = 16;

	/** Well uh... some rectangle. */
    private Rectangle viewRect = new Rectangle();
    /** Well uh... some size. */
    private static Dimension size = new Dimension();
	/** The Rectangle to paint the icon in. */
    private Rectangle iconRect = new Rectangle();
	/** The Rectangle to paint the text in. */
    private Rectangle textRect = new Rectangle();
    /** Dummy icon for the layout... we don't use an icon internally, but whatever. */
    private ImageIcon dummyIcon = new ImageIcon(new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_4BYTE_ABGR));

	/** Amount of hover and focus, from 0 to 255 */
	private int hoverAmount = 0, focusAmount = 0;
	
	/** Timers for the animation */
	private Timer hoverTimer, focusTimer;
	
	/** Listeners for the animation */
	private MouseListener hoverListener;
	private FocusListener focusListener;
	
	/**
	 * The painter for the button slab.
	 */
	private ButtonSlabPainter slabPainter = new ButtonSlabPainter();

	/**
	 * Whether this delegate is painting for a JCheckBox (<code>true</code>)
	 * or a JRadioButton (<code>false</code>).
	 */
	private boolean isCheckBox;
    
	public static ComponentUI createUI(JComponent c) {
		c.setOpaque(false);
		((AbstractButton) c).setRolloverEnabled(true);
		JoxyCheckRadioUI ui = new JoxyCheckRadioUI();
		ui.isCheckBox = c instanceof JCheckBox;
		return ui;
	}
	@Override
	protected void installDefaults(AbstractButton b) {
		super.installDefaults(b);

		b.setFont(UIManager.getFont("Button.font"));
	}
	
	@Override
	protected void installListeners(AbstractButton b) {
		super.installListeners(b);
		
		hoverListener = new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				hoverTimer.start();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				hoverTimer.start();
			}
		};
		b.addMouseListener(hoverListener);
		
		focusListener = new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				focusTimer.start();
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				focusTimer.start();
			}
		};
		b.addFocusListener(focusListener);
		
		createTimers(b);
	}
	
	@Override
	protected void uninstallListeners(AbstractButton b) {
		super.uninstallListeners(b);
		
		b.removeMouseListener(hoverListener);
		b.removeFocusListener(focusListener);
	}
	
	private void createTimers(final AbstractButton b) {
		hoverTimer = new Timer(40, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (b.getModel().isRollover()) {
					hoverAmount += 60;
				} else {
					hoverAmount -= 60;
				}
				if (hoverAmount > 255) {
					hoverAmount = 255;
					hoverTimer.stop();
				}
				if (hoverAmount < 0) {
					hoverAmount = 0;
					hoverTimer.stop();
				}
				b.repaint();
			}
		});
		
		focusTimer = new Timer(40, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (b.hasFocus()) {
					focusAmount += 60;
				} else {
					focusAmount -= 60;
				}
				if (focusAmount > 255) {
					focusAmount = 255;
					focusTimer.stop();
				}
				if (focusAmount < 0) {
					focusAmount = 0;
					focusTimer.stop();
				}
				b.repaint();
			}
		});
	}
	
	@Override
	public synchronized void paint(Graphics g, JComponent c) {
		AbstractButton b = (AbstractButton) c;
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
		// Layout the button, i.e. determine the place for icon and text
		FontMetrics fm = b.getFontMetrics(b.getFont());
        Insets i = c.getInsets();
        size = b.getSize(size);
        
        // [ws] TODO why is this necessary?
        iconRect = new Rectangle();
        
        viewRect.x = i.left;
        viewRect.y = i.top;
        viewRect.width = size.width - (i.right + viewRect.x);
        viewRect.height = size.height - (i.bottom + viewRect.y);
		String clippedText = SwingUtilities.layoutCompoundLabel(
	            c, fm, b.getText(), dummyIcon,
	                    b.getVerticalAlignment(), b.getHorizontalAlignment(),
	                    b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
	                    viewRect, iconRect, textRect,
	                    b.getText() == null ? 0 : b.getIconTextGap());

	/*	System.out.println("view " + viewRect);
		System.out.println("icon " + iconRect);
		System.out.println("text " + textRect);
		System.out.println();  */
		
		// shadow
		g2.setColor(new Color(0, 0, 0, 80));
		g2.fill(new RoundRectangle2D.Double(iconRect.x, iconRect.y, iconRect.width, iconRect.height, ARC, ARC));
		g2.setColor(new Color(0, 0, 0, 40));
		g2.fill(new RoundRectangle2D.Double(iconRect.x, iconRect.y + 1, iconRect.width, iconRect.height, ARC, ARC));
		g2.fill(new RoundRectangle2D.Double(iconRect.x - 1, iconRect.y + 1, iconRect.width + 2, iconRect.height + 1, ARC+3, ARC+3));
		
		// decorations
		FocusIndicatorPainter.paint(g2, iconRect.x, iconRect.y, iconRect.width, iconRect.height, focusAmount);
		HoverIndicatorPainter.paint(g2, iconRect.x, iconRect.y, iconRect.width, iconRect.height, hoverAmount);
		
		// slab
		slabPainter.setColor(c);
		slabPainter.setRound(!isCheckBox);
		slabPainter.paint(g2, iconRect.x, iconRect.y, iconRect.width, iconRect.height);
		
		// the circle
		Color fg = b.getForeground();
		if (b.getModel().isPressed() || (b.getModel().isSelected() && !b.isEnabled())) {
			g2.setColor(new Color(fg.getRed(), fg.getGreen(), fg.getBlue(), 50));
			if (isCheckBox) {
				g2.setStroke(new BasicStroke(1.5f));
				g2.translate(iconRect.getCenterX() - 9, iconRect.getCenterY() - 9);
				g2.drawLine(4, 9, 7, 12);
				g2.drawLine(7, 12, 13, 5);
				g2.translate(-iconRect.getCenterX() + 9, -iconRect.getCenterY() + 9);
			} else {
				g2.translate(iconRect.getCenterX() - 9, iconRect.getCenterY() - 9);
				g2.fill(new Ellipse2D.Double(6, 6, 5.5, 5.5));
				g2.translate(-iconRect.getCenterX() + 9, -iconRect.getCenterY() + 9);
			}
		} else if (b.getModel().isSelected()) {
			g2.setColor(fg);
			if (isCheckBox) {
				g2.setStroke(new BasicStroke(1.5f));
				g2.translate(iconRect.getCenterX() - 9, iconRect.getCenterY() - 9);
				g2.drawLine(4, 9, 7, 12);
				g2.drawLine(7, 12, 13, 5);
				g2.translate(-iconRect.getCenterX() + 9, -iconRect.getCenterY() + 9);
			} else {
				g2.translate(iconRect.getCenterX() - 9, iconRect.getCenterY() - 9);
				g2.fill(new Ellipse2D.Double(6, 6, 5.5, 5.5));
				g2.translate(-iconRect.getCenterX() + 9, -iconRect.getCenterY() + 9);
			}
		}
		
		// Draw text
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		// Set text color. In the disabled state, a button has a different text color
		g2.setColor(b.getModel().isEnabled() ? b.getForeground() : UIManager.getColor("Button.disabledForeground"));
		View v = (View) c.getClientProperty(BasicHTML.propertyKey);
		if (v != null) { // Text contains HTML
			v.paint(g2, textRect);
		} else { // No HTML, draw ourselves
			int w = fm.stringWidth(clippedText);
			int h = fm.getHeight();
			JoxyGraphics.drawString(g2, clippedText, textRect.x + (textRect.width - w) / 2, textRect.y + (textRect.height + h) / 2 - 3);
		}
	}
	
	@Override
	public Icon getDefaultIcon() {
		return dummyIcon;
	}
}
