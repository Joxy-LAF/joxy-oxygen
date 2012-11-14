package joxy;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.*;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSpinnerUI;

import joxy.painter.InputFieldPainter;

/**
 * Class overriding the default Spinner (BasicSpinnerUI) to provide a good
 * integration with the Oxygen KDE style. Part of the Joxy Look and Feel.
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
	public void paint(Graphics g, JComponent c) {

		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
		InputFieldPainter.paint(g2, 0, 0, spinner.getWidth(), spinner.getHeight());
		
		super.paint(g, c);
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
	
	@Override
	protected JComponent createEditor() {
		JComponent e = super.createEditor();
		
		if (e instanceof DefaultEditor) {
			((DefaultEditor) e).getTextField().putClientProperty("joxy.isEditor", true);
		}
		
		return e;
	}
}
