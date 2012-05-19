package joxy;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicMenuItemUI;
import javax.swing.text.View;

import joxy.painter.MenuItemBackgroundPainter;
import joxy.utils.JoxyGraphics;

/**
 * Joxy's UI delegate for the JMenuItem.
 * 
 * <p>JMenuItems support animation, see {@link JoxyButtonUI} for more
 * explanation on this. In JMenus we have an ActionListener that controls the
 * animation for if the item is clicked (it should then not be selected
 * anymore).</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyMenuItemUI extends BasicMenuItemUI {
	
	/** The width and height of the arcs that form up
	 *  the corners of the rounded rectangles. */
	public static final int ARC = 8;
	/** The Rectangle to paint the icon in. */
    private Rectangle paintIconR = new Rectangle();
	/** The Rectangle to paint the text in. */
    private Rectangle paintTextR = new Rectangle();

	/** Amount of hover and focus, from 0 to 255 */
	private int hoverAmount = 0;
	
	/** Timers for the animation */
	private Timer hoverTimer;
	
	/** Listeners for the animation */
	private MouseListener hoverListener;
	private ActionListener actionListener;
	
	private boolean hovered = false;
	
	public static ComponentUI createUI(JComponent c) {
		c.setOpaque(false);
		int i = 0;
		if (c instanceof JCheckBoxMenuItem) {
            i = 1;
        } else if (c instanceof JRadioButtonMenuItem) {
            i = 2;
        }
		JoxyMenuItemUI menuItemUI = new JoxyMenuItemUI(i);
		return menuItemUI;
	}

    /**
     * Creates a new JoxyMenuItemUI.
     * @param i Type of the menu item
     */
	public JoxyMenuItemUI(int i) {
	}
	
	@Override
	protected void installDefaults() {
		super.installDefaults();

		menuItem.setOpaque(false);
		menuItem.setRolloverEnabled(true);
		menuItem.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
		menuItem.setFont(UIManager.getFont("Button.font"));
	}
	
	@Override
	protected void installListeners() {
		super.installListeners();
		
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
		menuItem.addMouseListener(hoverListener);
		
		// when one clicks the menu item, it is not hovered anymore
		actionListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				hovered = false;
				hoverTimer.start();
			}
		};
		menuItem.addActionListener(actionListener);
		
		createTimers();
	}
	
	@Override
	protected void uninstallListeners() {
		super.uninstallListeners();
		
		menuItem.removeMouseListener(hoverListener);
		menuItem.removeActionListener(actionListener);
	}
	
	private void createTimers() {
		hoverTimer = new Timer(40, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (hovered) {
					hoverAmount += 40;
				} else {
					hoverAmount -= 40;
				}
				if (hoverAmount > 150) {
					hoverAmount = 150;
					hoverTimer.stop();
				}
				if (hoverAmount < 0) {
					hoverAmount = 0;
					hoverTimer.stop();
				}
				menuItem.repaint();
			}
		});
	}
	
	@Override
	public void paint(Graphics g, JComponent c) {
		
		JMenuItem mi = (JMenuItem) c;
		
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
		// draw the hover
		MenuItemBackgroundPainter.paint(g2, 2, 1, mi.getWidth() - 4, mi.getHeight() - 3, hoverAmount);
		
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

		// Layout the menu item, i.e. determine the place for icon and text
		FontMetrics f = mi.getFontMetrics(mi.getFont());
		String clippedText = layout(mi, f, c.getWidth(), c.getHeight());
		
		// Draw icon
		if (mi.getIcon() != null) {
			mi.getIcon().paintIcon(mi, g2, paintIconR.x, paintIconR.y);
		}
		
		// Draw text
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g2.setColor(Color.BLACK);
		g2.setFont(mi.getFont());
		View v = (View) c.getClientProperty(BasicHTML.propertyKey);
		if (v != null) { // Text contains HTML
			v.paint(g2, paintTextR);
		} else { // No HTML, draw ourselves
			int w = f.stringWidth(clippedText);
			int h = f.getHeight();
			// TODO [ws] That 3 added to the x coordinate is to be consistent with JoxyMenuUI.
			JoxyGraphics.drawString(g2, clippedText, paintTextR.x + (paintTextR.width - w) / 2 + 3, paintTextR.y + (paintTextR.height + h) / 2 - 3);
		}
	}
	
	/**
	 * This method is copied from the BasicLabelUI class.
	 * What it does exactly (especially in combination with the "layoutCL" method
	 * that is also copied from BasicLabelUI) we don't quite understand, but hey,
	 * it works...
	 */
	private String layout(JMenuItem mi, FontMetrics fm, int width, int height) {
		Insets insets = mi.getInsets(null);
		String text = mi.getText();
		Icon icon = (mi.isEnabled()) ? mi.getIcon() : mi.getDisabledIcon();
		Rectangle paintViewR = new Rectangle();
		paintViewR.x = insets.left;
		paintViewR.y = insets.top;
		paintViewR.width = width - (insets.left + insets.right);
		paintViewR.height = height - (insets.top + insets.bottom);
		paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
		paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;
		return layoutCL(mi, fm, text, icon, paintViewR, paintIconR,
				paintTextR);
	}
	
    /**
     * Forwards the call to SwingUtilities.layoutCompoundLabel().
     * This method is here so that a subclass could do Label specific
     * layout and to shorten the method name a little.
     *
     * @see SwingUtilities#layoutCompoundLabel
     */
    protected String layoutCL(
        JMenuItem mi,
        FontMetrics fontMetrics,
        String text,
        Icon icon,
        Rectangle viewR,
        Rectangle iconR,
        Rectangle textR)
    {
        return SwingUtilities.layoutCompoundLabel(
            mi,
            fontMetrics,
            text,
            icon,
            mi.getVerticalAlignment(),
            mi.getHorizontalAlignment(),
            mi.getVerticalTextPosition(),
            mi.getHorizontalTextPosition(),
            viewR,
            iconR,
            textR,
            mi.getIconTextGap());
    }
}
