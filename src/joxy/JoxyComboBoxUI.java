package joxy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;

import joxy.painter.ButtonSlabPainter;
import joxy.painter.FocusIndicatorPainter;
import joxy.painter.HoverIndicatorPainter;
import joxy.painter.PressedButtonSlabPainter;

public class JoxyComboBoxUI extends BasicComboBoxUI {
	
	/** The width and height of the arcs that form up
	 *  the corners of the rounded rectangles. */
	public static final int ARC = 8;
	
	public static ComponentUI createUI(JComponent c) {
		return new JoxyComboBoxUI();
	}
	
	@Override
	protected void installDefaults() {
		comboBox.setFont(UIManager.getFont("Button.font"));
		comboBox.setOpaque(false);
	}
	
	@Override
	public void paint(Graphics g, JComponent c) {
		hasFocus = comboBox.hasFocus();
		
		if (!comboBox.isEditable()) {
			paintBackgroundNonEditable((Graphics2D) g);
            Rectangle r = rectangleForCurrentValue();
			paintCurrentValue(g, r, hasFocus);
		}
	}

	protected void paintBackgroundNonEditable(Graphics2D g2) {
		if (popup.isVisible()) { // [ws] TODO this does not work well...
			PressedButtonSlabPainter.paint(g2, 2, 2, comboBox.getWidth() - 4,
					comboBox.getHeight() - 4);
		} else {
			// If mouse is over the component, draw hover indicator
			if (false) { // [ws] TODO!
				HoverIndicatorPainter.paint(g2, 2, 2, comboBox.getWidth() - 4,
						comboBox.getHeight() - 4);
			} else {
				// If it has the focus, draw focus indicator
				if (comboBox.isFocusOwner()) {
					FocusIndicatorPainter.paint(g2, 2, 2, comboBox.getWidth() - 4,
							comboBox.getHeight() - 4);
				} else {
					// No blue borders necessary, so draw shadow
					g2.setColor(new Color(0, 0, 0, 40));
					g2.fill(new RoundRectangle2D.Double(2, 2, comboBox.getWidth() - 4,
							comboBox.getHeight() - 4, ARC, ARC));
					g2.setColor(new Color(0, 0, 0, 20));
					g2.fill(new RoundRectangle2D.Double(2, 3, comboBox.getWidth() - 4,
							comboBox.getHeight() - 4, ARC, ARC));
					g2.fill(new RoundRectangle2D.Double(2, 4, comboBox.getWidth() - 4,
							comboBox.getHeight() - 4, ARC, ARC));
				}
			}

			ButtonSlabPainter.paint(g2, 2, 2, comboBox.getWidth() - 4,
					comboBox.getHeight() - 4);
		}

		// TODO Draw disabled buttons differently
		if (!comboBox.isEnabled()) {

		}
	}
	
	@Override
	public Dimension getPreferredSize(JComponent c) {
		// Simply pick the preferred size of the BasicComboBoxUI and make it
		// larger to account for the larger decorations.
		// If you consider this ugly... you are right.
		Dimension dim = super.getPreferredSize(c);
		dim.width += 10;
		dim.height += 4;
		
		return dim;
	}
	
	@Override
	protected JButton createArrowButton() {
		// TODO Auto-generated method stub
		return new JButton("dropdown");
	}
}
