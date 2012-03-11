package joxy;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
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
	
	public static ComponentUI createUI(JComponent c) {
		JoxyRootPaneUI rootPaneUI = new JoxyRootPaneUI();
		return rootPaneUI;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>The logic for this method is taken from the actual Oxygen rendering code.</p>
	 */
	@Override
	public void paint(Graphics g, JComponent c) {
		Graphics2D g2 = (Graphics2D) g;
		int splitY = (int) Math.min(300, .75 * c.getHeight());
		
		// determine colors to use
		Color color = UIManager.getColor("Window.background");
		
	    Color backgroundTopColor = getBackgroundTopColor(color);
        Color backgroundBottomColor = getBackgroundBottomColor(color);
        Color backgroundRadialColor = getBackgroundRadialColor(color);

		// draw upper linear gradient
		g2.setPaint(new GradientPaint(0, 0, backgroundTopColor, 0, splitY, backgroundBottomColor));
		g2.fillRect(0, 0, c.getWidth(), c.getHeight()); // Note that compared to the original code, we always take an offset of 0
												 // (this is also true in the original code really)
        
		// [ws] TODO I realized this is not really needed - Java does this automatically when
		// painting "outside" the gradient part
		// draw lower flat part
		//g2.setColor(backgroundBottomColor);
		//g2.fillRect(0, splitY, c.getWidth(), c.getHeight()-splitY);
		
		// draw upper radial gradient
		int radialWidth = Math.min(600, c.getWidth());
		
		Color radial1 = new Color(backgroundRadialColor.getRed(), backgroundRadialColor.getGreen(),backgroundRadialColor.getBlue(), 0);
		Color radial2 = new Color(backgroundRadialColor.getRed(), backgroundRadialColor.getGreen(),backgroundRadialColor.getBlue(), 37);
		Color radial3 = new Color(backgroundRadialColor.getRed(), backgroundRadialColor.getGreen(),backgroundRadialColor.getBlue(), 105);
		Color radial4 = new Color(backgroundRadialColor.getRed(), backgroundRadialColor.getGreen(),backgroundRadialColor.getBlue(), 255);
		
		g2.setPaint(new RadialGradientPaint(new Rectangle2D.Double((c.getWidth()-radialWidth)/2, -23 - 64, radialWidth, 2 * 64),
				new float[] {0.0f, 0.5f, 0.75f, 1.0f}, // Distribution of colors over gradient
				new Color[] {radial4, radial3, radial2, radial1},
				RadialGradientPaint.CycleMethod.NO_CYCLE));
		
		g2.fillRect((c.getWidth()-radialWidth)/2, 0, radialWidth, 64); // last one is gradientHeight
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
