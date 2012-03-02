package joxy.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

/**
 * A class that tries to make the default Java font rendering more
 * like KDE does.
 */
public class JoxyGraphics {
	
	public static final boolean NATIVE_TEXT_RENDERING = false;
	
	private static Hashtable<String, BufferedImage> imageCache;

	static {
		if (NATIVE_TEXT_RENDERING){
			System.loadLibrary("nativeTextRenderer");
			
			imageCache = new Hashtable<String, BufferedImage>();
		}
	}
	
	/**
	 * Draws a string with the given Graphics object. This is done by a call to Qt.
	 * 
	 * @param c   The component that we are drawing for. May not be <code>null</code>.
	 * @param str The String to draw.
	 * @param x   The x-coordinate of the left side of the text.
	 * @param y   The y-coordinate of the lower side of the text.
	 */
	public static void drawString(Graphics2D g2, String str, float x, float y) {
	    if (NATIVE_TEXT_RENDERING) {
	    	// Try to use the cache.
	    	BufferedImage img = imageCache.get(str);
	    	
	    	// If not in cache, call native method to create it, and put it in the cache.
	    	if (img == null) {
	    		Output.debug("Native text rendering called for \"" + str + "\"");
	    		
				img = new BufferedImage(400, 30, BufferedImage.TYPE_INT_ARGB);
				
				drawStringNative(str, img);
				
				imageCache.put(str, img);
	    	}
	    	
			g2.drawImage(img, (int) x, (int) y - 10, null);
	    	
	    } else {
	    	g2.drawString(str, x, y);
	    }
	}
	
	/**
	 * Paints the given string onto the given image.
	 * @param str Some string.
	 * @param image Some image.
	 */
	private static native void drawStringNative(String str, BufferedImage image);
}
