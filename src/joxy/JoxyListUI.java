package joxy;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicListUI;

import joxy.painter.InputFieldPainter;
import joxy.painter.TextFieldFocusIndicatorPainter;
import joxy.painter.TextFieldHoverIndicatorPainter;
import joxy.renderer.JoxyListCellRenderer;
import sun.swing.SwingUtilities2;

/**
 * Joxy's UI delegate for the JList.
 * 
 * <p>The JoxyListUI supports animations for the focus and hovered states.
 * See {@link JoxyButtonUI} for more details.</p>
 * 
 * <p>JoxyListUI also handles painting the hover and selected states of
 * the list elements. To be able to know which element is hovered, an additional
 * {@link MouseMotionListener} is added to the JList.</p>
 */
public class JoxyListUI extends BasicListUI {

	/**
	 * Index of the currently hovered list element.
	 */
	private int hoveredRow = -1;
	
	/**
	 * Whether the JList is hovered at the moment.
	 */
	private boolean hovered = false;
	
	/**
	 * Amount of hover and focus, from 0 to 255.
	 */
	private int hoverAmount = 0, focusAmount = 0;

	/**
	 * Timers for the animation.
	 */
	private Timer hoverTimer, focusTimer;

	/**
	 * Listeners for the animation.
	 */
	private MouseListener mouseListener;
	private FocusListener focusListener;
	
	/**
	 * A listener for managing the currently hovered row.
	 */
	private MouseMotionListener mouseMotionListener;
	
	/**
	 * The painter for the input field.
	 */
	private InputFieldPainter fieldPainter = new InputFieldPainter();
	
	public static ComponentUI createUI(JComponent c) {
		JoxyListUI ui = new JoxyListUI();
		return ui;
	}
	
	@Override
	protected void installListeners() {
		super.installListeners();

		mouseListener = new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {
				updateHoveredRow(e);
				list.repaint();
				
				hovered = true;
				hoverTimer.start();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				hoveredRow = -1;
				list.repaint();

				hovered = false;
				hoverTimer.start();
			}
		};
		
		mouseMotionListener = new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				updateHoveredRow(e);
				list.repaint();
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {}
		};
		
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
		
		list.addMouseListener(mouseListener);
		list.addMouseMotionListener(mouseMotionListener);
		list.addFocusListener(focusListener);
		
		createTimers();
	}

	private void updateHoveredRow(MouseEvent e) {
		hoveredRow = SwingUtilities2.loc2IndexFileList(list, e.getPoint());
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
				list.repaint();
			}
		});
		
		focusTimer = new Timer(40, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (list.hasFocus()) {
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
				list.repaint();
			}
		});
	}
	
	@Override
	protected void uninstallListeners() {
		super.uninstallListeners();
		
		list.removeMouseListener(mouseListener);
		list.removeMouseMotionListener(mouseMotionListener);
		list.removeFocusListener(focusListener);
	}
	
	@Override
	protected void installDefaults() {
		super.installDefaults();

		list.setCellRenderer(new JoxyListCellRenderer());
		list.setOpaque(false);
		list.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
	}
	
	@Override
	public void paint(Graphics g, JComponent c) {
		
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        Rectangle vr = list.getVisibleRect();

		if (list.getName() != null && list.getName().equals("ComboBox.list")) {
			super.paint(g2, c); // this also paints the contents of the list
		} else {
			fieldPainter.paint(g2, vr.x, vr.y, vr.width, vr.height);
			
			Shape oldClip = g2.getClip();
			g2.setClip(vr.x + 2, vr.y + 2, vr.width - 4, vr.height - 5);
			super.paint(g2, c); // this also paints the contents of the list
			g2.setClip(oldClip);
			
			if (c.isEnabled()) {
				TextFieldFocusIndicatorPainter.paint(g2, vr.x, vr.y, vr.width, vr.height, focusAmount);
				TextFieldHoverIndicatorPainter.paint(g2, vr.x, vr.y, vr.width, vr.height, Math.max(0, hoverAmount - focusAmount));
			}
		}
	}
    
    @Override
    protected void paintCell(Graphics g, int row, Rectangle rowBounds,
    		ListCellRenderer cellRenderer, ListModel dataModel,
    		ListSelectionModel selModel, int leadIndex) {
    	
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        Object value = dataModel.getElementAt(row);

        Component rendererComponent = cellRenderer.getListCellRendererComponent(list, value, row, selModel.isSelectedIndex(row), false);
        
        int cx = rowBounds.x;
        int cy = rowBounds.y;
        int cw = rowBounds.width;
        int ch = rowBounds.height;

        // If this is a list in a file dialog, shrink the renderer to preferred
        // size such that selection is only shown around the file name, instead of
        // across the whole list cell.
        if (list.getClientProperty("List.isFileList") != null &&
        		list.getClientProperty("List.isFileList").equals(Boolean.TRUE)) {
            int w = Math.min(cw, rendererComponent.getPreferredSize().width + 4);
            if (!list.getComponentOrientation().isLeftToRight()) {
                cx += (cw - w);
            }
            cw = w;
        }
        
        // hover effect
		if (row == hoveredRow) {
			// TODO take these colours from KDE configuration
			GradientPaint hoverGradient = new GradientPaint(0, cy, new Color(235, 242, 249),
					0, cy + ch, new Color(225, 236, 246));
			g2.setPaint(hoverGradient);
			g2.fill(new RoundRectangle2D.Float(cx, cy, cw, ch, 6, 6));

			g2.setColor(new Color(214, 231, 247));
			g2.setStroke(new BasicStroke(1));
			g2.draw(new RoundRectangle2D.Float(cx, cy, cw, ch, 6, 6));
		}
		
		// selection effect
		if (selModel.isSelectedIndex(row)) {
			// TODO take these colours from KDE configuration
			GradientPaint hoverGradient = new GradientPaint(0, cy, new Color(35, 152, 241),
					0, cy + ch, new Color(28, 120, 190));
			g2.setPaint(hoverGradient);
			g2.fill(new RoundRectangle2D.Float(cx, cy, cw, ch, 6, 6));

			g2.setColor(new Color(28, 120, 190));
			g2.setStroke(new BasicStroke(1));
			g2.draw(new RoundRectangle2D.Float(cx, cy, cw, ch, 6, 6));
		}
        // Also if we don't have our default Joxy renderer, at least try to allow
        // the hover and selection effects to be visible
        if (rendererComponent instanceof JComponent) {
        	((JComponent) rendererComponent).setOpaque(false);
        }
        
        rendererPane.paintComponent(g, rendererComponent, list, cx, cy, cw, ch, true);
    }
}
