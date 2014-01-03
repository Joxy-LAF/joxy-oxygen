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
 * This class represents a color in the HSV (hue, saturation, value) color model.
 * 
 * This is used in the colour dialog.
 * 
 * @see HSVColor
 */
public class HSVColor {
	
	/**
	 * The hue (H) component of this color (<code>0.0 <= h < 1.0</code>).
	 */
	private float h;

	/**
	 * The saturation (S) component of this color (<code>0.0 <= s < 1.0</code>).
	 */
	private float s;
	
	/**
	 * The value (V) component of this color (<code>0.0 <= v < 1.0</code>).
	 */
	private float v;
	
	/**
	 * The alpha component of this color (<code>0.0 <= a < 1.0</code>).
	 */
	private float a;
	
	/**
	 * Generates a HCYColor (with alpha value 1.0, which means opaque) by
	 * specifying the H, C and Y component. If the given H, C and Y values
	 * are out of range, they will be wrapped / normalized automatically.
	 * 
	 * @param h The hue.
	 * @param s The chroma.
	 * @param v The luma.
	 */
	public HSVColor(float h, float s, float v) {
		this.h = wrap(h);
		this.s = normalize(s);
		this.v = normalize(v);
		
		this.a = 1;
	}
	
	/**
	 * Generates a HSVColor out of a simple Java (RGB) color.
	 * 
	 * @param color The Java RGB color, non-<code>null</code>.
	 * @throws IllegalArgumentException If <code>color</code> is
	 * <code>null</code>.
	 */
	public HSVColor(Color color) {
		
		if (color == null) {
			throw new IllegalArgumentException("The color may not be null");
		}
		
		float r = color.getRed() / 255.0f;
		float g = color.getGreen() / 255.0f;
		float b = color.getBlue() / 255.0f;
		this.a = color.getAlpha();
		
        float max = Math.max(Math.max(r, g), b);
        float min = Math.min(Math.min(r, g), b);
        float d = (float) (6.0 * (max - min));
		
		if (min == max)
			setH(0f);
		else if (r == max)
			setH(((g - b) / d));
		else if (g == max)
			setH((float) (((b - r) / d) + (1.0 / 3)));
		else
			setH((float) (((r - g) / d) + (2.0 / 3)));
		
		setS((max - min) / max);
		
		setV(max);
	}
	
	/**
	 * Generates a Java color of this HCYColor.
	 * @return The resulting {@link Color} object.
	 */
	public Color toColor() {
		// start with sane component values
		float s = getS();
		float v = getV();
		
		// calculate some needed variables
		float hs = (float) (getH() * 6.0);
		float f = hs - (int) hs;
		float p = v * (1 - s);
		float q = v * (1 - f * s);
		float t = v * (1 - (1 - f) * s);
		float r, g, b;
		if (hs < 1.0) {
			r = v;
			g = t;
			b = p;
		} else if (hs < 2.0) {
			r = q;
			g = v;
			b = p;
		} else if (hs < 3.0) {
			r = p;
			g = v;
			b = t;
		} else if (hs < 4.0) {
			r = p;
			g = q;
			b = v;
		} else if (hs < 5.0) {
			r = t;
			g = p;
			b = v;
		} else {
			r = v;
			g = p;
			b = q;
		}
		
		return new Color(r, g, b, a);
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
	 * Returns the saturation (S) component of this color.
	 * @return The saturation component <code>y</code> (<code>0.0 <= s < 1.0</code>).
	 */
	public float getS() {
		return normalize(s);
	}

	/**
	 * Sets the saturation (S) component of this color.
	 * @param s The new saturation component. If needed, this value is normalized.
	 */
	public void setS(float s) {
		this.s = s;
	}

	/**
	 * Returns the value (V) component of this color.
	 * @return The value component <code>y</code> (<code>0.0 <= v < 1.0</code>).
	 */
	public float getV() {
		return normalize(v);
	}

	/**
	 * Sets the value (V) component of this color.
	 * @param v The new value component. If needed, this value is normalized.
	 */
	public void setV(float v) {
		this.v = v;
	}
}
