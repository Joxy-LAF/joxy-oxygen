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

package joxy.utils;

import java.awt.Color;

/**
 * This class represents a color in the HCY (hue, chroma, luma) color model.
 * KDE uses this model internally to modify colors, and this class allows Joxy
 * to do the same.
 * 
 * <p>The logic for many methods of this class is taken from KDE's HCY color
 * implementation.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class HCYColor {
	
	private float h; // hue
	private float c; // chroma
	private float y; // luma
	private float a; // alpha
	
	private static float[] yc = new float[] {0.299f, 0.587f, 0.114f};
	
	/**
	 * Generates a HCYColor out of a simple Java (RGB) color.
	 * 
	 * @param color The Java RGB color, non-null.
	 */
	public HCYColor(Color color) {
		
		assert color != null;
		
		float r = gamma(color.getRed() / 255.0f);
		float g = gamma(color.getGreen() / 255.0f);
		float b = gamma(color.getBlue() / 255.0f);
		a = color.getAlpha();
		
		// luma component
		setY(lumag(r, g, b));

		// hue component
        float p = Math.max(Math.max(r, g), b);
        float n = Math.min(Math.min(r, g), b);
        float d = (float) (6.0 * (p - n));
		
		if (n == p)
			setH(0.0f);
		else if (r == p)
			setH(((g - b) / d));
		else if (g == p)
			setH((float) (((b - r) / d) + (1.0 / 3.0)));
		else
			setH((float) (((r - g) / d) + (2.0 / 3.0)));
		
		// chroma component
		if (0.0 == y || 1.0 == y)
			setC(0.0f);
		else
			setC(Math.max( (y - n) / y, (p - y) / (1 - y) ));
	}
	
	/**
	 * Generates a Java color of this HCYColor.
	 */
	public Color toColor() {
		// start with sane component values
		float h2 = wrap(getH());
		float c2 = normalize(getC());
		float y2 = normalize(getY());
		
		// calculate some needed variables
		float hs = (float) (h2 * 6.0);
		float th;
		float tm;
		if (hs < 1.0) {
			th = hs;
			tm = yc[0] + yc[1] * th;
		}
		else if (hs < 2.0) {
			th = (float) (2.0 - hs);
			tm = yc[1] + yc[0] * th;
		}
		else if (hs < 3.0) {
			th = (float) (hs - 2.0);
			tm = yc[1] + yc[2] * th;
		}
		else if (hs < 4.0) {
			th = (float) (4.0 - hs);
			tm = yc[2] + yc[1] * th;
		}
		else if (hs < 5.0) {
			th = (float) (hs - 4.0);
			tm = yc[2] + yc[0] * th;
		} else {
			th = (float) (6.0 - hs);
			tm = yc[0] + yc[2] * th;
		}

		// calculate RGB channels in sorted order
		float tn, to, tp;
		if (tm >= y2) {
			tp = (float) (y2 + y2 * c2 * (1.0 - tm) / tm);
			to = y2 + y2 * c2 * (th - tm) / tm;
			tn = y2 - (y2 * c2);
		} else {
			tp = (float) (y2 + (1.0 - y2) * c2);
			to = (float) (y2 + (1.0 - y2) * c2 * (th - tm) / (1.0 - tm));
			tn = (float) (y2 - (1.0 - y2) * c2 * tm / (1.0 - tm));
		}
		
		// return RGB channels in appropriate order
		if (hs < 1.0)
			return new Color(inverseGamma(tp), inverseGamma(to), inverseGamma(tn), a / 255.0f);
		else if (hs < 2.0)
			return new Color(inverseGamma(to), inverseGamma(tp), inverseGamma(tn), a / 255.0f);
		else if (hs < 3.0)
			return new Color(inverseGamma(tn), inverseGamma(tp), inverseGamma(to), a / 255.0f);
		else if (hs < 4.0)
			return new Color(inverseGamma(tn), inverseGamma(to), inverseGamma(tp), a / 255.0f);
		else if (hs < 5.0)
			return new Color(inverseGamma(to), inverseGamma(tn), inverseGamma(tp), a / 255.0f);
		else
			return new Color(inverseGamma(tp), inverseGamma(tn), inverseGamma(to), a / 255.0f);
	}

	/**
	 * Gamma function.
	 */
	public static float gamma(float n) {
        return (float) Math.pow(normalize(n), 2.2f);
    }
	
	/**
	 * Inverse gamma function, see {@link #gamma(float)}.
	 */
	public static float inverseGamma(float n) {
		return (float) Math.pow(normalize(n), 1.0/2.2);
	}
	
	/**
	 * Wraps a value to the range [0, d].
	 */
	public static float wrap(float n, float d) {
		float r = n % d;
		return (float) (r < 0.0 ? d + r : (r > 0.0 ? r : 0.0));
	}
	
	/**
	 * Wraps a value to the range [0, 1].
	 */
	public static float wrap(float n) {
		return wrap(n, 1.0f);
	}

	
	/**
	 * Normalizes the input, that means, map values lower than 0 to 0, and map values
	 * higher than 1 to 1.
	 * 
	 * @param n The input number.
	 * @return The following value:
	 * <ul>
	 *   <li>0 if n < 0;</li>
	 *   <li>n if 0 < n < 1;</li>
	 *   <li>1 if n > 1.</li>
	 * </ul>
	 */
	public static float normalize(float n) {
		return (float) (n < 1.0 ? (n > 0.0 ? n : 0.0) : 1.0);
	}
	
	/**
	 * TODO documentation
	 */
	public static float lumag(float r, float g, float b) {
	    return r*yc[0] + g*yc[1] + b*yc[2];
	}

	//-- GETTERS AND SETTERS --
	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getC() {
		return c;
	}

	public void setC(float c) {
		this.c = c;
	}

	public float getH() {
		return h;
	}

	public void setH(float h) {
		this.h = h;
	}
}
