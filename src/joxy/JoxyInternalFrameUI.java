package joxy;

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
		System.out.println("Blub blub");
	}
	
	@Override
	protected void installDefaults() {
		super.installDefaults();
	}

}
