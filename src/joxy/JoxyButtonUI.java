package joxy;

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
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;

import joxy.painter.ButtonSlabPainter;
import joxy.painter.DarkEngravingPainter;
import joxy.painter.FocusIndicatorPainter;
import joxy.painter.ToolbarHoverIndicatorPainter;
import joxy.painter.PressedButtonSlabPainter;
import joxy.painter.HoverIndicatorPainter;
import joxy.utils.ColorUtils;
import joxy.utils.ColorUtils.ShadeRoles;
import joxy.utils.JoxyGraphics;
import joxy.utils.TileSet;


/**
 * Joxy's UI delegate for the JButton.
 * 
 * <p>This class is a bit messy, because there are two approaches being
 * developed: both translating the Oxygen C++ code, and just trying around.
 * The latter is default, for trying the translated code, set
 * USE_NEW_BUTTON_CODE to true. However, this is not finished yet.</p>
 * 
 * <p>The JoxyButtonUI supports animations for the focus and hovered states.
 * This means that the hover/focus indicator will fade in and out when it
 * appears and disappears again. This is done with two variables, hoverAmount
 * and focusAmount, and two Timers, hoverTimer and focusTimer. These timers
 * are normally stopped, but if the hoverListener or the focusListener, created
 * in the installListeners() method, fires, these timers are activated, and
 * start modifying the hover and focus amounts. The button will be continuously
 * repainted, until the animation stops &mdash; then the timers will stop
 * themselves (to prevent them from using resources when there is no
 * animation).</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyButtonUI extends BasicButtonUI {
	
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
	/** Whether to use the new code for painting buttons */
	private static final boolean USE_NEW_BUTTON_CODE = false;
	
	/** Amount of hover and focus, from 0 to 255 */
	private int hoverAmount = 0, focusAmount = 0;
	
	/** Timers for the animation */
	private Timer hoverTimer, focusTimer;
	
	/** Listeners for the animation */
	private MouseListener hoverListener;
	private FocusListener focusListener;
	
	public static ComponentUI createUI(JComponent c) {
		((AbstractButton) c).setRolloverEnabled(true);
		JoxyButtonUI ui = new JoxyButtonUI();
		return ui;
	}
	
	@Override
	protected void installDefaults(AbstractButton b) {
		super.installDefaults(b);

		b.setOpaque(false);
		b.setRolloverEnabled(true);
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
		
		if (USE_NEW_BUTTON_CODE) {
		// Note that in the original code, the button can be painted in flat mode. In Java, there is no such thing as
		// flat mode, so we only draw the non-flat mode.
		// TODO [ws] There is one in Java! See isContentAreaPainted().
		boundRectangle = new Rectangle(0, 0, c.getWidth() - 1, c.getHeight() - 1);

		// match color to the window background
		g2.setColor(ColorUtils.backgroundColor(UIManager.getColor("Button.background"), c, boundRectangle.getCenterX(), boundRectangle.getCenterY()));
		fillButtonSlab(g2, g2.getColor(), b.getModel().isPressed());
		
		/*  if( enabled && hoverAnimated && !( opts & Sunken ) )
            {
            	renderButtonSlab( painter, slabRect, buttonColor, opts, hoverOpacity, AnimationHover, TileSet::Ring );
         */
		if (b.isEnabled() && !b.getModel().isPressed() /* note that we forget about "State_On" here, that,
														   according to the Qt 4.7 specification, indicates "if the widget is checked".
														   As far as I know, we do not have such a state in Java and hence, ignore it.
														   [TC 01-12-2011] */) {
			renderButtonSlab(g2, c, JoxyLookAndFeel.ANIMATION_HOVER, new TileSet(TileSet.RING)); /* other parameters are implicit via the Graphics2D object or global */

		/*  } else if( enabled && !mouseOver && focusAnimated && !( opts & Sunken ) ) {
                renderButtonSlab( painter, slabRect, buttonColor, opts, focusOpacity, AnimationFocus, TileSet::Ring );
		 */
		} else if (b.isEnabled() && !b.getModel().isRollover() && !b.getModel().isPressed()) {
			
		
		/*
            } else {
                renderButtonSlab( painter, slabRect, buttonColor, opts ); */


		/*  }  */
		}
		} else {

			// Check whether the button is a toolbar button; see JoxyToolBarUI
			if (b.isContentAreaFilled()) {
				if (b.getModel().isPressed()) {
					PressedButtonSlabPainter.paint(g2, 2, 2, c.getWidth() - 4, c.getHeight() - 4);
				} else {
					// shadow
					g2.setColor(new Color(0, 0, 0, 80));
					g2.fill(new RoundRectangle2D.Double(2, 2, c.getWidth() - 4, c.getHeight() - 4, ARC, ARC));
					g2.setColor(new Color(0, 0, 0, 40));
					g2.fill(new RoundRectangle2D.Double(2, 3, c.getWidth() - 4, c.getHeight() - 4, ARC, ARC));
					g2.fill(new RoundRectangle2D.Double(1, 3, c.getWidth() - 2, c.getHeight() - 3, ARC+3, ARC+3));

					// decorations
					FocusIndicatorPainter.paint(g2, 2, 2, c.getWidth() - 4, c.getHeight() - 4, focusAmount);
					HoverIndicatorPainter.paint(g2, 2, 2, c.getWidth() - 4, c.getHeight() - 4, hoverAmount);
					
					// slab
					ButtonSlabPainter.paint(g2, 2, 2, c.getWidth() - 4, c.getHeight() - 4);
				}
			} else {
				if (b.getModel().isPressed()) {
					DarkEngravingPainter.paint(g2, 2, 2, c.getWidth() - 4, c.getHeight() - 4);
				} else {
					// If mouse is over the component, draw hover indicator; note it is a special indicator
					// for toolbar buttons
					ToolbarHoverIndicatorPainter.paint(g2, 2, 2, c.getWidth() - 4, c.getHeight() - 4, hoverAmount);
				}
			}		
		
		// Layout the button, i.e. determine the place for icon and text
		FontMetrics f = b.getFontMetrics(b.getFont());
		String clippedText = layout((JButton) c, f, c.getWidth(), c.getHeight());
		
		// Draw icon
		if (b.getIcon() != null) {
			b.getIcon().paintIcon(b, g2, paintIconR.x, paintIconR.y);
		}
		
		// Draw text
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		// Set text color. In the disabled state, a button has a different text color
		g2.setColor(b.getModel().isEnabled() ? b.getForeground() : UIManager.getColor("Button.foregroundInactive"));
		View v = (View) c.getClientProperty(BasicHTML.propertyKey);
		if (v != null) { // Text contains HTML
			v.paint(g2, paintTextR);
		} else { // No HTML, draw ourselves
			int w = f.stringWidth(clippedText);
			int h = f.getHeight();
			JoxyGraphics.drawString(g2, clippedText, paintTextR.x + (paintTextR.width - w) / 2, paintTextR.y + (paintTextR.height + h) / 2 - 3);
		}
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
	private String layout(JButton button, FontMetrics fm, int width, int height) {
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
        JButton button,
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
    
	private static void renderButtonSlab(Graphics2D g, JComponent c, int animationMode, TileSet ts) {
		/*AbstractButton b = (AbstractButton) c;
		Color buttonColor = g.getColor();
		
		// fill
		if (true) { // TODO to be replaced with a call that determines whether the background should be drawn
			fillButtonSlab(g, boundRectangle, buttonColor, b.getModel().isPressed());
		}
		
		// edges
		if (b.getModel().isPressed()) {
			slabSunken(buttonColor); // this, in fact, returns a TileSet (?)
		} else {
			Color glowColor = slabShadowColor(buttonColor, options, opacity, mode)
			slab(buttonColor, glow, 0.0) // this, in fact, also returns a TileSet :-)
		}*/
		
		// TODO Implement this
		/* *** /kstyles/oxygen/oxygenstyle.cpp ***
		    void Style::renderButtonSlab( QPainter *painter, QRect r, const QColor &color, StyleOptions options, qreal opacity,
		        AnimationMode mode,
		        TileSet::Tiles tiles ) const
		    {
		        if( ( r.width() <= 0 ) || ( r.height() <= 0 ) ) return;
		
		        r.translate( 0,-1 );
		        if( !painter->clipRegion().isEmpty() ) painter->setClipRegion( painter->clipRegion().translated( 0,-1 ) );
		
		        // fill
		        if( !( options & NoFill ) ) helper().fillButtonSlab( *painter, r, color, options&Sunken );
		
		        // edges
		        // for slabs, hover takes precedence over focus ( other way around for holes )
		        // but in any case if the button is sunken we don't show focus nor hover
		        TileSet *tile(0L);
		        if( options & Sunken )
		        {
		            tile = helper().slabSunken( color );
		
		        } else {
		
		            QColor glow = slabShadowColor( color, options, opacity, mode );
		            tile = helper().slab( color, glow, 0.0 );
		
		        }
		
		        if( tile )
		        { tile->render( r, painter, tiles ); }
		
		    }
		 */
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
	
	/**
	 * Performs the actual painting of the slab on boundRectangle.
	 * @param g    The Graphics object to paint with.
	 * @param size *** probably some size *** TODO
	 */
//	private static void fillSlab(Graphics g, int size) {
//        /* *** /kstyles/oxygen/oxygenstyle.cpp ***
//            const qreal s( qreal( size ) * ( 3.6 + ( 0.5 * _slabThickness ) ) / 7.0 );
//        	const QRectF r( QRectF( rect ).adjusted( s, s, -s, -s ) );
//	        if( !r.isValid() ) return;
//	
//	        p.drawRoundedRect( r, s/2, s/2 );
//         */
//		Graphics2D g2 = (Graphics2D) g;
//
//		double s = size * (3.6 + (0.5 * SLAB_THICKNESS)) / 7.0;
//		g2.fill(new RoundRectangle2D.Double(boundRectangle.x + s,
//						    				boundRectangle.y + s,
//						    				boundRectangle.width - s,
//						    				boundRectangle.height - s, s/2.0, s/2.0));
//	}
	
}
