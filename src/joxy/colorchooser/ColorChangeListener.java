package joxy.colorchooser;

import java.awt.Color;

/**
 * A listener interface with which a {@link DiagramComponent} can indicate
 * to other components that the colour has been changed.
 */
public interface ColorChangeListener {
	
	/**
	 * Called when the colour has been changed.
	 * 
	 * @param newColor The newly selected colour.
	 */
	public void colorChanged(Color newColor);
}
