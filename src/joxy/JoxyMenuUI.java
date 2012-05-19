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
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicMenuUI;
import javax.swing.text.View;

import joxy.painter.MenuItemBackgroundPainter;
import joxy.utils.JoxyGraphics;

/**
 * Joxy's UI delegate for the JMenu.
 * 
 * <p>JMenus support animation, see {@link JoxyButtonUI} for more
 * explanation on this. In JMenus we have a MenuListener that controls the
 * animation for the "selected" state.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyMenuUI extends BasicMenuUI {

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
	private MenuListener menuOpenListener;
	
	private boolean hovered = false;
	private boolean menuOpen = false;
    
	public static ComponentUI createUI(JComponent c) {
		JoxyMenuUI menuUI = new JoxyMenuUI();
		return menuUI;
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
		
		menuOpenListener = new MenuListener() {
			
			@Override
			public void menuSelected(MenuEvent e) {
				menuOpen = true;
				hoverTimer.start();
			}
			
			@Override
			public void menuDeselected(MenuEvent e) {
				menuOpen = false;
				hoverTimer.start();
			}
			
			@Override
			public void menuCanceled(MenuEvent e) {
				menuOpen = false;
				hoverTimer.start();
			}
		};
		
		((JMenu) menuItem).addMenuListener(menuOpenListener);
		
		createTimers();
	}
	
	@Override
	protected void uninstallListeners() {
		super.uninstallListeners();
		
		menuItem.removeMouseListener(hoverListener);
		((JMenu) menuItem).removeMenuListener(menuOpenListener);
	}
	
	private void createTimers() {
		hoverTimer = new Timer(40, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (hovered || menuOpen) {
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
		// TODO Auto-generated method stub
		//super.paint(g, c);
		
		JMenu m = (JMenu) c;
		
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		

		// draw the hover
		MenuItemBackgroundPainter.paint(g2, 2, 1, m.getWidth() - 4, m.getHeight() - 3, hoverAmount);
		
		// TODO Draw disabled buttons differently
		if (!m.getModel().isEnabled()) {
			
		}
		
		
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		// Layout the menu, i.e. determine the place for icon and text
		FontMetrics f = m.getFontMetrics(m.getFont());
		String clippedText = layout(m, f, c.getWidth(), c.getHeight());
		
		// Draw icon
		if (m.getIcon() != null) {
			m.getIcon().paintIcon(m, g2, paintIconR.x, paintIconR.y);
		}
		
		// Draw text
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g2.setColor(Color.BLACK);
		g2.setFont(m.getFont());
		View v = (View) c.getClientProperty(BasicHTML.propertyKey);
		if (v != null) { // Text contains HTML
			v.paint(g2, paintTextR);
		} else { // No HTML, draw ourselves
			int w = f.stringWidth(clippedText);
			int h = f.getHeight();
			// TODO [ws] That 3 added to the x coordinate is a temporary fix for the disalignment of menu bar items.
			JoxyGraphics.drawString(g2, clippedText, paintTextR.x + (paintTextR.width - w) / 2 + 3, paintTextR.y + (paintTextR.height + h) / 2 - 3);
		}
	}
	
	/**
	 * This method is copied from the BasicLabelUI class.
	 * What it does exactly (especially in combination with the "layoutCL" method
	 * that is also copied from BasicLabelUI) we don't quite understand, but hey,
	 * it works...
	 */
	private String layout(JMenu m, FontMetrics fm, int width, int height) {
		Insets insets = m.getInsets(null);
		String text = m.getText();
		Icon icon = (m.isEnabled()) ? m.getIcon() : m.getDisabledIcon();
		Rectangle paintViewR = new Rectangle();
		paintViewR.x = insets.left;
		paintViewR.y = insets.top;
		paintViewR.width = width - (insets.left + insets.right);
		paintViewR.height = height - (insets.top + insets.bottom);
		paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
		paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;
		return layoutCL(m, fm, text, icon, paintViewR, paintIconR,
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
        JMenu m,
        FontMetrics fontMetrics,
        String text,
        Icon icon,
        Rectangle viewR,
        Rectangle iconR,
        Rectangle textR)
    {
        return SwingUtilities.layoutCompoundLabel(
            m,
            fontMetrics,
            text,
            icon,
            m.getVerticalAlignment(),
            m.getHorizontalAlignment(),
            m.getVerticalTextPosition(),
            m.getHorizontalTextPosition(),
            viewR,
            iconR,
            textR,
            m.getIconTextGap());
    }
}
