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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
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
import javax.swing.plaf.basic.BasicToggleButtonUI;
import javax.swing.text.View;

import joxy.JoxyLookAndFeel;
import joxy.color.ColorUtils;
import joxy.color.ColorUtils.ShadeRoles;
import joxy.painter.ButtonSlabPainter;
import joxy.painter.DarkEngravingPainter;
import joxy.painter.FocusIndicatorPainter;
import joxy.painter.HoverIndicatorPainter;
import joxy.painter.ToolbarHoverIndicatorPainter;
import joxy.utils.JoxyGraphics;

/**
 * Joxy's UI delegate for the JToggleButton.
 * 
 * <p>This class is largely copied from {@link JoxyButtonUI}, but note that
 * a "selected" toggle button looks different from a "pressed" button.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyToggleButtonUI extends BasicToggleButtonUI {
	
	/** The width and height of the arcs that form up
	 *  the corners of the rounded rectangles. */
	public static final int ARC = 8;
	/** This is a constant that is derived from original code. */
	private static final double SLAB_THICKNESS = 0.45;
	/** The Rectangle to paint the icon in. */
    private Rectangle paintIconR = new Rectangle();
	/** The Rectangle to paint the text in. */
    private Rectangle paintTextR = new Rectangle();
    /** The Rectangle that stores the bounds of this button */
	private static Rectangle boundRectangle = new Rectangle();

	/** Amount of hover and focus, from 0 to 255 */
	private int hoverAmount = 0, focusAmount = 0;
	
	/** Timers for the animation */
	private Timer hoverTimer, focusTimer;
	
	/** Listeners for the animation */
	private MouseListener hoverListener;
	private FocusListener focusListener;
	
	/**
	 * The painter for the button slab, if it is not a toolbar button.
	 */
	private ButtonSlabPainter slabPainter = new ButtonSlabPainter();
	/**
	 * The painter for a selected button background.
	 */
	private DarkEngravingPainter selectedPainter = new DarkEngravingPainter();
	/**
	 * The painter for the pressed button, if it is a toolbar button.
	 */
	private DarkEngravingPainter pressedToolbarPainter = new DarkEngravingPainter();
	
    public static ComponentUI createUI(JComponent c) {
		c.setOpaque(false);
		((AbstractButton) c).setRolloverEnabled(true);
		JoxyToggleButtonUI ui = new JoxyToggleButtonUI();
		return ui;
    }
    
	@Override
	protected void installDefaults(AbstractButton b) {
		super.installDefaults(b);
		
		b.setBorder(BorderFactory.createEmptyBorder());
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
		
		// Check whether the button is a toolbar button; see JoxyToolBarUI
		if (b.isContentAreaFilled()) {
		    if (b.getModel().isPressed() || b.getModel().isSelected()) {
		    	pressedToolbarPainter.paint(g2, 0, 0, c.getWidth(), c.getHeight());
			} else {
				// shadow
				g2.setColor(new Color(0, 0, 0, 80));
				g2.fill(new RoundRectangle2D.Double(2, 2, c.getWidth() - 4, c.getHeight() - 4, ARC, ARC));
				g2.setColor(new Color(0, 0, 0, 40));
				g2.fill(new RoundRectangle2D.Double(2, 3, c.getWidth() - 4, c.getHeight() - 4, ARC, ARC));
				g2.fill(new RoundRectangle2D.Double(1, 3, c.getWidth() - 2, c.getHeight() - 2, ARC+6, ARC+6));

				// decorations
				FocusIndicatorPainter.paint(g2, 2, 2, c.getWidth() - 4, c.getHeight() - 4, focusAmount);
				HoverIndicatorPainter.paint(g2, 2, 2, c.getWidth() - 4, c.getHeight() - 4, hoverAmount);
				
				// slab
				slabPainter.setColor(c);
				slabPainter.paint(g2, 2, 2, c.getWidth() - 4, c.getHeight() - 4);
			}
		} else {
			if (b.getModel().isPressed() || b.getModel().isSelected()) {
				selectedPainter.paint(g2, 0, 0, c.getWidth(), c.getHeight());
			} else {
				// If mouse is over the component, draw hover indicator; note it is a special indicator
				// for toolbar buttons
				ToolbarHoverIndicatorPainter.paint(g2, 2, 2, c.getWidth() - 4, c.getHeight() - 4, hoverAmount);
			}
		}

		
		// TODO Draw disabled buttons differently
		if (!b.getModel().isEnabled()) {
			
		}
		
		
		// Layout the button, i.e. determine the place for icon and text
		FontMetrics f = b.getFontMetrics(b.getFont());
		String clippedText = layout((JToggleButton) c, f, c.getWidth(), c.getHeight());
		
		// Draw icon
		if (b.getIcon() != null) {
			b.getIcon().paintIcon(b, g2, paintIconR.x, paintIconR.y);
		}
		
		// Draw text
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g2.setColor(Color.BLACK);
		View v = (View) c.getClientProperty(BasicHTML.propertyKey);
		if (v != null) { // Text contains HTML
			v.paint(g2, paintTextR);
		} else { // No HTML, draw ourselves
			int w = f.stringWidth(clippedText);
			int h = f.getHeight();
			JoxyGraphics.drawString(g2, clippedText, paintTextR.x + (paintTextR.width - w) / 2, paintTextR.y + (paintTextR.height + h) / 2 - 3);
		}
	}
	
	@Override
	public Dimension getPreferredSize(JComponent c) {
		// Simply pick the preferred size of the BasicButtonUI and make it
		// larger to account for the larger decorations.
		// If you consider this ugly... you are right.
		Dimension dim = super.getPreferredSize(c);
		
		// [ws] Fix for bug with NullPointerExceptions
		// It seems that if the super implementation returns null, we can safely
		// return null as well.
		if (dim == null) {
			return null;
		}
		
		dim.width += 10;
		dim.height += 8;
		
		// Only add to the size if the button has its background painted, i.e. if it is
		// not a toolbar button, and if it has text on it, i.e. if it is not an icon-only button.
		if (((AbstractButton) c).isContentAreaFilled()
				&& !(((AbstractButton) c).getText() == null)
				&& !((AbstractButton) c).getText().equals("")) {
			dim.width += 14;
		}
		
		return dim;
	}
	
	/**
	 * This method is copied from the BasicLabelUI class.
	 * What it does exactly (especially in combination with the "layoutCL" method
	 * that is also copied from BasicLabelUI) we don't quite understand, but hey,
	 * it works...
	 */
	private String layout(AbstractButton button, FontMetrics fm, int width, int height) {
		Insets insets = button.getInsets(null);
		String text = button.getText();
		Icon icon = (button.isEnabled()) ? button.getIcon() : button.getDisabledIcon();
		Rectangle paintViewR = new Rectangle();
		paintViewR.x = insets.left;
		paintViewR.y = insets.top;
		paintViewR.width = width - (insets.left + insets.right);
		paintViewR.height = height - (insets.top + insets.bottom);
		paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
		paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;
		return layoutCL(button, fm, text, icon, paintViewR, paintIconR,
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
    	AbstractButton button,
        FontMetrics fontMetrics,
        String text,
        Icon icon,
        Rectangle viewR,
        Rectangle iconR,
        Rectangle textR)
    {
        return SwingUtilities.layoutCompoundLabel(
            button,
            fontMetrics,
            text,
            icon,
            button.getVerticalAlignment(),
            button.getHorizontalAlignment(),
            button.getVerticalTextPosition(),
            button.getHorizontalTextPosition(),
            viewR,
            iconR,
            textR,
            button.getIconTextGap());
    }
	
	/**
	 * Fill the background of the button on the rectangle boundRectangle.
	 * @param g      The Graphics object to paint with.
	 * @param color  The color to fill.
	 * @param sunken If {@code true}, the background is painted in such a way that the
	 *               button will appear 'sunken' or 'pressed'.
	 */
	private static void fillButtonSlab(Graphics g, Color color, boolean sunken) {
        
		Graphics2D g2 = (Graphics2D) g;
		// TODO missing part here:
		/*  if( sunken && calcShadowColor( color ).value() > color.value() )
	        {
	
	            QLinearGradient innerGradient( 0, r.top(), 0, r.bottom() + r.height() );
	            innerGradient.setColorAt( 0.0, color );
	            innerGradient.setColorAt( 1.0, calcLightColor( color ) );
	            painter.setBrush( innerGradient );
	
	        } 
		 */
		if (sunken) {
			GradientPaint gradient = new GradientPaint(0.0f, boundRectangle.y, calculateLightColor(color), 0.0f, boundRectangle.y + boundRectangle.height, color);
			g2.setPaint(gradient);
		} else {
			GradientPaint gradient = new GradientPaint(0.0f, boundRectangle.y + 0.2f * boundRectangle.height, calculateLightColor(color), 0.0f, boundRectangle.y + 0.6f * boundRectangle.height, color);
			g2.setPaint(gradient);
        }

		int size = 7; // this is the hardcoded default value in the original C++ code of the fillSlab method
		
		double s = size * (3.6 + (0.5 * SLAB_THICKNESS)) / 7.0;
		g2.fill(new RoundRectangle2D.Double(boundRectangle.x + s,
						    				boundRectangle.y + s,
						    				boundRectangle.width - s,
						    				boundRectangle.height - s, s/2.0, s/2.0));
	}

	private static Color calculateLightColor(Color color) {
		/*  const quint64 key( color.rgba() );
	        QColor* out( _lightColorCache.object( key ) );
	        if( !out )
	        {
	            out = new QColor( highThreshold( color ) ? color: KColorScheme::shade( color, KColorScheme::LightShade, _contrast ) );
	            _lightColorCache.insert( key, out );
	        }
	
	        return *out;
		 */
		
		return ColorUtils.highThreshold(color) ? color : ColorUtils.shadeScheme(color, ShadeRoles.LightShade, UIManager.getInt("General.contrast"));
	}
}
