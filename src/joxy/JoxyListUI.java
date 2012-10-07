package joxy;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicListUI;

import joxy.painter.InputFieldPainter;
import joxy.renderer.JoxyListCellRenderer;

/**
 * Only a stub.
 */
public class JoxyListUI extends BasicListUI {

	// -1: no row is hovered
	private int hoveredRow = -1;

	private MouseListener mouseListener;
	private MouseMotionListener mouseMotionListener;
	
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
				hoveredRow = JoxyListUI.this.convertYToRow(e.getY());
				list.repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				hoveredRow = -1;
				list.repaint();
			}
		};
		
		mouseMotionListener = new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				hoveredRow = JoxyListUI.this.convertYToRow(e.getY()); // TODO this is buggy!
				list.repaint();
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {}
		};

		list.addMouseListener(mouseListener);
		list.addMouseMotionListener(mouseMotionListener);
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
		
		paintBackground(g2, c);
		
		super.paint(g2, c); // this also paints the contents of the list
		
		// TODO only paint hover/focus indicator and shadow here! So split this.
		// TODO the background scrolls with the content if the JList is in a JScrollPane
	}
	
    private void paintBackground(Graphics2D g2, JComponent c) {
		
		InputFieldPainter.paint(g2, 0, 0, c.getWidth(), c.getHeight());
		
		if (c.isEnabled()) {
			//TextFieldFocusIndicatorPainter.paint(g2, 0, 0, c.getWidth(), c.getHeight(), focusAmount);
			//TextFieldHoverIndicatorPainter.paint(g2, 0, 0, c.getWidth(), c.getHeight(), Math.max(0, hoverAmount - focusAmount));
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
