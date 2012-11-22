package joxy;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.basic.BasicSliderUI.ScrollListener;

import joxy.painter.*;

/**
 * Joxy's UI delegate for the JComboBox.
 * 
 * <p>A combo box can be either editable or non-editable, and Joxy draws both variants
 * differently, just like KDE does. A non-editable combo box looks like a JButton; an
 * editable combo box looks like a JTextField.</p>
 * 
 * <p>In both variants, it is possible to use the scroll wheel to traverse through the
 * options. This is done by adding a {@link ScrollListener}.</p>
 * 
 * <p>The JoxyComboBoxUI supports animations for the focus and hovered states, but only
 * for non-editable combo boxes. See JoxyButtonUI for more details. Support for those
 * animations on editable combo boxes is planned.</p>
 */
public class JoxyComboBoxUI extends BasicComboBoxUI {
	
	/** The width and height of the arcs that form up
	 *  the corners of the rounded rectangles. */
	public static final int ARC = 8;
	
	/** Amount of hover and focus, from 0 to 255 */
	private int hoverAmount = 0, focusAmount = 0;
	
	/** Timers for the animation */
	private Timer hoverTimer, focusTimer;
	
	/** Listeners for the animation */
	private MouseListener hoverListener;
	private FocusListener focusListener;
	
	private boolean hovered = false;
	
	/** Listeners for scrolling */
	private MouseWheelListener scrollListener;
	
	/**
	 * The painter for the button slab.
	 */
	private ButtonSlabPainter slabPainter = new ButtonSlabPainter();
	
	public static ComponentUI createUI(JComponent c) {
		return new JoxyComboBoxUI();
	}
	
	public void installUI(JComponent c) {
		super.installUI(c);
		
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
		
		hoverListener = new MouseListener() {

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
			
			@Override
			public void mouseEntered(MouseEvent e) {
				hovered = true;
				hoverTimer.start();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				hovered = false;
				hoverTimer.start();
			}
		};
		comboBox.addMouseListener(hoverListener);
		
		focusListener = new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				focusTimer.start();
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				focusTimer.start();
			}
		};
		comboBox.addFocusListener(focusListener);
		
		createTimers(comboBox);
		
		// A scroll listener to enable scrolling through the elements of the combo box
		scrollListener = new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {

					if (e.getWheelRotation() == 1) {
						int currentIndex = comboBox.getSelectedIndex();
						if (currentIndex < comboBox.getItemCount() - 1) {
							comboBox.setSelectedIndex(currentIndex + 1);
						}
					}

					if (e.getWheelRotation() == -1) {
						int currentIndex = comboBox.getSelectedIndex();
						if (currentIndex > 0) {
							comboBox.setSelectedIndex(currentIndex - 1);
						}
					}

				}
			}
		};
		
		comboBox.addMouseWheelListener(scrollListener);
	}
	
	private void createTimers(final JComboBox comboBox) {
		hoverTimer = new Timer(40, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (hovered) {
					hoverAmount += 60;
				} else {
					hoverAmount -= 60;
				}
				if (hoverAmount > 255) {
					hoverAmount = 255;
					hoverTimer.stop();
				}
				if (hoverAmount < 0) {
					hoverAmount = 0;
					hoverTimer.stop();
				}
				comboBox.repaint();
			}
		});
		
		focusTimer = new Timer(40, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (comboBox.hasFocus()) {
					focusAmount += 60;
				} else {
					focusAmount -= 60;
				}
				if (focusAmount > 255) {
					focusAmount = 255;
					focusTimer.stop();
				}
				if (focusAmount < 0) {
					focusAmount = 0;
					focusTimer.stop();
				}
				comboBox.repaint();
			}
		});
	}
	
	@Override
	protected void uninstallListeners() {
		super.uninstallListeners();
		
		comboBox.removeMouseListener(hoverListener);
		comboBox.removeFocusListener(focusListener);
		comboBox.removeMouseWheelListener(scrollListener);
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
			// shadow
			g2.setColor(new Color(0, 0, 0, 80));
			g2.fill(new RoundRectangle2D.Double(2, 2, comboBox.getWidth() - 4, comboBox.getHeight() - 4, ARC, ARC));
			g2.setColor(new Color(0, 0, 0, 40));
			g2.fill(new RoundRectangle2D.Double(2, 3, comboBox.getWidth() - 4, comboBox.getHeight() - 4, ARC, ARC));
			g2.fill(new RoundRectangle2D.Double(1, 3, comboBox.getWidth() - 2, comboBox.getHeight() - 2, ARC+6, ARC+6));
	
			// decorations
			FocusIndicatorPainter.paint(g2, 2, 2, comboBox.getWidth() - 4, comboBox.getHeight() - 4, focusAmount);
			HoverIndicatorPainter.paint(g2, 2, 2, comboBox.getWidth() - 4, comboBox.getHeight() - 4, hoverAmount);

			slabPainter.paint(g2, 2, 2, comboBox.getWidth() - 4, comboBox.getHeight() - 4);
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

		InputFieldPainter.paint(g2, 0, 0, comboBox.getWidth(), comboBox.getHeight());
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
	
	@Override
	public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
		// nothing!
	}
	
	/**
	 * Copy from super, with ugly changes
	 */
	@Override
	public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
        ListCellRenderer renderer = comboBox.getRenderer();
        Component c;

        if ( hasFocus && !isPopupVisible(comboBox) ) {
            c = renderer.getListCellRendererComponent( listBox,
                                                       comboBox.getSelectedItem(),
                                                       -1,
                                                       true,
                                                       false );
        }
        else {
            c = renderer.getListCellRendererComponent( listBox,
                                                       comboBox.getSelectedItem(),
                                                       -1,
                                                       false,
                                                       false );
        }
        
        c.setFont(comboBox.getFont());
        c.setForeground(comboBox.getForeground());
        c.setBackground(new Color(0, 0, 0, 0)); // transparent

        // ugly! these coordinates should not be fixed, compare super
        currentValuePane.paintComponent(g,c,comboBox, 5, 0, comboBox.getWidth() - 20, comboBox.getHeight());
	}
	
	@Override
	protected ComboBoxEditor createEditor() {
		ComboBoxEditor e = super.createEditor();
		
		if (e.getEditorComponent() instanceof JTextField) {
			((JTextField) e.getEditorComponent()).putClientProperty("joxy.isEditor", true);
		}
		
		return e;
	}
}
