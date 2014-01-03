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

package joxy.color;

import java.awt.Color;

/**
 * This class represents a color in the HCY (hue, chroma, luma) color model.
 * KDE uses this model internally to modify colors, and this class allows Joxy
 * to do the same.
 * 
 * The HCY color model is similar to the HSV / HSL color model, but it uses
 * some correction values.
 * 
 * The logic for many methods of this class is taken from KDE's HCY color
 * implementation.
 * 
 * @see HSVColor
 */
public class HCYColor {
	
	/**
	 * The hue (H) component of this color (<code>0.0 <= h < 1.0</code>).
	 */
	private float h;

	/**
	 * The chroma (C) component of this color (<code>0.0 <= c < 1.0</code>).
	 */
	private float c;
	
	/**
	 * The luma (Y) component of this color (<code>0.0 <= y < 1.0</code>).
	 */
	private float y;
	
	/**
	 * The alpha component of this color (<code>0.0 <= a < 1.0</code>).
	 */
	private float a;
	
	/**
	 * An array containing correction values for converting HCY values
	 * into RGB values.
	 */
	private static float[] yc = new float[] {0.299f, 0.587f, 0.114f};
	
	/**
	 * Generates a HCYColor (with alpha value 1.0, which means opaque) by
	 * specifying the H, C and Y component. If the given H, C and Y values
	 * are out of range, they will be wrapped / normalized automatically.
	 * 
	 * @param h The hue.
	 * @param c The chroma.
	 * @param y The luma.
	 */
	public HCYColor(float h, float c, float y) {
		this.h = wrap(h);
		this.c = normalize(c);
		this.y = normalize(y);
		
		this.a = 1;
	}
	
	/**
	 * Generates a HCYColor out of a simple Java (RGB) color.
	 * 
	 * @param color The Java RGB color, non-<code>null</code>.
	 * @throws IllegalArgumentException If <code>color</code> is
	 * <code>null</code>.
	 */
	public HCYColor(Color color) {
		
		if (color == null) {
			throw new IllegalArgumentException("The color may not be null");
		}
		
		float r = gamma(color.getRed() / 255.0f);
		float g = gamma(color.getGreen() / 255.0f);
		float b = gamma(color.getBlue() / 255.0f);
		this.a = color.getAlpha();
		
		// luma component
		setY(lumag(r, g, b));

		// hue component
        float max = Math.max(Math.max(r, g), b);
        float min = Math.min(Math.min(r, g), b);
        float d = (float) (6.0 * (max - min));
		
		if (min == max)
			setH(0.0f);
		else if (r == max)
			setH(((g - b) / d));
		else if (g == max)
			setH((float) (((b - r) / d) + (1.0 / 3)));
		else
			setH((float) (((r - g) / d) + (2.0 / 3)));
		
		// chroma component
		if (0.0 == y || 1.0 == y)
			setC(0.0f);
		else
			setC(Math.max( (y - min) / y, (max - y) / (1 - y) ));
	}
	
	/**
	 * Generates a Java color of this HCYColor.
	 * @return The resulting {@link Color} object.
	 */
	public Color toColor() {
		// start with sane component values
		float c2 = getC();
		float y2 = getY();
		
		// calculate some needed variables
		float hs = (float) (getH() * 6.0);
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
	 * 
	 * @param n The input value.
	 * @return The resulting value.
	 */
	public static float gamma(float n) {
        return (float) Math.pow(normalize(n), 2.2f);
    }
	
	/**
	 * Inverse gamma function, see {@link #gamma(float)}.
	 * 
	 * @param n The input value.
	 * @return The resulting value.
	 */
	public static float inverseGamma(float n) {
		return (float) Math.pow(normalize(n), 1.0/2.2);
	}
	
	/**
	 * Wraps a value to the range [0, d].
	 * 
	 * @param n The input value.
	 * @param d The upper-bound of the range to wrap to.
	 * @return The resulting value.
	 */
	public static float wrap(float n, float d) {
		float r = (n % d + d) % d;
		return r < 0.0 ? d + r : r;
	}
	
	/**
	 * Wraps a value to the range [0, 1].
	 * 
	 * @param n The input value.
	 * @return The resulting value.
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

	/**
	 * Returns the hue (H) component of this color.
	 * @return The hue component <code>y</code> (<code>0.0 <= h < 1.0</code>).
	 */
	public float getH() {
		return wrap(h);
	}

	/**
	 * Sets the hue (H) component of this color.
	 * @param h The new hue component. If needed, this value is wrapped.
	 */
	public void setH(float h) {
		this.h = h;
	}

	/**
	 * Returns the chroma (C) component of this color.
	 * @return The chroma component <code>y</code> (<code>0.0 <= c < 1.0</code>).
	 */
	public float getC() {
		return normalize(c);
	}

	/**
	 * Sets the chroma (C) component of this color.
	 * @param c The new chroma component. If needed, this value is normalized.
	 */
	public void setC(float c) {
		this.c = c;
	}

	/**
	 * Returns the luma (Y) component of this color.
	 * @return The luma component <code>y</code> (<code>0.0 <= y < 1.0</code>).
	 */
	public float getY() {
		return normalize(y);
	}

	/**
	 * Sets the luma (Y) component of this color.
	 * @param y The new luma component. If needed, this value is normalized.
	 */
	public void setY(float y) {
		this.y = y;
	}
}
