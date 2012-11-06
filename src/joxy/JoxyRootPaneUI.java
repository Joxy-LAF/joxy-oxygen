package joxy;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRootPaneUI;

import joxy.utils.ColorUtils;
import joxy.utils.ColorUtils.ShadeRoles;

/**
 * Class overriding the default Rootpane (BasicRootpaneUI) to provide a good
 * integration with the Oxygen KDE style. Part of the Joxy Look and Feel.
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyRootPaneUI extends BasicRootPaneUI {
	
	public static final float CONTRAST_ADJUSTMENT = 0f;
	
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
		
		// speed is important here
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		
		int splitY = (int) Math.min(300 - 23, .75 * (c.getHeight() + 23));
		
		// determine colors to use
		Color color = UIManager.getColor("Window.background");
		assert color != null : "Wait, Window.background is null?";

		// draw linear gradient
		BufferedImage gradient = backgroundCache.get(c.getHeight());
		
		if (gradient == null) {
			// Output.debug("Linear background: created new image for height " + c.getHeight());
			BufferedImage newGradient = new BufferedImage(1, c.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D imgg2 = (Graphics2D) (newGradient.getGraphics());
		    Color backgroundTopColor = getBackgroundTopColor(color);
	        Color backgroundBottomColor = getBackgroundBottomColor(color);
			imgg2.setPaint(new GradientPaint(0, -23, backgroundTopColor, 0, splitY, backgroundBottomColor));
			imgg2.fillRect(0, 0, 1, c.getHeight());
			backgroundCache.put(c.getHeight(), newGradient);
			g2.drawImage(newGradient, AffineTransform.getScaleInstance(c.getWidth(), 1), null);
		} else {
			// Output.debug("Linear background: used cached image for height " + c.getHeight());
			g2.drawImage(gradient, AffineTransform.getScaleInstance(c.getWidth(), 1), null);
		}
		
		// draw upper radial gradient
        Color backgroundRadialColor = getBackgroundRadialColor(color);
		int radialWidth = Math.min(600, c.getWidth());
		if (c.getWidth() >= 600) {
			// Output.debug("Radial background: used the cached 600px image");
			g2.drawImage(radialGradient600px, (c.getWidth()-600)/2, 0, null);
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
	
			g2.fillRect(0, 0, c.getWidth(), 64); // last one is gradientHeight
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
			backgroundContrast -= CONTRAST_ADJUSTMENT;
			
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
			backgroundContrast -= CONTRAST_ADJUSTMENT;
			
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
			backgroundContrast -= CONTRAST_ADJUSTMENT;
		
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
