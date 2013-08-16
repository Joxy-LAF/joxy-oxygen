package joxy.ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import javax.swing.text.View;

import joxy.painter.RoundButtonSlabPainter;
import joxy.painter.RoundFocusIndicatorPainter;
import joxy.painter.RoundHoverIndicatorPainter;
import joxy.utils.JoxyGraphics;

/**
 * Joxy's UI delegate for the JRadioButton.
 * 
 * <p>This class is largely copied from {@link JoxyButtonUI}.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyRadioButtonUI extends BasicRadioButtonUI {

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
    
	public static ComponentUI createUI(JComponent c) {
		c.setOpaque(false);
		((AbstractButton) c).setRolloverEnabled(true);
		JoxyRadioButtonUI ui = new JoxyRadioButtonUI();
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
		g2.fill(new Ellipse2D.Double(iconRect.x, iconRect.y, iconRect.width, iconRect.height));
		g2.setColor(new Color(0, 0, 0, 40));
		g2.fill(new Ellipse2D.Double(iconRect.x, iconRect.y + 1, iconRect.width, iconRect.height));
		g2.fill(new Ellipse2D.Double(iconRect.x - 1, iconRect.y + 1, iconRect.width + 2, iconRect.height + 1));
		
		// decorations
		RoundFocusIndicatorPainter.paint(g2, iconRect.x, iconRect.y, iconRect.width, iconRect.height, focusAmount);
		RoundHoverIndicatorPainter.paint(g2, iconRect.x, iconRect.y, iconRect.width, iconRect.height, hoverAmount);
		
		// slab
		RoundButtonSlabPainter.paint(g2, iconRect.x, iconRect.y, iconRect.width, iconRect.height);
		
		// the circle
		if (b.getModel().isPressed() || (b.getModel().isSelected() && !b.isEnabled())) { // [ws] TODO only for KDE 4.8
			g2.setColor(new Color(0, 0, 0, 50));
			g2.translate(iconRect.getCenterX() - 9, iconRect.getCenterY() - 9);
			g2.fill(new Ellipse2D.Double(6, 6, 5.5, 5.5));
			g2.translate(-iconRect.getCenterX() + 9, -iconRect.getCenterY() + 9);
		} else if (b.getModel().isSelected()) {
			g2.setColor(Color.BLACK);
			g2.translate(iconRect.getCenterX() - 9, iconRect.getCenterY() - 9);
			g2.fill(new Ellipse2D.Double(6, 6, 5.5, 5.5));
			g2.translate(-iconRect.getCenterX() + 9, -iconRect.getCenterY() + 9);
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
