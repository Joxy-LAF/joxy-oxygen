package joxy;

import java.awt.Component;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSpinnerUI;

/**
 * Class overriding the default Spinner (BasicSpinnerUI) to provide a good
 * integration with the Oxygen KDE style. Part of the Joxy Look and Feel.
 * 
 * [ws] TODO I suppose that to style the editor field, we need to implement the JFormattedTextField.
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxySpinnerUI extends BasicSpinnerUI {
	
    public static ComponentUI createUI(JComponent b) {
        return new JoxySpinnerUI();
    }
	
	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
	}
	
	@Override
	protected void installDefaults() {
		super.installDefaults();
		
		spinner.setBorder(null);
		spinner.setOpaque(false);
	}
	
	@Override
	protected void installListeners() {
		super.installListeners();
		
		// A scroll listener to enable scrolling through the elements of the combo box
		MouseWheelListener scrollListener = new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {

					if (e.getWheelRotation() == -1) {
						spinner.setValue(spinner.getNextValue());
					}

					if (e.getWheelRotation() == 1) {
						spinner.setValue(spinner.getPreviousValue());
					}

				}
			}
		};
		
		spinner.addMouseWheelListener(scrollListener);
	}
	
	@Override
	protected Component createNextButton() {
		JoxyArrowButton b = new JoxyArrowButton(SwingConstants.NORTH);
		installNextButtonListeners(b);
		return b;
	}
	
	@Override
	protected Component createPreviousButton() {
		JoxyArrowButton b = new JoxyArrowButton(SwingConstants.SOUTH);
		installPreviousButtonListeners(b);
		return b;
	}
}
