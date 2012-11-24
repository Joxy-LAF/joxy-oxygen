package joxy.painter;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * The abstract super class of all painters. A painter is a class that is able to paint
 * a certain object, for example a button slab.
 * 
 * <p>A painter always has a paint method. This super class encapsulates this method to
 * introduce caching. A painter can also require data, such as colors or the opacity.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public abstract class Painter {
	
	/**
	 * The cache of this painter.
	 */
	private BufferedImage cache = null;
	
	/**
	 * Paints the object given the currently set data, using the {@link Graphics2D}
	 * object provided.
	 * 
	 * <p>This method is only used internally, and doesn't cache the image. To draw the
	 * object into a GUI, use the {@link #paint(Graphics2D, int, int, int, int)} method.
	 * 
	 * @param g2 The Graphics2D object to paint with.
	 * @param width The desired width.
	 * @param height The desired height.
	 */
	protected abstract void paintObject(Graphics2D g2, float width, float height);
	
	/**
	 * Paints the object given the currently set data, using the {@link Graphics2D}
	 * object provided.
	 * 
	 * <p>If the data hasn't changed and the width and height are the same as for the
	 * previous call of this method, the object will be painted from the cache.</p>
	 * 
	 * @param g2 The Graphics2D object to paint with.
	 * @param x The x-coordinate of the upper-left corner.
	 * @param y The y-coordinate of the upper-left corner.
	 * @param width The desired width of the object.
	 * @param height The desired height of the object.
	 */
	public void paint(Graphics2D g2, int x, int y, int width, int height) {
		
		if (width <= 0 || height <= 0) {
			return;
		}
		
		if (cache == null || cache.getWidth() != width || cache.getHeight() != height) {
			cache = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D gimg = (Graphics2D) cache.getGraphics();
			gimg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			gimg.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			
			paintObject(gimg, width, height);
		}
		
		g2.drawImage(cache, x, y, null);
	}
}
