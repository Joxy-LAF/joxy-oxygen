package joxy;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.*;

import joxy.painter.InputFieldPainter;
import joxy.painter.TextFieldFocusIndicatorPainter;
import joxy.painter.TextFieldHoverIndicatorPainter;
import joxy.utils.Utils;

/**
 * Joxy's UI delegate for the JTextField.
 * 
 * <p>The JoxyTextFieldUI supports animations for the focus and hovered states.
 * See JoxyButtonUI for more details. Furthermore the clear button in the
 * text field is animated.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyTextFieldUI extends BasicTextFieldUI {
	
	/**
	 * The text field we are painting for.
	 */
	protected JTextField textField;
	
	/**
	 * A {@link MouseListener} that clears the contents of the text field when
	 * the clear button has been pressed.
	 */
	MouseListener clearListener;
	
	/**
	 * A {@link DocumentListener} that hides the clear button if there is no
	 * text in the text field.
	 */
	DocumentListener changeListener;
	
	/** Timer for the clear button animation */
	private Timer clearButtonTimer;
	
	/**
	 * The opacity of the clear button. 0 means non-visible, 255 is fully opaque.
	 */
	int clearButtonOpacity = 0;
	
	/**
	 * Amount of hover and focus, from 0 to 255.
	 */
	private int hoverAmount = 0, focusAmount = 0;

	/** Timers for the animation */
	private Timer hoverTimer, focusTimer;
	
	/** Listeners for the animation */
	private MouseListener hoverListener;
	private FocusListener focusListener;
	
	private boolean hovered = false;
	
	/**
	 * The icon to use for the clear button.
	 */
	private static ImageIcon clearIcon = Utils.getOxygenIcon("actions/edit-clear-locationbar-rtl", 16);
	
	/**
	 * For some reason it doesn't work to add the changeListener to the Document of
	 * the text field in installListeners(). Therefore we do it in the paint method.
	 * Of course it only needs to be added once, and therefore we use this variable.
	 * TODO figure out why just using installListeners() doesn't work
	 */
	boolean changeListenerAdded = false;
	
	public static ComponentUI createUI(JComponent c) {
		c.setOpaque(false);
		return new JoxyTextFieldUI(c);
	}

	public JoxyTextFieldUI(JComponent c) {
		textField = (JTextField) c;
	}
	
	@Override
	protected void installDefaults() {
		// TODO Auto-generated method stub
		super.installDefaults();
		
		// [ws] TODO deze dingen kunnen eigenlijk ook gewoon in de defaults...
		// [ws] TODO This is bad, since the clear button could not be shown, and then the 25
		// pixels on the right side are strange
		textField.setBorder(BorderFactory.createEmptyBorder(3, 5, 5, 25));
		textField.setFont(UIManager.getFont("Button.font"));
		textField.setOpaque(false);
	}
	
	@Override
	protected void installListeners() {
		super.installListeners();
		
		clearListener = new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				// Clear the text if
				// 1. the clear button is visible;
				// 2. the click happened inside the clear button;
				// 3. the text field is not an editor, for example in an editable JComboBox,
				//    that manages its own borders et cetera;
				// 4. the text field is editable (in fact, the clear button should not be
				//    visible if the field is non-editable, but for some reason that still
				//    happens).
				if (clearButtonOpacity > 0 && e.getX() > textField.getWidth() - 24
						          && textField.getClientProperty("joxy.isEditor") == null
						          && textField.isEditable()) {
					textField.setText("");
				}
			}
		};
		
		textField.addMouseListener(clearListener);
		
		changeListener = new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateClearButton();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateClearButton();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateClearButton();
			}
			
			private void updateClearButton() {
				clearButtonTimer.start();
			}
		};
		
		hoverListener = new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
			
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
		textField.addMouseListener(hoverListener);
		
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
		textField.addFocusListener(focusListener);
			
		createTimers();
	}
	
	private void createTimers() {
		clearButtonTimer = new Timer(40, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (textField.getText().length() > 0) {
					clearButtonOpacity += 70;
				} else {
					clearButtonOpacity -= 70;
				}
				if (clearButtonOpacity > 255) {
					clearButtonOpacity = 255;
					clearButtonTimer.stop();
				}
				if (clearButtonOpacity < 0) {
					clearButtonOpacity = 0;
					clearButtonTimer.stop();
				}
				textField.repaint();
			}
		});
		
		// start the timer once, to ensure the button is visible if there is text
		// in the text field initially
		clearButtonTimer.start();
		
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
				textField.repaint();
			}
		});
		
		focusTimer = new Timer(40, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (textField.hasFocus()) {
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
				textField.repaint();
			}
		});
	}
	
	@Override
	protected String getPropertyPrefix() {
		return "TextField";
	}
	

	/**
	 * Note: copied from {@link BasicTextFieldUI}.
	 */
    @Override
	public View create(Element elem) {
        Document doc = elem.getDocument();
        Object i18nFlag = doc.getProperty("i18n"/*AbstractDocument.I18NProperty*/);
        if (Boolean.TRUE.equals(i18nFlag)) {
            // To support bidirectional text, we build a more heavyweight
            // representation of the field.
            String kind = elem.getName();
            if (kind != null) {
                if (kind.equals(AbstractDocument.ContentElementName)) {
                    return new GlyphView(elem);
                } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                    return new I18nFieldView(elem);
                }
            }
            // this shouldn't happen, should probably throw in this case.
        }
        return new FieldView(elem);
    }
    
    /**
     * A field view that support bidirectional text via the
     * support provided by ParagraphView.
     */
    static class I18nFieldView extends ParagraphView {

        I18nFieldView(Element elem) {
            super(elem);
        }

        /**
         * Fetch the constraining span to flow against for
         * the given child index.  There is no limit for
         * a field since it scrolls, so this is implemented to
         * return <code>Integer.MAX_VALUE</code>.
         */
        @Override
		public int getFlowSpan(int index) {
            return Integer.MAX_VALUE;
        }

        @Override
		protected void setJustification(int j) {
            // Justification is done in adjustAllocation(), so disable
            // ParagraphView's justification handling by doing nothing here.
        }

        static boolean isLeftToRight( java.awt.Component c ) {
            return c.getComponentOrientation().isLeftToRight();
        }

        /**
         * Adjusts the allocation given to the view
         * to be a suitable allocation for a text field.
         * If the view has been allocated more than the
         * preferred span vertically, the allocation is
         * changed to be centered vertically.  Horizontally
         * the view is adjusted according to the horizontal
         * alignment property set on the associated JTextField
         * (if that is the type of the hosting component).
         *
         * @param a the allocation given to the view, which may need
         *  to be adjusted.
         * @return the allocation that the superclass should use.
         */
        Shape adjustAllocation(Shape a) {
            if (a != null) {
                Rectangle bounds = a.getBounds();
                int vspan = (int) getPreferredSpan(Y_AXIS);
                int hspan = (int) getPreferredSpan(X_AXIS);
                if (bounds.height != vspan) {
                    int slop = bounds.height - vspan;
                    bounds.y += slop / 2;
                    bounds.height -= slop;
                }

                // horizontal adjustments
                Component c = getContainer();
                if (c instanceof JTextField) {
                    JTextField field = (JTextField) c;
                    BoundedRangeModel vis = field.getHorizontalVisibility();
                    int max = Math.max(hspan, bounds.width);
                    int value = vis.getValue();
                    int extent = Math.min(max, bounds.width - 1);
                    if ((value + extent) > max) {
                        value = max - extent;
                    }
                    vis.setRangeProperties(value, extent, vis.getMinimum(),
                                           max, false);
                    if (hspan < bounds.width) {
                        // horizontally align the interior
                        int slop = bounds.width - 1 - hspan;

                        int align = ((JTextField)c).getHorizontalAlignment();
                        if(isLeftToRight(c)) {
                            if(align==LEADING) {
                                align = LEFT;
                            }
                            else if(align==TRAILING) {
                                align = RIGHT;
                            }
                        }
                        else {
                            if(align==LEADING) {
                                align = RIGHT;
                            }
                            else if(align==TRAILING) {
                                align = LEFT;
                            }
                        }

                        switch (align) {
                        case SwingConstants.CENTER:
                            bounds.x += slop / 2;
                            bounds.width -= slop;
                            break;
                        case SwingConstants.RIGHT:
                            bounds.x += slop;
                            bounds.width -= slop;
                            break;
                        }
                    } else {
                        // adjust the allocation to match the bounded range.
                        bounds.width = hspan;
                        bounds.x -= vis.getValue();
                    }
                }
                return bounds;
            }
            return null;
        }

        /**
         * Update the visibility model with the associated JTextField
         * (if there is one) to reflect the current visibility as a
         * result of changes to the document model.  The bounded
         * range properties are updated.  If the view hasn't yet been
         * shown the extent will be zero and we just set it to be full
         * until determined otherwise.
         */
        void updateVisibilityModel() {
            Component c = getContainer();
            if (c instanceof JTextField) {
                JTextField field = (JTextField) c;
                BoundedRangeModel vis = field.getHorizontalVisibility();
                int hspan = (int) getPreferredSpan(X_AXIS);
                int extent = vis.getExtent();
                int maximum = Math.max(hspan, extent);
                extent = (extent == 0) ? maximum : extent;
                int value = maximum - extent;
                int oldValue = vis.getValue();
                if ((oldValue + extent) > maximum) {
                    oldValue = maximum - extent;
                }
                value = Math.max(0, Math.min(value, oldValue));
                vis.setRangeProperties(value, extent, 0, maximum, false);
            }
        }

        // --- View methods -------------------------------------------

        /**
         * Renders using the given rendering surface and area on that surface.
         * The view may need to do layout and create child views to enable
         * itself to render into the given allocation.
         *
         * @param g the rendering surface to use
         * @param a the allocated region to render into
         *
         * @see View#paint
         */
        @Override
		public void paint(Graphics g, Shape a) {
            Rectangle r = (Rectangle) a;
            
    		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

            g.clipRect(r.x, r.y, r.width, r.height);
            super.paint(g, adjustAllocation(a));
        }

        /**
         * Determines the resizability of the view along the
         * given axis.  A value of 0 or less is not resizable.
         *
         * @param axis View.X_AXIS or View.Y_AXIS
         * @return the weight -> 1 for View.X_AXIS, else 0
         */
        @Override
		public int getResizeWeight(int axis) {
            if (axis == View.X_AXIS) {
                return 1;
            }
            return 0;
        }

        /**
         * Provides a mapping from the document model coordinate space
         * to the coordinate space of the view mapped to it.
         *
         * @param pos the position to convert >= 0
         * @param a the allocated region to render into
         * @return the bounding box of the given position
         * @exception BadLocationException  if the given position does not
         *   represent a valid location in the associated document
         * @see View#modelToView
         */
        @Override
		public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException {
            return super.modelToView(pos, adjustAllocation(a), b);
        }

        /**
         * Provides a mapping from the document model coordinate space
         * to the coordinate space of the view mapped to it.
         *
         * @param p0 the position to convert >= 0
         * @param b0 the bias toward the previous character or the
         *  next character represented by p0, in case the
         *  position is a boundary of two views.
         * @param p1 the position to convert >= 0
         * @param b1 the bias toward the previous character or the
         *  next character represented by p1, in case the
         *  position is a boundary of two views.
         * @param a the allocated region to render into
         * @return the bounding box of the given position is returned
         * @exception BadLocationException  if the given position does
         *   not represent a valid location in the associated document
         * @exception IllegalArgumentException for an invalid bias argument
         * @see View#viewToModel
         */
        @Override
		public Shape modelToView(int p0, Position.Bias b0,
                                 int p1, Position.Bias b1, Shape a)
            throws BadLocationException
        {
            return super.modelToView(p0, b0, p1, b1, adjustAllocation(a));
        }

        /**
         * Provides a mapping from the view coordinate space to the logical
         * coordinate space of the model.
         *
         * @param fx the X coordinate >= 0.0f
         * @param fy the Y coordinate >= 0.0f
         * @param a the allocated region to render into
         * @return the location within the model that best represents the
         *  given point in the view
         * @see View#viewToModel
         */
        @Override
		public int viewToModel(float fx, float fy, Shape a, Position.Bias[] bias) {
            return super.viewToModel(fx, fy, adjustAllocation(a), bias);
        }

        /**
         * Gives notification that something was inserted into the document
         * in a location that this view is responsible for.
         *
         * @param changes the change information from the associated document
         * @param a the current allocation of the view
         * @param f the factory to use to rebuild if the view has children
         * @see View#insertUpdate
         */
        @Override
		public void insertUpdate(DocumentEvent changes, Shape a, ViewFactory f) {
            super.insertUpdate(changes, adjustAllocation(a), f);
            updateVisibilityModel();
        }

        /**
         * Gives notification that something was removed from the document
         * in a location that this view is responsible for.
         *
         * @param changes the change information from the associated document
         * @param a the current allocation of the view
         * @param f the factory to use to rebuild if the view has children
         * @see View#removeUpdate
         */
        @Override
		public void removeUpdate(DocumentEvent changes, Shape a, ViewFactory f) {
            super.removeUpdate(changes, adjustAllocation(a), f);
            updateVisibilityModel();
        }

    }
    
    /**
	 * {@inheritDoc}
	 * 
	 * <p>This method has been copied from the superclass, but the background
	 * is always painted, also if the field is non-opaque.</p>
	 */
	@Override
	protected void paintSafely(Graphics g) {
		
		// see comment of changeListenerAdded
		if (!changeListenerAdded) {
			changeListenerAdded = true;
			textField.getDocument().addDocumentListener(changeListener);
		}
		
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
		if (textField.isEditable() && textField.getClientProperty("joxy.isEditor") == null) {
			paintBackground(g);
			
			if (clearButtonOpacity > 0) {
				paintClearButton(g, clearButtonOpacity);
			}
		}
		
		Highlighter highlighter = textField.getHighlighter();
        Caret caret = textField.getCaret();
        
        // paint the highlights
        if (highlighter != null) {
            highlighter.paint(g);
        }

        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        
        // paint the view hierarchy
        Rectangle alloc = getVisibleEditorRect();
        if (alloc != null) {
            getRootView(textField).paint(g, alloc);
        }
        
        // paint the caret
        if (caret != null) {
            caret.paint(g);
        }
	}
	
	@Override
    protected void paintBackground(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		InputFieldPainter.paint(g2, 0, 0, textField.getWidth(), textField.getHeight());
		
		if (textField.isEnabled()) {
			TextFieldFocusIndicatorPainter.paint(g2, 0, 0, textField.getWidth(), textField.getHeight(), focusAmount);
			TextFieldHoverIndicatorPainter.paint(g2, 0, 0, textField.getWidth(), textField.getHeight(), Math.max(0, hoverAmount - focusAmount));
		}
    }
    
	/**
	 * Paints the clear button of a text field.
	 * @param g The Graphics object to draw with.
	 * @param opacity The opacity, between 0 and 255. This is used for the fading animation.
	 */
    private void paintClearButton(Graphics g, int opacity) {
		Graphics2D g2 = (Graphics2D) g;

		if (clearIcon == null) {
			return;
		}
		
		clearIcon.paintIcon(textField, g2, textField.getWidth() - 20, textField.getHeight() / 2 - 8);
		
		// TODO This is ugly; it should be possible to paint the image with opacity
		g2.setColor(new Color(255, 255, 255, 255 - opacity));
		g2.fillRect(textField.getWidth() - 20, textField.getHeight() / 2 - 8, 16, 16);
	}
}
