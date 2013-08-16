package joxy.utils;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.UIManager;

/**
 * This class contains some static methods to help with painting.
 */
public class PaintHelper {
	
	/**
	 * Paints the underlying components of this component, in the area that is covered by
	 * the component.
	 * 
	 * <p>Java uses the opaque value for two reasons: both indicating whether a component
	 * should have a background and whether they may be optimized for drawing (that means
	 * that the parent doesn't have to be drawn, since the component has a background and
	 * thus fills all of its pixels). However, in Joxy the text components that do have a
	 * background still don't fill all of their pixels.</p>
	 * 
	 * <p>We still want to keep the opaque value as it is, to please applications relying
	 * on it (Netbeans for example). Therefore we keep opaque on true, and then paint the
	 * background ourselves. This method is responsible for that.</p>
	 * 
	 * <p><b>TODO</b>: this method is not finished yet!</p>
	 * 
	 * @param g The Graphics object to paint with.
	 */
	public static void paintUnderlying(Graphics g, JComponent c) {
		g.setColor(UIManager.getColor("Window.background"));
		g.fillRect(0, 0, c.getWidth(), c.getHeight());
	}
}
