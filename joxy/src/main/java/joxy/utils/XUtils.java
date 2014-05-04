package joxy.utils;

import java.awt.Component;
import java.awt.Window;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * This class contains some utilities for interacting with X.
 */
public class XUtils {
	
	/**
	 * Test to integrate with the Oxygen window decoration...
	 */
	@SuppressWarnings("restriction")
	public static void setOxygenGradientHint(Window window, boolean hint) {
		try {
			// long windowId = peer.getWindow();
			Field peerField = Component.class.getDeclaredField("peer");
			peerField.setAccessible(true);
			Class<?> xWindowPeerClass = Class.forName("sun.awt.X11.XWindowPeer");
			Method getWindowMethod = xWindowPeerClass.getMethod("getWindow", new Class[0]);
			long windowId = ((Long) getWindowMethod.invoke(peerField.get(window), new Object[0])).longValue();
			
			sun.awt.X11.XAtom.get("_KDE_OXYGEN_BACKGROUND_GRADIENT").setCard32Property(windowId, hint ? 1 : 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

