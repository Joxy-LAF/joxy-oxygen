package joxy.utils;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import joxy.JoxyRootPaneUI;

/**
 * This class provides some methods to change colors, that is,
 * darken, lighten and shade for example. The implementations
 * of all methods are derived from the implementation of
 * sort-like methods from the KDE codebase.
 * Part of the Joxy Look and Feel.
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class ColorUtils {

	public enum ShadeRoles {LightShade, MidlightShade, MidShade, DarkShade, ShadowShade}
	
	public static boolean highThreshold(Color color) {
        // const QColor lighter( KColorScheme::shade( color, KColorScheme::LightShade, 0.5 ) );
        // const bool result( KColorUtils::luma( lighter ) < KColorUtils::luma( color ) );
		Color lighter = shadeScheme(color, ShadeRoles.LightShade, 0.5f);
        return luma(lighter) < luma(color);
	}
	
	public static boolean lowThreshold(Color color) {
		// const QColor darker( KColorScheme::shade( color, KColorScheme::MidShade, 0.5 ) );
        // const bool result( KColorUtils::luma( darker ) > KColorUtils::luma( color ) );
		Color darker = ColorUtils.shadeScheme(color, ShadeRoles.MidShade, 0.5f);
        return luma(darker) < luma(color);
	}
	
	/**
	 * Adjust the luma and chroma components of a color.
	 * The amount is added to the corresponding component.
	 * 
     * @param lumaAmount   amount by which to adjust the luma component of the color; 0.0 results in no change, -1.0 turns anything black, 1.0 turns anything white
     * @param chromaAmount (optional) amount by which to adjust the chroma component of the color; 0.0 results in no change, -1.0 minimizes chroma, 1.0 maximizes chroma 
	 * @return The resulting color.
	 */
	public static Color shade(Color color, float lumaAmount, float chromaAmount) {
		 HCYColor result = new HCYColor(color);
		 result.setY(HCYColor.normalize(result.getY() + lumaAmount));
		 result.setC(HCYColor.normalize(result.getC() + chromaAmount));
		 return result.toColor();
	}

	/**
	 * Refer to documentation of {@link #shade(Color, float, float)} with chromaAmount = 0.0.
	 */
	public static Color shade(Color color, float lumaAmount) {
		return ColorUtils.shade(color, lumaAmount, 0.0f);
	}
	
	/**
	 * Returns the luminance of the given Java color.
	 * @param color The color to determine the luminance of, non-null.
	 * @return The luminance.
	 */
	public static float luma(Color color) {
		assert color != null;
		
		HCYColor c = new HCYColor(color);
		return c.getY();
	}

	public static Color darken(Color color, float amount, float chromaGain) {
		HCYColor result = new HCYColor(color);
		result.setY(HCYColor.normalize(result.getY() + (1.0f - amount)));
		result.setC(HCYColor.normalize(result.getC() + chromaGain));
		return result.toColor();
	}
	
	public static Color darken(Color color, float amount) {
		return darken(color, amount, 1.0f);
	}

	/**
	 * Shade the color based on a ShadeRole.
	 */
	public static Color shadeScheme(Color color, ShadeRoles role, float contrast, float chromaAdjust) {

		assert color != null;
		
		contrast = (1.0f > contrast ? (-1.0f < contrast ? contrast : -1.0f) : 1.0f);
		float y = luma(color);
		float yi = 1.0f - y;

		// handle very dark colors (base, mid, dark, shadow == midlight, light)
		if (y < 0.006) {
		    switch (role) {
		    case LightShade:
		        return shade(color, 0.05f + 0.95f * contrast, chromaAdjust);
		    case MidShade:
		        return shade(color, 0.01f + 0.20f * contrast, chromaAdjust);
		    case DarkShade:
		        return shade(color, 0.02f + 0.40f * contrast, chromaAdjust);
		    default:
		        return shade(color, 0.03f + 0.60f * contrast, chromaAdjust);
		    }
		}

		// handle very light colors (base, midlight, light == mid, dark, shadow)
		if (y > 0.93) {
		    switch (role) {
		    case MidlightShade:
		        return shade(color, -0.02f - 0.20f * contrast, chromaAdjust);
		    case DarkShade:
		        return shade(color, -0.06f - 0.60f * contrast, chromaAdjust);
		    case ShadowShade:
		        return shade(color, -0.10f - 0.90f * contrast, chromaAdjust);
		    default:
		        return shade(color, -0.04f - 0.40f * contrast, chromaAdjust);
		    }
		}

		// handle everything else
		float lightAmount = (0.05f + y * 0.55f) * (0.25f + contrast * 0.75f);
		float darkAmount =  (      - y        ) * (0.55f + contrast * 0.35f);
		
		switch (role) {
            case LightShade:
                return shade(color, lightAmount, chromaAdjust);
            case MidlightShade:
                return shade(color, (0.15f + 0.35f * yi) * lightAmount, chromaAdjust);
            case MidShade:
                return shade(color, (0.35f + 0.15f * y) * darkAmount, chromaAdjust);
            case DarkShade:
                return shade(color, darkAmount, chromaAdjust);
            default:
                return darken(shade(color, darkAmount, chromaAdjust), 0.5f + 0.3f * y);
		}
	}
	
	public static Color shadeScheme(Color color, ShadeRoles role, float contrast) {
		return shadeScheme(color, role, contrast, 0.0f);
	}

	public static Color backgroundColor(Color color, JComponent c, double centerX, double centerY) {
		/* *** /libs/oxygen/oxygenhelper.h ***
			//! returns menu background color matching position in a given top level widget
	        virtual const QColor& backgroundColor( const QColor& color, const QWidget* w, const QPoint& point )
	        {
	            if( !( w && w->window() ) || checkAutoFillBackground( w ) ) return color;
	            else return backgroundColor( color, w->window()->height(), w->mapTo( w->window(), point ).y() );
	        }
	        
            //! returns menu background color matching position in a top level widget of given height
	        virtual const QColor& backgroundColor( const QColor& color, int height, int y )
	        { return backgroundColor( color, qMin( qreal( 1.0 ), qreal( y )/qMin( 300, 3*height/4 ) ) ); }
		 */
		Color result = null;
		
		int y = SwingUtilities.convertPoint(c, (int) centerX, (int) centerY, c.getRootPane()).y; // get window coordinates
		float ratio = (float) Math.min(1.0, y / Math.min(300, 0.75 * c.getHeight()));
		if (ratio < 0.5) {
			result = mix(JoxyRootPaneUI.getBackgroundTopColor(color), color, 2.0f * ratio);
		} else {
			result = mix(color, JoxyRootPaneUI.getBackgroundBottomColor(color), 2.0f * ratio);
		}
		
		/*
	        const quint64 key( ( quint64( color.rgba() ) << 32 ) | int( ratio*512 ) );
	        QColor *out( _backgroundColorCache.object( key ) );
	        if( !out )
	        {
	            if( ratio < 0.5 )
	            {
	
	                const qreal a( 2.0*ratio );
	                out = new QColor( KColorUtils::mix( backgroundTopColor( color ), color, a ) );
	
	            } else {
	
	                const qreal a( 2.0*ratio-1 );
	                out = new QColor( KColorUtils::mix( color, backgroundBottomColor( color ), a ) );
	
	            }
	
	            _backgroundColorCache.insert( key, out );
	
	        }
	
	        return *out;
		 */
		
		if (Utils.useRNDColorScheme) {
			return new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
		}
		return result;
	}

	/**
	 * Mixes two given colors.
	 * 
	 * @param c1  First color to mix.
	 * @param c2  Second color to mix.
	 * @param bias If this is near 0, c1 is used more, and if this is near 1, c2 is used more.
	 * @return Mix of two given colors.
	 */
	public static Color mix(Color c1, Color c2, float bias) {
		if (bias <= 0.0) return c1;
		if (bias >= 1.0) return c2;
		if (bias == Double.NaN) return c1; // special case copied from original code
		
		// [ws] bug fixed: /256f was missing
		float r = mixNumberFloat(c1.getRed() / 256f, c2.getRed() / 256f, bias);
		float g = mixNumberFloat(c1.getGreen() / 256f, c2.getGreen() / 256f, bias);
		float b = mixNumberFloat(c1.getBlue() / 256f,  c2.getBlue() / 256f,  bias);
		float a = mixNumberFloat(c1.getAlpha() / 256f, c2.getAlpha() / 256f, bias);
		
		return new Color(r, g, b, a);
	}
	
	private static float mixNumberFloat(float a, float b, float bias) {
	    return a + (b - a) * bias;
	}
	
	/* The following methods don't come from KColorUtils, but from OxygenHelper,
	   but well... they are color related methods too. */
	
	/**
	 * Calculates a brighter variant of the given color.
	 * @param color The color to brighten.
	 * @return The calculated color.
	 */
    public static Color calcLightColor(Color color) {
        if (highThreshold(color)) {
        	return color;
        }
        
		// [ws] TODO this should go to Utils since it is copied
		int contrast = UIManager.getInt("General.contrast");
		
		// Remark: in the original code, it stated "0.9 * contrast / 0.7". But this turns out to refer
		// to contrastF, that divides the contrast by 10.
		// double backgroundContrast = Math.min(1, 0.9 * contrast / 7);
		// backgroundContrast -= CONTRAST_ADJUSTMENT;
		
		return ColorUtils.shadeScheme(color, ShadeRoles.LightShade, contrast);
	}
    
	/**
	 * Calculates a darker variant of the given color.
	 * @param color The color to darken.
	 * @return The calculated color.
	 */
    public static Color calcDarkColor(Color color) {
		// [ws] TODO this should go to Utils since it is copied
		int contrast = UIManager.getInt("General.contrast");
		
		// Remark: in the original code, it stated "0.9 * contrast / 0.7". But this turns out to refer
		// to contrastF, that divides the contrast by 10.
		// double backgroundContrast = Math.min(1, 0.9 * contrast / 7);
		// backgroundContrast -= CONTRAST_ADJUSTMENT;
		
        if (lowThreshold(color)) {
        	return ColorUtils.mix(calcLightColor(color), color, 0.3f + 0.7f * contrast);
        }
		
		return ColorUtils.shadeScheme(color, ShadeRoles.MidShade, contrast);
	}
    
	/**
	 * Calculates a shadow variant of the given color.
	 * @param color The color to make a shadow variant of.
	 * @return The calculated color.
	 */
    public static Color calcShadowColor(Color color) {
		// [ws] TODO this should go to Utils since it is copied
		int contrast = UIManager.getInt("General.contrast");
		
		// Remark: in the original code, it stated "0.9 * contrast / 0.7". But this turns out to refer
		// to contrastF, that divides the contrast by 10.
		// double backgroundContrast = Math.min(1, 0.9 * contrast / 7);
		// backgroundContrast -= CONTRAST_ADJUSTMENT;
		
        if (lowThreshold(color)) {
        	return ColorUtils.mix(calcLightColor(color), color, 0.3f + 0.7f * contrast);
        }
		
		return ColorUtils.shadeScheme(color, ShadeRoles.MidShade, contrast);
	}
}
