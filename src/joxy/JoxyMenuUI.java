package joxy;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

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

	/** Amount of hover and focus, from 0 to 255 */
	private int hoverAmount = 0;
	
	/** Timers for the animation */
	private Timer hoverTimer;
	
	/** Listeners for the animation */
	private MouseListener hoverListener;
	private MenuListener menuOpenListener;
	
	private boolean hovered = false;
	private boolean menuOpen = false;
	
	/** Rectangles for the layout */
	protected Rectangle iconRect, textRect, accRect, arrowRect;
    
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
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>This method is overridden, so that the width will actually
	 * be big enough for both the MenuItem text and the accelerator
	 * to fit in non-toplevel menus.</p>
	 * 
	 * <p>Only in non-toplevel menus, we take accelerators into account.</p>
	 * 
	 * @see JoxyMenuItemUI
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
		// For the width, we take the maximum of the width as defined by the super
		// and the width of the text plus the icon space
		// Note that a toplevel menu will not have an icon and therefore, we do not take into account space
		// for it.
		// Also note that a JMenu cannot have an accelerator, it will throw a RuntimeException if you try
		// to add one! Hence, we do not have to take an accelerator into account...
		int minW = Math.max((super.getPreferredSize(c) != null ? super.getPreferredSize(c).width : 0),
							textWidth + (isTopLevelMenu() ? 0 : textRect.x));
		int minH = (super.getPreferredSize(c) != null ? super.getPreferredSize(c).height : f.getHeight());
		return (super.getPreferredSize(c) != null ? new Dimension(minW, minH) : null);
	}
	
	@Override
	public void paint(Graphics g, JComponent c) {
		JMenuItem mi = (JMenuItem) c;
		
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

		if (isTopLevelMenu()) {
			// draw the hover
			MenuItemBackgroundPainter.paint(g2, 2, 1, mi.getWidth() - 4, mi.getHeight() - 3, hoverAmount);
		} else {
			// draw the hover, with right side gradient
			BufferedImage hoverImage = new BufferedImage(mi.getWidth(), mi.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2im = (Graphics2D) hoverImage.getGraphics();
			g2im.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2im.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			
			MenuItemBackgroundPainter.paint(g2im, 2, 1, mi.getWidth() - 4, mi.getHeight() - 3, hoverAmount);
			g2im.setComposite(AlphaComposite.DstIn);
			GradientPaint gradient = new GradientPaint(mi.getWidth() - 30, 0, new Color(0, 0, 0, 255), mi.getWidth(), 0, new Color(0, 0, 0, 0));
			g2im.setPaint(gradient);
			g2im.fillRect(mi.getWidth() - 30, 0, 30, mi.getHeight());
			g2.drawImage(hoverImage, 0, 0, null);
		}
		
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		
		if (isTopLevelMenu()) {
			
			// draw text
			FontMetrics f = menuItem.getFontMetrics(menuItem.getFont());
			int w = f.stringWidth(menuItem.getText());
			int h = f.getHeight();
			
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
			g2.setColor(Color.BLACK);
			g2.setFont(mi.getFont());
			View v = (View) c.getClientProperty(BasicHTML.propertyKey);
			if (v != null) { // Text contains HTML
				v.paint(g2, textRect);
			} else { // No HTML, draw ourselves
				// TODO [ws] That 3 added to the x coordinate is to be consistent with JoxyMenuUI.
				JoxyGraphics.drawString(g2, menuItem.getText(), (menuItem.getWidth() - w) / 2, (menuItem.getHeight() + h) / 2 - 3);
			}
						
		} else {
			// layout the item
			layout();
			
			// draw icon
			if (mi.getIcon() != null) {
				mi.getIcon().paintIcon(mi, g2, iconRect.x, iconRect.y);
			}
			
			// draw text
			FontMetrics f = menuItem.getFontMetrics(menuItem.getFont());
			int h = f.getHeight();
			
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
			g2.setColor(Color.BLACK);
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
			
			// draw arrow
			g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(1.25f));
			
			g2.draw(new Line2D.Double(arrowRect.getMinX(), arrowRect.getMinY(), arrowRect.getMaxX(), arrowRect.getCenterY()));
			g2.draw(new Line2D.Double(arrowRect.getMinX(), arrowRect.getMaxY(), arrowRect.getMaxX(), arrowRect.getCenterY()));
			
			// TODO paint the "fading" effect of the highlight instead of the normal one
		}
	}

	/**
	 * Layout the menu item. Fortunately in KDE this is slightly simpler than in
	 * the default Java LAFs: if there is no icon (also if there is no icon on all
	 * the items in a menu), there is just space maintained as if there was an icon.
	 * That means that if there is no icon, we just are able to assume there is one,
	 * so it is not necessary to look if others do have an icon.
	 * 
	 * Note: this only makes sense for non-toplevel menus.
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
	 * Returns true iff this menu is a top-level menu.
	 * @return Whether this menu has a JMenuBar as a parent.
	 */
	protected boolean isTopLevelMenu() {
		return menuItem.getParent() instanceof JMenuBar;
	}
	
	/**
	 * Return a human-readable representation of the accelerator text.
	 * Copied from MenuItemLayoutHelper.
	 * @param delimiter The string used to delimit the various parts
	 * of the accelerator, such as "+".
	 * @return A string such as "Ctrl+O".
	 */
	private String getAccText(String delimiter) {
        String accText = "";
        KeyStroke accelerator = menuItem.getAccelerator();
        if (accelerator != null) {
            int modifiers = accelerator.getModifiers();
            if (modifiers > 0) {
                accText = KeyEvent.getKeyModifiersText(modifiers);
                accText += delimiter;
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
