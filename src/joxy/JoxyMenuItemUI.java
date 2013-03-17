package joxy;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicMenuItemUI;
import javax.swing.text.View;

import joxy.icon.IconEffects;
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

	/** Amount of hover and focus, from 0 to 255 */
	private int hoverAmount = 0;
	
	/** Timers for the animation */
	private Timer hoverTimer;
	
	/** Listeners for the animation */
	private MouseListener hoverListener;
	private ActionListener actionListener;
	
	private boolean hovered = false;
	
	/** Rectangles for the layout */
	protected Rectangle iconRect, textRect, accRect, arrowRect;
	
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
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>This method is overridden, so that the width will actually
	 * be big enough for both the MenuItem text and the accelerator
	 * to fit in non-toplevel menus.</p>
	 */
	@Override
	public Dimension getPreferredSize(JComponent c) {
		// We will use the offset of the text as well, this may have not been set
		if (textRect == null) {
			layout();
		}
		JMenuItem mi = (JMenuItem) c;
		FontMetrics f = menuItem.getFontMetrics(menuItem.getFont());
		int textWidth = f.stringWidth(mi.getText());
		int accTextWidth = f.stringWidth(getAccText("+"));
		if (accTextWidth > 0) {
			accTextWidth += 40; // extra space between accelerator and text
		}
		// For the width, we take the maximum of the width as defined by the super
		// and the width of the text plus the icon space plus some little space between text and accelerator
		int minW = Math.max((super.getPreferredSize(c) != null ? super.getPreferredSize(c).width : 0),
							textWidth + accTextWidth + textRect.x);
		int minH = (super.getPreferredSize(c) != null ? super.getPreferredSize(c).height : f.getHeight());
		return (super.getPreferredSize(c) != null ? new Dimension(minW, minH) : null);
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
		
		hoverTimer.stop();
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
				if (menuItem != null) {
					menuItem.repaint();
				} else {
					hoverTimer.stop();
				}
			}
		});
	}
	
	@Override
	public void paint(Graphics g, JComponent c) {
		
		JMenuItem mi = (JMenuItem) c;
		
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

		// only draw the hover effect if the menu item is enabled
		if (mi.isEnabled()) {
			// draw the hover
			MenuItemBackgroundPainter.paint(g2, 2, 1, mi.getWidth() - 4, mi.getHeight() - 3, hoverAmount);
		}
		
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		
		// layout the item
		layout();
		
		// draw icon
		if (mi.getIcon() != null) {
			if (!mi.isEnabled()) {
				// If the button is disabled, draw the icon fainter
				IconEffects.getDisabledIcon(mi.getIcon()).paintIcon(mi, g2, iconRect.x, iconRect.y);
			} else if (hovered) {
				// If the button is hovered, draw the icon lighter
				IconEffects.getActiveIcon(mi.getIcon()).paintIcon(mi, g2, iconRect.x, iconRect.y);
			} else {
				mi.getIcon().paintIcon(mi, g2, iconRect.x, iconRect.y);
			}
		}
		
		// draw text
		FontMetrics f = menuItem.getFontMetrics(menuItem.getFont());
		int h = f.getHeight();
		
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g2.setColor(mi.getModel().isEnabled() ? mi.getForeground() : UIManager.getColor("Button.disabledForeground"));
		g2.setFont(mi.getFont());
		View v = (View) c.getClientProperty(BasicHTML.propertyKey);
		if (v != null) { // Text contains HTML
			v.paint(g2, textRect);
		} else { // No HTML, draw ourselves
			// TODO [ws] That 3 added to the x coordinate is to be consistent with JoxyMenuUI.
			JoxyGraphics.drawString(g2, menuItem.getText(), textRect.x, textRect.y + (textRect.height + h) / 2 - 3);
		}
		
		// draw accelerator
		if (menuItem.getAccelerator() != null) {
			int w = f.stringWidth(getAccText("+"));
			JoxyGraphics.drawString(g2, getAccText("+"), accRect.x - w, accRect.y + (accRect.height + h) / 2 - 3);
		}
		
		// don't draw the arrow for JMenuItems, since they don't have a submenu
	}

	/**
	 * Layout the menu item. Fortunately in KDE this is slightly simpler than in
	 * the default Java LAFs: if there is no icon (also if there is no icon on all
	 * the items in a menu), there is just space maintained as if there was an icon.
	 * That means that if there is no icon, we just are able to assume there is one,
	 * so it is not necessary to look if others do have an icon.
	 */
	protected void layout() {
		// simply put the icon on the left side of the item, and vertically centered
		int iconWidth, iconHeight;
		
		if (menuItem.getIcon() != null) {
			iconWidth = menuItem.getIcon().getIconWidth();
			iconHeight = menuItem.getIcon().getIconHeight();
		} else {
			iconWidth = 0;
			iconHeight = 0;
		}
		
		iconRect = new Rectangle(4, (menuItem.getHeight() - iconHeight) / 2, iconWidth, iconHeight);
		
		// position the text (width does not matter -- we ignore it)
		int textX = Math.max(22, iconHeight) + 5;
		textRect = new Rectangle(textX, 0, 10, menuItem.getHeight());
		
		// position the arrow
		int arrowWidth = 4;
		int arrowHeight = 7;
		int arrowX = menuItem.getWidth() - arrowWidth - 7;
		arrowRect = new Rectangle(arrowX, (menuItem.getHeight() - arrowHeight) / 2, arrowWidth, arrowHeight);
		
		// position the accelerator
		accRect = new Rectangle(arrowX - 5, 0, 0, menuItem.getHeight());
	}
	
	/**
	 * Return a human-readable representation of the accelerator text.
	 * Copied from MenuItemLayoutHelper.
	 * @param acceleratorDelimiter The string used to delimit the various parts
	 * of the accelerator, such as "+".
	 * @return A string such as "Ctrl+O".
	 */
	private String getAccText(String acceleratorDelimiter) {
        String accText = "";
        KeyStroke accelerator = menuItem.getAccelerator();
        if (accelerator != null) {
            int modifiers = accelerator.getModifiers();
            if (modifiers > 0) {
                accText = KeyEvent.getKeyModifiersText(modifiers);
                accText += acceleratorDelimiter;
            }
            int keyCode = accelerator.getKeyCode();
            if (keyCode != 0) {
                accText += KeyEvent.getKeyText(keyCode);
            } else {
                accText += accelerator.getKeyChar();
            }
        }
        return accText;
    }
}
