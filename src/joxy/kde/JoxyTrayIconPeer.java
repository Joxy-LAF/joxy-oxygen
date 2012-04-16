package joxy.kde;

import java.awt.AWTException;
import java.awt.TrayIcon;

import java.awt.TrayIcon.MessageType;
import java.awt.peer.*;

import joxy.utils.Utils;

@SuppressWarnings("restriction")
public class JoxyTrayIconPeer implements TrayIconPeer {

	private String tooltip = "";
	
	public JoxyTrayIconPeer(TrayIcon target) throws AWTException {
		// Well... probably should do something?
	}

	@Override
	public void dispose() {
		throw new UnsupportedOperationException("Not implemented: dispose");
	}

	@Override
	public void setToolTip(String tooltip) {
		this.tooltip = tooltip;
	}

	@Override
	public void updateImage() {
		throw new UnsupportedOperationException("Not implemented: updateImage");
	}

	@Override
	public void displayMessage(String caption, String text, String messageType) {
		Utils.displayNotification(caption, text, MessageType.valueOf(messageType));
	}

	@Override
	public void showPopupMenu(int x, int y) {
		throw new UnsupportedOperationException("Not implemented: showPopupMenu");
	}

}
