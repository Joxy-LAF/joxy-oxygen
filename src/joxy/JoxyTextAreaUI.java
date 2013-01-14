package joxy;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextAreaUI;
import javax.swing.text.Caret;
import javax.swing.text.Highlighter;

import joxy.border.JoxyBevelBorder;
import joxy.painter.InputFieldPainter;
import joxy.painter.TextFieldFocusIndicatorPainter;
import joxy.painter.TextFieldHoverIndicatorPainter;

/**
 * Joxy's UI delegate for the JTextArea.
 * 
 * <p>The JoxyTextAreaUI supports animations for the focus and hovered states.
 * See JoxyButtonUI for more details. This class is largely copied from
 * {@link JoxyTextFieldUI}.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyTextAreaUI extends BasicTextAreaUI {
	
	/**
	 * The text area we are painting for.
	 */
	JTextArea textArea;

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
	
	// switched to false when painting the parent
	// TODO better documentation
	private boolean shouldPaint = true;
	
	/**
	 * The painter for the input field.
	 */
	private InputFieldPainter fieldPainter = new InputFieldPainter();
	
	public static ComponentUI createUI(JComponent c) {
		return new JoxyTextAreaUI(c);
	}
	
	public JoxyTextAreaUI(JComponent c) {
		textArea = (JTextArea) c;
	}
	
	@Override
	protected void installDefaults() {
		// TODO Auto-generated method stub
		super.installDefaults();
		
		// [ws] TODO deze dingen kunnen eigenlijk ook gewoon in de defaults...
		textArea.setOpaque(false);
		textArea.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
		textArea.setFont(UIManager.getFont("Button.font"));
	}
	
	@Override
	protected void installListeners() {
		super.installListeners();
		
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
		textArea.addMouseListener(hoverListener);
		
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
		textArea.addFocusListener(focusListener);
		
		createTimers();
	}
	
	private void createTimers() {
		
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
				textArea.repaint();
			}
		});
		
		focusTimer = new Timer(40, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (textArea.hasFocus()) {
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
				textArea.repaint();
			}
		});
	}
	
	@Override
	protected String getPropertyPrefix() {
		return "TextArea";
	}
	
    /**
	 * {@inheritDoc}
	 * 
	 * <p>This method has been copied from the superclass.</p>
	 */
	@Override
	protected void paintSafely(Graphics g) {
		
		if (shouldPaint) {
			Graphics2D g2 = (Graphics2D) g;
			
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			
			//if (textArea.isOpaque()) {
				paintBackground(g);
			//}
			
			Highlighter highlighter = textArea.getHighlighter();
	        Caret caret = textArea.getCaret();
	        
	        // paint the highlights
	        if (highlighter != null) {
	            highlighter.paint(g);
	        }
	
	        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
	        
	        // paint the view hierarchy
	        // TODO do clipping, to prevent text from overflowing beyond the border if the component
	        // is in a JScrollPane
	        // this happens because in a scroll pane, the EmptyBorder for this component obviously
	        // doesn't have an influence anymore
	        // note: JList is doing this already
	        Rectangle alloc = getVisibleEditorRect();
	        if (alloc != null) {
	            getRootView(textArea).paint(g, alloc);
	        }
	        
	        // paint the caret
	        if (caret != null) {
	            caret.paint(g);
	        }
		}
    }
	
	@Override
    protected void paintBackground(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		Rectangle vr = textArea.getVisibleRect();
		
		if (textArea.isEditable()) {
			fieldPainter.paint(g2, vr.x, vr.y, vr.width, vr.height);
		} else {
			JoxyBevelBorder.paintActualBorder(g2, vr.x, vr.y, vr.width, vr.height, BevelBorder.LOWERED);
		}
		
		if (textArea.isEnabled()) {
			TextFieldFocusIndicatorPainter.paint(g2, vr.x, vr.y, vr.width, vr.height, focusAmount);
			TextFieldHoverIndicatorPainter.paint(g2, vr.x, vr.y, vr.width, vr.height, Math.max(0, hoverAmount - focusAmount));
		}
	}
}
