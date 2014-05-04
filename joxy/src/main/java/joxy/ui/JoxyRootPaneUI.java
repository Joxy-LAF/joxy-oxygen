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
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRootPaneUI;

import joxy.color.ColorUtils;
import joxy.color.ColorUtils.ShadeRoles;
import joxy.utils.*;

/**
 * Joxy's UI delegate for the JRootPaneUI.
 * 
 * <p>This class is responsible for drawing the radial background of windows.
 * Several ways of caching are used, since this is done very often. See {@link #backgroundCache},
 * {@link #radialGradient600px} and {@link #currentCache}.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyRootPaneUI extends BasicRootPaneUI {
	
	/**
	 * In this hash table we store cached images of the <b>linear</b> part of the
	 * background to improve performance. Because these images are just stretchable
	 * in the <i>x</i> direction, they are 1 pixel wide.
	 */
	private static Hashtable<Integer, BufferedImage> backgroundCache = new Hashtable<Integer, BufferedImage>();
	
	/**
	 * In this image we cache the radial gradient that is 600 pixels wide. It is
	 * too memory-wasting to store every radial gradient, but because 600 pixels is the
	 * maximum width for these gradient, this width will be used for every window with
	 * width greater or equal than 600 pixels. So it is worth caching this gradient.
	 */
	private static BufferedImage radialGradient600px = null;
	
	/**
	 * In this image we cache the entire current radial background. That is useful since
	 * when the window size hasn't changed, the radial background will be entirely the same
	 * and thus, it can be completely taken from the cache.
	 */
	private static BufferedImage currentCache = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	
	/**
	 * Whether we still have to use {@link XUtils#setOxygenGradientHint(Frame, boolean)}
	 * to tell Oxygen that it should draw the radial background.
	 */
	private boolean shouldTellOxygenAboutRadialBackground = true;
	
	/**
	 * Initialize the {@link #radialGradient600px}.
	 */
	static {
		radialGradient600px = new BufferedImage(600, 64, BufferedImage.TYPE_INT_ARGB);
		Color color = UIManager.getColor("Window.background");
        Color backgroundRadialColor = getBackgroundRadialColor(color);
		
		Color radial1 = new Color(backgroundRadialColor.getRed(), backgroundRadialColor.getGreen(),backgroundRadialColor.getBlue(), 0);
		Color radial2 = new Color(backgroundRadialColor.getRed(), backgroundRadialColor.getGreen(),backgroundRadialColor.getBlue(), 37);
		Color radial3 = new Color(backgroundRadialColor.getRed(), backgroundRadialColor.getGreen(),backgroundRadialColor.getBlue(), 105);
		Color radial4 = new Color(backgroundRadialColor.getRed(), backgroundRadialColor.getGreen(),backgroundRadialColor.getBlue(), 255);
		
		Graphics2D imgg2 = (Graphics2D) (radialGradient600px.getGraphics());
		
		imgg2.setPaint(new RadialGradientPaint(new Rectangle2D.Double(0, -64 - 23, 600, 2 * 64),
				new float[] {0.0f, 0.5f, 0.75f, 1.0f}, // Distribution of colors over gradient
				new Color[] {radial4, radial3, radial2, radial1},
				RadialGradientPaint.CycleMethod.NO_CYCLE));

		imgg2.fillRect(0, 0, 600, 64); // last one is gradientHeight
	}
	
	public static ComponentUI createUI(JComponent c) {
		JoxyRootPaneUI rootPaneUI = new JoxyRootPaneUI();
		return rootPaneUI;
	}
	
	@Override
	protected void installDefaults(JRootPane c) {
		super.installDefaults(c);
		
		// this is a fix for bug 15; see the description there
		c.setOpaque(true);
	}
	
	@Override
	protected void uninstallDefaults(JRootPane c) {
		super.uninstallDefaults(c);
		
		// tell Oxygen that we are not drawing the radial background anymore
		Window w = SwingUtilities.getWindowAncestor(c);
		XUtils.setOxygenGradientHint(w, false);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>The logic for this method is taken from the actual Oxygen rendering code,
	 * but with a different caching scheme.</p>
	 */
	@Override
	public void paint(Graphics g, JComponent c) {
		
		Graphics2D g2 = (Graphics2D) g;
		
		// Bug 12: some applications (breaking the API) create a JFrame and apply the LAF thereafter.
		// That means that JoxyRootPaneUI will be applied already, coinciding with other LAF components,
		// for example Metal. Most worrying, all kinds of stuff can happen to the defaults. Therefore
		// we check if the LAF is Joxy, and if not, we update the component tree ourselves.
		if (!Utils.isJoxyActive()) {
			Output.warning("Application created the JRootPane after setting the LAF, but without using \n" +
					"SwingUtilities.updateComponentTreeUI(frame). Joxy will do that now.");
			SwingUtilities.updateComponentTreeUI(c);
			return;
		}
		
		// Bug 23: let Oxygen know that we are drawing the radial background, so that
		// Oxygen starts drawing it in the window decoration too.
		if (shouldTellOxygenAboutRadialBackground) {
			Window w = SwingUtilities.getWindowAncestor(c);
			XUtils.setOxygenGradientHint(w, true);
			shouldTellOxygenAboutRadialBackground = false;
		}
		
		// if the currentCache is not up-to-date, draw a new one
		if (c.getWidth() != currentCache.getWidth() || c.getHeight() != currentCache.getHeight()) {
			paintBackgroundToCache(c.getWidth(), c.getHeight());
		}
		
		// actually draw the background
		g2.drawImage(currentCache, 0, 0, null);
	}
	
	/**
	 * Actually draws the background onto the {@link #currentCache}.
	 * @param width The desired width.
	 * @param height The desired height.
	 */
	protected void paintBackgroundToCache(int width, int height) {
		
		currentCache = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) currentCache.getGraphics();
		
		// speed is important here
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		
		int splitY = (int) Math.min(300 - 23, .75 * (height + 23));
		
		// determine colors to use
		Color color = UIManager.getColor("Window.background");

		// draw linear gradient
		BufferedImage gradient = backgroundCache.get(height);
		
		if (gradient == null) {
			// Output.debug("Linear background: created new image for height " + height);
			BufferedImage newGradient = new BufferedImage(1, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D imgg2 = (Graphics2D) (newGradient.getGraphics());
		    Color backgroundTopColor = getBackgroundTopColor(color);
	        Color backgroundBottomColor = getBackgroundBottomColor(color);
			imgg2.setPaint(new GradientPaint(0, -23, backgroundTopColor, 0, splitY, backgroundBottomColor));
			imgg2.fillRect(0, 0, 1, height);
			backgroundCache.put(height, newGradient);
			g2.drawImage(newGradient, AffineTransform.getScaleInstance(width, 1), null);
		} else {
			// Output.debug("Linear background: used cached image for height " + height);
			g2.drawImage(gradient, AffineTransform.getScaleInstance(width, 1), null);
		}
		
		// draw upper radial gradient
        Color backgroundRadialColor = getBackgroundRadialColor(color);
		int radialWidth = Math.min(600, width);
		if (width >= 600) {
			// Output.debug("Radial background: used the cached 600px image");
			g2.drawImage(radialGradient600px, (width-600)/2, 0, null);
		} else {
			// Output.debug("Radial background: created a new image");
			Color radial1 = new Color(backgroundRadialColor.getRed(), backgroundRadialColor.getGreen(),backgroundRadialColor.getBlue(), 0);
			Color radial2 = new Color(backgroundRadialColor.getRed(), backgroundRadialColor.getGreen(),backgroundRadialColor.getBlue(), 37);
			Color radial3 = new Color(backgroundRadialColor.getRed(), backgroundRadialColor.getGreen(),backgroundRadialColor.getBlue(), 105);
			Color radial4 = new Color(backgroundRadialColor.getRed(), backgroundRadialColor.getGreen(),backgroundRadialColor.getBlue(), 255);
	
			g2.setPaint(new RadialGradientPaint(new Rectangle2D.Double(0, -23 - 64, radialWidth, 2 * 64),
					new float[] {0.0f, 0.5f, 0.75f, 1.0f}, // Distribution of colors over gradient
					new Color[] {radial4, radial3, radial2, radial1},
					RadialGradientPaint.CycleMethod.NO_CYCLE));
	
			g2.fillRect(0, 0, width, 64); // last one is gradientHeight
		}
	}

	/**
	 * Returns the color to use for the upper part of the pane.
	 * @param baseColor The color that is given in the defaults.
	 * @return The generated color.
	 */
	public static Color getBackgroundTopColor(Color baseColor) {
		Color out = new Color(0, 0, 0);
		
		//if (ColorUtils.lowThreshold(baseColor)) {
		//	out = ColorUtils.shadeScheme(baseColor, ShadeRoles.MidlightShade, 0.0f);
		//} else {
			float my = ColorUtils.luma(ColorUtils.shadeScheme(baseColor, ShadeRoles.LightShade, 0.0f));
			float by = ColorUtils.luma(baseColor);
			
			int contrast = UIManager.getInt("General.contrast");
			
			// Remark: in the original code, it stated "0.9 * contrast / 0.7". But this turns out to refer
			// to contrastF, that divides the contrast by 10.
			double backgroundContrast = Math.min(1, 0.9 * contrast / 7);
			
			out = ColorUtils.shade(baseColor, (float) ((my-by) * backgroundContrast));
		//}
		
		return out;
	}
	
	public static Color getBackgroundBottomColor(Color baseColor) {
		Color out = new Color(0, 0, 0);
        
		Color midColor = ColorUtils.shadeScheme(baseColor, ShadeRoles.MidShade, 0.0f);
            //if( lowThreshold( color ) ) out = new QColor( midColor );
           // else {
	    //if (ColorUtils.lowThreshold(baseColor)) { // [ws] FIXME volgens mij zit er een ernstige bug in lowThreshold.
		//	out = midColor;
		//} else {
            float by = ColorUtils.luma(baseColor);
            float my = ColorUtils.luma(midColor);

			int contrast = UIManager.getInt("General.contrast");
			
			// Remark: in the original code, it stated "0.9 * contrast / 0.7". But this turns out to refer
			// to contrastF, that divides the contrast by 10.
			double backgroundContrast = Math.min(1, 0.9 * contrast / 7);
			
            out = ColorUtils.shade(baseColor, (float) ((my-by) * backgroundContrast));

        //}
	    
        return out;
	}
	
	public static Color getBackgroundRadialColor(Color baseColor) {
		
		assert baseColor != null;
		
		Color out = new Color(0, 0, 0);
		
		//if (ColorUtils.lowThreshold(baseColor)) {
		//	out = ColorUtils.shadeScheme(baseColor, ShadeRoles.LightShade, 0.0f);
		//} //else if ((ColorUtils.highThreshold(baseColor)) { // TODO zolang we highThreshold nog niet hebben
		//}
			int contrast = UIManager.getInt("General.contrast");
			
			// Remark: in the original code, it stated "0.9 * contrast / 0.7". But this turns out to refer
			// to contrastF, that divides the contrast by 10.
			double backgroundContrast = Math.min(1, 0.9 * contrast / 7);
		
			out = ColorUtils.shadeScheme(baseColor, ShadeRoles.LightShade, (float) backgroundContrast);
		//}
		
		return out;
	}
	
    @Override
    public void update(Graphics g, JComponent c) {
        if (c.isOpaque()) {
    		paint(g, c);
        }
    }
}
