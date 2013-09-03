/**
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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

/**
 * A class that tries to make the default Java font rendering more
 * like KDE does.
 */
public class JoxyGraphics {
	
	/**
	 * Whether text rendering should be done by a call to Qt (instead of doing it in Java).
	 */
	public static final boolean NATIVE_TEXT_RENDERING = true;
	
	/**
	 * Whether the native text rendering is actually loaded.
	 */
	private static boolean couldInitializeNative = false;
	
	/**
	 * Whether Qt-rendered text should be cached. This has no meaning when NATIVE_TEXT_RENDERING = false.
	 */
	public static final boolean TEXT_CACHING = false;
	
	/**
	 * HashTable used for the caching,
	 */
	private static Hashtable<String, BufferedImage> imageCache;

	static {
		if (NATIVE_TEXT_RENDERING) {
			try {
				System.loadLibrary("joxy");
				couldInitializeNative = true;
			} catch (Throwable t) {
				Output.warning("Native text rendering requested (joxy.utils.JoxyGraphics.NATIVE_TEXT_RENDERING == true),\n" +
						"but could not initialize the native library code. Joxy will fallback on Java text rendering.");
			}
			
			if (couldInitializeNative) {
				initializeNative();
				
				if (TEXT_CACHING) {
					imageCache = new Hashtable<String, BufferedImage>();
				}
			}
		}
	}
	
	/**
	 * Draws a string with the given Graphics object. This is done by a call to Qt.
	 * 
	 * @param g2  The Graphics2D object we are painting on. The font family, font size and color
	 * to draw with, are inferred from this object.
	 * @param str The String to draw.
	 * @param x   The x-coordinate of the left side of the text.
	 * @param y   The y-coordinate of the lower side of the text.
	 */
	public static void drawString(Graphics2D g2, String str, float x, float y) {
	    if (NATIVE_TEXT_RENDERING && couldInitializeNative) {
	    	
	    	String key = null;
	    	BufferedImage img = null;
	    	
			if (TEXT_CACHING) {
		    	// Try to use the cache.
		    	key = str + "/" + g2.getFont().getName() + "/" + g2.getFont().getSize() + "/" + g2.getFont().getStyle() + "/" + g2.getColor().getRGB();
		    	img = imageCache.get(key);
			}
	    	
	    	// If not in cache, call native method to create it, and put it in the cache.
	    	if (!TEXT_CACHING || img == null) {
	    		int width = g2.getFontMetrics().stringWidth(str) + 2;
	    		int height = g2.getFontMetrics().getHeight();
	    		
	    		if (width <= 0) {
	    			width = 1;
	    		}
	    		
	    		if (height <= 0) {
	    			height = 1;
	    		}
	    		
				img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
				
				// TODO size is an ugly hack...
				drawStringNative(str, img, width, height, g2.getFont().getFamily(), (int)(g2.getFont().getSize() / 1.4f + 0.5f), g2.getFont().getStyle(), g2.getColor().getRGB());

				if (TEXT_CACHING) {
					imageCache.put(key, img);
				}
	    	}
	    	
	    	// Because of the crappy cooperation between Java and Qt, there is an
	    	// offset of 3 (or 4 perhaps, we are not sure yet) pixels needed.
			g2.drawImage(img, (int) x + 1, (int) y - img.getHeight() + 3, null);
	    	
	    } else {
	    	g2.drawString(str, x, y);
	    }
	}
	
	/**
	 * Initializes the native code. This method <b>must</b> be called before any other
	 * native call. This is a native method, implemented in <code>joxy_utils_JoxyGraphics.cpp</code>.
	 */
	private static native void initializeNative();

	/**
	 * Paints the given string onto the given image. This is a native method, implemented
	 * in <code>joxy_utils_JoxyGraphics.cpp</code>.
	 * 
	 * @param str Some string.
	 * @param image Some image.
	 * @param width The width of the image given.
	 * @param height The height of the image given.
	 * @param font The name of the font to draw in.
	 * @param fontSize The font size.
	 * @param style The style, as Java Font style, to use.
	 * @param color The color, as RGB integer value, to draw in.
	 */
	private static native void drawStringNative(String str, BufferedImage image, int width, int height, String font, int fontSize, int style, int color);
}
