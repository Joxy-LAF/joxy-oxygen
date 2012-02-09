package joxy.utils;

import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * A class that tries to make the default Java font rendering more
 * like KDE does.
 */
public class JoxyGraphics {
	
	//static {
	//	System.loadLibrary("nativeTextRenderer");
	//}
	
	/**
	 * Draws a string with the given Graphics object. This is done by a call to Qt.
	 * 
	 * @modifies c
	 * @param c   The component that we are drawing for. May not be <code>null</code>.
	 * @param str The String to draw.
	 * @param x   The x-coordinate of the left side of the text.
	 * @param y   The y-coordinate of the lower side of the text.
	 */
	public static void drawString(Graphics2D g2, String str, float x, float y) {
		
		//while (c.isLightweight()) {
		//	c = c.getParent();
		//}
		
		//Output.debug(c);
		
		//if (/*c instanceof JFrame*/ false) {
		//	Graphics g = ((Window) c).getGraphics();
		//	drawStringNative(g, Window.class, str, x, y);
		//} else {
		    g2.drawString(str, x, y);
		//}
	}
	
	public static native void drawStringNative(Graphics g, Class c, String str, float x, float y);
}
