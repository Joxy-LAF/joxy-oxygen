package joxy.kde;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.TrayIcon;
import java.awt.peer.TrayIconPeer;

import sun.awt.X11.*;

// Yes, we know that this is restricted access, but why?
@SuppressWarnings("restriction")
/**
 * Attempt to create a toolkit that does some native stuff for Joxy.
 */
public class JoxyToolkit extends XToolkit {
	@Override
	public TrayIconPeer createTrayIcon(TrayIcon target) throws HeadlessException, AWTException {
		return new JoxyTrayIconPeer(target);
	}
}
