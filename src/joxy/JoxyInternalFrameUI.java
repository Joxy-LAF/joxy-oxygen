package joxy;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicInternalFrameUI;

public class JoxyInternalFrameUI extends BasicInternalFrameUI {

	public static ComponentUI createUI(JComponent c) {
		JoxyInternalFrameUI ui = new JoxyInternalFrameUI((JInternalFrame) c);
		return ui;
	}
	
	public JoxyInternalFrameUI(JInternalFrame b) {
		super(b);
	}
	
	@Override
	protected void installDefaults() {
		super.installDefaults();
		frame.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	}
	
	@Override
	public void paint(Graphics g, JComponent c) {
		super.paint(g, c);
		
		frame.getRootPane().getUI().paint(g, c);
	}
}
