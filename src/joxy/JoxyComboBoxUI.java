package joxy;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

import joxy.painter.ButtonSlabPainter;
import joxy.painter.FocusIndicatorPainter;
import joxy.painter.HoverIndicatorPainter;
import joxy.painter.PressedButtonSlabPainter;

public class JoxyComboBoxUI extends BasicComboBoxUI {
	
	/** The width and height of the arcs that form up
	 *  the corners of the rounded rectangles. */
	public static final int ARC = 8;
	
	protected boolean hovered = false;
	
	public static ComponentUI createUI(JComponent c) {
		return new JoxyComboBoxUI();
	}
	
	public void installUI(JComponent c) {
		super.installUI(c);
		
		((JComponent) currentValuePane).setOpaque(false);
		Component editorComponent = comboBox.getEditor().getEditorComponent();
		if (editorComponent instanceof JComponent) {
			((JComponent) editorComponent).setOpaque(false);
			((JComponent) editorComponent).setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		}
	}
	
	@Override
	protected void installDefaults() {
		comboBox.setFont(UIManager.getFont("Button.font"));
		comboBox.setOpaque(false);
		comboBox.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
	}
	
	@Override
	protected void installListeners() {
		super.installListeners();
		
		MouseListener mouseListener = new MouseListener() {
			
			@Override
			public void mouseExited(MouseEvent e) {
				hovered = false;
				comboBox.repaint();
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				hovered = true;
				comboBox.repaint();
			}
			
			// [ws] TODO The following repaints are just workarounds for some non-repainting problems
			// that probably need fixing elsewhere
			@Override
			public void mouseReleased(MouseEvent e) {
				comboBox.repaint();
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				comboBox.repaint();
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				comboBox.repaint();
			}
		};
		
		comboBox.addMouseListener(mouseListener);
	}
	
	@Override
	public void paint(Graphics g, JComponent c) {
		hasFocus = comboBox.hasFocus();
		
		if (!comboBox.isEditable()) {
			paintBackgroundNonEditable((Graphics2D) g);
            Rectangle r = rectangleForCurrentValue();
			paintCurrentValue(g, r, hasFocus);
		} else {
			paintBackgroundEditable((Graphics2D) g);
		}
	}

	/**
	 * Paints the background of a combo box that is non-editable.
	 * @param g2 The graphics object to paint with.
	 */
	protected void paintBackgroundNonEditable(Graphics2D g2) {
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
		if (isPopupVisible(comboBox)) { // [ws] TODO this does not work well...
			PressedButtonSlabPainter.paint(g2, 2, 2, comboBox.getWidth() - 4,
					comboBox.getHeight() - 4);
		} else {
			// If mouse is over the component, draw hover indicator
			if (hovered) { // [ws] TODO!
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
	
	/**
	 * Paints the background of a combo box that is editable.
	 * @param g2 The graphics object to paint with.
	 */
	protected void paintBackgroundEditable(Graphics2D g2) {
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

		g2.setColor(Color.WHITE);
		g2.fill(new RoundRectangle2D.Double(0, 0, comboBox.getWidth() - 1, comboBox.getHeight() - 1, ARC, ARC));

		g2.setStroke(new BasicStroke(1.2f));
		g2.setColor(new Color(140, 140, 140));
		g2.draw(new RoundRectangle2D.Double(0, 0, comboBox.getWidth(), comboBox.getHeight(), ARC, ARC));
	}
	
	@Override
	protected JButton createArrowButton() {
		JButton button = new JoxyArrowButton(BasicArrowButton.SOUTH);
		button.setName("ComboBox.arrowButton");
		return button;
	}
	
	@Override
	protected ComboPopup createPopup() {
		ComboPopup popup = new BasicComboPopup(comboBox);
		
		popup.getList().setOpaque(true);

        LookAndFeel.installBorder(popup.getList(), "List.border");

        LookAndFeel.installColorsAndFont(popup.getList(), "List.background", "List.foreground", "List.font");
        
        Color sbg = popup.getList().getSelectionBackground();
        if (sbg == null || sbg instanceof UIResource) {
        	popup.getList().setSelectionBackground(UIManager.getColor("List.selectionBackground"));
        }
        
        Color sfg = popup.getList().getSelectionForeground();
        if (sfg == null || sfg instanceof UIResource) {
        	popup.getList().setSelectionForeground(UIManager.getColor("List.selectionForeground"));
        }

		return popup;
	}
}
